const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL || '/api'

export class ApiError extends Error {
  constructor(message, { status, code } = {}) {
    super(message)
    this.name = 'ApiError'
    this.status = status
    this.code = code
  }
}

function getAccessToken() {
  return localStorage.getItem('accessToken')
}

async function request(path, options = {}) {
  const headers = new Headers(options.headers || {})
  const token = getAccessToken()

  if (options.body && !headers.has('Content-Type')) {
    headers.set('Content-Type', 'application/json')
  }

  if (token) {
    headers.set('Authorization', `Bearer ${token}`)
  }

  const response = await fetch(
    `${API_BASE_URL}${path}`,
    {
      ...options,
      headers,
    },
  )

  if (response.status === 204) {
    return null
  }

  const body = await response.json().catch(() => null)

  if (!response.ok) {
    throw new ApiError(
      body?.message || `HTTP ${response.status}`,
      {
        status: response.status,
        code: body?.code || 'HTTP_ERROR',
      },
    )
  }

  return body
}

export const api = {
  login(payload) {
    return request('/auth/login', {
      method: 'POST',
      body: JSON.stringify(payload),
    })
  },

  signup(payload) {
    return request('/auth/signup', {
      method: 'POST',
      body: JSON.stringify(payload),
    })
  },

  getPostings() {
    return request('/postings')
  },

  getPosting(postingId) {
    return request(`/postings/${postingId}`)
  },

  getPostingRelevance(postingId) {
    return request(`/postings/${postingId}/relevance`)
  },

  getPostingInfo(postingId) {
    return request(`/postings/${postingId}/info`)
  },

  getMyApplications() {
    return request('/me/applications')
  },

  applyPosting(postingId) {
    return request(`/postings/${postingId}/applications`, {
      method: 'POST',
    })
  },

  getMyStarredPostings() {
    return request('/me/starred-postings')
  },

  starPosting(postingId) {
    return request(`/me/starred-postings/${postingId}`, {
      method: 'POST',
    })
  },

  unstarPosting(postingId) {
    return request(`/me/starred-postings/${postingId}`, {
      method: 'DELETE',
    })
  },

  getMe() {
    return request('/users/me')
  },

  addCertificate(payload) {
    return request('/users/me/certificates', {
      method: 'POST',
      body: JSON.stringify(payload),
    })
  },

  deleteCertificate(certificateId) {
    return request(
      `/users/me/certificates/${certificateId}`,
      {
        method: 'DELETE',
      },
    )
  },

  updateApplicationStatus(applicationId, status) {
    return request(
      `/admin/applications/${applicationId}/status`,
      {
        method: 'PATCH',
        body: JSON.stringify({ status }),
      },
    )
  },
}
