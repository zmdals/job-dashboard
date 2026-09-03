<script setup>
// 자격증・어학이 사용
import ChipTag from './ChipTag.vue';
import InputWithAction from './InputWithAction.vue';

defineProps({
    items: {type:Array, required: true},
    idKey: {type:String, required: true},
    editing: Boolean,
    labelKey: {type:String, required: true},
    labelText: {type:String, default: '이름'},
    metaKey: String,
    metaText: {type:String, default: '취득일'},
    addLabel: {type:String, default: '+ 추가'},
})
defineEmits(['add', 'remove'])
</script>

<template>
    <template v-if="!editing">
        <ChipTag
            v-for="item in items"
            :key="item[idKey]"
            :label="item[labelKey]"
            :meta="metaKey ? item[metaKey] : ''"
        ></ChipTag>
    </template>
    <template v-else>
        <div class="edit-group" v-for="item in items" :key="item[idKey]">
            <InputWithAction
                :label="labelText"
                v-model="item[labelKey]"
                buttonText="삭제"
                @action="$emit('remove', item[idKey])"
            ></InputWithAction>
            <InputWithAction
                v-if="metaKey"
                :label="metaText"
                v-model="item[metaKey]"
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
