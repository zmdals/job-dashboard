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

  function saveAccessToken(token, remember = false) {
    if (!token) {
      throw new Error('서버에서 인증 토큰을 받지 못했습니다.')
    }

    accessToken.value = token

    const storage = remember ? localStorage : sessionStorage
    const otherStorage = remember ? sessionStorage : localStorage
    storage.setItem('accessToken', token)
    otherStorage.removeItem('accessToken')
  }

  async function login(email, password, remember = false) {
    loading.value = true
    error.value = null

    try {
      const response = await api.login({ email, password })
      saveAccessToken(response.accessToken, remember)

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
      const response = await api.signup(payload)
      saveAccessToken(response.accessToken)
      return response
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
