<script setup>
import { computed, onMounted, ref } from 'vue'
import ModalBox from '@/components/LegoBox/ModalBox.vue'
import { api } from '@/api/client'

const props = defineProps({
  application: {
    type: Object,
    required: true,
  },
  initialAction: {
    type: String,
    default: 'view',
    validator: (value) => ['create', 'view'].includes(value),
  },
})

const emit = defineEmits(['close', 'analyzed'])

const analysis = ref(null)
const loading = ref(false)
const reanalyzing = ref(false)
const checkingCoverLetter = ref(true)
const hasCoverLetter = ref(false)
const errorMessage = ref('')

const subtitle = computed(() =>
  [props.application.companyName, props.application.jobTitle].filter(Boolean).join(' · '),
)

async function createAnalysis(includeCoverLetter = true, isReanalysis = false) {
  if (isReanalysis) {
    reanalyzing.value = true
  } else {
    loading.value = true
  }
  errorMessage.value = ''

  console.info('[AI 분석 옵션]', {
    applicationId: props.application.id,
    includeCoverLetter,
  })

  try {
    analysis.value = await api.requestApplicationAnalysis(props.application.id, includeCoverLetter)
    emit('analyzed', props.application.id)
  } catch (error) {
    errorMessage.value = error.message || 'AI 분석을 요청하지 못했습니다.'
  } finally {
    loading.value = false
    reanalyzing.value = false
  }
}

async function getAnalysis() {
  loading.value = true
  errorMessage.value = ''

  try {
    analysis.value = await api.getApplicationAnalysis(props.application.id)
  } catch (error) {
    errorMessage.value = error.message || '분석 결과를 불러오지 못했습니다.'
  } finally {
    loading.value = false
  }
}

async function checkCoverLetter() {
  checkingCoverLetter.value = true

  try {
    const coverLetter = await api.getCoverLetter(props.application.id)
    hasCoverLetter.value = coverLetter?.id != null
  } catch {
    hasCoverLetter.value = false
  } finally {
    checkingCoverLetter.value = false
  }
}

function loadInitialAnalysis() {
  if (props.initialAction === 'create') {
    return createAnalysis(true)
  }

  return getAnalysis()
}

onMounted(async () => {
  await checkCoverLetter()
  await loadInitialAnalysis()
})
</script>

<template>
  <ModalBox title="AI 지원 적합도 분석" :subtitle="subtitle" wide @close="emit('close')">
    <p v-if="loading" class="state">AI 분석 결과를 불러오는 중입니다.</p>

    <div v-else-if="errorMessage" class="state error" role="alert">
      <p>{{ errorMessage }}</p>
      <button type="button" @click="loadInitialAnalysis">다시 시도</button>
    </div>

    <div v-else-if="analysis" class="analysis-layout">
      <section class="score-panel">
        <small>종합 적합도</small>
        <strong>{{ analysis.score }}<span>%</span></strong>
        <p>{{ analysis.summary }}</p>
        <span v-if="analysis.includedCoverLetter" class="included-label">자기소개서 포함</span>
      </section>

      <div class="analysis-details">
        <section class="result-section strength">
          <h4>강점</h4>
          <ul v-if="analysis.strengths?.length">
            <li v-for="item in analysis.strengths" :key="item">{{ item }}</li>
          </ul>
          <p v-else>분석된 강점이 없습니다.</p>
        </section>

        <section class="result-section weakness">
          <h4>보완할 점</h4>
          <ul v-if="analysis.weaknesses?.length">
            <li v-for="item in analysis.weaknesses" :key="item">{{ item }}</li>
          </ul>
          <p v-else>분석된 보완점이 없습니다.</p>
        </section>

        <section v-if="analysis.recommendation" class="result-section recommendation">
          <h4>AI 추천</h4>
          <p>{{ analysis.recommendation }}</p>
        </section>

        <p v-if="analysis.includedCoverLetter === false" class="cover-letter-guide">
          자기소개서를 작성하면 점수가 올라갈 수 있어요. 작성 후 다시 분석해 보세요.
        </p>
      </div>
    </div>

    <template #actions>
      <button
        type="button"
        class="close-action"
        :class="{ 'full-width': !hasCoverLetter }"
        @click="emit('close')"
      >
        닫기
      </button>
      <button
        v-if="analysis && hasCoverLetter"
        class="primary"
        type="button"
        :disabled="reanalyzing"
        @click="createAnalysis(true, true)"
      >
        {{ reanalyzing ? '재분석 중...' : '자기소개서 포함 재분석' }}
      </button>
    </template>
  </ModalBox>
</template>

<style scoped>
.analysis-layout {
  display: grid;
  grid-template-columns: minmax(190px, 0.7fr) minmax(0, 1.5fr);
  gap: 18px;
}

.score-panel,
.result-section {
  border: 1px solid #e7e7e7;
  border-radius: 8px;
  background: #fff;
}

.score-panel {
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 24px;
  background: #fff5f5;
  text-align: center;
}

.score-panel small {
  color: #777;
  font-size: 11px;
}

.score-panel strong {
  margin: 8px 0 12px;
  color: #e31b23;
  font-size: 48px;
}

.score-panel strong span {
  margin-left: 3px;
  font-size: 15px;
}

.score-panel p,
.result-section p,
.result-section li {
  color: #666;
  font-size: 12px;
  line-height: 1.7;
}

.score-panel p,
.result-section p {
  margin: 0;
}

.included-label {
  align-self: center;
  margin-top: 14px;
  padding: 5px 9px;
  border-radius: 999px;
  color: #9c171d;
  background: #ffe2e3;
  font-size: 10px;
  font-weight: 700;
}

.close-action.full-width {
  width: 100%;
  flex-basis: 100%;
}

.analysis-details {
  display: grid;
  gap: 12px;
}

.result-section {
  padding: 17px 18px;
}

.result-section h4 {
  margin: 0 0 9px;
  font-size: 13px;
}

.result-section ul {
  display: grid;
  gap: 5px;
  margin: 0;
  padding-left: 18px;
}

.strength li::marker {
  color: #1f6b4a;
}

.weakness li::marker {
  color: #e31b23;
}

.recommendation {
  border-color: #eadfca;
  background: #fffaf0;
}

.cover-letter-guide {
  margin: 0;
  padding: 13px 15px;
  border-radius: 6px;
  color: #805d1e;
  background: #fff6df;
  font-size: 11px;
}

.state {
  margin: 0;
  padding: 45px 0;
  text-align: center;
}

.state.error {
  color: #e31b23;
}

.state.error p {
  margin: 0;
}

.state.error button {
  margin-top: 12px;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 5px;
  background: #fff;
  cursor: pointer;
}

@media (max-width: 760px) {
  .analysis-layout {
    grid-template-columns: 1fr;
  }
}
</style>
