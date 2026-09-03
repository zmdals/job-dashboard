<script setup>
import { ref } from 'vue';
import InfoCard from '@/components/InfoCard.vue';
import EditableTimeLineGroup from '@/components/EditableTimeLineGroup.vue';
import ProfileFormModal from '@/components/ProfileFormModal.vue';

const education = ref([
    {
        id: 1,
        name: '한국대학교',
        detail: '생명공학과 · 학사',
        period: '2019.03 – 2023.02',
        description: '학점 3.72 / 4.5',
    },
    {
        id: 2,
        name: '한국대학교 대학원',
        detail: '생물공정 전공 · 재학',
        period: '2023.03 – ',
        description: '',
    },
])

const career = ref([
    {
        id: 1,
        name: '바이오공정연구실',
        detail: '학부연구생',
        period: '2022.01 – 2023.02',
        description: '14개월 · 세포배양·정제 실험',
    },
    {
        id: 2,
        name: '한빛소재',
        detail: '하계 인턴',
        period: '2024.07 – 2024.08',
        description: '품질분석 보조',
    },
])

const projects = ref([
    {
        id: 1,
        name: '교내 캡스톤 경진대회 장려상',
        detail: '',
        period: '2022.11',
        description: '미생물 배양 자동화',
    },
])

const certificates = ref([
    { id: 1, name: '화학분석기사', detail: '몰라', period: '2024.05', description: '몰라' },
    { id: 2, name: 'OPIc IM2', detail: '', period: '2025.03', description: '' },
    { id: 3, name: 'GMP 교육 수료', detail: '', period: '', description: '' },
])



function removeItem(list, id) {
    const index = list.findIndex(item => item.id === id)
    if (index !== -1) list.splice(index, 1)
}

const modalState = ref(null)
// { category, mode: 'add' | 'edit', list, item }

function openAddModal(category, list) {
    modalState.value = { category, mode: 'add', list, item: { name: '', detail: '', period: '', description: '' } }
}
function openEditModal(category, list, item) {
    modalState.value = { category, mode: 'edit', list, item }
}
function closeModal() {
    modalState.value = null
}
function saveModal(formValue) {
    const { mode, list, item } = modalState.value
    if (mode === 'add') {
        list.push({ id: Date.now(), ...formValue })
    } else {
        const index = list.findIndex(i => i.id === item.id)
        if (index !== -1) list[index] = { ...item, ...formValue }
    }
    closeModal()
}
</script>

<template>
    <h2 class="page-title">내정보</h2>
    <div class="info-grid">
        <InfoCard title="학력" @add="openAddModal('학력', education)">
            <EditableTimeLineGroup
                :items="education"
                idKey="id"
                @edit="item => openEditModal('학력', education, item)"
                @remove="id => removeItem(education, id)"
            ></EditableTimeLineGroup>
        </InfoCard>
        <InfoCard title="자격증 · 어학" @add="openAddModal('자격증 · 어학', certificates)">
            <EditableTimeLineGroup
                :items="certificates"
                idKey="id"
                @edit="item => openEditModal('자격증 · 어학', certificates, item)"
                @remove="id => removeItem(certificates, id)"
            ></EditableTimeLineGroup>
        </InfoCard>
        <InfoCard title="경력" @add="openAddModal('경력', career)">
            <EditableTimeLineGroup
                :items="career"
                idKey="id"
                @edit="item => openEditModal('경력', career, item)"
                @remove="id => removeItem(career, id)"
            ></EditableTimeLineGroup>
        </InfoCard>
        <InfoCard title="프로젝트" @add="openAddModal('프로젝트', projects)">
            <EditableTimeLineGroup
                :items="projects"
                idKey="id"
                @edit="item => openEditModal('프로젝트', projects, item)"
                @remove="id => removeItem(projects, id)"
            ></EditableTimeLineGroup>
        </InfoCard>
    </div>

    <ProfileFormModal
        v-if="modalState"
        :category="modalState.category"
        :mode="modalState.mode"
        :item="modalState.item"
        @save="saveModal"
        @close="closeModal"
    ></ProfileFormModal>
</template>

<style scoped>
.page-title {
    margin: 0 0 16px;
    font-size: 20px;
    font-weight: 700;
    color: #1a1d1f;
}

.info-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
    align-items: start;
}

.info-grid :deep(.full-width) {
    grid-column: 1 / -1;
}

</style>
