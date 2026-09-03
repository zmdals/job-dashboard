<script setup>
defineProps({
  modelValue: {
    type: String,
    default: 'login',
  },
})

defineEmits(['update:modelValue'])
</script>

<template>
  <div class="auth-tabs" role="tablist" aria-label="인증 방식">
    <button
      type="button"
      role="tab"
      :aria-selected="modelValue === 'login'"
      :class="{ selected: modelValue === 'login' }"
      @click="$emit('update:modelValue', 'login')"
    >
      로그인
    </button>
    <button
      type="button"
      role="tab"
      :aria-selected="modelValue === 'signup'"
      :class="{ selected: modelValue === 'signup' }"
      @click="$emit('update:modelValue', 'signup')"
    >
      회원가입
    </button>
  </div>
</template>

<style scoped>
.auth-tabs {
  display: grid;
  grid-template-columns: 1fr 1fr;
  margin-bottom: 27px;
  border-bottom: 1px solid #e7e7e7;
}

button {
  padding: 0 0 13px;
  border: 0;
  border-bottom: 2px solid transparent;
  color: #aaa;
  background: transparent;
  font: inherit;
  font-size: 14px;
  font-weight: 700;
  cursor: pointer;
}

button.selected {
  border-bottom-color: #e31b23;
  color: #202124;
}
</style>
