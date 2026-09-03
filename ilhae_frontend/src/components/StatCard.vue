<script setup>

defineProps({
  label: {
    type: String,
    required: true,
  },
  value: {
    type: [String, Number],
    required: true,
  },
})
</script>

<template>
    <div class="stat-card">
      <span class="label">{{ label }}</span>
      <span class="value">{{ value }}</span>
    </div>


</template>

<style scoped>
.stat-card {
    display: flex;
    flex-direction: column;
    gap: 10px;
    padding: 20px 22px;
    background: #fff;
    border: 1px solid #ececec;
    border-radius: 12px;
}

.label {
    font-size: 13px;
    color: #6b7178;
}

.value {
    font-size: 26px;
    font-weight: 700;
    color: #1a1d1f;
}
</style>