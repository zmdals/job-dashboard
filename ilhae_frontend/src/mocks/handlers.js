import { delay, http, HttpResponse } from 'msw'
import { APPLICATION_STATUSES } from './mockData'
import { getDb, nextId, saveDb } from './db'
import { createMockJwt, readMockJwt } from './auth'

const API = '/api'

const json = (body, status = 200) =>
  HttpResponse.json(body, { status })

const error = (status, code, message) =>
  json({ code, message }, status)

async function latency() {
  await delay(200)
}

function publicUser(user) {
  const { password, ...safeUser } = user
  return safeUser
}

function findPosting(db, postingId) {
  return db.postings.find((posting) => posting.id === postingId)
}

function applicationView(db, application) {
  return {
    ...application,
    posting: findPosting(db, application.postingId),
  }
}

function currentUser(request, db) {
  const payload = readMockJwt(request)

  if (!payload?.sub) {
    return null
  }

  return db.users.find((user) => user.id === payload.sub) ?? null
}

function relevanceFor(user, posting) {
  const userSkills = new Set(user.skills.map((skill) => skill.toLowerCase()))

  const matchedSkills = posting.techStack.filter((skill) =>
    userSkills.has(skill.toLowerCase()),
  )

  const missingSkills = posting.techStack.filter(
    (skill) => !userSkills.has(skill.toLowerCase()),
  )

  const score = posting.techStack.length
    ? Math.round((matchedSkills.length / posting.techStack.length) * 100)
    : 0

  return {
    postingId: posting.id,
    score,
    matchedSkills,
    missingSkills,
    summary:
      score >= 70
        ? '보유 기술과 공고의 핵심 기술이 상당 부분 일치합니다.'
        : score >= 40
          ? '일부 기술이 일치하며 추가 역량 확인이 필요합니다.'
          : '현재 등록된 기술 기준으로는 일치도가 낮습니다.',
  }
}

export const handlers = [
  http.post(`${API}/auth/login`, async ({ request }) => {
    await latency()

    const { id, password } = await request.json()
    const db = getDb()

    const user = db.users.find(
      (candidate) =>
        candidate.email === id &&
        candidate.password === password,
    )

    if (!user) {
      return error(
        401,
        'INVALID_CREDENTIALS',
        '아이디 또는 비밀번호가 올바르지 않습니다.',
      )
    }

    return json({
      accessToken: createMockJwt(user),
    })
  }),

  http.post(`${API}/auth/signup`, async ({ request }) => {
    await latency()

    const { id, password, email, name } = await request.json()
    const db = getDb()

    if (!id || !password || !email || !name) {
      return error(
        400,
        'INVALID_REQUEST',
        'id, password, email, name은 필수입니다.',
      )
    }

    if (db.users.some((user) => user.loginId === id)) {
      return error(
        409,
        'DUPLICATE_LOGIN_ID',
        '이미 사용 중인 아이디입니다.',
      )
    }

    if (db.users.some((user) => user.email === email)) {
      return error(
        409,
        'DUPLICATE_EMAIL',
        '이미 사용 중인 이메일입니다.',
      )
    }

    const user = {
      id: nextId('user'),
      loginId: id,
      password,
      email,
      name,
      role: 'USER',
      skills: [],
      certificates: [],
    }

    db.users.push(user)
    db.starredByUserId[user.id] = []
    saveDb(db)

    return json(publicUser(user), 201)
  }),

  http.get(`${API}/postings`, async () => {
    await latency()
    return json(getDb().postings)
  }),

  http.get(`${API}/postings/:postingId`, async ({ params }) => {
    await latency()

    const db = getDb()
    const posting = findPosting(db, params.postingId)

    if (!posting) {
      return error(
        404,
        'POSTING_NOT_FOUND',
        '공고를 찾을 수 없습니다.',
      )
    }

    return json(posting)
  }),

  http.get(
    `${API}/postings/:postingId/relevance`,
    async ({ request, params }) => {
      await latency()

      const db = getDb()
      const user = currentUser(request, db)

      if (!user) {
        return error(401, 'UNAUTHORIZED', '로그인이 필요합니다.')
      }

      const posting = findPosting(db, params.postingId)

      if (!posting) {
        return error(
          404,
          'POSTING_NOT_FOUND',
          '공고를 찾을 수 없습니다.',
        )
      }

      return json(relevanceFor(user, posting))
    },
  ),

  http.get(`${API}/postings/:postingId/info`, async ({ params }) => {
    await latency()

    const info = getDb().postingInfoById[params.postingId]

    if (!info) {
      return error(
        404,
        'POSTING_INFO_NOT_FOUND',
        '공고 관련 정보를 찾을 수 없습니다.',
      )
    }

    return json(info)
  }),

  http.get(`${API}/me/applications`, async ({ request }) => {
    await latency()

    const db = getDb()
    const user = currentUser(request, db)

    if (!user) {
      return error(401, 'UNAUTHORIZED', '로그인이 필요합니다.')
    }

    return json(
      db.applications
        .filter((application) => application.userId === user.id)
        .map((application) => applicationView(db, application)),
    )
  }),

  http.post(
    `${API}/postings/:postingId/applications`,
    async ({ request, params }) => {
      await latency()

      const db = getDb()
      const user = currentUser(request, db)

      if (!user) {
        return error(401, 'UNAUTHORIZED', '로그인이 필요합니다.')
      }

      const posting = findPosting(db, params.postingId)

      if (!posting) {
        return error(
          404,
          'POSTING_NOT_FOUND',
          '공고를 찾을 수 없습니다.',
        )
      }

      const alreadyApplied = db.applications.some(
        (application) =>
          application.userId === user.id &&
          application.postingId === posting.id,
      )

      if (alreadyApplied) {
        return error(
          409,
          'ALREADY_APPLIED',
          '이미 지원한 공고입니다.',
        )
      }

      const now = new Date().toISOString()

      const application = {
        id: nextId('application'),
        userId: user.id,
        postingId: posting.id,
        status: 'APPLIED',
        appliedAt: now,
        updatedAt: now,
      }

      db.applications.push(application)
      saveDb(db)

      return json(applicationView(db, application), 201)
    },
  ),

  http.get(`${API}/me/starred-postings`, async ({ request }) => {
    await latency()

    const db = getDb()
    const user = currentUser(request, db)

    if (!user) {
      return error(401, 'UNAUTHORIZED', '로그인이 필요합니다.')
    }

    const starredIds = db.starredByUserId[user.id] ?? []

    return json(
      db.postings.filter((posting) =>
        starredIds.includes(posting.id),
      ),
    )
  }),

  http.post(
    `${API}/me/starred-postings/:postingId`,
    async ({ request, params }) => {
      await latency()

      const db = getDb()
      const user = currentUser(request, db)

      if (!user) {
        return error(401, 'UNAUTHORIZED', '로그인이 필요합니다.')
      }

      if (!findPosting(db, params.postingId)) {
        return error(
          404,
          'POSTING_NOT_FOUND',
          '공고를 찾을 수 없습니다.',
        )
      }

      const starredIds = db.starredByUserId[user.id] ?? []

      if (starredIds.includes(params.postingId)) {
        return error(
          409,
          'ALREADY_STARRED',
          '이미 즐겨찾기한 공고입니다.',
        )
      }

      starredIds.push(params.postingId)
      db.starredByUserId[user.id] = starredIds
      saveDb(db)

      return json(
        {
          postingId: params.postingId,
        },
        201,
      )
    },
  ),

  http.delete(
    `${API}/me/starred-postings/:postingId`,
    async ({ request, params }) => {
      await latency()

      const db = getDb()
      const user = currentUser(request, db)

      if (!user) {
        return error(401, 'UNAUTHORIZED', '로그인이 필요합니다.')
      }

      db.starredByUserId[user.id] = (
        db.starredByUserId[user.id] ?? []
      ).filter((postingId) => postingId !== params.postingId)

      saveDb(db)

      return new HttpResponse(null, { status: 204 })
    },
  ),

  http.get(`${API}/users/me`, async ({ request }) => {
    await latency()

    const db = getDb()
    const user = currentUser(request, db)

    if (!user) {
      return error(401, 'UNAUTHORIZED', '로그인이 필요합니다.')
    }

    return json(publicUser(user))
  }),

  http.post(
    `${API}/users/me/certificates`,
    async ({ request }) => {
      await latency()

      const db = getDb()
      const user = currentUser(request, db)

      if (!user) {
        return error(401, 'UNAUTHORIZED', '로그인이 필요합니다.')
      }

      const body = await request.json()

      if (!body?.name) {
        return error(
          400,
          'INVALID_REQUEST',
          '자격증 이름은 필수입니다.',
        )
      }

      const certificate = {
        id: nextId('cert'),
        name: body.name,
        issuer: body.issuer ?? null,
        issuedAt: body.issuedAt ?? null,
        expiresAt: body.expiresAt ?? null,
        credentialId: body.credentialId ?? null,
      }

      user.certificates.push(certificate)
      saveDb(db)

      return json(certificate, 201)
    },
  ),

  http.delete(
    `${API}/users/me/certificates/:certificateId`,
    async ({ request, params }) => {
      await latency()

      const db = getDb()
      const user = currentUser(request, db)

      if (!user) {
        return error(401, 'UNAUTHORIZED', '로그인이 필요합니다.')
      }

      const before = user.certificates.length

      user.certificates = user.certificates.filter(
        (certificate) => certificate.id !== params.certificateId,
      )

      if (user.certificates.length === before) {
        return error(
          404,
          'CERTIFICATE_NOT_FOUND',
          '자격증을 찾을 수 없습니다.',
        )
      }

      saveDb(db)

      return new HttpResponse(null, { status: 204 })
    },
  ),

  http.patch(
    `${API}/admin/applications/:applicationId/status`,
    async ({ request, params }) => {
      await latency()

      const db = getDb()
      const user = currentUser(request, db)

      if (!user) {
        return error(401, 'UNAUTHORIZED', '로그인이 필요합니다.')
      }

      if (user.role !== 'ADMIN') {
        return error(
          403,
          'FORBIDDEN',
          '관리자 권한이 필요합니다.',
        )
      }

      const { status } = await request.json()

      if (!APPLICATION_STATUSES.includes(status)) {
        return error(
          400,
          'INVALID_APPLICATION_STATUS',
          '유효하지 않은 지원 상태입니다.',
        )
      }

      const application = db.applications.find(
        (item) => item.id === params.applicationId,
      )

      if (!application) {
        return error(
          404,
          'APPLICATION_NOT_FOUND',
          '지원 정보를 찾을 수 없습니다.',
        )
      }

      application.status = status
      application.updatedAt = new Date().toISOString()
      saveDb(db)

      return json(applicationView(db, application))
    },
  ),
]
