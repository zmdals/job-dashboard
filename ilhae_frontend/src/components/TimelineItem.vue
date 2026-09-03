<script setup>
    defineProps({
        title: String,
        period: String,
        extra: String,
    })
</script>

<template>
    <div class="timeline-item">
        <span class="title">{{ title }}</span>
        <span class="period">{{ period }} <span v-if="extra">· {{ extra }}</span></span>
    </div>
</template>

<style scoped>
.timeline-item {
    display: flex;
    flex-direction: column;
    gap: 4px;
    flex-basis: 100%;
    padding-bottom: 14px;
    margin-bottom: 14px;
    border-bottom: 1px solid #f0f0f0;
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