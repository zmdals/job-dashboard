# 취업했음청년 — AI-Ready 취업 대시보드

취업 지원 전 과정(탐색 → 지원 → 면접 → 결과)을 한 곳에서 관리하고, 채용 공고와 사용자의 프로필을 AI 분석하여, 합격률 예측, 자소서 피드백, 면접 예상 질문을 제공하는 웹 서비스입니다.

> SKALA 광주 4기 - 3반 4조 (쉬었음청년 조)
> 미니프로젝트(AI-READY 웹 서비스 설계)

### 배포주소 : https://job-dashboard-skala3-4.vercel.app/

## **R&R**

| 이름     | 역할                         | 담당                                                       | GitHub                          |
| -------- | ---------------------------- | ---------------------------------------------------------- | ------------------------------- |
| 임승민   | Backend, DB                  | Auth, Application, AI Mock API, 코드 리뷰, GitHub 형상관리 | https://github.com/zmdals       |
| 황가인   | Backend, DB                  | User & User 하위 API 개발,                                 |
| ERD 생성 | https://github.com/gainhwang |
| 장서연   | Frontend                     | 지원 현황 · 내 프로필 페이지, API 연동 구현                | https://github.com/seoyeonskala |
| 신동운   | PM                           | FE-자기소개서 API 연동,                                    |

BE-선호 공고 CRUD 개발,
발표 | https://github.com/shindw3798 |
| 신동범 | Frontend | 로그인 · 채용 공고 페이지,
반응형 모바일 웹 화면 구현 | https://github.com/tls8012 |

## 프로젝트 개요

현재 AI 기능은 Mock API로 동작하며, Service 내부만 교체하면 실제 AI로 전환할 수 있는 AI-Ready 구조로 설계했습니다.

### 핵심 기능

- **채용공고 탐색** — AI 적합도 점수 표시, 회사정보 조회, 즐겨찾기
- **맞춤 기업 리포트** — 유저 프로필 기반 기업 자료(특허/IR/논문 등) 관련도 분석
- **지원 관리** — 8단계 상태 관리(서류준비 → 최종합격), 자소서 작성
- **AI 분석** — 스펙 기반 매칭 분석, 자소서 추가 시 점수 갱신, 면접 예상 질문 생성
- **내 프로필** — 학력/경력/자격증/프로젝트/수상 관리

## 기술 스택

| 영역     | 기술                              |
| -------- | --------------------------------- |
| Frontend | Vue.js 3, Vite, Pinia, Vue Router |
| Backend  | Spring Boot 4.1.1, Java 21        |
| Database | Supabase (PostgreSQL)             |
| 인증     | JWT (Spring Security)             |
| API 문서 | Swagger (springdoc-openapi)       |
| 배포     | Vercel (FE), Render (BE)          |

## 시스템 아키텍처

```
Vue.js (Vercel)
    ↓ /api/* (Vercel Rewrite)
Spring Boot (Render)
    ↓ JDBC
Supabase (PostgreSQL)
```

## ERD

!ERD

## 핵심 API 명세

| 기능       | 핵심 API                                                        |
| ---------- | --------------------------------------------------------------- |
| 인증       | `POST /api/auth/signup`, `POST /api/auth/login`                 |
| 공고 탐색  | `GET /api/postings?page=0&size=10`                              |
| 공고 지원  | `POST /api/postings/{postingId}/applications`                   |
| 지원 관리  | `GET /api/me/applications`                                      |
| 자기소개서 | `POST /api/applications/{applicationId}/cover-letter`           |
| AI 분석    | `POST /api/applications/{applicationId}/ai/analysis`            |
| 면접 준비  | `POST /api/applications/{applicationId}/ai/interview-questions` |

## AI-Ready 설계

교안에서 제시한 4대 원칙을 적용했습니다.

| 원칙             | 적용                                                                 |
| ---------------- | -------------------------------------------------------------------- |
| Interface First  | Mock이든 AI든 Controller/DTO 동일, FE 변경 없이 AI 전환              |
| Structured Data  | strengths/weaknesses를 JSON TEXT로 저장, AI 응답을 변환 없이 DB 저장 |
| Async Pipeline   | AiStatus(PENDING/COMPLETED/FAILED) enum이 모든 AI 엔티티에 준비      |
| Config Isolation | JWT secret, DB URL 전부 .env 관리, AI API Key도 동일 방식으로 추가   |

### Mock → AI 전환 시 변경 범위

```
변경: AiMockService 메서드 내부 (하드코딩 → AI API 호출)
유지: Controller, DTO, Entity, Repository, FE 전부
```

## 프로젝트 구조 (Backend)

```
backend/src/main/java/com/jobdashboard/backend/
├── config/          SecurityConfig, SwaggerConfig
├── controller/      16개 Controller
├── dto/             도메인별 Req/Res
├── entity/          14개 Entity + 7개 Enum
├── exception/       GlobalExceptionHandler
├── repository/      14개 Repository
├── security/        JWT 필터, UserDetails
├── service/         12개 Service
└── util/            MockScoreUtil
```

## 로컬 실행

### Backend

```bash
cd backend
# .env 파일 설정 (DB URL, JWT Secret 등)
./gradlew bootRun
# http://localhost:8080/swagger-ui/index.html
```

### Frontend

```bash
cd frontend
npm install
npm run dev
# http://localhost:5173
```

## 배포 환경

| 서비스   | 플랫폼      | URL                                       |
| -------- | ----------- | ----------------------------------------- |
| Frontend | Vercel      | https://job-dashboard-skala3-4.vercel.app |
| Backend  | Render      | (Render URL)                              |
| Database | Supabase    | (Cloud PostgreSQL)                        |
| 모니터링 | UptimeRobot | BE Cold Start 방지 (5분 주기 헬스체크)    |

## 설계 결정 근거

| 결정                                                 | 근거                                                       |
| ---------------------------------------------------- | ---------------------------------------------------------- |
| 적합도를 relevance(비저장)와 analysis(저장)로 이원화 | 목록에서 빠르게 훑는 용도 vs 지원 후 정밀 분석 용도 분리   |
| ApplicationStatus를 8단계로 확장                     | 와이어프레임 UI 요구사항과 일치, 상단 카드 카운트 가능     |
| VALID_TRANSITIONS 맵으로 상태 전이 제어              | 비정상 상태 변경 차단, 데이터 무결성 보장                  |
| MatchAnalysis 덮어쓰기 방식                          | 3일 scope 내 구현 가능한 단순 구조                         |
| Company 테이블 분리 + dart_corp_code                 | 향후 DART API 연동으로 기업 재무정보 자동 수집 가능        |
| CompanyEvidence 테이블                               | AI가 분석할 기업 자료 저장소, sourceType enum으로 분류     |
| BaseEntity 상속 2단계                                | createdAt만 필요한 테이블과 updatedAt도 필요한 테이블 구분 |
| setter 없이 update 메서드                            | 엔티티 불변성 유지, 변경 지점 추적 용이                    |

## 향후 로드맵

```
Phase 1 (현재): Mock API 7개 — 인터페이스 확정
Phase 2: Spring AI 연동 — 자소서 피드백, 면접 질문, 기업 리포트 설명
Phase 3: FastAPI 연동 — 적합도/합격률 정량 분석
Phase 4: 공공 API 연동 — 공고 자동 수집 + DART 기업 데이터
Phase 5: 비동기 전환 — AiStatus 활용 폴링 구조
```

## 트러블슈팅

### 1. data.sql 재시작 시 중복 키 에러README

서버를 재시작할 때마다 INSERT가 다시 실행되면서
`duplicate key value violates unique constraint` 에러 발생
모든 INSERT에 `ON CONFLICT (id) DO NOTHING`을 추가하여
이미 존재하는 데이터는 건너뛰도록 해결

### 2. JobPosting에서 company_name 컬럼 잔여

엔티티에서 companyName 필드를 삭제하고 Company FK로 교체했지만,
`ddl-auto: update` 설정에서는 기존 컬럼을 삭제하지 않아
`NOT NULL` 제약조건 에러 발생
Supabase에서 `ALTER TABLE job_posting DROP COLUMN company_name` 직접 실행으로 해결
개발 중에는 `ddl-auto: create-drop`으로 전환

### 3. @Builder.Default 누락으로 기본값 무시

ApplicationStatus에 `PREPARING` 기본값을 설정했지만
@Builder.Default 없이 Builder로 생성하면 null이 들어가서
DB NOT NULL 제약조건 위반.

### 4. relevanceScore 목록 vs 상세 불일치

Random으로 점수를 생성하니 같은 공고도 호출마다 점수가 달라지는 문제
postingId의 hashCode 기반 고정 점수 생성 유틸(MockScoreUtil)을 만들어
동일 공고 = 동일 점수로 해결

### 5. CoverLetter API 경로 단수/복수 혼용

조회/생성은 /cover-letter(단수), 수정/삭제는 /cover-letters(복수)로 경로가 달라서
FE에서 404 에러 발생
API 경로를 단수로 통일하여 해결

### 6. ApplicationStatus 확장 시 FE 미동기화

BE에서 지원 상태를 5단계에서 8단계(CODING_TEST, FIRST_INTERVIEW 등)로 확장했으나
FE 드롭다운에 반영이 안 돼 선택 불가능한 상태값이 존재
Enum 가이드 문서를 작성하여 FE 팀원에게 공유하고,
이후 상태값 변경은 Swagger 기준으로 동기화하는 규칙을 세움
