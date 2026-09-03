<script setup>
import { computed } from 'vue'

const props = defineProps({
  modelValue: {
    type: Number,
    default: 1,
  },
  totalItems: {
    type: Number,
    default: 0,
  },
  pageSize: {
    type: Number,
    default: 3,
  },
})

const emit = defineEmits(['update:modelValue'])

const pageCount = computed(() => Math.max(1, Math.ceil(props.totalItems / props.pageSize)))
const pages = computed(() => Array.from({ length: pageCount.value }, (_, index) => index + 1))

function move(page) {
  emit('update:modelValue', Math.min(pageCount.value, Math.max(1, page)))
}
</script>

<template>
  <nav v-if="totalItems > pageSize" class="pagination" aria-label="채용공고 페이지 이동">
    <button class="arrow" type="button" aria-label="이전 페이지" :disabled="modelValue === 1" @click="move(modelValue - 1)">‹</button>
    <button
      v-for="page in pages"
      :key="page"
      type="button"
      :class="{ active: page === modelValue }"
      :aria-current="page === modelValue ? 'page' : undefined"
      @click="move(page)"
    >
      {{ page }}
    </button>
    <button class="arrow" type="button" aria-label="다음 페이지" :disabled="modelValue === pageCount" @click="move(modelValue + 1)">›</button>
  </nav>
</template>

<style scoped>
.pagination {
  display: flex;
  justify-content: center;
  gap: 8px;
  margin-top: 28px;
}

button {
  min-width: 28px;
  height: 28px;
  padding: 0 6px;
  border: 0;
  color: #999;
  background: transparent;
  font: inherit;
  font-size: 12px;
  cursor: pointer;
}

button.active {
  border-radius: 5px;
  color: #fff;
  background: #e31b23;
  font-weight: 700;
}

button.arrow {
  color: #777;
  font-size: 16px;
}

button:disabled {
  color: #ccc;
  cursor: default;
}
</style>
