<script setup>
import { computed } from 'vue'
import ModalBox from './ModalBox.vue'

const props = defineProps({
  posting: {
    type: Object,
    required: true,
  },
  applied: {
    type: Boolean,
    default: false,
  },
})

defineEmits(['close', 'apply'])

const companyName = computed(() => props.posting.companyName || props.posting.company?.name || '회사명 미정')
const subtitle = computed(() => [props.posting.jobType, props.posting.location].filter(Boolean).join(' · '))
</script>

<template>
  <ModalBox :title="`${companyName} ${posting.title}`" :subtitle="subtitle" @close="$emit('close')">
    <section class="detail-section">
      <h4>공고 내용</h4>
      <p>{{ posting.description || '상세 공고 내용은 아직 등록되지 않았습니다.' }}</p>
    </section>
    <section v-if="posting.annualIncome" class="detail-section">
      <h4>연봉</h4>
      <p>{{ posting.annualIncome }}</p>
    </section>

    <template #actions>
      <button type="button" @click="$emit('close')">닫기</button>
      <button class="primary" type="button" :disabled="applied" @click="$emit('apply')">
        {{ applied ? '지원완료' : '지원하기' }}
      </button>
    </template>
  </ModalBox>
</template>

<style scoped>
.detail-section {
  padding: 18px 0;
  border-top: 1px solid #e7e7e7;
}

h4 {
  margin: 0 0 10px;
  font-size: 13px;
}

p {
  margin: 0;
  color: #666;
  font-size: 12px;
  line-height: 1.8;
}
</style>
