import { delay, http, HttpResponse } from 'msw'
import { getDb, nextId, saveDb } from './db'
import { createMockJwt, readMockJwt } from './auth'
import {
  APPLICATION_STATUS_TRANSITIONS,
  APPLICATION_STATUS_VALUES,
} from '@/constants/applicationStatus'

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

function currentApplicationUser(request, db) {
  return currentUser(request, db) ?? db.users[0] ?? null
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
    const user = currentApplicationUser(request, db)

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
      const user = currentApplicationUser(request, db)

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
        status: 'PREPARING',
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

  // =========================
  // My Specs — Education
  // =========================

  http.get(`${API}/me/educations`, async ({ request }) => {
    await latency()

    const db = getDb()
    const user = currentUser(request, db)

    if (!user) {
      return error(401, 'UNAUTHORIZED', '로그인이 필요합니다.')
    }

    return json(user.educations ?? [])
  }),

  http.get(
    `${API}/me/educations/:educationId`,
    async ({ request, params }) => {
      await latency()

      const db = getDb()
      const user = currentUser(request, db)

      if (!user) {
        return error(401, 'UNAUTHORIZED', '로그인이 필요합니다.')
      }

      const education = (user.educations ?? []).find(
        (item) => item.id === params.educationId,
      )

      if (!education) {
        return error(404, 'EDUCATION_NOT_FOUND', '학력 정보를 찾을 수 없습니다.')
      }

      return json(education)
    },
  ),

  http.post(`${API}/me/educations`, async ({ request }) => {
    await latency()

    const db = getDb()
    const user = currentUser(request, db)

    if (!user) {
      return error(401, 'UNAUTHORIZED', '로그인이 필요합니다.')
    }

    const body = await request.json()

    if (!body?.schoolName) {
      return error(400, 'INVALID_REQUEST', '학교명은 필수입니다.')
    }

    const education = {
      id: nextId('education'),
      schoolName: body.schoolName,
      degree: body.degree ?? null,
      major: body.major ?? null,
      startDate: body.startDate ?? null,
      endDate: body.endDate ?? null,
      educationStatus: body.educationStatus ?? null,
    }

    user.educations = user.educations ?? []
    user.educations.push(education)
    saveDb(db)

    return json(education, 201)
  }),

  http.put(
    `${API}/me/educations/:educationId`,
    async ({ request, params }) => {
      await latency()

      const db = getDb()
      const user = currentUser(request, db)

      if (!user) {
        return error(401, 'UNAUTHORIZED', '로그인이 필요합니다.')
      }

      const index = (user.educations ?? []).findIndex(
        (item) => item.id === params.educationId,
      )

      if (index < 0) {
        return error(404, 'EDUCATION_NOT_FOUND', '학력 정보를 찾을 수 없습니다.')
      }

      const body = await request.json()

      user.educations[index] = {
        ...user.educations[index],
        ...body,
        id: params.educationId,
      }
      saveDb(db)

      return json(user.educations[index])
    },
  ),

  http.delete(
    `${API}/me/educations/:educationId`,
    async ({ request, params }) => {
      await latency()

      const db = getDb()
      const user = currentUser(request, db)

      if (!user) {
        return error(401, 'UNAUTHORIZED', '로그인이 필요합니다.')
      }

      const before = (user.educations ?? []).length

      user.educations = (user.educations ?? []).filter(
        (item) => item.id !== params.educationId,
      )

      if (user.educations.length === before) {
        return error(404, 'EDUCATION_NOT_FOUND', '학력 정보를 찾을 수 없습니다.')
      }

      saveDb(db)

      return new HttpResponse(null, { status: 204 })
    },
  ),

  // =========================
  // My Specs — Career
  // =========================

  http.get(`${API}/me/careers`, async ({ request }) => {
    await latency()

    const db = getDb()
    const user = currentUser(request, db)

    if (!user) {
      return error(401, 'UNAUTHORIZED', '로그인이 필요합니다.')
    }

    return json(user.careers ?? [])
  }),

  http.get(`${API}/me/careers/:careerId`, async ({ request, params }) => {
    await latency()

    const db = getDb()
    const user = currentUser(request, db)

    if (!user) {
      return error(401, 'UNAUTHORIZED', '로그인이 필요합니다.')
    }

    const career = (user.careers ?? []).find(
      (item) => item.id === params.careerId,
    )

    if (!career) {
      return error(404, 'CAREER_NOT_FOUND', '경력 정보를 찾을 수 없습니다.')
    }

    return json(career)
  }),

  http.post(`${API}/me/careers`, async ({ request }) => {
    await latency()

    const db = getDb()
    const user = currentUser(request, db)

    if (!user) {
      return error(401, 'UNAUTHORIZED', '로그인이 필요합니다.')
    }

    const body = await request.json()

    if (!body?.companyName) {
      return error(400, 'INVALID_REQUEST', '회사명은 필수입니다.')
    }

    const career = {
      id: nextId('career'),
      companyName: body.companyName,
      position: body.position ?? null,
      startDate: body.startDate ?? null,
      endDate: body.endDate ?? null,
      description: body.description ?? null,
    }

    user.careers = user.careers ?? []
    user.careers.push(career)
    saveDb(db)

    return json(career, 201)
  }),

  http.put(
    `${API}/me/careers/:careerId`,
    async ({ request, params }) => {
      await latency()

      const db = getDb()
      const user = currentUser(request, db)

      if (!user) {
        return error(401, 'UNAUTHORIZED', '로그인이 필요합니다.')
      }

      const index = (user.careers ?? []).findIndex(
        (item) => item.id === params.careerId,
      )

      if (index < 0) {
        return error(404, 'CAREER_NOT_FOUND', '경력 정보를 찾을 수 없습니다.')
      }

      const body = await request.json()

      user.careers[index] = {
        ...user.careers[index],
        ...body,
        id: params.careerId,
      }
      saveDb(db)

      return json(user.careers[index])
    },
  ),

  http.delete(
    `${API}/me/careers/:careerId`,
    async ({ request, params }) => {
      await latency()

      const db = getDb()
      const user = currentUser(request, db)

      if (!user) {
        return error(401, 'UNAUTHORIZED', '로그인이 필요합니다.')
      }

      const before = (user.careers ?? []).length

      user.careers = (user.careers ?? []).filter(
        (item) => item.id !== params.careerId,
      )

      if (user.careers.length === before) {
        return error(404, 'CAREER_NOT_FOUND', '경력 정보를 찾을 수 없습니다.')
      }

      saveDb(db)

      return new HttpResponse(null, { status: 204 })
    },
  ),

  // =========================
  // My Specs — Project
  // =========================

  http.get(`${API}/me/projects`, async ({ request }) => {
    await latency()

    const db = getDb()
    const user = currentUser(request, db)

    if (!user) {
      return error(401, 'UNAUTHORIZED', '로그인이 필요합니다.')
    }

    return json(user.projects ?? [])
  }),

  http.get(`${API}/me/projects/:projectId`, async ({ request, params }) => {
    await latency()

    const db = getDb()
    const user = currentUser(request, db)

    if (!user) {
      return error(401, 'UNAUTHORIZED', '로그인이 필요합니다.')
    }

    const project = (user.projects ?? []).find(
      (item) => item.id === params.projectId,
    )

    if (!project) {
      return error(404, 'PROJECT_NOT_FOUND', '프로젝트 정보를 찾을 수 없습니다.')
    }

    return json(project)
  }),

  http.post(`${API}/me/projects`, async ({ request }) => {
    await latency()

    const db = getDb()
    const user = currentUser(request, db)

    if (!user) {
      return error(401, 'UNAUTHORIZED', '로그인이 필요합니다.')
    }

    const body = await request.json()

    if (!body?.projectName) {
      return error(400, 'INVALID_REQUEST', '프로젝트명은 필수입니다.')
    }

    const project = {
      id: nextId('project'),
      projectName: body.projectName,
      role: body.role ?? null,
      techStack: body.techStack ?? null,
      description: body.description ?? null,
    }

    user.projects = user.projects ?? []
    user.projects.push(project)
    saveDb(db)

    return json(project, 201)
  }),

  http.put(
    `${API}/me/projects/:projectId`,
    async ({ request, params }) => {
      await latency()

      const db = getDb()
      const user = currentUser(request, db)

      if (!user) {
        return error(401, 'UNAUTHORIZED', '로그인이 필요합니다.')
      }

      const index = (user.projects ?? []).findIndex(
        (item) => item.id === params.projectId,
      )

      if (index < 0) {
        return error(404, 'PROJECT_NOT_FOUND', '프로젝트 정보를 찾을 수 없습니다.')
      }

      const body = await request.json()

      user.projects[index] = {
        ...user.projects[index],
        ...body,
        id: params.projectId,
      }
      saveDb(db)

      return json(user.projects[index])
    },
  ),

  http.delete(
    `${API}/me/projects/:projectId`,
    async ({ request, params }) => {
      await latency()

      const db = getDb()
      const user = currentUser(request, db)

      if (!user) {
        return error(401, 'UNAUTHORIZED', '로그인이 필요합니다.')
      }

      const before = (user.projects ?? []).length

      user.projects = (user.projects ?? []).filter(
        (item) => item.id !== params.projectId,
      )

      if (user.projects.length === before) {
        return error(404, 'PROJECT_NOT_FOUND', '프로젝트 정보를 찾을 수 없습니다.')
      }

      saveDb(db)

      return new HttpResponse(null, { status: 204 })
    },
  ),

  // =========================
  // My Specs — Certificate
  // =========================

  http.get(`${API}/me/certificates`, async ({ request }) => {
    await latency()

    const db = getDb()
    const user = currentUser(request, db)

    if (!user) {
      return error(401, 'UNAUTHORIZED', '로그인이 필요합니다.')
    }

    return json(user.certificates ?? [])
  }),

  http.get(
    `${API}/me/certificates/:certificateId`,
    async ({ request, params }) => {
      await latency()

      const db = getDb()
      const user = currentUser(request, db)

      if (!user) {
        return error(401, 'UNAUTHORIZED', '로그인이 필요합니다.')
      }

      const certificate = (user.certificates ?? []).find(
        (item) => item.id === params.certificateId,
      )

      if (!certificate) {
        return error(404, 'CERTIFICATE_NOT_FOUND', '자격증을 찾을 수 없습니다.')
      }

      return json(certificate)
    },
  ),

  http.post(
    `${API}/me/certificates`,
    async ({ request }) => {
      await latency()

      const db = getDb()
      const user = currentUser(request, db)

      if (!user) {
        return error(401, 'UNAUTHORIZED', '로그인이 필요합니다.')
      }

      const body = await request.json()

      if (!body?.certName) {
        return error(
          400,
          'INVALID_REQUEST',
          '자격증 이름은 필수입니다.',
        )
      }

      const certificate = {
        id: nextId('cert'),
        certName: body.certName,
        issuer: body.issuer ?? null,
        acquiredDate: body.acquiredDate ?? null,
        languageScore: body.languageScore ?? null,
      }

      user.certificates = user.certificates ?? []
      user.certificates.push(certificate)
      saveDb(db)

      return json(certificate, 201)
    },
  ),

  http.put(
    `${API}/me/certificates/:certificateId`,
    async ({ request, params }) => {
      await latency()

      const db = getDb()
      const user = currentUser(request, db)

      if (!user) {
        return error(401, 'UNAUTHORIZED', '로그인이 필요합니다.')
      }

      const index = (user.certificates ?? []).findIndex(
        (item) => item.id === params.certificateId,
      )

      if (index < 0) {
        return error(404, 'CERTIFICATE_NOT_FOUND', '자격증을 찾을 수 없습니다.')
      }

      const body = await request.json()

      user.certificates[index] = {
        ...user.certificates[index],
        ...body,
        id: params.certificateId,
      }
      saveDb(db)

      return json(user.certificates[index])
    },
  ),

  http.delete(
    `${API}/me/certificates/:certificateId`,
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

  // =========================
  // My Specs — combined
  // =========================

  http.get(`${API}/me/specs`, async ({ request }) => {
    await latency()

    const db = getDb()
    const user = currentUser(request, db)

    if (!user) {
      return error(401, 'UNAUTHORIZED', '로그인이 필요합니다.')
    }

    return json({
      educations: user.educations ?? [],
      careers: user.careers ?? [],
      projects: user.projects ?? [],
      certificates: user.certificates ?? [],
    })
  }),

  http.patch(
    `${API}/applications/:applicationId/status`,
    async ({ request, params }) => {
      await latency()

      const db = getDb()
      const user = currentApplicationUser(request, db)

      if (!user) {
        return error(401, 'UNAUTHORIZED', '로그인이 필요합니다.')
      }

      const { applicationStatus, status: legacyStatus, memo } = await request.json()
      const status = applicationStatus ?? legacyStatus

      if (!APPLICATION_STATUS_VALUES.includes(status)) {
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

      const allowedStatuses = APPLICATION_STATUS_TRANSITIONS[application.status] ?? []

      if (!allowedStatuses.includes(status)) {
        return error(
          409,
          'INVALID_APPLICATION_STATUS_TRANSITION',
          `${application.status}에서 ${status}(으)로 상태를 변경할 수 없습니다.`,
        )
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
      const user = currentApplicationUser(request, db)

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
