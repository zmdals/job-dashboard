<script setup>
import { computed, onMounted, ref } from 'vue'
import ModalBox from '@/components/LegoBox/ModalBox.vue'
import { api } from '@/api/client'

const props = defineProps({
  posting: { type: Object, required: true },
})

defineEmits(['emitCloseCard'])

const companyName = computed(() => props.posting.companyName || props.posting.company?.name || '')
const subtitle = computed(() =>
  [companyName.value, props.posting.title, '내 프로필 비교'].filter(Boolean).join(' · '),
)

const loading = ref(true)
const errorMessage = ref('')
const relevance = ref(null)

onMounted(async () => {
  console.log('[AI relevance] 요청 시작', { postingId: props.posting.id })

  try {
    const result = await api.getPostingRelevance(props.posting.id)
    console.log('[AI relevance] 응답', result)
    relevance.value = result
  } catch (e) {
    console.error('[AI relevance] 실패', e)
    errorMessage.value = e.message || '적합도 분석을 불러오지 못했습니다.'
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <ModalBox
    title="AI 적합도 분석"
    :subtitle="subtitle"
    @close="$emit('emitCloseCard')"
  >
    <p v-if="loading" class="state">분석 중입니다...</p>
    <p v-else-if="errorMessage" class="state error">{{ errorMessage }}</p>
    <template v-else>
      <div class="fit-score">
        <strong>
          {{ relevance?.score ?? '—' }}<span v-if="relevance?.score != null">%</span>
        </strong>
        <p>내 프로필과 공고를 분석한<br />예상 적합도입니다.</p>
      </div>
      <section class="detail-section" v-if="relevance?.summary">
        <h4>분석 요약</h4>
        <p>{{ relevance.summary }}</p>
      </section>
      <section class="detail-section" v-if="relevance?.strengths?.length">
        <h4>강점</h4>
        <ul>
          <li v-for="item in relevance.strengths" :key="item">{{ item }}</li>
        </ul>
      </section>
      <section class="detail-section" v-if="relevance?.weaknesses?.length">
        <h4>보완할 점</h4>
        <ul>
          <li v-for="item in relevance.weaknesses" :key="item">{{ item }}</li>
        </ul>
      </section>
    </template>

    <template #actions>
      <button type="button" @click="$emit('emitCloseCard')">닫기</button>
    </template>
  </ModalBox>
</template>

<style scoped>
.fit-score {
  display: flex;
  align-items: center;
  gap: 18px;
  padding: 17px;
  border-radius: 7px;
  background: #fff0f0;
}

.fit-score strong {
  color: #e31b23;
  font-size: 34px;
}

.fit-score p,
.detail-section p {
  margin: 0;
  color: #666;
  font-size: 12px;
  line-height: 1.8;
}

.detail-section {
  padding: 18px 0;
  border-top: 1px solid #e7e7e7;
}

h4 {
  margin: 0 0 10px;
  font-size: 13px;
}

.state {
  margin: 0;
  padding: 24px 0;
  color: #999;
  font-size: 12px;
  text-align: center;
}

.state.error {
  color: #e31b23;
}

.detail-section ul {
  margin: 0;
  padding-left: 18px;
  color: #666;
  font-size: 12px;
  line-height: 1.8;
}
</style>
