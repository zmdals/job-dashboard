<script setup>
import ApplicationTable from '@/components/ApplicationTable.vue';
import StatCard from '@/components/StatCard.vue';
import {ref} from 'vue';
const stats = ref([
    { label: '지원완료', value: 12 },
    { label: '서류합격', value: 4 },
    { label: '면접 예정', value: 2 },
    { label: '최종합격', value: 1 },
])
const applications = ref([
    {
  id: 1,
  status: '서류합격',
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
</script>

<template>
    <div class="applicationStatus">
        <h3>지원현황</h3>
        <p>총 {{ stats.find(s=> s.label==='지원완료').value }}건</p>
    </div>
    <div class="status">
        <StatCard v-for="s in stats" :key="s.label" v-bind="s"></StatCard>
    </div>
    <ApplicationTable :applications="applications"/>
</template>

<style scoped>
.applicationStatus {
    display: flex;
    align-items: baseline;
    gap: 10px;
    padding: 32px 40px 20px;
}

.applicationStatus h3 {
    margin: 0;
    font-size: 20px;
    font-weight: 700;
    color: #1a1d1f;
}

.applicationStatus p {
    margin: 0;
    font-size: 13px;
    color: #9aa0a6;
}

.status {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 16px;
    padding: 0 40px 24px;
}
</style>