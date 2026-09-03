<script setup>
import { computed, ref, watch } from 'vue'
import ModalBox from '@/components/LegoBox/ModalBox.vue'
import { api } from '@/api/client'

const props = defineProps({
  application: {
    type: Object,
    required: true,
  },
  mode: {
    type: String,
    required: true,
    validator: (value) => ['cover-letter', 'interview'].includes(value),
  },
})

defineEmits(['close'])

const questionsLoading = ref(false)
const questionsGenerating = ref(false)
const questionsExist = ref(false)
const questionsErrorMessage = ref('')
const interviewQuestions = ref([])

async function fetchInterviewQuestions() {
  questionsLoading.value = true
  questionsErrorMessage.value = ''

  console.log('[interview-questions] 조회 시작', { applicationId: props.application.id })

  try {
    const result = await api.getInterviewQuestions(props.application.id)
    console.log('[interview-questions] 조회 성공', result)
    interviewQuestions.value = result ?? []
    questionsExist.value = true
  } catch (e) {
    if (e.status === 404) {
      console.log('[interview-questions] 아직 생성된 질문 없음')
      questionsExist.value = false
    } else {
      console.error('[interview-questions] 조회 실패', e)
      questionsErrorMessage.value = e.message || '면접 질문을 불러오지 못했습니다.'
    }
  } finally {
    questionsLoading.value = false
  }
}

async function generateInterviewQuestions() {
  questionsGenerating.value = true
  questionsErrorMessage.value = ''

  console.log('[interview-questions] 생성 시작', { applicationId: props.application.id })

  try {
    const result = await api.generateInterviewQuestions(props.application.id)
    console.log('[interview-questions] 생성 성공', result)
    interviewQuestions.value = result ?? []
    questionsExist.value = true
  } catch (e) {
    console.error('[interview-questions] 생성 실패', e)
    questionsErrorMessage.value = e.message || '면접 질문 생성에 실패했습니다.'
  } finally {
    questionsGenerating.value = false
  }
}

watch(
  () => [props.mode, props.application.id],
  ([mode]) => {
    if (mode === 'interview') {
      fetchInterviewQuestions()
    }
  },
  { immediate: true },
)

const isInterviewMode = computed(() => props.mode === 'interview')
const title = computed(() => (isInterviewMode.value ? '예상 면접 질문' : '자기소개서'))
const subtitle = computed(() =>
  [props.application.companyName, props.application.jobTitle].filter(Boolean).join(' · '),
)

const coverLetter = ref(null)
const feedback = ref(null)
const form = ref({ title: '', content: '' })
const loading = ref(false)
const saving = ref(false)
const analyzing = ref(false)
const loadError = ref('')
const formError = ref('')

async function loadCoverLetter() {
  if (isInterviewMode.value) return

  loading.value = true
  loadError.value = ''
  formError.value = ''
  feedback.value = null

  try {
    coverLetter.value = await api.getCoverLetter(props.application.id)
  } catch (error) {
    if (error.status === 404) {
      coverLetter.value = null
      return
    }

    loadError.value = error.message || '자기소개서를 불러오지 못했습니다.'
  } finally {
    loading.value = false
  }
}

async function createCoverLetter() {
  formError.value = ''

  if (!form.value.content.trim()) {
    formError.value = '자기소개서 내용을 입력해 주세요.'
    return
  }

  saving.value = true

  try {
    coverLetter.value = await api.createCoverLetter(props.application.id, {
      title: form.value.title.trim() || null,
      content: form.value.content.trim(),
    })
    feedback.value = null
  } catch (error) {
    formError.value = error.message || '자기소개서 생성에 실패했습니다.'
  } finally {
    saving.value = false
  }
}

async function analyzeCoverLetter() {
  if (!coverLetter.value?.id) return

  analyzing.value = true
  formError.value = ''

  try {
    feedback.value = await api.requestCoverLetterFeedback(coverLetter.value.id)
  } catch (error) {
    formError.value = error.message || 'AI 피드백을 불러오지 못했습니다.'
  } finally {
    analyzing.value = false
  }
}

watch(
  () => [props.application.id, props.mode],
  () => {
    coverLetter.value = null
    form.value = { title: '', content: '' }
    void loadCoverLetter()
  },
  { immediate: true },
)

</script>

<template>
  <ModalBox
    :title="title"
    :subtitle="subtitle"
    :wide="isInterviewMode || Boolean(feedback)"
    @close="$emit('close')"
  >
    <template v-if="isInterviewMode">
      <div class="preparation-layout">
        <section class="document-panel question-panel">
          <header class="panel-head">
            <span>예상 면접 질문</span>
            <small v-if="questionsExist">총 {{ interviewQuestions.length }}문항</small>
          </header>

          <p v-if="questionsLoading" class="state">불러오는 중입니다...</p>
          <p v-else-if="questionsErrorMessage" class="state error">{{ questionsErrorMessage }}</p>
          <div v-else-if="!questionsExist" class="generate-panel">
            <p class="state">아직 생성된 면접 질문이 없어요.</p>
            <button type="button" :disabled="questionsGenerating" @click="generateInterviewQuestions">
              {{ questionsGenerating ? '생성 중...' : '면접 질문 생성' }}
            </button>
          </div>
          <ol v-else>
            <li v-for="question in interviewQuestions" :key="question.id ?? question.question">
              {{ question.question }}
              <small v-if="question.sampleAnswer">{{ question.sampleAnswer }}</small>
            </li>
          </ol>
        </section>
      </div>
    </template>

    <template v-else>
      <p v-if="loading" class="state">자기소개서를 불러오는 중입니다.</p>

      <div v-else-if="loadError" class="state error">
        <p>{{ loadError }}</p>
        <button type="button" @click="loadCoverLetter">다시 시도</button>
      </div>

      <div v-else class="preparation-layout" :class="{ sideBySide: feedback }">
        <section class="document-panel">
          <header class="panel-head">
            <span>{{ coverLetter ? '저장된 자기소개서' : '새 자기소개서' }}</span>
            <small v-if="coverLetter">v{{ coverLetter.version }}</small>
            <small v-else>작성 후 저장해 주세요</small>
          </header>

          <div v-if="coverLetter" class="cover-letter-view">
            <h4>{{ coverLetter.title || '제목 없음' }}</h4>
            <p>{{ coverLetter.content }}</p>
          </div>

          <form v-else class="cover-letter-form" @submit.prevent="createCoverLetter">
            <label for="cover-letter-title">제목</label>
            <input
              id="cover-letter-title"
              v-model="form.title"
              type="text"
              placeholder="자기소개서 제목 (선택)"
            />
            <label for="cover-letter-content">내용</label>
            <textarea
              id="cover-letter-content"
              v-model="form.content"
              placeholder="자기소개서 내용을 입력해 주세요"
              required
            ></textarea>
          </form>
        </section>

        <section v-if="feedback" class="document-panel feedback-panel">
          <header class="panel-head">
            <span>AI 자기소개서 피드백</span>
            <strong>{{ feedback.score }}점</strong>
          </header>
          <p class="feedback-summary">{{ feedback.summary }}</p>
          <div class="feedback-group">
            <h4>강점</h4>
            <ul>
              <li v-for="item in feedback.strengths" :key="item">{{ item }}</li>
            </ul>
          </div>
          <div class="feedback-group improvement">
            <h4>개선할 점</h4>
            <ul>
              <li v-for="item in feedback.improvements" :key="item">{{ item }}</li>
            </ul>
          </div>
        </section>
      </div>

      <p v-if="formError" class="form-error" role="alert">{{ formError }}</p>
    </template>

    <template #actions>
      <button type="button" @click="$emit('close')">닫기</button>
      <button
        v-if="!isInterviewMode && !loading && !loadError && !coverLetter"
        class="primary"
        type="button"
        :disabled="saving"
        @click="createCoverLetter"
      >
        {{ saving ? '생성 중...' : '자기소개서 생성' }}
      </button>
      <button
        v-if="!isInterviewMode && coverLetter"
        class="primary"
        type="button"
        :disabled="analyzing"
        @click="analyzeCoverLetter"
      >
        {{ analyzing ? '분석 중...' : feedback ? 'AI 다시 분석' : 'AI 분석' }}
      </button>
    </template>
  </ModalBox>
</template>

<style scoped>
.preparation-layout {
  display: grid;
  grid-template-columns: minmax(0, 1fr);
  gap: 16px;
}

.preparation-layout.sideBySide {
  grid-template-columns: repeat(2, minmax(0, 1fr));
}

.document-panel {
  min-width: 0;
  padding: 20px;
  border: 1px solid #e7e7e7;
  border-radius: 8px;
  background: #fff;
}

.panel-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 18px;
  padding-bottom: 12px;
  border-bottom: 1px solid #e7e7e7;
}

.panel-head span {
  font-size: 14px;
  font-weight: 700;
}

.panel-head small {
  color: #999;
  font-size: 10px;
}

.document-section + .document-section {
  margin-top: 18px;
}

.cover-letter-view p {
  white-space: pre-wrap;
}

.cover-letter-form {
  display: grid;
  gap: 8px;
}

.cover-letter-form label {
  margin-top: 5px;
  font-size: 12px;
  font-weight: 700;
}

.cover-letter-form input,
.cover-letter-form textarea {
  width: 100%;
  padding: 11px;
  border: 1px solid #e7e7e7;
  border-radius: 5px;
  outline: 0;
  font: inherit;
  font-size: 12px;
}

.cover-letter-form textarea {
  min-height: 220px;
  line-height: 1.7;
  resize: vertical;
}

.cover-letter-form input:focus,
.cover-letter-form textarea:focus {
  border-color: #e31b23;
  box-shadow: 0 0 0 3px #fff0f0;
}

.panel-head strong {
  color: #e31b23;
  font-size: 18px;
}

.feedback-summary {
  padding: 13px;
  border-radius: 6px;
  background: #fff5f5;
}

.feedback-group {
  margin-top: 18px;
}

.feedback-group ul {
  display: grid;
  gap: 7px;
  margin: 0;
  padding-left: 18px;
}

.feedback-group li::marker {
  color: #1f6b4a;
}

.feedback-group.improvement li::marker {
  color: #e31b23;
}

.state {
  margin: 0;
  padding: 35px 0;
  text-align: center;
}

.state.error {
  color: #e31b23;
}

.state.error button {
  margin-top: 12px;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 5px;
  background: #fff;
  cursor: pointer;
}

.form-error {
  margin: 12px 0 0;
  color: #e31b23;
}

h4 {
  margin: 0 0 7px;
  font-size: 12px;
}

p,
li {
  color: #666;
  font-size: 11px;
  line-height: 1.75;
}

p {
  margin: 0;
}

ol {
  display: grid;
  gap: 12px;
  margin: 0;
  padding-left: 20px;
}

li {
  padding-left: 4px;
}

li::marker {
  color: #e31b23;
  font-weight: 700;
}

li small {
  display: block;
  margin-top: 4px;
  color: #999;
}

.state {
  padding: 24px 0;
  color: #999;
  text-align: center;
}

.state.error {
  color: #e31b23;
}

.generate-panel {
  display: grid;
  gap: 12px;
  place-items: center;
}

.generate-panel button {
  padding: 8px 16px;
  border: 1px solid #e31b23;
  border-radius: 5px;
  color: #e31b23;
  background: #fff;
  font-size: 12px;
  cursor: pointer;
}

.generate-panel button:disabled {
  border-color: #ddd;
  color: #bbb;
  cursor: not-allowed;
}

@media (max-width: 760px) {
  .preparation-layout.sideBySide {
    grid-template-columns: 1fr;
  }

  .document-panel {
    padding: 16px;
  }
}
</style>
