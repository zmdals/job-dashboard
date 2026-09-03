function toBase64Url(value) {
  return btoa(value)
    .replaceAll('+', '-')
    .replaceAll('/', '_')
    .replaceAll('=', '')
}

function fromBase64Url(value) {
  const normalized = value
    .replaceAll('-', '+')
    .replaceAll('_', '/')

  const padding = '='.repeat((4 - (normalized.length % 4)) % 4)

  return atob(normalized + padding)
}

export function createMockJwt(user) {
  const header = toBase64Url(
    JSON.stringify({
      alg: 'none',
      typ: 'JWT',
    }),
  )

  const payload = toBase64Url(
    JSON.stringify({
      sub: user.id,
      role: user.role,
    }),
  )

  return `${header}.${payload}.mock-signature`
}

export function readMockJwt(request) {
  const authorization = request.headers.get('Authorization')

  if (!authorization?.startsWith('Bearer ')) {
    return null
  }

  const token = authorization.slice('Bearer '.length)
  const [, payload] = token.split('.')

  if (!payload) {
    return null
  }

  try {
    return JSON.parse(fromBase64Url(payload))
  } catch {
    return null
  }
}
