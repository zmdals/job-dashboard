<script setup>
import { computed } from 'vue'
import ModalBox from '@/components/LegoBox/ModalBox.vue'
import { usePostingsStore } from '@/stores/postingsStore'

const props = defineProps({
  companyId: { type: [String, Number], required: true },
  companyName: { type: String, default: '' },
  jobTitle: { type: String, default: '' },
})

defineEmits(['emitCloseCard'])

const postingsStore = usePostingsStore()
const relevance = computed(() => postingsStore.getCompanyRelevance(props.companyId))
const subtitle = computed(() =>
  [props.companyName, props.jobTitle, '내 프로필 비교'].filter(Boolean).join(' · '),
)
</script>

<template>
  <ModalBox title="AI 적합도 분석" :subtitle="subtitle" @close="$emit('emitCloseCard')">
    <div class="fit-score">
      <strong>{{ relevance.score }}%</strong>
      <p>내 프로필과 공고를 분석한<br />예상 합격률입니다.</p>
    </div>
    <section class="detail-section">
      <h4>분석 결과</h4>
      <div v-if="relevance.matchedSkills.length" class="analysis-list">
        <span v-for="skill in relevance.matchedSkills" :key="skill">{{ skill }} 역량 일치</span>
      </div>
      <p v-else>{{ relevance.summary }}</p>
    </section>
    <section v-if="relevance.missingSkills.length" class="detail-section">
      <h4>보완하면 좋은 역량</h4>
      <p>{{ relevance.missingSkills.join(' · ') }}</p>
    </section>

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

.analysis-list {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 8px;
}

.analysis-list span {
  padding: 9px;
  border-radius: 5px;
  color: #55705b;
  background: #eff7ef;
  font-size: 11px;
}

.analysis-list span::before {
  margin-right: 5px;
  content: '✓';
}

@media (max-width: 760px) {
  .analysis-list {
    grid-template-columns: 1fr;
  }
}
</style>
