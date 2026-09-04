import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import { api } from '@/api/client'

export const useAuthStore = defineStore('auth', () => {
  const hasPersistentLogin = Boolean(localStorage.getItem('accessToken'))
  const initialStorage = hasPersistentLogin ? localStorage : sessionStorage
  const accessToken = ref(initialStorage.getItem('accessToken'))
  const userId = ref(initialStorage.getItem('authUserId'))
  const userName = ref(initialStorage.getItem('authUserName'))
  const loading = ref(false)
  const error = ref(null)

  const isAuthenticated = computed(
    () => Boolean(accessToken.value),
  )

  function saveAuth(response, remember = false) {
    if (!response?.accessToken) {
      throw new Error('서버에서 인증 토큰을 받지 못했습니다.')
    }

    accessToken.value = response.accessToken
    userId.value = response.userId ?? null
    userName.value = response.name ?? null

    const storage = remember ? localStorage : sessionStorage
    const otherStorage = remember ? sessionStorage : localStorage
    storage.setItem('accessToken', response.accessToken)

    if (response.userId != null) {
      storage.setItem('authUserId', String(response.userId))
    } else {
      storage.removeItem('authUserId')
    }

    if (response.name) {
      storage.setItem('authUserName', response.name)
    } else {
      storage.removeItem('authUserName')
    }

    for (const key of ['accessToken', 'authUserId', 'authUserName']) {
      otherStorage.removeItem(key)
    }
  }

  async function login(email, password, remember = false) {
    loading.value = true
    error.value = null

    try {
      const response = await api.login({ email, password })
      saveAuth(response, remember)

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
      saveAuth(response)
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
    userId.value = null
    userName.value = null
    error.value = null

    for (const storage of [localStorage, sessionStorage]) {
      for (const key of ['accessToken', 'authUserId', 'authUserName']) {
        storage.removeItem(key)
      }
    }
  }

  return {
    accessToken,
    userId,
    userName,
    loading,
    error,
    isAuthenticated,
    login,
    signup,
    logout,
  }
})
