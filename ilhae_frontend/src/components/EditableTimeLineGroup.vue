<script setup>
// 학력, 자격증, 경력, 수상경력, 프로젝트가 사용
import TimelineItem from './TimelineItem.vue';
defineProps({
    items: {type:Array, required: true},
    idKey: {type:String, required: true},
})
defineEmits(['edit', 'remove'])
</script>

<template>
    <TimelineItem
        v-for="item in items"
        :key="item[idKey]"
        :title="item.name"
        :period="item.period"
        :extra="item.description"
        @edit="$emit('edit', item)"
        @remove="$emit('remove', item[idKey])"
    ></TimelineItem>
</template>
