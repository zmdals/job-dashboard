import { ref } from 'vue'
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

    async function fetchPostings() {
      return run(async () => {
        postings.value = await api.getPostings()
        return postings.value
      })
    }

    async function fetchPosting(postingId) {
      return run(async () => {
        selectedPosting.value =
          await api.getPosting(postingId)

        return selectedPosting.value
      })
    }

    async function fetchRelevance(postingId) {
      return run(async () => {
        relevance.value =
          await api.getPostingRelevance(postingId)

        return relevance.value
      })
    }

    async function fetchInfo(postingId) {
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
      fetchPostings,
      fetchPosting,
      fetchRelevance,
      fetchInfo,
    }
  },
)
