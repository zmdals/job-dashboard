<script setup>
import { ref } from 'vue';
import InfoCard from '@/components/InfoCard.vue';

import EditableTimeLineGroup from '@/components/EditableTimeLineGroup.vue';
import EditableChipGroup from '@/components/EditableChipGroup.vue';

function useTimelineActions(listRef, idKey) {
    function add() {
        listRef.value.push({ [idKey]: Date.now(), title: '', period: '', extra: '' })
    }
    function remove(id) {
        listRef.value = listRef.value.filter(item => item[idKey] !== id)
    }
    return { add, remove }
}

const education = ref([
    {
        education_id: 1,
        title: '한국대학교 생명공학과 학사',
        period: '2019.03 – 2023.02',
        extra: '학점 3.72 / 4.5',
    },
    {
        education_id: 2,
        title: '한국대학교 대학원 (재학)',
        period: '2023.03 – ',
        extra: '생물공정 전공',
    },
])

const career = ref([
    {
        career_id: 1,
        title: '바이오공정연구실 학부연구생',
        period: '2022.01 – 2023.02',
        extra: '14개월 · 세포배양·정제 실험',
    },
    {
        career_id: 2,
        title: '한빛소재 하계 인턴',
        period: '2024.07 – 2024.08',
        extra: '품질분석 보조',
    },
])

const projects = ref([
    {
        projects_id: 1,
        title: '교내 캡스톤 경진대회 장려상',
        period: '2022.11',
        extra: '미생물 배양 자동화',
    },
])

const certificates = ref([
    { certificate_id: 1, cert_name: '화학분석기사', acquired_date: '2024.05' },
    { certificate_id: 2, cert_name: 'OPIc IM2', acquired_date: '2025.03' },
    { certificate_id: 3, cert_name: 'GMP 교육 수료', acquired_date: '' },
])

const awards = ref([
    {
        award_id: 1,
        award_name: '교내 캡스톤 경진대회 장려상',
        award_date: '2022.11',
        organizer: '미생물 배양 자동화',
    },
])

const { add: addEducation, remove: removeEducation } = useTimelineActions(education, 'education_id')
const { add: addCareer, remove: removeCareer } = useTimelineActions(career, 'career_id')
const { add: addProject, remove: removeProject } = useTimelineActions(projects, 'projects_id')
const { add: addAward, remove: removeAward } = useTimelineActions(awards, 'award_id')

function addCertificate() {
    certificates.value.push({ certificate_id: Date.now(), cert_name: '', acquired_date: '' })
}
function removeCertificate(id) {
    certificates.value = certificates.value.filter(c => c.certificate_id !== id)
}
</script>

<template>
    <h2 class="page-title">내정보</h2>
    <div class="info-grid">
        <InfoCard title="학력" v-slot="{ editing }">
            <EditableTimeLineGroup
                :items="education"
                idKey="education_id"
                :editing="editing"
                :fields="[
                    {key:'title', label:'학교・전공'},
                    { key: 'period', label: '기간' },
                    { key: 'extra', label: '비고' },
                ]"
                @add="addEducation"
                @remove="removeEducation"
            ></EditableTimeLineGroup>
        </InfoCard>
        <InfoCard title="경력" v-slot="{editing}">
            <EditableTimeLineGroup
                :items="career"
                idKey="career_id"
                :editing="editing"
                :fields="[
                    {key:'title', label:'경력'},
                    {key:'period', label:'기간'},
                    {key:'extra', label:'비고'},
                ]"
                @add="addCareer"
                @remove="removeCareer"
            ></EditableTimeLineGroup>
        </InfoCard>
        <InfoCard title="프로젝트" v-slot="{editing}">
            <EditableTimeLineGroup
                :items="projects"
                idKey="projects_id"
                :editing="editing"
                :fields="[
                    {key:'title', label:'프로젝트명'},
                    {key:'period', label:'기간'},
                    {key:'extra', label:'비고'},
                ]"
                @add="addProject"
                @remove="removeProject"
            ></EditableTimeLineGroup>
        </InfoCard>
        <InfoCard title="자격증 · 어학" v-slot="{ editing }">
            <EditableChipGroup
                :items="certificates"
                idKey="certificate_id"
                :editing="editing"
                labelKey="cert_name"
                labelText="자격증・어학명"
                metaKey="acquired_date"
                metaText="취득일"
                @add="addCertificate"
                @remove="removeCertificate"
            ></EditableChipGroup>
        </InfoCard>
        <InfoCard title="수상경력"  v-slot="{editing}">
            <EditableTimeLineGroup
                :items="awards"
                idKey="award_id"
                :editing="editing"
                :fields="[
                    {key:'award_name', label:'수상'},
                    {key:'award_date', label:'수상 날짜'},
                    {key:'organizer', label:'비고'},
                ]"
                @add="addAward"
                @remove="removeAward"
            ></EditableTimeLineGroup>
            
        </InfoCard>
        <InfoCard title="희망 취업 조건"></InfoCard>
    </div>
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

</style>