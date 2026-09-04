import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import { api } from '@/api/client'

export const useMeStore = defineStore('me', () => {
  const profile = ref(null)
  const applications = ref([])
  const starredPostings = ref([])

  const loading = ref(false)
  const error = ref(null)
  const hasLoadedProfile = ref(false)
  const hasLoadedApplications = ref(false)
  const hasLoadedStarredPostings = ref(false)
  let pendingRequests = 0
  let profileRequest = null
  let applicationsRequest = null
  let starredPostingsRequest = null

  const starredPostingIds = computed(
    () => new Set(
      starredPostings.value.map(
        (posting) => String(posting.jobPostingId ?? posting.id),
      ),
    ),
  )

  const appliedPostingIds = computed(
    () => new Set(
      applications.value.map(
        (application) => String(application.jobPostingId),
      ),
    ),
  )

  const applicationStatusCounts = computed(() =>
    applications.value.reduce((counts, application) => {
      counts[application.status] = (counts[application.status] ?? 0) + 1
      return counts
    }, {}),
  )

  function normalizeApplication(application) {
    return {
      ...application,
      jobPostingId:
        application.jobPostingId ??
        application.postingId ??
        application.posting?.id,
      companyName:
        application.companyName ??
        application.posting?.companyName ??
        application.posting?.company?.name,
      jobTitle:
        application.jobTitle ?? application.posting?.title,
      status:
        application.status ?? application.applicationStatus,
    }
  }

  function normalizeStarredPosting(starredPosting) {
    const hasSeparatePostingId = starredPosting.jobPostingId != null
    const jobPostingId = starredPosting.jobPostingId ?? starredPosting.id

    return {
      ...starredPosting,
      id: jobPostingId,
      jobPostingId,
      starredPostingId:
        starredPosting.starredPostingId ??
        (hasSeparatePostingId ? starredPosting.id : null),
      title: starredPosting.title ?? starredPosting.jobTitle,
    }
  }

  function isStarred(postingId) {
    return starredPostingIds.value.has(String(postingId))
  }

  function isApplied(postingId) {
    return appliedPostingIds.value.has(String(postingId))
  }

  function getApplicationByPostingId(postingId) {
    return applications.value.find(
      (application) =>
        String(application.jobPostingId) === String(postingId),
    ) ?? null
  }

  async function run(action) {
    pendingRequests += 1
    loading.value = true
    error.value = null

    try {
      return await action()
    } catch (e) {
      error.value = e
      throw e
    } finally {
      pendingRequests -= 1
      loading.value = pendingRequests > 0
    }
  }

  async function fetchMe() {
    return run(async () => {
      profile.value = await api.getMe()
      hasLoadedProfile.value = true
      return profile.value
    })
  }

  function ensureMe() {
    if (hasLoadedProfile.value) {
      return Promise.resolve(profile.value)
    }

    if (!profileRequest) {
      profileRequest = fetchMe().finally(() => {
        profileRequest = null
      })
    }

    return profileRequest
  }

  async function fetchApplications() {
    return run(async () => {
      const response = await api.getMyApplications()
      applications.value = response.map(normalizeApplication)
      hasLoadedApplications.value = true

      return applications.value
    })
  }

  function ensureApplications() {
    if (hasLoadedApplications.value) {
      return Promise.resolve(applications.value)
    }

    if (!applicationsRequest) {
      applicationsRequest = fetchApplications().finally(() => {
        applicationsRequest = null
      })
    }

    return applicationsRequest
  }

  async function apply(postingId) {
    return run(async () => {
      const application = normalizeApplication(
        await api.applyPosting(postingId),
      )

      const index = applications.value.findIndex(
        (item) => item.id === application.id,
      )

      if (index >= 0) {
        applications.value[index] = application
      } else {
        applications.value.push(application)
      }
      return application
    })
  }

  async function updateApplicationStatus(
    applicationId,
    status,
    memo = null,
  ) {
    return run(async () => {
      const application = normalizeApplication(
        await api.updateApplicationStatus(
          applicationId,
          status,
          memo,
        ),
      )
      const index = applications.value.findIndex(
        (item) => String(item.id) === String(applicationId),
      )

      if (index >= 0) {
        applications.value[index] = application
      } else {
        applications.value.unshift(application)
      }

      return application
    })
  }

  async function deleteApplication(applicationId) {
    return run(async () => {
      const result = await api.deleteApplication(applicationId)
      applications.value = applications.value.filter(
        (application) =>
          String(application.id) !== String(applicationId),
      )
      return result
    })
  }

  function updateApplicationFlags(applicationId, flags) {
    const index = applications.value.findIndex(
      (application) => String(application.id) === String(applicationId),
    )

    if (index >= 0) {
      applications.value[index] = {
        ...applications.value[index],
        ...flags,
      }
    }
  }

  function markApplicationAnalyzed(applicationId) {
    updateApplicationFlags(applicationId, { hasAnalysis: true })
  }

  function markApplicationHasCoverLetter(applicationId) {
    updateApplicationFlags(applicationId, { hasCoverLetter: true })
  }

  async function fetchStarredPostings() {
    return run(async () => {
      const response = await api.getMyStarredPostings()
      starredPostings.value = response.map(normalizeStarredPosting)
      hasLoadedStarredPostings.value = true

      return starredPostings.value
    })
  }

  function ensureStarredPostings() {
    if (hasLoadedStarredPostings.value) {
      return Promise.resolve(starredPostings.value)
    }

    if (!starredPostingsRequest) {
      starredPostingsRequest = fetchStarredPostings().finally(() => {
        starredPostingsRequest = null
      })
    }

    return starredPostingsRequest
  }

  async function toggleStar(postingId) {
    return run(async () => {
      if (isStarred(postingId)) {
        await api.unstarPosting(postingId)
      } else {
        await api.starPosting(postingId)
      }

      const response = await api.getMyStarredPostings()
      starredPostings.value = response.map(normalizeStarredPosting)
      hasLoadedStarredPostings.value = true
    })
  }

  async function addCertificate(payload) {
    return run(async () => {
      const certificate =
        await api.addCertificate(payload)

      if (profile.value) {
        profile.value.certificates.push(certificate)
      }

      return certificate
    })
  }

  async function deleteCertificate(certificateId) {
    return run(async () => {
      await api.deleteCertificate(certificateId)

      if (profile.value) {
        profile.value.certificates =
          profile.value.certificates.filter(
            (certificate) =>
              certificate.id !== certificateId,
          )
      }
    })
  }

  function reset() {
    profile.value = null
    applications.value = []
    starredPostings.value = []
    error.value = null
    hasLoadedProfile.value = false
    hasLoadedApplications.value = false
    hasLoadedStarredPostings.value = false
  }

  return {
    profile,
    applications,
    starredPostings,
    loading,
    error,
    hasLoadedProfile,
    hasLoadedApplications,
    hasLoadedStarredPostings,
    starredPostingIds,
    appliedPostingIds,
    applicationStatusCounts,
    isStarred,
    isApplied,
    getApplicationByPostingId,
    fetchMe,
    ensureMe,
    fetchApplications,
    ensureApplications,
    apply,
    updateApplicationStatus,
    deleteApplication,
    markApplicationAnalyzed,
    markApplicationHasCoverLetter,
    fetchStarredPostings,
    ensureStarredPostings,
    toggleStar,
    addCertificate,
    deleteCertificate,
    reset,
  }
})
