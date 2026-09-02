# Recruiting API Starter

```text
POST   /api/auth/login
POST   /api/auth/signup

GET    /api/postings
GET    /api/postings/:postingId
GET    /api/postings/:postingId/relevance
GET    /api/postings/:postingId/info

GET    /api/me/applications
POST   /api/postings/:postingId/applications

GET    /api/me/starred-postings
POST   /api/me/starred-postings/:postingId
DELETE /api/me/starred-postings/:postingId

GET    /api/users/me
POST   /api/users/me/certificates
DELETE /api/users/me/certificates/:certificateId

//PATCH  /api/admin/applications/:applicationId/status
```

## 구조

```text
openapi/
  openapi.yaml            API 계약 원본

src/
  api/
    client.js             실제 fetch 호출

  mocks/
    mockData.js           초기 데이터
    db.js                 localStorage 기반 mock DB
    auth.js               mock JWT 생성/해석
    handlers.js           MSW API 구현
    browser.js            MSW browser worker
    enableMocking.js      개발 환경에서 MSW 활성화

  stores/
    authStore.js
    postingsStore.js
    meStore.js
    adminStore.js

  main.example.js
```

핵심 흐름:

```text
Vue Component
     ↓
Pinia Store
     ↓
api/client.js
     ↓
fetch('/api/...')
     ↓
MSW ON  → handlers.js
MSW OFF → 실제 Backend
```

프론트 코드는 mock/real 여부를 알 필요가 없습니다.

---

## 1. 설치

Vue 3 프로젝트에서:

```bash
npm i pinia
npm i -D msw
```

MSW용 Service Worker 생성:

```bash
npx msw init public --save
```

그러면 프로젝트의 `public/` 아래에
`mockServiceWorker.js`가 생성됩니다.

---

## 2. 환경 변수

`.env.local`

```env
VITE_USE_MSW=true
VITE_API_BASE_URL=/api
```

실제 백엔드로 전환:

```env
VITE_USE_MSW=false
VITE_API_BASE_URL=/api
```

---

## 3. main.js

`src/main.example.js`처럼 앱 mount 전에 MSW를 시작합니다.

```js
await enableMocking()
```

그 이후부터 `fetch('/api/...')` 요청을 MSW가 가로챕니다.

---

## 4. Mock 계정

일반 사용자:

```text
id: hong123
password: password123!
```

관리자:

```text
id: admin
password: admin123!
```

관리자 계정은 다음 API 테스트용입니다.

```text
PATCH /api/admin/applications/:applicationId/status
```

---

## 5. 현재 최소 규약

아직 너무 많은 것을 확정하지 않기 위해 최소한만 잡았습니다.

### 인증

보호 API:

```http
Authorization: Bearer <JWT>
```

로그인 응답:

```json
{
  "accessToken": "..."
}
```

사용자 정보는 로그인 응답에 중복해서 넣지 않고:

```text
GET /api/users/me
```

에서 받습니다.

### 오류

```json
{
  "code": "POSTING_NOT_FOUND",
  "message": "공고를 찾을 수 없습니다."
}
```

### 목록

현재는 페이지네이션을 정하지 않았으므로 단순 배열입니다.

```json
[
  {},
  {}
]
```

추후 필요할 때:

```json
{
  "items": [],
  "page": 1,
  "size": 20,
  "total": 123
}
```

같은 규약으로 변경하면 됩니다.

### 지원 상태

현재 최소 enum:

```text
APPLIED
DOCUMENT_PASSED
DOCUMENT_FAILED
INTERVIEW
FINAL_PASSED
FINAL_FAILED
WITHDRAWN
```

---

## 6. 사용 예시

로그인:

```js
import { useAuthStore } from '@/stores/authStore'

const auth = useAuthStore()

await auth.login('hong123', 'password123!')
```

공고:

```js
import { usePostingsStore } from '@/stores/postingsStore'

const postings = usePostingsStore()

await postings.fetchPostings()
await postings.fetchPosting('posting-001')
await postings.fetchRelevance('posting-001')
await postings.fetchInfo('posting-001')
```

내 지원/즐겨찾기:

```js
import { useMeStore } from '@/stores/meStore'

const me = useMeStore()

await me.fetchMe()
await me.fetchApplications()
await me.fetchStarredPostings()

await me.apply('posting-001')
await me.toggleStar('posting-002')
```

자격증:

```js
await me.addCertificate({
  name: 'SQLD',
  issuer: '한국데이터산업진흥원',
  issuedAt: '2026-06-01',
})

await me.deleteCertificate('cert-001')
```

관리자:

```js
import { useAdminStore } from '@/stores/adminStore'

const admin = useAdminStore()

await admin.updateApplicationStatus(
  'application-001',
  'DOCUMENT_PASSED',
)
```

---

## 7. Swagger/OpenAPI

`openapi/openapi.yaml`이 현재 API 계약의 원본입니다.

Swagger Editor/Swagger UI 같은 OpenAPI 호환 도구에서
이 YAML을 열면 API 문서 형태로 볼 수 있습니다.

중요한 원칙은:

```text
openapi.yaml
     │
     ├── Frontend client
     ├── MSW handlers
     └── Backend
```

셋이 같은 계약을 보게 하는 것입니다.

새 규칙이 결정되면 먼저 OpenAPI를 바꾸고,
그 다음 MSW와 실제 백엔드를 맞추는 식으로 진행하면 됩니다.

---

## 8. Mock 데이터 리셋

MSW 상태 변경은 localStorage에 저장됩니다.

초기화:

```js
localStorage.removeItem('recruiting-api-mock-db-v1')
localStorage.removeItem('accessToken')
location.reload()
```

---

## 9. 지금 일부러 안 정한 것

다음은 필요해질 때 추가하는 것을 권장합니다.

- 공고 검색/필터/페이지네이션
- refresh token
- 토큰 만료/재발급 규약
- password 정책
- DTO별 상세 validation
- 공통 response envelope
- 상태 변경 transition 규칙
- 관리자 application 목록 API
- optimistic update 정책
- 날짜/시간 timezone 규칙
- API versioning (`/api/v1`)
- OpenAPI 기반 client/type 자동 생성

지금은 화면 개발을 시작하기 충분한 정도만 구성했습니다.
