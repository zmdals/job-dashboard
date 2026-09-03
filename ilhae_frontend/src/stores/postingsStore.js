import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import { api } from '@/api/client'

export const usePostingsStore = defineStore(
  'postings',
  () => {
    const postings = ref([])
    const selectedPosting = ref(null)
    const relevance = ref(null)
    const relevanceByPostingId = ref({})
    const scoreByPostingId = ref({})
    const info = ref(null)

    const loading = ref(false)
    const error = ref(null)
    const hasLoadedPostings = ref(false)
    let pendingRequests = 0
    let postingsRequest = null
    const relevanceRequests = new Map()

    const postingCount = computed(() => postings.value.length)

    function getPostingById(postingId) {
      return postings.value.find(
        (posting) => String(posting.id) === String(postingId),
      ) ?? null
    }

    function extractScore(value) {
      if (typeof value === 'number') return value

      return value?.score ??
        value?.relevanceScore ??
        value?.aiFitScore ??
        value?.matchScore ??
        value?.passProbability ??
        value?.relevance?.score ??
        null
    }

    function cachePostingScore(postingId, value) {
      const score = extractScore(value)

      if (score !== null && score !== undefined) {
        scoreByPostingId.value[String(postingId)] = score
      }
    }

    function getPostingScore(postingId) {
      return scoreByPostingId.value[String(postingId)] ?? null
    }

    function getRelevance(postingId) {
      return relevanceByPostingId.value[String(postingId)] ?? null
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
        postings.value.forEach((posting) => {
          cachePostingScore(posting.id, posting)
        })
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
        cachePostingScore(postingId, selectedPosting.value)

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
        cachePostingScore(posting.id, posting)
        selectedPosting.value = posting
        return posting
      })
    }

    async function updatePosting(postingId, payload) {
      return run(async () => {
        const posting = await api.updatePosting(postingId, payload)
        cachePostingScore(postingId, posting)
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

        delete scoreByPostingId.value[String(postingId)]
        delete relevanceByPostingId.value[String(postingId)]
      })
    }

    async function fetchRelevance(postingId) {
      return run(async () => {
        const result = await api.getPostingRelevance(postingId)
        const key = String(postingId)

        relevanceByPostingId.value[key] = result
        cachePostingScore(postingId, result)
        relevance.value = result

        return result
      })
    }

    function ensureRelevance(postingId) {
      const key = String(postingId)
      const cached = relevanceByPostingId.value[key]

      if (cached) {
        relevance.value = cached
        return Promise.resolve(cached)
      }

      if (!relevanceRequests.has(key)) {
        const request = fetchRelevance(postingId).finally(() => {
          relevanceRequests.delete(key)
        })

        relevanceRequests.set(key, request)
      }

      return relevanceRequests.get(key)
    }

    function ensurePostingScore(postingId) {
      const cachedScore = getPostingScore(postingId)

      if (cachedScore !== null) {
        return Promise.resolve(cachedScore)
      }

      return ensureRelevance(postingId).then((result) =>
        extractScore(result),
      )
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
      relevanceByPostingId,
      scoreByPostingId,
      info,
      loading,
      error,
      hasLoadedPostings,
      postingCount,
      getPostingById,
      getPostingScore,
      getRelevance,
      fetchPostings,
      ensurePostings,
      fetchPosting,
      createPosting,
      updatePosting,
      deletePosting,
      fetchRelevance,
      ensureRelevance,
      ensurePostingScore,
      fetchInfo,
    }
  },
)
