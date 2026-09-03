<script setup>
defineProps({ title: String })
defineEmits(['add'])
</script>

<template>
    <div class="info-card">
        <div class="card-header">
            <span class="title">{{ title }}</span>
            <button type="button" class="add-link" @click="$emit('add')">+ 추가</button>
        </div>
        <div class="card-body">
            <slot />
        </div>
    </div>
</template>

<style scoped>
.info-card {
    background: #fff;
    border: 1px solid #ececec;
    border-radius: 12px;
    padding: 20px 22px;
}

.card-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding-bottom: 14px;
    margin-bottom: 14px;
    border-bottom: 1px solid #f0f0f0;
}

.card-header .title {
    font-size: 15px;
    font-weight: 700;
    color: #1a1d1f;
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

.add-link:hover {
    border-color: #e31b23;
    color: #e31b23;
}

.card-body {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
    min-height: 20px;
}
</style>