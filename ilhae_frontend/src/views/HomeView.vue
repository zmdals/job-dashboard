<script setup>
import ApplicationTable from '@/components/ApplicationTable.vue';
import StatCard from '@/components/StatCard.vue';
import {ref, computed} from 'vue';
const applications = ref([
    {
  id: 1,
  status: '1차면접',
  company: '네이버',
  role: '프론트엔드',
  announceDate: '2026-09-01',
  nextSchedule: '2026-09-10 면접',
},
    {
  id: 2,
  status: '지원완료',
  company: '카카오',
  role: '백엔드',
  announceDate: '2026-08-25',
  nextSchedule: null,
},
    {
  id: 3,
  status: '최종합격',
  company: '토스',
  role: '풀스택',
  announceDate: '2026-08-15',
  nextSchedule: '2026-09-05 입사 예정',
},

])

const selectedFilter = ref('전체 지원')
function selectFilter(label) {
    selectedFilter.value = label
}

const filterStatusMap = {
    '서류합격': ['코딩테스트 (필기시험)'],
    '면접 예정': ['1차면접', '2차면접', '최종면접'],
    '최종합격': ['최종합격'],
}

const stats = computed(() => [
    { label: '전체 지원', value: applications.value.length },
    { label: '서류합격', value: applications.value.filter(app => filterStatusMap['서류합격'].includes(app.status)).length },
    { label: '면접 예정', value: applications.value.filter(app => filterStatusMap['면접 예정'].includes(app.status)).length },
    { label: '최종합격', value: applications.value.filter(app => filterStatusMap['최종합격'].includes(app.status)).length },
])

const filteredApplications = computed(() => {
    if (selectedFilter.value === '전체 지원') {
        return applications.value
    }
    const matchStatuses = filterStatusMap[selectedFilter.value] || []
    return applications.value.filter(app => matchStatuses.includes(app.status))
})
</script>

<template>
    <div class="applicationStatus">
        <h3>지원현황</h3>
        <p>나의 취업 여정을 한눈에 확인해 보세요.</p>
    </div>
    <div class="status">
        <StatCard
            v-for="s in stats"
            :key="s.label"
            v-bind="s"
            :active="selectedFilter === s.label"
            @click="selectFilter(s.label)"
        ></StatCard>
    </div>
    <ApplicationTable :applications="filteredApplications"/>
</template>

<style scoped>
.applicationStatus {
    display: flex;
    align-items: baseline;
    gap: 10px;
    max-width: 1060px;
    margin: 0 auto;
    padding: 56px 24px 20px;
}

.applicationStatus h3 {
    margin: 0;
    font-size: 30px;
    font-weight: 700;
    letter-spacing: -.07em;
    color: #202124;
}

.applicationStatus p {
    margin: 0;
    font-size: 13px;
    color: #858585;
}

.status {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 12px;
    max-width: 1060px;
    margin: 0 auto;
    padding: 0 24px 24px;
}

.status :deep(.stat-card:first-child .value) {
    color: #e31b23;
}
</style>