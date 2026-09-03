<script setup>
import { computed, onMounted, ref } from 'vue'
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router'
import ModalBox from '@/components/LegoBox/ModalBox.vue'
import { useCompaniesStore } from '@/stores/companiesStore'

const props = defineProps({
  companyId: { type: [String, Number], required: true },
})

defineEmits(['emitCloseCard'])

const companiesStore = useCompaniesStore()
const router = useRouter()
const { loading } = storeToRefs(companiesStore)
const loadError = ref(false)

const company = computed(() => companiesStore.getCompany(props.companyId))

onMounted(async () => {
  try {
    await companiesStore.ensureCompany(props.companyId)
  } catch {
    loadError.value = true
  }
})

const handleDetailRequest = () => {
  if (company.value?.id == null) return

  router.push({
    name: 'posting-report',
    params: { companyId: props.companyId },
  })
}
</script>

<template>
  <ModalBox
    :title="company?.name || '회사정보'"
    :subtitle="company?.description || ''"
    @close="$emit('emitCloseCard')"
  >
    <p v-if="loadError" class="state error">회사정보를 불러오지 못했습니다.</p>
    <p v-else-if="loading && !company" class="state">회사정보를 불러오는 중입니다.</p>
    <template v-else>
      <section class="detail-section">
        <h4>회사 한눈에 보기</h4>
        <p>{{ company?.description || '회사 소개 정보가 아직 등록되지 않았습니다.' }}</p>
      </section>
      <section class="detail-section">
        <h4>산업 분야</h4>
        <p>{{ company?.industry || '산업 정보가 아직 등록되지 않았습니다.' }}</p>
      </section>
      <section v-if="company?.dartCorpCode" class="detail-section">
        <h4>DART 기업 코드</h4>
        <p>{{ company.dartCorpCode }}</p>
      </section>
    </template>

    <template #actions>
      <button type="button" :disabled="company?.id == null" @click="handleDetailRequest">
        맞춤 기업 리포트
      </button>
      <a v-if="company?.url" class="primary" :href="company.url" target="_blank" rel="noreferrer">
        회사 홈페이지
      </a>
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

.state {
  padding: 24px 0;
  text-align: center;
}

.state.error {
  color: #e31b23;
}
</style>
