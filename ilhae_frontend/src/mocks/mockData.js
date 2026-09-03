export const APPLICATION_STATUSES = [
  'PREPARING',
  'APPLIED',
  'IN_PROGRESS',
  'ACCEPTED',
  'REJECTED',
]

export const initialMockDb = {
  users: [
    {
      id: 'user-001',
      loginId: 'hong123',
      password: 'password123!',
      email: 'hong@example.com',
      name: '홍길동',
      role: 'USER',
      skills: ['Vue', 'JavaScript', 'Python'],
      certificates: [
        {
          id: 'cert-001',
          name: '정보처리기사',
          issuer: '한국산업인력공단',
          issuedAt: '2025-06-15',
          expiresAt: null,
          credentialId: null,
        },
      ],
    },
    {
      id: 'admin-001',
      loginId: 'admin',
      password: 'admin123!',
      email: 'admin@example.com',
      name: '관리자',
      role: 'ADMIN',
      skills: [],
      certificates: [],
    },
  ],

  companies: [
    { id: 'company-001', name: 'Example AI' },
    { id: 'company-002', name: 'Data Lab' },
    { id: 'company-003', name: 'Cloud Works' },
  ],

  postings: [
    {
      id: 'posting-001',
      title: 'Frontend Engineer',
      companyId: 'company-001',
      companyName: 'Example AI',
      relevanceScore: 86,
      location: '서울',
      jobType: 'FULL_TIME',
      experienceLevel: 'JUNIOR',
      techStack: ['Vue', 'TypeScript', 'JavaScript'],
      summary: 'Vue 기반 웹 서비스를 함께 개발할 프론트엔드 엔지니어를 모집합니다.',
      description:
        '제품 팀과 협업하여 Vue 기반 웹 애플리케이션을 개발하고 사용자 경험을 개선합니다.',
      requirements: [
        'JavaScript 기반 웹 개발 경험',
        'Vue 또는 유사 프레임워크 사용 경험',
        'REST API 연동 경험',
      ],
      preferredQualifications: [
        'TypeScript 사용 경험',
        '프론트엔드 테스트 경험',
      ],
      deadline: '2026-09-30T14:59:59Z',
      createdAt: '2026-08-25T00:00:00Z',
    },
    {
      id: 'posting-002',
      title: 'AI Platform Engineer',
      companyId: 'company-002',
      companyName: 'Data Lab',
      relevanceScore: 74,
      location: '판교',
      jobType: 'FULL_TIME',
      experienceLevel: 'JUNIOR',
      techStack: ['Python', 'FastAPI', 'Docker'],
      summary: 'AI 서비스용 백엔드와 데이터 파이프라인을 개발합니다.',
      description:
        '모델 서비스와 데이터 파이프라인을 안정적으로 운영하기 위한 플랫폼을 개발합니다.',
      requirements: [
        'Python 개발 경험',
        'HTTP API에 대한 기본 이해',
      ],
      preferredQualifications: [
        'FastAPI 경험',
        'Docker 기반 개발 경험',
      ],
      deadline: '2026-10-10T14:59:59Z',
      createdAt: '2026-08-28T00:00:00Z',
    },
    {
      id: 'posting-003',
      title: 'Web Frontend Intern',
      companyId: 'company-003',
      companyName: 'Cloud Works',
      relevanceScore: 68,
      location: '서울',
      jobType: 'INTERN',
      experienceLevel: 'ENTRY',
      techStack: ['Vue', 'JavaScript', 'CSS'],
      summary: '웹 프론트엔드 개발 인턴을 모집합니다.',
      description:
        'Vue 기반 서비스의 신규 화면 개발과 기존 화면 개선 업무를 수행합니다.',
      requirements: [
        'JavaScript 기본 문법 이해',
        'HTML/CSS 기본 지식',
      ],
      preferredQualifications: [
        'Vue 프로젝트 경험',
        '개인 또는 팀 프로젝트 경험',
      ],
      deadline: '2026-09-20T14:59:59Z',
      createdAt: '2026-08-30T00:00:00Z',
    },
  ],

  applications: [
    {
      id: 'application-001',
      userId: 'user-001',
      postingId: 'posting-002',
      status: 'APPLIED',
      appliedAt: '2026-09-01T05:10:00Z',
      updatedAt: '2026-09-01T05:10:00Z',
    },
  ],

  starredByUserId: {
    'user-001': ['posting-001', 'posting-003'],
    'admin-001': [],
  },

  postingInfoById: {
    'posting-001': {
      postingId: 'posting-001',
      company: {
        id: 'company-001',
        name: 'Example AI',
        description: 'AI 기반 웹 서비스를 개발하는 기업입니다.',
        homepageUrl: 'https://example.com',
      },
      papers: [
        {
          id: 'paper-001',
          title: 'Example Research Paper',
          url: 'https://example.com/papers/1',
          publishedAt: '2025-05-01',
        },
      ],
      articles: [
        {
          id: 'article-001',
          title: 'Example AI Engineering Blog',
          url: 'https://example.com/blog/1',
          publishedAt: '2026-01-20',
        },
      ],
    },
    'posting-002': {
      postingId: 'posting-002',
      company: {
        id: 'company-002',
        name: 'Data Lab',
        description: 'AI 인프라와 데이터 플랫폼을 개발하는 기업입니다.',
        homepageUrl: 'https://example.com/data-lab',
      },
      papers: [],
      articles: [],
    },
    'posting-003': {
      postingId: 'posting-003',
      company: {
        id: 'company-003',
        name: 'Cloud Works',
        description: '웹 기반 업무 서비스를 개발하는 기업입니다.',
        homepageUrl: 'https://example.com/cloud-works',
      },
      papers: [],
      articles: [],
    },
  },
}
