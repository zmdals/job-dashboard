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

const coverLetterSections = [
  {
    title: '지원 동기',
    content:
      '사용자의 문제를 발견하고 기술로 해결하는 과정에 관심을 가져왔습니다. 지원 기업의 서비스 방향과 제가 쌓아온 프로젝트 경험이 맞닿아 있다고 생각해 지원했습니다.',
  },
  {
    title: '직무 역량과 경험',
    content:
      '팀 프로젝트에서 요구사항을 정리하고 우선순위를 조율했으며, 데이터를 바탕으로 개선점을 찾고 결과를 공유한 경험이 있습니다. 협업 과정에서 맡은 일을 끝까지 책임지는 태도를 중요하게 생각합니다.',
  },
  {
    title: '입사 후 포부',
    content:
      '빠르게 업무와 제품을 이해하고 동료들과 적극적으로 소통하겠습니다. 작은 개선을 꾸준히 쌓아 사용자와 조직 모두에게 도움이 되는 구성원으로 성장하겠습니다.',
  },
]

</script>

<template>
  <ModalBox
    :title="title"
    :subtitle="subtitle"
    :wide="isInterviewMode"
    @close="$emit('close')"
  >
    <div class="preparation-layout" :class="{ sideBySide: isInterviewMode }">
      <section class="document-panel">
        <header class="panel-head">
          <span>자기소개서</span>
          <small>임시 작성본</small>
        </header>
        <div v-for="section in coverLetterSections" :key="section.title" class="document-section">
          <h4>{{ section.title }}</h4>
          <p>{{ section.content }}</p>
        </div>
      </section>

      <section v-if="isInterviewMode" class="document-panel question-panel">
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

    <template #actions>
      <button type="button" @click="$emit('close')">닫기</button>
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
