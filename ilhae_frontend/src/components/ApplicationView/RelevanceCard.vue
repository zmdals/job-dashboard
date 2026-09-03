<script setup>
import { computed } from 'vue'
import ModalBox from '@/components/LegoBox/ModalBox.vue'

const props = defineProps({
  posting: { type: Object, required: true },
})

defineEmits(['emitCloseCard'])

const companyName = computed(() => props.posting.companyName || props.posting.company?.name || '')
const subtitle = computed(() =>
  [companyName.value, props.posting.title, '내 프로필 비교'].filter(Boolean).join(' · '),
)
</script>

<template>
  <ModalBox
    title="AI 적합도 분석"
    :subtitle="subtitle"
    @close="$emit('emitCloseCard')"
  >
    <div class="fit-score">
      <strong>
        {{ posting.relevanceScore ?? '—' }}<span v-if="posting.relevanceScore != null">%</span>
      </strong>
      <p>내 프로필과 공고를 분석한<br />예상 적합도입니다.</p>
    </div>
    <section class="detail-section">
      <h4>분석 결과</h4>
      <p v-if="posting.relevanceScore != null">
        채용공고 조회 시 함께 제공된 AI 적합도 점수입니다.
      </p>
      <p v-else>아직 산출된 적합도 점수가 없습니다.</p>
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

</style>
