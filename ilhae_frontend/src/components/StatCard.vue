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
  active: {
    type: Boolean,
    default: false,
  },
})
</script>

<template>
    <button type="button" class="stat-card" :class="{ active }">
      <span class="label">{{ label }}</span>
      <span class="value">{{ value }}</span>
    </button>
</template>

<style scoped>
.stat-card {
    display: flex;
    width: 100%;
    flex-direction: column;
    align-items: flex-start;
    justify-content: flex-start;
    gap: 10px;
    padding: 19px 20px;
    background: #fff;
    border: 1px solid #e7e7e7;
    border-radius: 8px;
    font: inherit;
    text-align: left;
    cursor: pointer;
    transition: border-color .2s, box-shadow .2s;
}

.stat-card:hover,
.stat-card.active {
    border-color: #e31b23;
    box-shadow: 0 4px 14px rgba(227, 27, 35, .08);
}

.stat-card.active .label {
    color: #e31b23;
}

.label {
    font-size: 11px;
    color: #858585;
}

.value {
    font-size: 25px;
    font-weight: 700;
    color: #202124;
}
</style>