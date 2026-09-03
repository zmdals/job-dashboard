import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import { api } from '@/api/client'

export const usePostingsStore = defineStore(
  'postings',
  () => {
    const postings = ref([])
    const selectedPosting = ref(null)
    const relevance = ref(null)
    const info = ref(null)

    const loading = ref(false)
    const error = ref(null)
    const hasLoadedPostings = ref(false)
    let pendingRequests = 0
    let postingsRequest = null

    const postingCount = computed(() => postings.value.length)

    function getPostingById(postingId) {
      return postings.value.find(
        (posting) => String(posting.id) === String(postingId),
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

    async function fetchPostings() {
      return run(async () => {
        postings.value = await api.getPostings()
        hasLoadedPostings.value = true
        return postings.value
      })
    }

    function ensurePostings() {
      if (hasLoadedPostings.value) {
        return Promise.resolve(postings.value)
      }

      if (!postingsRequest) {
        postingsRequest = fetchPostings().finally(() => {
          postingsRequest = null
        })
      }

      return postingsRequest
    }

    async function fetchPosting(postingId) {
      return run(async () => {
        selectedPosting.value = await api.getPosting(postingId)

        const index = postings.value.findIndex(
          (posting) => String(posting.id) === String(postingId),
        )

        if (index >= 0) {
          postings.value[index] = selectedPosting.value
        }

        return selectedPosting.value
      })
    }

    async function createPosting(payload) {
      return run(async () => {
        const posting = await api.createPosting(payload)
        postings.value.push(posting)
        selectedPosting.value = posting
        return posting
      })
    }

    async function updatePosting(postingId, payload) {
      return run(async () => {
        const posting = await api.updatePosting(postingId, payload)
        const index = postings.value.findIndex(
          (item) => String(item.id) === String(postingId),
        )

        if (index >= 0) {
          postings.value[index] = posting
        } else {
          postings.value.push(posting)
        }

        if (String(selectedPosting.value?.id) === String(postingId)) {
          selectedPosting.value = posting
        }

        return posting
      })
    }

    async function deletePosting(postingId) {
      return run(async () => {
        await api.deletePosting(postingId)
        postings.value = postings.value.filter(
          (posting) => String(posting.id) !== String(postingId),
        )

        if (String(selectedPosting.value?.id) === String(postingId)) {
          selectedPosting.value = null
        }
      })
    }

    async function fetchRelevance(postingId) {
      relevance.value = null
      return run(async () => {
        relevance.value =
          await api.getPostingRelevance(postingId)

        return relevance.value
      })
    }

    async function fetchInfo(postingId) {
      info.value = null
      return run(async () => {
        info.value =
          await api.getPostingInfo(postingId)

        return info.value
      })
    }

    return {
      postings,
      selectedPosting,
      relevance,
      info,
      loading,
      error,
      hasLoadedPostings,
      postingCount,
      getPostingById,
      fetchPostings,
      ensurePostings,
      fetchPosting,
      createPosting,
      updatePosting,
      deletePosting,
      fetchRelevance,
      fetchInfo,
    }
  },
)
