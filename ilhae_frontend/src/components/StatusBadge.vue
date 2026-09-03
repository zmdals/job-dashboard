<script setup>
const colorMap = {
    '서류합격': { bg: '#dff2e8', color: '#1f6b4a' },
    '지원완료': { bg: '#f0efec', color: '#6b7178' },
    '최종합격': { bg: '#1f6b4a', color: '#fff' },
    '불합격':   { bg: '#f0efec', color: '#a7acb2' },
}
const props = defineProps(['status'])
const style = colorMap[props.status]
</script>

<template>
    <span class="badge" :style="{background:style.bg, color:style.color}">
        {{ status }}
    </span>
</template>

<style scoped>
.badge {
    display: inline-flex;
    align-items: center;
    padding: 4px 12px;
    border-radius: 999px;
    font-size: 13px;
    font-weight: 600;
}
</style>