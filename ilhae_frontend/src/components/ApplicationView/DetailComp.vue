<script setup>
import { computed, watch } from 'vue'
import { storeToRefs } from 'pinia'
import { useRoute, useRouter } from 'vue-router'
import CompanyReportCard from '@/components/LegoBox/CompanyReportCard.vue'
import { useCompaniesStore } from '@/stores/companiesStore'

const route = useRoute()
const router = useRouter()
const companiesStore = useCompaniesStore()
const { loading, error } = storeToRefs(companiesStore)

const companyId = computed(() => String(route.params.companyId))
const company = computed(() => companiesStore.getCompany(companyId.value))
const evidences = computed(() => companiesStore.getCompanyEvidences(companyId.value))

watch(
  companyId,
  async (id) => {
    await Promise.allSettled([
      companiesStore.ensureCompany(id),
      companiesStore.ensureCompanyEvidences(id),
    ])
  },
  { immediate: true },
)

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
        <h2>{{ company?.name || '기업' }} 맞춤 기업 리포트</h2>
        <p>내 프로필과 관련성이 높은 기업 자료를 AI가 모아봤어요.</p>
      </header>

      <p v-if="loading && !evidences.length" class="report-state">
        맞춤 기업 자료를 불러오는 중입니다.
      </p>
      <p v-else-if="error && !evidences.length" class="report-state error">
        맞춤 기업 자료를 불러오지 못했습니다.
      </p>
      <p v-else-if="!evidences.length" class="report-state">등록된 기업 자료가 없습니다.</p>
      <div v-else class="report-grid">
        <CompanyReportCard v-for="evidence in evidences" :key="evidence.id" :evidence="evidence" />
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

.report-state {
  margin: 0;
  padding: 48px 20px;
  border-top: 1px solid #333;
  color: #999;
  text-align: center;
}

.report-state.error {
  color: #e31b23;
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
