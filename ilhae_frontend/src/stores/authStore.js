import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import { api } from '@/api/client'

export const useAuthStore = defineStore('auth', () => {
  const accessToken = ref(
    localStorage.getItem('accessToken') ??
      sessionStorage.getItem('accessToken'),
  )
  const loading = ref(false)
  const error = ref(null)

  const isAuthenticated = computed(
    () => Boolean(accessToken.value),
  )

  async function login(id, password, remember = false) {
    loading.value = true
    error.value = null

    try {
      const response = await api.login({ id, password })

      accessToken.value = response.accessToken

      const storage = remember ? localStorage : sessionStorage
      const otherStorage = remember ? sessionStorage : localStorage
      storage.setItem('accessToken', response.accessToken)
      otherStorage.removeItem('accessToken')

      return response
    } catch (e) {
      error.value = e
      throw e
    } finally {
      loading.value = false
    }
  }

  async function signup(payload) {
    loading.value = true
    error.value = null

    try {
      return await api.signup(payload)
    } catch (e) {
      error.value = e
      throw e
    } finally {
      loading.value = false
    }
  }

  function logout() {
    accessToken.value = null
    error.value = null
    localStorage.removeItem('accessToken')
    sessionStorage.removeItem('accessToken')
  }

  return {
    accessToken,
    loading,
    error,
    isAuthenticated,
    login,
    signup,
    logout,
  }
})
