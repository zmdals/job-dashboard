<script setup>
import { computed, onMounted, ref } from 'vue'
import { storeToRefs } from 'pinia'
import ModalBox from '@/components/LegoBox/ModalBox.vue'
import { usePostingsStore } from '@/stores/postingsStore'

const props = defineProps({
  id: { type: [String, Number], required: true },
})

defineEmits(['emitCloseCard'])

const postingsStore = usePostingsStore()
const { info, loading } = storeToRefs(postingsStore)
const loadError = ref(false)

const company = computed(() => info.value?.company ?? null)
const resourceCount = computed(() => (info.value?.papers?.length ?? 0) + (info.value?.articles?.length ?? 0))

onMounted(async () => {
  try {
    await postingsStore.fetchInfo(props.id)
  } catch {
    loadError.value = true
  }
})
</script>

<template>
  <ModalBox
    :title="company?.name || '회사정보'"
    :subtitle="company?.description || ''"
    @close="$emit('emitCloseCard')"
  >
    <p v-if="loadError" class="state error">회사정보를 불러오지 못했습니다.</p>
    <p v-else-if="loading && !info" class="state">회사정보를 불러오는 중입니다.</p>
    <template v-else>
      <section class="detail-section">
        <h4>회사 한눈에 보기</h4>
        <p>{{ company?.description || '회사 소개 정보가 아직 등록되지 않았습니다.' }}</p>
      </section>
      <section class="detail-section">
        <h4>관련 자료</h4>
        <p>관련 논문과 아티클 {{ resourceCount }}건</p>
      </section>
    </template>

    <template #actions>
      <button type="button" @click="$emit('emitCloseCard')">닫기</button>
      <a v-if="company?.homepageUrl" class="primary" :href="company.homepageUrl" target="_blank" rel="noreferrer">회사 홈페이지</a>
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
