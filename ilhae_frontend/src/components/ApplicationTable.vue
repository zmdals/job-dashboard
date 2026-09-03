<script setup>
import { ref } from 'vue'
import {
  APPLICATION_STATUS_GROUPS,
  APPLICATION_STATUS_OPTIONS,
  APPLICATION_STATUS_TRANSITIONS,
} from '@/constants/applicationStatus'
import ApplicationAnalysisModal from '@/components/LegoBox/ApplicationAnalysisModal.vue'
import ApplicationPreparationModal from '@/components/LegoBox/ApplicationPreparationModal.vue'

const props = defineProps({
  applications: {
    type: Array,
    required: true,
  },
})

const emit = defineEmits([
  'updateStatus',
  'delete',
  'analysisUpdated',
  'coverLetterCreated',
])

const selectedApplication = ref(null)
const preparationMode = ref('cover-letter')
const selectedAnalysisApplication = ref(null)
const analysisInitialAction = ref('view')

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

function canViewInterviewQuestions(status) {
  return APPLICATION_STATUS_GROUPS['면접 예정'].includes(status)
}

function openPreparation(application, mode) {
  selectedApplication.value = application
  preparationMode.value = mode
}

function closePreparation() {
  selectedApplication.value = null
}

function openAnalysis(application) {
  selectedAnalysisApplication.value = application
  analysisInitialAction.value = application.hasAnalysis ? 'view' : 'create'
}

function closeAnalysis() {
  selectedAnalysisApplication.value = null
}

function handleAnalysisUpdated(applicationId) {
  emit('analysisUpdated', applicationId)
}

function handleCoverLetterCreated(applicationId) {
  emit('coverLetterCreated', applicationId)
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
      <div class="preparation-actions">
        <button type="button" @click="openPreparation(app, 'cover-letter')">자기소개서</button>
        <button
          type="button"
          :disabled="!canViewInterviewQuestions(app.status)"
          :title="canViewInterviewQuestions(app.status) ? '' : '면접 예정 단계에서 확인할 수 있습니다.'"
          @click="openPreparation(app, 'interview')"
        >
          예상 면접 질문
        </button>
        <button class="analysis-button" type="button" @click="openAnalysis(app)">
          {{ app.hasAnalysis ? '분석 결과 보기' : 'AI 분석하기' }}
        </button>
      </div>
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

  <ApplicationPreparationModal
    v-if="selectedApplication"
    :application="selectedApplication"
    :mode="preparationMode"
    @close="closePreparation"
    @cover-letter-created="handleCoverLetterCreated"
  />

  <ApplicationAnalysisModal
    v-if="selectedAnalysisApplication"
    :application="selectedAnalysisApplication"
    :initial-action="analysisInitialAction"
    @close="closeAnalysis"
    @analyzed="handleAnalysisUpdated"
  />
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
    grid-template-columns: 1.3fr .9fr 1.5fr minmax(110px, 1fr) 42px;
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

.company-meta {
    color: #999;
    font-size: 11px;
}

.preparation-actions {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 6px;
}

.preparation-actions .analysis-button {
  border-color: #e31b23;
  color: #e31b23;
}

.preparation-actions button {
  min-height: 32px;
  padding: 6px 7px;
  border: 1px solid #ddd;
  border-radius: 5px;
  color: #666;
  background: #fff;
  font-size: 10px;
  cursor: pointer;
}

.preparation-actions button:hover:not(:disabled),
.preparation-actions button:focus-visible:not(:disabled) {
  border-color: #e31b23;
  color: #e31b23;
}

.preparation-actions button:disabled {
  color: #bbb;
  background: #f5f5f5;
  cursor: not-allowed;
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
