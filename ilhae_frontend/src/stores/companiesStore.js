import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import { api } from '@/api/client'

export const useCompaniesStore = defineStore('companies', () => {
  const companyById = ref({})
  const evidencesByCompanyId = ref({})
  const loading = ref(false)
  const error = ref(null)
  const companyRequests = new Map()
  const evidenceRequests = new Map()
  let pendingRequests = 0

  const companyCount = computed(() => Object.keys(companyById.value).length)

  async function run(action) {
    pendingRequests += 1
    loading.value = true
    error.value = null

    try {
      return await action()
    } catch (requestError) {
      error.value = requestError
      throw requestError
    } finally {
      pendingRequests -= 1
      loading.value = pendingRequests > 0
    }
  }

  function getCompany(companyId) {
    return companyById.value[String(companyId)] ?? null
  }

  function getCompanyEvidences(companyId) {
    return evidencesByCompanyId.value[String(companyId)] ?? []
  }

  async function fetchCompany(companyId) {
    return run(async () => {
      const company = await api.getCompany(companyId)
      companyById.value[String(companyId)] = company
      return company
    })
  }

  function ensureCompany(companyId) {
    const key = String(companyId)
    const cached = companyById.value[key]

    if (cached) return Promise.resolve(cached)

    if (!companyRequests.has(key)) {
      companyRequests.set(
        key,
        fetchCompany(companyId).finally(() => companyRequests.delete(key)),
      )
    }

    return companyRequests.get(key)
  }

  async function fetchCompanyEvidences(companyId) {
    return run(async () => {
      const evidences = await api.getCompanyEvidences(companyId)
      evidencesByCompanyId.value[String(companyId)] = evidences
      return evidences
    })
  }

  function ensureCompanyEvidences(companyId) {
    const key = String(companyId)

    if (Object.hasOwn(evidencesByCompanyId.value, key)) {
      return Promise.resolve(evidencesByCompanyId.value[key])
    }

    if (!evidenceRequests.has(key)) {
      evidenceRequests.set(
        key,
        fetchCompanyEvidences(companyId).finally(() => evidenceRequests.delete(key)),
      )
    }

    return evidenceRequests.get(key)
  }

  function reset() {
    companyById.value = {}
    evidencesByCompanyId.value = {}
    error.value = null
  }

  return {
    companyById,
    evidencesByCompanyId,
    loading,
    error,
    companyCount,
    getCompany,
    getCompanyEvidences,
    fetchCompany,
    ensureCompany,
    fetchCompanyEvidences,
    ensureCompanyEvidences,
    reset,
  }
})
