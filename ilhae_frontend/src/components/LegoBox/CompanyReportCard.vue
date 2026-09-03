<script setup>
defineProps({
  type: { type: String, required: true },
  title: { type: String, required: true },
  description: { type: String, required: true },
  relevance: { type: Number, required: true },
})
</script>

<template>
  <article class="report-card">
    <small class="resource-type">{{ type }}</small>
    <h3>{{ title }}</h3>
    <p>{{ description }}</p>
    <small class="relevance">관련도 {{ relevance }}%</small>
  </article>
</template>

<style scoped>
.report-card {
  min-height: 180px;
  padding: 20px;
  border: 1px solid #e7e7e7;
  border-radius: 8px;
  background: #fff;
}

.resource-type {
  color: #e31b23;
  font-size: 10px;
}

h3 {
  margin: 14px 0 10px;
  font-size: 15px;
}

p {
  margin: 0 0 14px;
  color: #666;
  font-size: 12px;
  line-height: 1.7;
}

.relevance {
  color: #55705b;
  font-size: 11px;
}
</style>
