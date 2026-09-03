<script setup>
// 학력, 경력, 프로젝트, 수상경력이 사용
import InputWithAction from './InputWithAction.vue';
import TimelineItem from './TimelineItem.vue';
defineProps({
    items: {type:Array, required: true},
    idKey: {type:String, required: true},
    editing: Boolean,
    fields: {type:Array, required:true},
    addLabel: {type:String, default: '+ 추가'},
})
defineEmits(['add', 'remove'])
</script>

<template>
    <template v-if="!editing">
        <TimelineItem
            v-for="item in items"
            :key = "item[idKey]"
            :title="item[fields[0]?.key]"
            :period="item[fields[1]?.key]"
            :extra="item[fields[2]?.key]"
        ></TimelineItem>
    </template>
    <template v-else>
        <div class="edit-group" v-for="item in items" :key="item[idKey]">
            <InputWithAction
                v-for="(f, i) in fields"
                :key="f.key"
                :label="f.label"
                v-model="item[f.key]"
                :buttonText="i === 0 ? '삭제' : undefined"
                @action="i === 0 && $emit('remove', item[idKey])"
            ></InputWithAction>
        </div>
        <button @click="$emit('add')">{{ addLabel }}</button>
    </template>
</template>
<style scoped>
.edit-group {
    display: flex;
    flex-direction: column;
    gap: 8px;
    flex-basis: 100%;
    padding-bottom: 14px;
    margin-bottom: 14px;
    border-bottom: 1px solid #f0f0f0;
}

.edit-group:last-of-type {
    padding-bottom: 0;
    margin-bottom: 0;
    border-bottom: none;
}
</style>