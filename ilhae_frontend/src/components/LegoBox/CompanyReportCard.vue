<script setup>
defineProps({
  evidence: { type: Object, required: true },
})

const SOURCE_TYPE_LABELS = {
  PATENT: '특허',
  PAPER: '논문',
  PRESS_RELEASE: '보도자료',
  IR: 'IR',
  NEWS: '뉴스',
  REPORT: '보고서',
}
</script>

<template>
  <article class="report-card">
    <div class="card-meta">
      <small class="resource-type">
        {{ SOURCE_TYPE_LABELS[evidence.sourceType] || evidence.sourceType }}
      </small>
      <small v-if="evidence.isOfficial" class="official">공식 자료</small>
    </div>
    <h3>
      <a :href="evidence.sourceUrl" target="_blank" rel="noreferrer">{{ evidence.title }}</a>
    </h3>
    <p>{{ evidence.aiDescription }}</p>
    <footer>
      <small class="relevance">관련도 {{ evidence.relevanceScore }}%</small>
      <small class="published-at">{{ evidence.publishedAt }}</small>
    </footer>
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

.card-meta,
footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 10px;
}

.official {
  padding: 3px 6px;
  border-radius: 4px;
  color: #55705b;
  background: #eff7ef;
  font-size: 9px;
}

h3 {
  margin: 14px 0 10px;
  font-size: 15px;
}

h3 a {
  color: inherit;
  text-decoration: none;
}

h3 a:hover,
h3 a:focus-visible {
  color: #e31b23;
  text-decoration: underline;
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

.published-at {
  color: #999;
  font-size: 10px;
}
</style>
