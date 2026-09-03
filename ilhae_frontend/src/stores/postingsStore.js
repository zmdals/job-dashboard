import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import { api } from '@/api/client'

const DEMO_RELEVANCE_BY_COMPANY_ID = {
  1: {
    score: 86,
    matchedSkills: ['서비스 기획', '데이터 분석'],
    missingSkills: ['경력 요건'],
    summary: '핵심 직무 역량과 프로젝트 경험이 잘 맞습니다.',
  },
  2: {
    score: 74,
    matchedSkills: ['프론트엔드 개발', '협업 경험'],
    missingSkills: ['대규모 서비스 경험'],
    summary: '기술 역량이 대체로 일치하며 실무 경험 보완이 필요합니다.',
  },
  3: {
    score: 68,
    matchedSkills: ['백엔드 개발', 'API 설계'],
    missingSkills: ['클라우드 운영'],
    summary: '기본 역량은 일치하지만 인프라 경험을 보완하면 좋습니다.',
  },
  4: {
    score: 79,
    matchedSkills: ['데이터 분석', '문제 해결'],
    missingSkills: ['산업 도메인 경험'],
    summary: '분석 역량과 문제 해결 경험의 관련성이 높습니다.',
  },
  5: {
    score: 82,
    matchedSkills: ['제품 기획', '사용자 분석'],
    missingSkills: ['정량 성과'],
    summary: '제품 관점의 경험이 공고와 높은 관련성을 보입니다.',
  },
  6: {
    score: 71,
    matchedSkills: ['사용자 조사', '프로젝트 경험'],
    missingSkills: ['리서치 방법론'],
    summary: '관련 프로젝트를 보유했으며 전문 방법론 보완이 필요합니다.',
  },
  'company-001': {
    score: 86,
    matchedSkills: ['Vue', 'JavaScript'],
    missingSkills: ['TypeScript'],
    summary: '프론트엔드 핵심 역량이 공고와 잘 맞습니다.',
  },
  'company-002': {
    score: 74,
    matchedSkills: ['Python', 'API'],
    missingSkills: ['Docker'],
    summary: '백엔드 기본 역량이 공고와 대체로 일치합니다.',
  },
  'company-003': {
    score: 68,
    matchedSkills: ['JavaScript', 'CSS'],
    missingSkills: ['Vue 실무 경험'],
    summary: '기초 웹 역량을 보유했으며 프레임워크 경험 보완이 필요합니다.',
  },
}

const DEFAULT_DEMO_RELEVANCE = {
  score: 75,
  matchedSkills: ['직무 관련 프로젝트', '협업 경험'],
  missingSkills: ['실무 경험'],
  summary: '보유 경험과 공고의 주요 역량이 대체로 일치합니다.',
}

export const usePostingsStore = defineStore('postings', () => {
  const postings = ref([])
  const selectedPosting = ref(null)
  const relevanceByCompanyId = ref({
    ...DEMO_RELEVANCE_BY_COMPANY_ID,
  })
  const info = ref(null)
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

  function getCompanyRelevance(companyId) {
    return relevanceByCompanyId.value[String(companyId)] ?? DEFAULT_DEMO_RELEVANCE
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

  async function fetchInfo(postingId) {
    info.value = null
    return run(async () => {
      info.value = await api.getPostingInfo(postingId)

      return info.value
    })
  }

  return {
    postings,
    selectedPosting,
    relevanceByCompanyId,
    info,
    pagination,
    postingPages,
    loading,
    error,
    hasLoadedPostings,
    postingCount,
    getPostingById,
    getCompanyRelevance,
    fetchPostings,
    ensurePostings,
    fetchPosting,
    createPosting,
    updatePosting,
    deletePosting,
    fetchInfo,
  }
})
