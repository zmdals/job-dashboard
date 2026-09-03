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

function findCompany(db, companyId) {
  const company = (db.companies ?? []).find(
    (item) => item.id === companyId,
  )

  if (company) return company

  const legacyPosting = db.postings.find(
    (posting) =>
      posting.companyId === companyId ||
      posting.company?.id === companyId,
  )

  if (!legacyPosting) return null

  return {
    id: companyId,
    name: legacyPosting.companyName ?? legacyPosting.company?.name,
  }
}

function applicationView(db, application) {
  const posting = findPosting(db, application.postingId)

  return {
    id: application.id,
    jobPostingId: posting?.id,
    companyName: posting?.companyName,
    jobTitle: posting?.title,
    status: application.status,
    memo: application.memo ?? null,
    createdAt: application.appliedAt,
    updatedAt: application.updatedAt,
  }
}

function currentUser(request, db) {
  const payload = readMockJwt(request)

  if (!payload?.sub) {
    return null
  }

  return db.users.find((user) => user.id === payload.sub) ?? null
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

  http.get(`${API}/postings`, async ({ request }) => {
    await latency()

    const url = new URL(request.url)
    const page = Math.max(0, Number(url.searchParams.get('page') ?? 0))
    const size = Math.max(1, Number(url.searchParams.get('size') ?? 10))
    const postings = getDb().postings
    const start = page * size
    const content = postings.slice(start, start + size)
    const totalPages = Math.ceil(postings.length / size)

    return json({
      totalElements: postings.length,
      totalPages,
      numberOfElements: content.length,
      first: page === 0,
      last: totalPages === 0 || page >= totalPages - 1,
      size,
      content,
      number: page,
      empty: content.length === 0,
    })
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

  http.post(`${API}/postings`, async ({ request }) => {
    await latency()

    const body = await request.json()

    if (!body?.companyId || !body?.title) {
      return error(
        400,
        'INVALID_REQUEST',
        'companyId와 title은 필수입니다.',
      )
    }

    const db = getDb()
    const company = findCompany(db, body.companyId)

    if (!company) {
      return error(404, 'COMPANY_NOT_FOUND', '회사를 찾을 수 없습니다.')
    }

    const posting = {
      id: nextId('posting'),
      companyId: company.id,
      companyName: company.name,
      title: body.title,
      url: body.url ?? null,
      jobType: body.jobType ?? null,
      location: body.location ?? null,
      annualIncome: body.annualIncome ?? null,
      deadline: body.deadline ?? null,
      description: body.description ?? null,
    }

    db.postings.push(posting)
    saveDb(db)
    return json(posting)
  }),

  http.put(`${API}/postings/:postingId`, async ({ request, params }) => {
    await latency()

    const db = getDb()
    const index = db.postings.findIndex(
      (posting) => posting.id === params.postingId,
    )

    if (index < 0) {
      return error(
        404,
        'POSTING_NOT_FOUND',
        '공고를 찾을 수 없습니다.',
      )
    }

    const body = await request.json()

    if (!body?.companyId || !body?.title) {
      return error(
        400,
        'INVALID_REQUEST',
        'companyId와 title은 필수입니다.',
      )
    }

    const posting = {
      id: db.postings[index].id,
      companyId: db.postings[index].companyId,
      companyName: db.postings[index].companyName,
      title: body.title,
      url: body.url ?? null,
      jobType: body.jobType ?? null,
      location: body.location ?? null,
      annualIncome: body.annualIncome ?? null,
      deadline: body.deadline ?? null,
      description: body.description ?? null,
    }

    db.postings[index] = posting
    saveDb(db)
    return json(posting)
  }),

  http.delete(`${API}/postings/:postingId`, async ({ params }) => {
    await latency()

    const db = getDb()
    const before = db.postings.length
    db.postings = db.postings.filter(
      (posting) => posting.id !== params.postingId,
    )

    if (db.postings.length === before) {
      return error(
        404,
        'POSTING_NOT_FOUND',
        '공고를 찾을 수 없습니다.',
      )
    }

    saveDb(db)
    return json(null)
  }),

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
    `${API}/applications/:applicationId/status`,
    async ({ request, params }) => {
      await latency()

      const db = getDb()
      const user = currentUser(request, db)

      if (!user) {
        return error(401, 'UNAUTHORIZED', '로그인이 필요합니다.')
      }

      const { status, memo } = await request.json()

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

      if (application.userId !== user.id) {
        return error(403, 'FORBIDDEN', '지원 정보를 수정할 수 없습니다.')
      }

      application.status = status
      application.memo = memo ?? null
      application.updatedAt = new Date().toISOString()
      saveDb(db)

      return json(applicationView(db, application))
    },
  ),

  http.delete(
    `${API}/applications/:applicationId`,
    async ({ request, params }) => {
      await latency()

      const db = getDb()
      const user = currentUser(request, db)

      if (!user) {
        return error(401, 'UNAUTHORIZED', '로그인이 필요합니다.')
      }

      const index = db.applications.findIndex(
        (application) => application.id === params.applicationId,
      )

      if (index < 0) {
        return error(
          404,
          'APPLICATION_NOT_FOUND',
          '지원 정보를 찾을 수 없습니다.',
        )
      }

      if (db.applications[index].userId !== user.id) {
        return error(403, 'FORBIDDEN', '지원 정보를 삭제할 수 없습니다.')
      }

      db.applications.splice(index, 1)
      saveDb(db)
      return json(null)
    },
  ),
]
