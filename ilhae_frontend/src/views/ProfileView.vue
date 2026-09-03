<script setup>
import { ref, onMounted } from 'vue';
import InfoCard from '@/components/InfoCard.vue';
import EditableTimeLineGroup from '@/components/EditableTimeLineGroup.vue';
import ProfileFormModal from '@/components/ProfileFormModal.vue';
import { api } from '@/api/client';

const educationStatusOptions = [
    { value: 'ENROLLED', label: '재학' },
    { value: 'ON_LEAVE', label: '휴학' },
    { value: 'GRADUATED', label: '졸업' },
    { value: 'DROPPED_OUT', label: '중퇴' },
    { value: 'COMPLETED', label: '수료' },
]

const fieldSchemas = {
    '학력': [
        { key: 'schoolName', label: '학교명', type: 'text', placeholder: '학교 이름' },
        { key: 'degree', label: '학위', type: 'text', placeholder: '학사, 석사 등' },
        { key: 'major', label: '전공', type: 'text', placeholder: '전공명' },
        { key: 'startDate', label: '입학일', type: 'date' },
        { key: 'endDate', label: '졸업일', type: 'date' },
        { key: 'educationStatus', label: '상태', type: 'select', options: educationStatusOptions },
    ],
    '자격증 · 어학': [
        { key: 'certName', label: '자격증・어학명', type: 'text', placeholder: '자격증 이름' },
        { key: 'issuer', label: '발급기관', type: 'text', placeholder: '발급기관' },
        { key: 'acquiredDate', label: '취득일', type: 'date' },
        { key: 'languageScore', label: '점수', type: 'number', placeholder: '어학 점수(선택)' },
    ],
    '경력': [
        { key: 'companyName', label: '회사명', type: 'text', placeholder: '회사 이름' },
        { key: 'position', label: '직책', type: 'text', placeholder: '직책/직무' },
        { key: 'startDate', label: '입사일', type: 'date' },
        { key: 'endDate', label: '퇴사일', type: 'date' },
        { key: 'description', label: '설명', type: 'textarea', placeholder: '주요 업무나 성과를 입력해 주세요' },
    ],
    '프로젝트': [
        { key: 'projectName', label: '프로젝트명', type: 'text', placeholder: '프로젝트 이름' },
        { key: 'role', label: '역할', type: 'text', placeholder: '담당 역할' },
        { key: 'techStack', label: '기술 스택', type: 'text', placeholder: '사용한 기술' },
        { key: 'description', label: '설명', type: 'textarea', placeholder: '주요 내용이나 성과를 입력해 주세요' },
    ],
}

function mapEducation(item) {
    return {
        title: [item.schoolName, item.degree].filter(Boolean).join(' · '),
        period: [item.startDate, item.endDate].filter(Boolean).join(' – '),
        extra: item.major,
    }
}
function mapCertificate(item) {
    return {
        title: item.certName,
        period: item.acquiredDate,
        extra: item.issuer || (item.languageScore ? `점수 ${item.languageScore}` : ''),
    }
}
function mapCareer(item) {
    return {
        title: [item.companyName, item.position].filter(Boolean).join(' · '),
        period: [item.startDate, item.endDate].filter(Boolean).join(' – '),
        extra: item.description,
    }
}
function mapProject(item) {
    return {
        title: item.projectName,
        period: item.role,
        extra: item.techStack,
    }
}

const education = ref([])
const career = ref([])
const projects = ref([])
const certificates = ref([])

const apiByCategory = {
    '학력': { add: api.addEducation, update: api.updateEducation, remove: api.deleteEducation },
    '자격증 · 어학': { add: api.addCertificate, update: api.updateCertificate, remove: api.deleteCertificate },
    '경력': { add: api.addCareer, update: api.updateCareer, remove: api.deleteCareer },
    '프로젝트': { add: api.addProject, update: api.updateProject, remove: api.deleteProject },
}

onMounted(async () => {
    try {
        const specs = await api.getMySpecs()
        education.value = specs.educations ?? []
        career.value = specs.careers ?? []
        projects.value = specs.projects ?? []
        certificates.value = specs.certificates ?? []
    } catch (e) {
        console.error('내 스펙을 불러오지 못했습니다.', e)
    }
})

async function removeItem(category, list, id) {
    try {
        await apiByCategory[category].remove(id)
        const index = list.findIndex(item => item.id === id)
        if (index !== -1) list.splice(index, 1)
    } catch (e) {
        alert(e.message || '삭제에 실패했습니다.')
    }
}

const modalState = ref(null)
// { category, mode: 'add' | 'edit', list, item }

function openAddModal(category, list) {
    modalState.value = { category, mode: 'add', list, item: {} }
}
function openEditModal(category, list, item) {
    modalState.value = { category, mode: 'edit', list, item }
}
function closeModal() {
    modalState.value = null
}
async function saveModal(formValue) {
    const { mode, category, list, item } = modalState.value
    try {
        if (mode === 'add') {
            const created = await apiByCategory[category].add(formValue)
            list.push(created)
        } else {
            const updated = await apiByCategory[category].update(item.id, formValue)
            const index = list.findIndex(i => i.id === item.id)
            if (index !== -1) list[index] = updated
        }
        closeModal()
    } catch (e) {
        alert(e.message || '저장에 실패했습니다.')
    }
}
</script>

<template>
    <h2 class="page-title">내정보</h2>
    <div class="info-grid">
        <InfoCard title="학력" @add="openAddModal('학력', education)">
            <EditableTimeLineGroup
                :items="education"
                idKey="id"
                :mapItem="mapEducation"
                @edit="item => openEditModal('학력', education, item)"
                @remove="id => removeItem('학력', education, id)"
            ></EditableTimeLineGroup>
        </InfoCard>
        <InfoCard title="자격증 · 어학" @add="openAddModal('자격증 · 어학', certificates)">
            <EditableTimeLineGroup
                :items="certificates"
                idKey="id"
                :mapItem="mapCertificate"
                @edit="item => openEditModal('자격증 · 어학', certificates, item)"
                @remove="id => removeItem('자격증 · 어학', certificates, id)"
            ></EditableTimeLineGroup>
        </InfoCard>
        <InfoCard title="경력" @add="openAddModal('경력', career)">
            <EditableTimeLineGroup
                :items="career"
                idKey="id"
                :mapItem="mapCareer"
                @edit="item => openEditModal('경력', career, item)"
                @remove="id => removeItem('경력', career, id)"
            ></EditableTimeLineGroup>
        </InfoCard>
        <InfoCard title="프로젝트" @add="openAddModal('프로젝트', projects)">
            <EditableTimeLineGroup
                :items="projects"
                idKey="id"
                :mapItem="mapProject"
                @edit="item => openEditModal('프로젝트', projects, item)"
                @remove="id => removeItem('프로젝트', projects, id)"
            ></EditableTimeLineGroup>
        </InfoCard>
    </div>

    <ProfileFormModal
        v-if="modalState"
        :category="modalState.category"
        :mode="modalState.mode"
        :fields="fieldSchemas[modalState.category]"
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
