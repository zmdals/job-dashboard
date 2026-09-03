<script setup>
import {
  APPLICATION_STATUS_OPTIONS,
  APPLICATION_STATUS_TRANSITIONS,
} from '@/constants/applicationStatus'

const props = defineProps({
  applications: {
    type: Array,
    required: true,
  },
})

const emit = defineEmits(['updateStatus', 'delete'])

function formatDate(value) {
  if (!value) return '—'
  return String(value).slice(0, 10).replaceAll('-', '.')
}

function statusOptionsFor(currentStatus) {
  const availableStatuses = new Set([
    currentStatus,
    ...(APPLICATION_STATUS_TRANSITIONS[currentStatus] ?? []),
  ])

  return APPLICATION_STATUS_OPTIONS.filter(({ value }) => availableStatuses.has(value))
}

function isTerminalStatus(status) {
  return (APPLICATION_STATUS_TRANSITIONS[status] ?? []).length === 0
}

function updateStatus(application, event) {
  emit('updateStatus', application, event.target.value)
}
</script>

<template>
  <div class="app-head">
    <h3>내 지원 목록</h3>
    <span>총 {{ props.applications.length }}개</span>
  </div>
  <div class="app-table">
    <p v-if="!props.applications.length" class="empty-state">표시할 지원 내역이 없습니다.</p>
    <div v-for="app in props.applications" v-else :key="app.id" class="table-row">
      <div class="company-name">
        <span class="company-logo">{{ app.companyName?.slice(0, 1) || '?' }}</span>
        {{ app.companyName || '회사명 미정' }}
      </div>
      <div class="company-meta">{{ app.jobTitle || '채용공고' }}</div>
      <div class="deadline">지원일 {{ formatDate(app.createdAt) }}</div>
      <select
        class="status-select"
        :value="app.status"
        :aria-label="`${app.companyName || '회사'} 지원 상태`"
        :disabled="isTerminalStatus(app.status)"
        @change="updateStatus(app, $event)"
      >
        <option
          v-for="option in statusOptionsFor(app.status)"
          :key="option.value"
          :value="option.value"
        >
          {{ option.label }}
        </option>
      </select>
      <button class="delete-btn" type="button" @click="emit('delete', app.id)">삭제</button>
    </div>
  </div>
</template>

<style scoped>
.app-head {
    display: flex;
    align-items: center;
    justify-content: space-between;
    max-width: 1060px;
    margin: 0 auto;
    padding: 0 24px 14px;
}

.app-head h3 {
    margin: 0;
    font-size: 17px;
    letter-spacing: -.05em;
    color: #202124;
}

.app-head span {
    color: #999;
    font-size: 11px;
}

.app-table {
    max-width: 1060px;
    margin: 0 auto 80px;
    padding: 0 24px;
    border-top: 1px solid #333;
    background: #fff;
}

.empty-state {
  margin: 0;
  padding: 42px 22px;
  border-bottom: 1px solid #e7e7e7;
  color: #999;
  font-size: 12px;
  text-align: center;
}

.table-row {
    display: grid;
    grid-template-columns: 1.45fr 1fr 1fr minmax(110px, 1fr) 42px;
    align-items: center;
    gap: 20px;
    min-height: 78px;
    padding: 0 22px;
    border-bottom: 1px solid #e7e7e7;
}

.company-name {
    display: flex;
    align-items: center;
    gap: 12px;
    font-size: 13px;
    font-weight: 700;
    color: #202124;
}

.company-logo {
    display: grid;
    place-items: center;
    width: 34px;
    height: 34px;
    border-radius: 8px;
    color: #555;
    background: #f0f0f0;
    font-size: 11px;
}

.company-meta,
.deadline {
    color: #999;
    font-size: 11px;
}

.status-select {
    width: 100%;
    padding: 8px 9px;
    border: 1px solid #ddd;
    border-radius: 5px;
    color: #555;
    background: #fff;
    font-size: 11px;
}

.status-select:focus {
    border-color: #e31b23;
    outline: 0;
}

.status-select:disabled {
  color: #999;
  background: #f5f5f5;
  cursor: default;
}

.delete-btn {
    padding: 0;
    border: 0;
    color: #999;
    background: none;
    font-size: 11px;
    cursor: pointer;
}

.delete-btn:hover {
    color: #e31b23;
}
</style>
