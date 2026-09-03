<!-- components/AppCard.vue -->
<template>
  <div 
    class="rounded-box" 
    :class="[paddingClass, { 'has-shadow': shadow }]"
  >
    <slot />
  </div>
</template>

<script setup>
defineProps({
  // 호출할 때 원하는 배경색 코드를 넘겨받음 (기본값: 흰색)
  bgColor: {
    type: String,
    default: '#ffffff'
  },
  color: {
    type: String,
    default: '#000000'
  },
  paddingClass: {
    type: String,
    default: 'p-4'
  },
  shadow: {
    type: Boolean,
    default: true
  }
})
</script>

<style scoped>
.rounded-box {
  border-radius: 16px;
  overflow: hidden; /* 모서리 밖으로 콘텐츠 안 튀어나가게 방지 */
  background-color: v-bind(bgColor);
  color: v-bind(color);
}

.has-shadow {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}
</style>