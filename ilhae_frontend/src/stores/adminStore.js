import { ref } from 'vue'
import { defineStore } from 'pinia'
import { api } from '@/api/client'

export const useAdminStore = defineStore('admin', () => {
  const loading = ref(false)
  const error = ref(null)

  async function updateApplicationStatus(
    applicationId,
    status,
  ) {
    loading.value = true
    error.value = null

    try {
      return await api.updateApplicationStatus(
        applicationId,
        status,
      )
    } catch (e) {
      error.value = e
      throw e
    } finally {
      loading.value = false
    }
  }

  return {
    loading,
    error,
    updateApplicationStatus,
  }
})
