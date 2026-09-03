<script setup>
defineProps({
  modelValue: {
    type: String,
    default: 'all',
  },
})

defineEmits(['update:modelValue'])
</script>

<template>
  <nav class="posting-tabs" aria-label="채용공고 필터">
    <button
      type="button"
      :class="{ active: modelValue === 'all' }"
      @click="$emit('update:modelValue', 'all')"
    >
      전체공고
    </button>
    <button
      type="button"
      :class="{ active: modelValue === 'starred' }"
      @click="$emit('update:modelValue', 'starred')"
    >
      선호공고
    </button>
  </nav>
</template>

<style scoped>
.posting-tabs {
  display: flex;
  gap: 22px;
  margin-bottom: 18px;
  border-bottom: 1px solid #e7e7e7;
}

button {
  padding: 0 0 13px;
  border: 0;
  border-bottom: 2px solid transparent;
  color: #aaa;
  background: none;
  font: inherit;
  font-size: 13px;
  cursor: pointer;
}

button.active {
  border-color: #e31b23;
  color: #202124;
  font-weight: 700;
}
</style>
