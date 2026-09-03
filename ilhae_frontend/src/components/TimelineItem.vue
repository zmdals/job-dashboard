<script setup>
    defineProps({
        title: String,
        period: String,
        extra: String,
    })
    defineEmits(['edit', 'remove'])
</script>

<template>
    <div class="timeline-item">
        <div class="timeline-text">
            <span class="title">{{ title }}</span>
            <span class="period">{{ period }} <span v-if="extra">· {{ extra }}</span></span>
        </div>
        <div class="item-actions">
            <button type="button" @click="$emit('edit')">수정</button> | <button type="button" @click="$emit('remove')">삭제</button>
        </div>
    </div>
</template>

<style scoped>
.timeline-item {
    display: flex;
    align-items: flex-start;
    justify-content: space-between;
    gap: 14px;
    flex-basis: 100%;
    padding-bottom: 14px;
    margin-bottom: 14px;
    border-bottom: 1px solid #f0f0f0;
}

.timeline-text {
    display: flex;
    flex-direction: column;
    gap: 4px;
}

.item-actions {
    flex-shrink: 0;
    color: #9aa0a6;
    font-size: 11px;
}

.item-actions button {
    padding: 0;
    border: 0;
    color: inherit;
    background: none;
    font-size: inherit;
    cursor: pointer;
}

.item-actions button:hover {
    color: #e31b23;
}

.timeline-item:last-child {
    padding-bottom: 0;
    margin-bottom: 0;
    border-bottom: none;
}

.timeline-item .title {
    font-size: 14px;
    font-weight: 600;
    color: #1a1d1f;
}

.timeline-item .period {
    font-size: 13px;
    color: #9aa0a6;
}
</style>