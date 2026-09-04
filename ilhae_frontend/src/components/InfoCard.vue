<script setup>
import { ref } from 'vue'

defineProps({ title: String })
defineEmits(['add'])

const expanded = ref(false)

function toggleExpanded() {
  expanded.value = !expanded.value
}
</script>

<template>
  <section class="info-card" :class="{ expanded }">
    <div class="card-header">
      <span class="title">{{ title }}</span>
      <div class="card-actions">
        <button type="button" class="add-link" @click="$emit('add')">+ 추가</button>
        <button
          type="button"
          class="expand-button"
          :class="{ expanded }"
          :aria-label="expanded ? `${title} 접기` : `${title} 전체 보기`"
          :aria-expanded="expanded"
          @click="toggleExpanded"
        >
          <span aria-hidden="true">⌄</span>
        </button>
      </div>
    </div>
    <div class="card-body">
      <slot />
    </div>
  </section>
</template>

<style scoped>
.info-card {
  display: flex;
  height: 260px;
  flex-direction: column;
  padding: 20px 22px;
  border: 1px solid #ececec;
  border-radius: 12px;
  background: #fff;
}

.info-card.expanded {
  height: auto;
  min-height: 260px;
}

.card-header {
  display: flex;
  flex: 0 0 auto;
  align-items: center;
  justify-content: space-between;
  padding-bottom: 14px;
  margin-bottom: 14px;
  border-bottom: 1px solid #f0f0f0;
}

.card-header .title {
  color: #1a1d1f;
  font-size: 15px;
  font-weight: 700;
}

.card-actions {
  display: flex;
  align-items: center;
  gap: 6px;
}

.add-link {
  padding: 6px 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  color: #666;
  background: #fff;
  font-size: 11px;
  cursor: pointer;
}

.add-link:hover,
.add-link:focus-visible,
.expand-button:hover,
.expand-button:focus-visible {
  border-color: #e31b23;
  color: #e31b23;
}

.expand-button {
  display: grid;
  place-items: center;
  width: 28px;
  height: 28px;
  padding: 0;
  border: 1px solid #ddd;
  border-radius: 5px;
  color: #777;
  background: #fff;
  cursor: pointer;
}

.expand-button span {
  display: block;
  font-size: 17px;
  line-height: 1;
  transition: transform 0.2s ease;
}

.expand-button.expanded span {
  transform: rotate(180deg);
}

.card-body {
  display: flex;
  min-height: 0;
  flex: 1 1 auto;
  flex-wrap: wrap;
  gap: 10px;
  padding-right: 6px;
  overflow-y: auto;
  scrollbar-color: #ccc transparent;
  scrollbar-width: thin;
}

.info-card.expanded .card-body {
  overflow-y: visible;
}

.card-body::-webkit-scrollbar {
  width: 5px;
}

.card-body::-webkit-scrollbar-thumb {
  border-radius: 999px;
  background: #ccc;
}

@media (max-width: 480px) {
  .info-card {
    padding: 18px;
  }
}
</style>
