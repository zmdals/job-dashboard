-- ============================================================
-- Seed Data for Job Dashboard
-- 위치: src/main/resources/data.sql
-- ON CONFLICT DO NOTHING → 이미 있으면 건너뜀 (재시작 안전)
-- ============================================================

-- 1. User
INSERT INTO
    users (
        user_id,
        email,
        password,
        name,
        phone_number,
        created_at,
        updated_at
    )
VALUES (
        1,
        'kim@test.com',
        '$2a$10$dummyhashedpassword1234567890',
        '김청년',
        '010-1234-5678',
        NOW(),
        NOW()
    ) ON CONFLICT (user_id) DO NOTHING;

-- 2. Company
INSERT INTO
    company (
        id,
        name,
        industry,
        description,
        url,
        dart_corp_code,
        created_at,
        updated_at
    )
VALUES (
        1,
        'SK텔레콤',
        'ICT/통신',
        'AI와 ICT로 더 나은 연결을 만드는 기업',
        'https://www.sktelecom.com',
        '00164779',
        NOW(),
        NOW()
    ),
    (
        2,
        '네이버',
        'IT/플랫폼',
        '대한민국 대표 검색·커머스·콘텐츠 플랫폼 기업',
        'https://www.navercorp.com',
        '00258801',
        NOW(),
        NOW()
    ),
    (
        3,
        '카카오',
        'IT/플랫폼',
        '기술과 사람이 만드는 더 나은 세상',
        'https://www.kakaocorp.com',
        '00352015',
        NOW(),
        NOW()
    ),
    (
        4,
        '현대자동차',
        '자동차/제조',
        '미래 모빌리티 솔루션 기업',
        'https://www.hyundai.com',
        '00164742',
        NOW(),
        NOW()
    ),
    (
        5,
        '토스',
        '핀테크',
        '금융을 혁신하는 모바일 금융 플랫폼',
        'https://toss.im',
        NULL,
        NOW(),
        NOW()
    ),
    (
        6,
        '삼성전자',
        '전자/반도체',
        '글로벌 전자·반도체·디스플레이 기업',
        'https://www.samsung.com',
        '00126380',
        NOW(),
        NOW()
    ) ON CONFLICT (id) DO NOTHING;

-- 3. CompanyEvidence
INSERT INTO
    company_evidence (
        id,
        company_id,
        source_type,
        title,
        url,
        published_at,
        content,
        is_official,
        created_at,
        updated_at
    )
VALUES (
        1,
        1,
        'PATENT',
        'AI 기반 개인화 추천 기술',
        'https://patents.example.com/skt-ai-001',
        '2026-05-15',
        'SK텔레콤이 출원한 AI 기반 사용자 맞춤형 콘텐츠 추천 시스템 특허. 사용자 행동 패턴을 분석하여 개인화된 서비스를 제공하는 기술.',
        true,
        NOW(),
        NOW()
    ),
    (
        2,
        1,
        'PRESS_RELEASE',
        'SKT, AI 고객센터 도입 확대',
        'https://news.sktelecom.com/ai-cs',
        '2026-07-20',
        'SK텔레콤은 2026년 하반기부터 AI 고객센터를 전 서비스에 확대 적용. 자연어 처리 기반 상담 자동화율 85% 달성.',
        true,
        NOW(),
        NOW()
    ),
    (
        3,
        1,
        'IR',
        'SK텔레콤 2026 상반기 실적 발표',
        'https://ir.sktelecom.com/2026-h1',
        '2026-08-01',
        '매출 9.2조원, 영업이익 1.1조원 달성. AI 사업 매출 전년비 40% 성장. 구독 서비스 가입자 1,500만 돌파.',
        true,
        NOW(),
        NOW()
    ),
    (
        4,
        1,
        'NEWS',
        'SKT, 글로벌 AI 얼라이언스 합류',
        'https://news.example.com/skt-ai-alliance',
        '2026-06-28',
        'SK텔레콤이 글로벌 AI 기업 연합에 합류하며 AI 윤리 가이드라인 공동 제정에 참여.',
        false,
        NOW(),
        NOW()
    ),
    (
        5,
        2,
        'TECH_BLOG',
        '네이버 검색 AI HyperCLOVA X 업데이트',
        'https://d2.naver.com/hyperclova-2026',
        '2026-06-10',
        'HyperCLOVA X 기반 검색 품질 개선 및 요약 기능 강화. 검색 정확도 15% 향상, 멀티모달 검색 지원.',
        true,
        NOW(),
        NOW()
    ),
    (
        6,
        2,
        'PAPER',
        '초거대 AI 서비스의 사용자 경험 연구',
        'https://arxiv.org/naver-ux-llm',
        '2026-04-22',
        '대규모 언어 모델 기반 서비스에서 사용자 만족도에 영향을 미치는 주요 요인 분석. 응답 속도와 정확도의 상관관계 연구.',
        true,
        NOW(),
        NOW()
    ),
    (
        7,
        2,
        'IR',
        '네이버 2026 Q2 실적 발표',
        'https://ir.navercorp.com/2026-q2',
        '2026-07-28',
        '매출 2.8조원. 커머스 부문 전년비 22% 성장. 클라우드 사업 흑자 전환.',
        true,
        NOW(),
        NOW()
    ),
    (
        8,
        2,
        'PATENT',
        '실시간 번역 기반 글로벌 쇼핑 시스템',
        'https://patents.example.com/naver-translate',
        '2026-03-05',
        '네이버 파파고 기술을 활용한 실시간 다국어 상품 검색 및 번역 쇼핑 시스템 특허.',
        true,
        NOW(),
        NOW()
    ),
    (
        9,
        3,
        'NEWS',
        '카카오, 2026 하반기 개발자 대규모 채용',
        'https://news.example.com/kakao-hire',
        '2026-08-10',
        '카카오가 하반기 신입·경력 개발자 200명 이상 채용 계획 발표. 백엔드, AI, 데이터 엔지니어링 분야 집중.',
        false,
        NOW(),
        NOW()
    ),
    (
        10,
        3,
        'TECH_BLOG',
        '카카오톡 메시징 시스템 아키텍처 공개',
        'https://tech.kakao.com/messaging-arch',
        '2026-05-20',
        '초당 50만 메시지 처리를 위한 분산 시스템 아키텍처. Kafka 기반 이벤트 드리븐 설계 사례 공유.',
        true,
        NOW(),
        NOW()
    ),
    (
        11,
        3,
        'PRESS_RELEASE',
        '카카오페이 해외 결제 서비스 확대',
        'https://press.kakaocorp.com/pay-global',
        '2026-07-15',
        '카카오페이 해외 결제 서비스를 동남아 5개국으로 확대. 연내 일본·유럽 추가 진출 계획.',
        true,
        NOW(),
        NOW()
    ),
    (
        12,
        4,
        'IR',
        '현대차 2026 전기차 전략 발표',
        'https://ir.hyundai.com/ev-2026',
        '2026-07-05',
        '2027년까지 전기차 라인업 10종 확대 및 자율주행 Level 3 상용화 계획. 배터리 내재화율 50% 목표.',
        true,
        NOW(),
        NOW()
    ),
    (
        13,
        4,
        'PATENT',
        '자율주행 차량 간 협력 주행 기술',
        'https://patents.example.com/hyundai-v2v',
        '2026-04-10',
        '차량 간 V2V 통신을 활용한 협력 주행 시스템 특허. 긴급 상황 시 주변 차량과 자동 협조 제동.',
        true,
        NOW(),
        NOW()
    ),
    (
        14,
        4,
        'NEWS',
        '현대차 소프트웨어 인력 2000명 채용 계획',
        'https://news.example.com/hyundai-sw',
        '2026-08-05',
        '현대자동차그룹이 SDV 전환을 위해 2027년까지 소프트웨어 인력 2000명을 추가 채용한다고 발표.',
        false,
        NOW(),
        NOW()
    ),
    (
        15,
        6,
        'PATENT',
        '반도체 3nm GAA 공정 최적화 기술',
        'https://patents.example.com/samsung-3nm',
        '2026-03-18',
        '삼성전자 3nm GAA 공정의 수율 향상을 위한 신규 특허. 트랜지스터 밀도 30% 개선.',
        true,
        NOW(),
        NOW()
    ),
    (
        16,
        6,
        'IR',
        '삼성전자 2026 반도체 투자 계획',
        'https://ir.samsung.com/2026-invest',
        '2026-06-20',
        'HBM4 양산 투자 10조원 확정. 파운드리 2nm 공정 2027년 양산 목표.',
        true,
        NOW(),
        NOW()
    ),
    (
        17,
        6,
        'PAPER',
        'On-Device AI를 위한 경량화 모델 연구',
        'https://arxiv.org/samsung-ondevice-ai',
        '2026-05-08',
        '모바일 기기에서 대규모 언어 모델을 효율적으로 실행하기 위한 양자화 및 프루닝 기법 연구.',
        true,
        NOW(),
        NOW()
    ) ON CONFLICT (id) DO NOTHING;

-- 4. JobPosting
INSERT INTO
    job_posting (
        id,
        company_id,
        title,
        url,
        job_type,
        location,
        annual_income,
        deadline,
        description,
        created_at
    )
VALUES (
        1,
        1,
        '서비스 기획 · 신입',
        'https://recruit.sktelecom.com/job/1001',
        '정규직',
        '서울',
        '4000만원',
        '2026-09-18',
        '고객 경험을 개선하는 서비스 기획과 신규 기능의 기획, 운영을 담당합니다.',
        NOW()
    ),
    (
        2,
        2,
        '프론트엔드 개발 · 신입',
        'https://recruit.navercorp.com/job/2001',
        '정규직',
        '성남',
        '5000만원',
        '2026-09-12',
        '네이버 서비스의 프론트엔드 개발 및 사용자 경험 개선을 담당합니다.',
        NOW()
    ),
    (
        3,
        3,
        '백엔드 개발 · 신입',
        'https://recruit.kakaocorp.com/job/3001',
        '정규직',
        '판교',
        '5000만원',
        '2026-09-25',
        'Spring Boot 기반 대규모 트래픽 처리 백엔드 시스템을 개발합니다.',
        NOW()
    ),
    (
        4,
        4,
        '데이터 분석 · 신입',
        'https://recruit.hyundai.com/job/4001',
        '정규직',
        '서울',
        '4500만원',
        '2026-10-02',
        '자동차 생산·판매 데이터를 분석하여 비즈니스 인사이트를 도출합니다.',
        NOW()
    ),
    (
        5,
        5,
        '프로덕트 매니저 · 신입',
        'https://toss.im/career/5001',
        '정규직',
        '서울',
        '5500만원',
        '2026-10-05',
        '토스 금융 서비스의 프로덕트를 기획하고 성장시킵니다.',
        NOW()
    ),
    (
        6,
        6,
        'UX 리서처 · 신입',
        'https://careers.samsung.com/job/6001',
        '정규직',
        '수원',
        '4500만원',
        '2026-10-10',
        '사용자 리서치를 통해 제품의 사용성을 개선합니다.',
        NOW()
    ),
    (
        7,
        1,
        '데이터 엔지니어 · 신입',
        'https://recruit.sktelecom.com/job/1002',
        '정규직',
        '서울',
        '4800만원',
        '2026-10-15',
        '대규모 데이터를 안정적으로 수집하고 분석 플랫폼을 구축합니다.',
        NOW()
    ),
    (
        8,
        2,
        '백엔드 개발 · 경력',
        'https://recruit.navercorp.com/job/2002',
        '정규직',
        '성남',
        '6000만원',
        '2026-10-20',
        '고트래픽 서비스의 API와 서버 시스템을 개발합니다.',
        NOW()
    ),
    (
        9,
        3,
        'AI 서비스 기획 · 신입',
        'https://recruit.kakaocorp.com/job/3002',
        '정규직',
        '판교',
        '5200만원',
        '2026-10-18',
        'AI 기술을 활용한 신규 서비스의 기획과 운영을 담당합니다.',
        NOW()
    ),
    (
        10,
        4,
        '프론트엔드 개발 · 경력',
        'https://recruit.hyundai.com/job/4002',
        '정규직',
        '서울',
        '5800만원',
        '2026-10-22',
        '차량 서비스의 웹 프론트엔드와 사용자 경험을 개선합니다.',
        NOW()
    ),
    (
        11,
        5,
        '데이터 분석가 · 경력',
        'https://toss.im/career/5002',
        '정규직',
        '서울',
        '6500만원',
        '2026-10-25',
        '금융 데이터를 분석해 서비스 성장에 필요한 인사이트를 제공합니다.',
        NOW()
    ),
    (
        12,
        6,
        '서비스 디자이너 · 신입',
        'https://careers.samsung.com/job/6002',
        '정규직',
        '수원',
        '4700만원',
        '2026-10-28',
        '제품과 서비스 전반의 사용자 경험을 설계하고 개선합니다.',
        NOW()
    ) ON CONFLICT (id) DO NOTHING;

-- 5. JobRequirement
INSERT INTO
    job_requirement (
        id,
        job_posting_id,
        requirement_type,
        requirement_category,
        content,
        created_at
    )
VALUES (
        1,
        1,
        'REQUIRED',
        'CAREER',
        '서비스 기획 경험 또는 프로젝트 경험',
        NOW()
    ),
    (
        2,
        1,
        'REQUIRED',
        'SKILL',
        '데이터 기반 문제 정의 및 해결 능력',
        NOW()
    ),
    (
        3,
        1,
        'PREFERRED',
        'SKILL',
        'IT 서비스 및 플랫폼에 대한 이해도',
        NOW()
    ),
    (
        4,
        1,
        'PREFERRED',
        'CERTIFICATE',
        '관련 자격증 보유자',
        NOW()
    ),
    (
        5,
        2,
        'REQUIRED',
        'SKILL',
        'JavaScript/TypeScript 능숙',
        NOW()
    ),
    (
        6,
        2,
        'REQUIRED',
        'SKILL',
        'React 또는 Vue.js 프레임워크 경험',
        NOW()
    ),
    (
        7,
        2,
        'PREFERRED',
        'CAREER',
        '웹 프론트엔드 개발 인턴 이상 경험',
        NOW()
    ),
    (
        8,
        3,
        'REQUIRED',
        'SKILL',
        'Java/Spring Boot 개발 경험',
        NOW()
    ),
    (
        9,
        3,
        'REQUIRED',
        'SKILL',
        'RDBMS 설계 및 SQL 작성 능력',
        NOW()
    ),
    (
        10,
        3,
        'PREFERRED',
        'EDUCATION',
        '컴퓨터공학 관련 전공',
        NOW()
    ),
    (
        11,
        3,
        'PREFERRED',
        'CERTIFICATE',
        '정보처리기사 보유',
        NOW()
    ),
    (
        12,
        4,
        'REQUIRED',
        'SKILL',
        'Python, SQL 활용 데이터 분석 경험',
        NOW()
    ),
    (
        13,
        4,
        'PREFERRED',
        'SKILL',
        'Tableau, PowerBI 등 시각화 도구 사용 경험',
        NOW()
    ),
    (
        14,
        5,
        'REQUIRED',
        'SKILL',
        '프로덕트 기획 또는 서비스 운영 경험',
        NOW()
    ),
    (
        15,
        5,
        'PREFERRED',
        'CAREER',
        '핀테크 또는 금융 서비스 경험',
        NOW()
    ),
    (
        16,
        6,
        'REQUIRED',
        'EDUCATION',
        'HCI, 심리학, 디자인 관련 전공',
        NOW()
    ),
    (
        17,
        6,
        'PREFERRED',
        'SKILL',
        '정성·정량 리서치 방법론 활용 경험',
        NOW()
    ) ON CONFLICT (id) DO NOTHING;

-- 6. Education
INSERT INTO
    education (
        id,
        user_id,
        school_name,
        degree,
        major,
        start_date,
        end_date,
        status,
        created_at
    )
VALUES (
        1,
        1,
        '서울대학교',
        '학사',
        '컴퓨터공학과',
        '2019-03-01',
        '2025-02-28',
        'GRADUATED',
        NOW()
    ),
    (
        2,
        1,
        '한국외국어대학교',
        '학사',
        '영어학과',
        '2015-03-01',
        '2019-02-28',
        'GRADUATED',
        NOW()
    ) ON CONFLICT (id) DO NOTHING;

-- 7. Career
INSERT INTO
    career (
        id,
        user_id,
        company_name,
        position,
        start_date,
        end_date,
        description,
        created_at
    )
VALUES (
        1,
        1,
        '스타트업 A',
        '서비스 기획 인턴',
        '2024-07-01',
        '2024-12-31',
        '신규 서비스 기획 및 사용자 피드백 분석, 와이어프레임 작성',
        NOW()
    ),
    (
        2,
        1,
        '네이버 부스트캠프',
        '웹 풀스택 교육생',
        '2025-06-01',
        '2025-11-30',
        'JavaScript/React/Node.js 기반 풀스택 프로젝트 수행',
        NOW()
    ) ON CONFLICT (id) DO NOTHING;

-- 8. Certificate
INSERT INTO
    certificate (
        id,
        user_id,
        cert_name,
        issuer,
        acquired_date,
        language_score,
        created_at
    )
VALUES (
        1,
        1,
        '정보처리기사',
        '한국산업인력공단',
        '2024-06-15',
        NULL,
        NOW()
    ),
    (
        2,
        1,
        'SQLD',
        '한국데이터산업진흥원',
        '2024-03-20',
        NULL,
        NOW()
    ),
    (
        3,
        1,
        'TOEIC',
        'ETS',
        '2025-01-10',
        885,
        NOW()
    ) ON CONFLICT (id) DO NOTHING;

-- 9. Award
INSERT INTO
    award (
        id,
        user_id,
        award_name,
        organizer,
        award_date,
        description,
        created_at
    )
VALUES (
        1,
        1,
        '교내 창업 경진대회 우수상',
        '서울대학교',
        '2023-11-15',
        'AI 기반 취업 매칭 서비스 아이디어로 수상',
        NOW()
    ),
    (
        2,
        1,
        '해커톤 최우수상',
        'SKALA 부트캠프',
        '2026-07-20',
        '3일간 풀스택 웹 서비스 설계 및 구현',
        NOW()
    ) ON CONFLICT (id) DO NOTHING;

-- 10. Project
INSERT INTO
    project (
        id,
        user_id,
        project_name,
        role,
        tech_stack,
        description,
        created_at
    )
VALUES (
        1,
        1,
        '취업 대시보드 서비스',
        '백엔드 개발',
        'Spring Boot, PostgreSQL, Vue.js',
        '취업 지원 전 과정을 관리하고 AI 분석을 제공하는 대시보드 웹 서비스',
        NOW()
    ),
    (
        2,
        1,
        '배드민턴 동호회 관리앱',
        '풀스택 개발',
        'Spring Boot, Thymeleaf, MySQL',
        '직장 내 배드민턴 동호회 출석·대회 관리 웹 애플리케이션',
        NOW()
    ) ON CONFLICT (id) DO NOTHING;

-- 11. Application (applied_date 제외)
INSERT INTO
    application (
        id,
        user_id,
        job_posting_id,
        application_status,
        memo,
        created_at,
        updated_at
    )
VALUES (
        1,
        1,
        1,
        'APPLIED',
        NULL,
        NOW(),
        NOW()
    ),
    (
        2,
        1,
        2,
        'FIRST_INTERVIEW',
        '1차 면접 통과',
        NOW(),
        NOW()
    ),
    (
        3,
        1,
        3,
        'ACCEPTED',
        '최종 합격!',
        NOW(),
        NOW()
    ),
    (
        4,
        1,
        4,
        'PREPARING',
        NULL,
        NOW(),
        NOW()
    ),
    (
        5,
        1,
        5,
        'CODING_TEST',
        '코테 일정 9/5',
        NOW(),
        NOW()
    ),
    (
        6,
        1,
        6,
        'SECOND_INTERVIEW',
        '2차 면접 대기',
        NOW(),
        NOW()
    ) ON CONFLICT (id) DO NOTHING;

-- 12. CoverLetter
INSERT INTO
    cover_letter (
        id,
        application_id,
        title,
        content,
        ai_feedback,
        ai_status,
        version,
        created_at
    )
VALUES (
        1,
        1,
        'SK텔레콤 서비스 기획 지원 자기소개서',
        '고객의 문제를 발견하고 데이터 기반으로 서비스를 개선한 경험이 있습니다. 프로젝트에서 사용자 행동 데이터를 분석해 핵심 이탈 구간을 찾았고, 개선안을 제안하여 서비스 이용률을 높였습니다. 이러한 문제 정의와 실행 경험을 바탕으로 SK텔레콤의 고객 경험을 더욱 발전시키고 싶습니다.',
        NULL,
        'PENDING',
        1,
        NOW()
    ),
    (
        2,
        2,
        '네이버 프론트엔드 개발 지원 자기소개서',
        '사용자가 편리하게 서비스를 이용할 수 있도록 고민하며 프론트엔드 개발 역량을 쌓았습니다. Vue.js와 JavaScript를 활용해 컴포넌트를 설계하고, API 연동 과정에서 발생한 상태 관리 문제를 해결했습니다. 협업 과정에서는 명확한 인터페이스와 코드 리뷰를 통해 팀의 개발 속도와 품질을 함께 높였습니다.',
        '직무에 필요한 프론트엔드 경험과 협업 역량이 잘 드러납니다.',
        'COMPLETED',
        1,
        NOW()
    ),
    (
        3,
        3,
        '카카오 백엔드 개발 지원 자기소개서',
        'Spring Boot 기반 프로젝트에서 사용자 인증, 지원 내역 관리, 검색 API를 개발했습니다. 트랜잭션 처리와 데이터베이스 관계를 설계할 때 기능 구현뿐 아니라 예외 상황과 확장 가능성까지 고려했습니다. 카카오의 대규모 서비스 환경에서 안정적인 백엔드 시스템을 만드는 개발자로 성장하고 싶습니다.',
        '기술 경험과 지원 직무의 연결성이 좋습니다.',
        'COMPLETED',
        1,
        NOW()
    ) ON CONFLICT (id) DO NOTHING;

-- 13. StarredPosting
INSERT INTO
    starred_posting (
        id,
        user_id,
        job_posting_id,
        created_at
    )
VALUES (1, 1, 2, NOW()),
    (2, 1, 5, NOW()) ON CONFLICT (id) DO NOTHING;

-- 시퀀스 초기화 (PostgreSQL - 다음 INSERT 시 ID 충돌 방지)
SELECT setval (
        pg_get_serial_sequence ('users', 'user_id'), GREATEST(
            (
                SELECT MAX(user_id)
                FROM users
            ), 1
        )
    );

SELECT setval (
        pg_get_serial_sequence ('company', 'id'), GREATEST(
            (
                SELECT MAX(id)
                FROM company
            ), 1
        )
    );

SELECT setval (
        pg_get_serial_sequence ('company_evidence', 'id'), GREATEST(
            (
                SELECT MAX(id)
                FROM company_evidence
            ), 1
        )
    );

SELECT setval (
        pg_get_serial_sequence ('job_posting', 'id'), GREATEST(
            (
                SELECT MAX(id)
                FROM job_posting
            ), 1
        )
    );

SELECT setval (
        pg_get_serial_sequence ('job_requirement', 'id'), GREATEST(
            (
                SELECT MAX(id)
                FROM job_requirement
            ), 1
        )
    );

SELECT setval (
        pg_get_serial_sequence ('education', 'id'), GREATEST(
            (
                SELECT MAX(id)
                FROM education
            ), 1
        )
    );

SELECT setval (
        pg_get_serial_sequence ('career', 'id'), GREATEST(
            (
                SELECT MAX(id)
                FROM career
            ), 1
        )
    );

SELECT setval (
        pg_get_serial_sequence ('certificate', 'id'), GREATEST(
            (
                SELECT MAX(id)
                FROM certificate
            ), 1
        )
    );

SELECT setval (
        pg_get_serial_sequence ('award', 'id'), GREATEST(
            (
                SELECT MAX(id)
                FROM award
            ), 1
        )
    );

SELECT setval (
        pg_get_serial_sequence ('project', 'id'), GREATEST(
            (
                SELECT MAX(id)
                FROM project
            ), 1
        )
    );

SELECT setval (
        pg_get_serial_sequence ('application', 'id'), GREATEST(
            (
                SELECT MAX(id)
                FROM application
            ), 1
        )
    );

SELECT setval (
        pg_get_serial_sequence ('cover_letter', 'id'), GREATEST(
            (
                SELECT MAX(id)
                FROM cover_letter
            ), 1
        )
    );

SELECT setval (
        pg_get_serial_sequence ('starred_posting', 'id'), GREATEST(
            (
                SELECT MAX(id)
                FROM starred_posting
            ), 1
        )
    );