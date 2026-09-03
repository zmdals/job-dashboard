<script setup>
import { ref, watch, computed } from 'vue';
import ModalBox from './LegoBox/ModalBox.vue';

const props = defineProps({
    category: { type: String, required: true },
    mode: { type: String, default: 'add' },
    // [{ key, label, type: 'text'|'date'|'number'|'select'|'textarea', placeholder?, options?: [{value,label}] }]
    fields: { type: Array, required: true },
    item: { type: Object, default: () => ({}) },
})
const emit = defineEmits(['save', 'close'])

function buildForm(item) {
    const result = {}
    props.fields.forEach((field) => {
        result[field.key] = item[field.key] ?? (field.type === 'number' ? null : '')
    })
    return result
}

const form = ref(buildForm(props.item))
watch(() => props.item, (val) => {
    form.value = buildForm(val)
})

const modalTitle = computed(() => `${props.category} ${props.mode === 'edit' ? '수정' : '추가'}`)

function submit() {
    const payload = { ...form.value }
    props.fields.forEach((field) => {
        if (field.type === 'number' && payload[field.key] !== '' && payload[field.key] !== null) {
            payload[field.key] = Number(payload[field.key])
        }
    })
    emit('save', payload)
}
</script>

<template>
    <ModalBox :title="category" :subtitle="modalTitle" @close="$emit('close')">
        <form class="profile-form" @submit.prevent="submit">
            <template v-for="field in fields" :key="field.key">
                <label>{{ field.label }}</label>
                <select v-if="field.type === 'select'" v-model="form[field.key]">
                    <option v-for="opt in field.options" :key="opt.value" :value="opt.value">{{ opt.label }}</option>
                </select>
                <textarea
                    v-else-if="field.type === 'textarea'"
                    v-model="form[field.key]"
                    :placeholder="field.placeholder"
                ></textarea>
                <input
                    v-else
                    :type="field.type || 'text'"
                    v-model="form[field.key]"
                    :placeholder="field.placeholder"
                />
            </template>
        </form>
        <template #actions>
            <button type="button" @click="$emit('close')">취소</button>
            <button type="button" class="primary" @click="submit">저장하기</button>
        </template>
    </ModalBox>
</template>

<style scoped>
.profile-form {
    display: grid;
    gap: 8px;
}

.profile-form label {
    margin-top: 7px;
    font-size: 12px;
    font-weight: 700;
}

.profile-form input,
.profile-form textarea {
    width: 100%;
    padding: 11px;
    border: 1px solid #e7e7e7;
    border-radius: 5px;
    outline: 0;
    font: inherit;
    font-size: 12px;
}

.profile-form textarea {
    min-height: 80px;
    resize: vertical;
}
</style>
