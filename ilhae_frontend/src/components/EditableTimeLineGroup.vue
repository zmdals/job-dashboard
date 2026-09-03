<script setup>
// 학력, 자격증, 경력, 프로젝트가 사용
import TimelineItem from './TimelineItem.vue';
defineProps({
    items: {type:Array, required: true},
    idKey: {type:String, required: true},
    // item => { title, period, extra } — 카테고리마다 보기 화면에 뭘 보여줄지 매핑
    mapItem: {type: Function, required: true},
})
defineEmits(['edit', 'remove'])
</script>

<template>
    <TimelineItem
        v-for="item in items"
        :key="item[idKey]"
        v-bind="mapItem(item)"
        @edit="$emit('edit', item)"
        @remove="$emit('remove', item[idKey])"
    ></TimelineItem>
</template>
