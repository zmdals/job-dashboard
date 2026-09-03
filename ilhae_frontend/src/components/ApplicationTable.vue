<script setup>
import StatusBadge from './StatusBadge.vue';

defineProps({
  applications: {
    type: Array,
    required: true,
  },
})
</script>

<template>
    <div class="app-table">
        <div class="table-header">
            <span>상태</span>
            <span>회사명・직무</span>
            <span>서류 발표일</span>
            <span>다음 일정</span>
            <span></span>
        </div>
        <div class="table-row" v-for="app in applications" :key="app.id">
            <StatusBadge :status="app.status" />
            <span>{{ app.company }}·{{ app.role }}</span>
            <span class="muted">{{ app.announceDate }}</span>
            <span class="highlight">{{ app.nextSchedule || '-' }}</span>
            <span class="detail">상세</span>
        </div>
    </div>
</template>

<style scoped>
.app-table {
    margin: 0 40px 40px;
    border: 1px solid #ececec;
    border-radius: 12px;
    overflow: hidden;
}

.table-header,
.table-row {
    display: grid;
    grid-template-columns: 110px 1fr 160px 160px 60px;
    align-items: center;
    gap: 12px;
    padding: 16px 20px;
}

.table-header {
    background: #f7f7f8;
    font-size: 13px;
    color: #9aa0a6;
}

.table-row {
    border-top: 1px solid #f0f0f0;
    font-size: 14px;
    color: #1a1d1f;
}

.table-row .muted {
    color: #9aa0a6;
}

.table-row .highlight {
    font-weight: 600;
}

.table-row .detail {
    color: #9aa0a6;
    font-size: 13px;
    text-align: right;
    cursor: pointer;
}
</style>