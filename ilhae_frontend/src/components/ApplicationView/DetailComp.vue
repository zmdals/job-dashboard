<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import CompanyReportCard from '@/components/LegoBox/CompanyReportCard.vue'

const route = useRoute()
const router = useRouter()

const REPORTS_BY_COMPANY_ID = {
  1: {
    companyName: 'SK텔레콤',
    resources: [
      {
        type: '특허',
        title: 'AI 기반 개인화 추천 기술',
        description: '내 프로필의 서비스 기획 경험과 연결되는 SK텔레콤의 최근 특허입니다.',
        relevance: 92,
      },
      {
        type: '논문',
        title: '초거대 AI 서비스의 사용자 경험',
        description: '서비스 기획 직무와 관련된 연구 주제를 AI가 선별했습니다.',
        relevance: 87,
      },
      {
        type: '보고서',
        title: '2026 ICT 산업 전망 보고서',
        description: '지원 직무와 기업의 사업 방향을 이해하는 데 도움이 되는 자료입니다.',
        relevance: 81,
      },
    ],
  },
  2: {
    companyName: '네이버',
    resources: [
      {
        type: '기술자료',
        title: '대규모 프론트엔드 서비스 설계',
        description: '내 프론트엔드 프로젝트 경험과 연결되는 네이버의 기술 자료입니다.',
        relevance: 90,
      },
      {
        type: '논문',
        title: '사용자 중심 웹 인터페이스 연구',
        description: '사용자 경험 개선 역량과 관련된 연구 주제를 선별했습니다.',
        relevance: 85,
      },
      {
        type: '보고서',
        title: '2026 디지털 플랫폼 동향',
        description: '플랫폼 산업과 지원 직무를 이해하는 데 도움이 되는 자료입니다.',
        relevance: 80,
      },
    ],
  },
  3: {
    companyName: '카카오',
    resources: [
      {
        type: '기술자료',
        title: '안정적인 대규모 API 운영',
        description: '내 백엔드 개발 경험과 연결되는 카카오의 기술 자료입니다.',
        relevance: 88,
      },
      {
        type: '논문',
        title: '분산 서비스의 데이터 처리',
        description: '백엔드 직무 역량과 관련된 연구 주제를 AI가 선별했습니다.',
        relevance: 83,
      },
      {
        type: '보고서',
        title: '2026 플랫폼 기술 전망',
        description: '기업의 기술 방향을 이해하는 데 도움이 되는 자료입니다.',
        relevance: 78,
      },
    ],
  },
}

const MOCK_COMPANY_ALIASES = {
  'company-001': 1,
  'company-002': 2,
  'company-003': 3,
}

const companyId = computed(() => String(route.params.companyId))
const report = computed(() => {
  const reportId = MOCK_COMPANY_ALIASES[companyId.value] ?? companyId.value

  return (
    REPORTS_BY_COMPANY_ID[reportId] ?? {
      companyName: `기업 ${companyId.value}`,
      resources: [
        {
          type: '특허',
          title: '직무 연관 특허 자료',
          description: '내 프로필과 연결되는 기업의 특허 자료가 이곳에 표시됩니다.',
          relevance: 90,
        },
        {
          type: '논문',
          title: '직무 연관 연구 자료',
          description: '지원 직무와 관련된 기업 연구 자료가 이곳에 표시됩니다.',
          relevance: 85,
        },
        {
          type: '보고서',
          title: '산업 전망 보고서',
          description: '기업의 사업 방향을 파악할 수 있는 보고서가 이곳에 표시됩니다.',
          relevance: 80,
        },
      ],
    }
  )
})

function goBackToPostings() {
  router.push({ name: 'postings' })
}
</script>

<template>
  <main class="report-page">
    <section class="report-content">
      <button class="report-back" type="button" @click="goBackToPostings">
        ← 채용공고로 돌아가기
      </button>

      <header class="report-title">
        <h2>{{ report.companyName }} 맞춤 기업 리포트</h2>
        <p>내 프로필과 관련성이 높은 기업 자료를 AI가 모아봤어요.</p>
      </header>

      <div class="report-grid">
        <CompanyReportCard
          v-for="resource in report.resources"
          :key="`${resource.type}-${resource.title}`"
          v-bind="resource"
        />
      </div>
    </section>
  </main>
</template>

<style scoped>
.report-page {
  min-height: calc(100vh - 78px);
  background: #fafafa;
}

.report-content {
  width: min(100% - 48px, 1060px);
  margin: 0 auto;
  padding: 48px 0 80px;
}

.report-back {
  margin-bottom: 24px;
  padding: 8px 0;
  border: 0;
  color: #777;
  background: none;
  font-size: 12px;
  cursor: pointer;
}

.report-back:hover,
.report-back:focus-visible {
  color: #e31b23;
}

.report-title {
  margin-bottom: 28px;
}

.report-title h2 {
  margin: 0 0 8px;
  font-size: 30px;
  letter-spacing: -0.07em;
}

.report-title p {
  margin: 0;
  color: #858585;
  font-size: 13px;
}

.report-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 16px;
}

@media (max-width: 760px) {
  .report-page {
    min-height: 100vh;
  }

  .report-content {
    width: calc(100% - 32px);
    padding-top: 36px;
  }

  .report-title h2 {
    font-size: 26px;
  }

  .report-grid {
    grid-template-columns: 1fr;
  }
}
</style>
