<script setup>
import { ref, watch, computed } from 'vue';
import ModalBox from './LegoBox/ModalBox.vue';

const props = defineProps({
    category: { type: String, required: true },
    mode: { type: String, default: 'add' },
    item: {
        type: Object,
        default: () => ({ name: '', detail: '', period: '', description: '' }),
    },
})
const emit = defineEmits(['save', 'close'])

const form = ref({ ...props.item })
watch(() => props.item, (val) => {
    form.value = { ...val }
})

const modalTitle = computed(() => `${props.category} ${props.mode === 'edit' ? '수정' : '추가'}`)

function submit() {
    emit('save', { ...form.value })
}
</script>

<template>
    <ModalBox :title="category" :subtitle="modalTitle" @close="$emit('close')">
        <form class="profile-form" @submit.prevent="submit">
            <label>이름 / 기관</label>
            <input v-model="form.name" placeholder="학교, 회사, 자격증 이름" />
            <label>상세 내용</label>
            <input v-model="form.detail" placeholder="전공, 직무, 발급기관 등" />
            <label>기간</label>
            <input v-model="form.period" placeholder="예: 2022.03 - 2024.02" />
            <label>설명</label>
            <textarea v-model="form.description" placeholder="주요 내용이나 성과를 입력해 주세요"></textarea>
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
