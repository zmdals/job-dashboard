<script setup>
import { computed, onMounted, ref } from 'vue'
import { storeToRefs } from 'pinia'
import ApplicationTable from '@/components/ApplicationTable.vue'
import StatCard from '@/components/StatCard.vue'
import { APPLICATION_STATUS_GROUPS } from '@/constants/applicationStatus'
import { useMeStore } from '@/stores/meStore'

const meStore = useMeStore()
const { applications, loading, error } = storeToRefs(meStore)
const selectedFilter = ref('전체 지원')

function selectFilter(label) {
  selectedFilter.value = label
}

const stats = computed(() => [
  { label: '전체 지원', value: applications.value.length },
  {
    label: '진행중',
    value: applications.value.filter((app) => APPLICATION_STATUS_GROUPS.진행중.includes(app.status))
      .length,
  },
  {
    label: '면접 예정',
    value: applications.value.filter((app) =>
      APPLICATION_STATUS_GROUPS['면접 예정'].includes(app.status),
    ).length,
  },
  {
    label: '최종합격',
    value: applications.value.filter((app) =>
      APPLICATION_STATUS_GROUPS.최종합격.includes(app.status),
    ).length,
  },
])

const sortedApplications = computed(() =>
  [...applications.value].sort((a, b) => {
    const aTime = Date.parse(a.updatedAt ?? a.createdAt ?? '') || 0
    const bTime = Date.parse(b.updatedAt ?? b.createdAt ?? '') || 0
    return bTime - aTime
  }),
)

const filteredApplications = computed(() => {
  if (selectedFilter.value === '전체 지원') return sortedApplications.value
  const statuses = APPLICATION_STATUS_GROUPS[selectedFilter.value] || []
  return sortedApplications.value.filter((app) => statuses.includes(app.status))
})

onMounted(() => {
  meStore.fetchApplications().catch(() => {})
})

async function updateStatus(application, status) {
  try {
    await meStore.updateApplicationStatus(application.id, status, application.memo)
  } catch {
    return
  }
}

async function removeApplication(applicationId) {
  try {
    await meStore.deleteApplication(applicationId)
  } catch {
    return
  }
}

function markAnalysisAvailable(applicationId) {
  meStore.markApplicationAnalyzed(applicationId)
}
function markCoverLetterAvailable(applicationId) {
  meStore.markApplicationHasCoverLetter(applicationId)
}
</script>

<template>
  <main class="home-page">
    <div class="application-status">
      <h3>지원현황</h3>
      <p>나의 취업 여정을 한눈에 확인해 보세요.</p>
    </div>
    <div class="status-cards">
      <StatCard
        v-for="stat in stats"
        :key="stat.label"
        v-bind="stat"
        :active="selectedFilter === stat.label"
        @click="selectFilter(stat.label)"
      />
    </div>
    <p v-if="loading && !applications.length" class="list-state">지원 내역을 불러오는 중입니다.</p>
    <p v-else-if="error && !applications.length" class="list-state error">
      지원 내역을 불러오지 못했습니다.
    </p>
    <ApplicationTable
      v-else
      :applications="filteredApplications"
      @update-status="updateStatus"
      @delete="removeApplication"
      @analysis-updated="markAnalysisAvailable"
      @cover-letter-created="markCoverLetterAvailable"
    />
  </main>
</template>

<style scoped>
.home-page {
  min-height: calc(100vh - 78px);
  background: #fafafa;
}

.application-status {
  display: flex;
  align-items: baseline;
  gap: 10px;
  max-width: var(--page-content-width);
  margin: 0 auto;
  padding: var(--page-top-space) var(--page-gutter) 20px;
}
.application-status h3 {
  margin: 0;
  color: #202124;
  font-size: 30px;
  font-weight: 700;
  letter-spacing: -0.07em;
}
.application-status p {
  margin: 0;
  color: #858585;
  font-size: 13px;
}
.status-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
  max-width: var(--page-content-width);
  margin: 0 auto;
  padding: 0 var(--page-gutter) 24px;
}
.status-cards :deep(.stat-card:first-child .value) {
  color: #e31b23;
}
.list-state {
  width: calc(100% - var(--page-gutter) * 2);
  max-width: calc(var(--page-content-width) - var(--page-gutter) * 2);
  margin: 0 auto 80px;
  padding: 42px 24px;
  border-top: 1px solid #333;
  color: #999;
  text-align: center;
}
.list-state.error {
  color: #e31b23;
}

@media (max-width: 760px) {
  .home-page {
    min-height: calc(100vh - 138px);
  }

  .application-status {
    display: block;
  }
  .application-status h3 {
    margin-bottom: 8px;
  }
  .status-cards {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 480px) {
  .application-status {
    padding-bottom: 18px;
  }

  .application-status h3 {
    font-size: 26px;
  }

  .application-status p {
    line-height: 1.55;
  }

  .status-cards {
    gap: 8px;
    padding-bottom: 28px;
  }

  .list-state {
    padding-right: 12px;
    padding-left: 12px;
  }
}
</style>
