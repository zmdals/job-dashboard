import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import { api } from '@/api/client'

export const usePostingsStore = defineStore('postings', () => {
  const postings = ref([])
  const selectedPosting = ref(null)
  const pagination = ref({
    totalElements: 0,
    totalPages: 0,
    number: 0,
    size: 10,
    numberOfElements: 0,
    first: true,
    last: true,
  })
  const postingPages = ref({})

  const loading = ref(false)
  const error = ref(null)
  const hasLoadedPostings = ref(false)
  let pendingRequests = 0
  const postingsRequests = new Map()

  const postingCount = computed(() => pagination.value.totalElements)

  function getPostingById(postingId) {
    return postings.value.find((posting) => String(posting.id) === String(postingId)) ?? null
  }

  function updatePostingPageCaches(posting) {
    Object.values(postingPages.value).forEach((pageData) => {
      const index = pageData.content.findIndex((item) => String(item.id) === String(posting.id))

      if (index >= 0) {
        pageData.content[index] = posting
      }
    })
  }

  function invalidatePostingPages() {
    postingPages.value = {}
    hasLoadedPostings.value = false
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

  function normalizePostingPage(response, page, size) {
    if (Array.isArray(response)) {
      return {
        content: response,
        totalElements: response.length,
        totalPages: response.length ? 1 : 0,
        number: page,
        size,
        numberOfElements: response.length,
        first: true,
        last: true,
      }
    }

    return {
      content: response?.content ?? [],
      totalElements: response?.totalElements ?? 0,
      totalPages: response?.totalPages ?? 0,
      number: response?.number ?? page,
      size: response?.size ?? size,
      numberOfElements: response?.numberOfElements ?? response?.content?.length ?? 0,
      first: response?.first ?? page === 0,
      last: response?.last ?? true,
    }
  }

  function usePostingPage(pageData) {
    postings.value = pageData.content
    pagination.value = {
      totalElements: pageData.totalElements,
      totalPages: pageData.totalPages,
      number: pageData.number,
      size: pageData.size,
      numberOfElements: pageData.numberOfElements,
      first: pageData.first,
      last: pageData.last,
    }
  }

  async function fetchPostings({ page = 0, size = 10 } = {}) {
    return run(async () => {
      const response = await api.getPostings(page, size)
      const pageData = normalizePostingPage(response, page, size)
      const key = `${pageData.number}:${pageData.size}`

      postingPages.value[key] = pageData
      usePostingPage(pageData)
      hasLoadedPostings.value = true
      return postings.value
    })
  }

  function ensurePostings({ page = 0, size = 10 } = {}) {
    const key = `${page}:${size}`
    const cached = postingPages.value[key]

    if (cached) {
      usePostingPage(cached)
      return Promise.resolve(cached.content)
    }

    if (!postingsRequests.has(key)) {
      const request = fetchPostings({ page, size }).finally(() => {
        postingsRequests.delete(key)
      })

      postingsRequests.set(key, request)
    }

    return postingsRequests.get(key)
  }

  async function fetchPosting(postingId) {
    return run(async () => {
      selectedPosting.value = await api.getPosting(postingId)

      const index = postings.value.findIndex((posting) => String(posting.id) === String(postingId))

      if (index >= 0) {
        postings.value[index] = selectedPosting.value
      }

      updatePostingPageCaches(selectedPosting.value)

      return selectedPosting.value
    })
  }

  async function createPosting(payload) {
    return run(async () => {
      const posting = await api.createPosting(payload)
      postings.value.push(posting)
      selectedPosting.value = posting
      invalidatePostingPages()
      return posting
    })
  }

  async function updatePosting(postingId, payload) {
    return run(async () => {
      const posting = await api.updatePosting(postingId, payload)
      const index = postings.value.findIndex((item) => String(item.id) === String(postingId))

      if (index >= 0) {
        postings.value[index] = posting
      } else {
        postings.value.push(posting)
      }

      if (String(selectedPosting.value?.id) === String(postingId)) {
        selectedPosting.value = posting
      }

      updatePostingPageCaches(posting)

      return posting
    })
  }

  async function deletePosting(postingId) {
    return run(async () => {
      await api.deletePosting(postingId)
      postings.value = postings.value.filter((posting) => String(posting.id) !== String(postingId))

      if (String(selectedPosting.value?.id) === String(postingId)) {
        selectedPosting.value = null
      }

      invalidatePostingPages()
    })
  }

  return {
    postings,
    selectedPosting,
    pagination,
    postingPages,
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
  }
})
