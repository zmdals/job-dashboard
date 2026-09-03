import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import { api } from '@/api/client'

export const useMeStore = defineStore('me', () => {
  const profile = ref(null)
  const applications = ref([])
  const starredPostings = ref([])

  const loading = ref(false)
  const error = ref(null)

  const starredPostingIds = computed(
    () => new Set(
      starredPostings.value.map((posting) => posting.id),
    ),
  )

  const appliedPostingIds = computed(
    () => new Set(
      applications.value.map(
        (application) => application.postingId,
      ),
    ),
  )

  function isStarred(postingId) {
    return starredPostingIds.value.has(postingId)
  }

  function isApplied(postingId) {
    return appliedPostingIds.value.has(postingId)
  }

  async function run(action) {
    loading.value = true
    error.value = null

    try {
      return await action()
    } catch (e) {
      error.value = e
      throw e
    } finally {
      loading.value = false
    }
  }

  async function fetchMe() {
    return run(async () => {
      profile.value = await api.getMe()
      return profile.value
    })
  }

  async function fetchApplications() {
    return run(async () => {
      applications.value =
        await api.getMyApplications()

      return applications.value
    })
  }

  async function apply(postingId) {
    return run(async () => {
      const application =
        await api.applyPosting(postingId)

      applications.value.push(application)
      return application
    })
  }

  async function fetchStarredPostings() {
    return run(async () => {
      starredPostings.value =
        await api.getMyStarredPostings()

      return starredPostings.value
    })
  }

  async function toggleStar(postingId) {
    return run(async () => {
      if (isStarred(postingId)) {
        await api.unstarPosting(postingId)
      } else {
        await api.starPosting(postingId)
      }

      starredPostings.value =
        await api.getMyStarredPostings()
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

  return {
    profile,
    applications,
    starredPostings,
    loading,
    error,
    starredPostingIds,
    appliedPostingIds,
    isStarred,
    isApplied,
    fetchMe,
    fetchApplications,
    apply,
    fetchStarredPostings,
    toggleStar,
    addCertificate,
    deleteCertificate,
  }
})
