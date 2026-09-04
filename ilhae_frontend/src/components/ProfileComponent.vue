<script setup>
import { onMounted, ref } from 'vue'
import { api } from '@/api/client'
import EditableTimeLineGroup from '@/components/EditableTimeLineGroup.vue'
import InfoCard from '@/components/InfoCard.vue'
import ProfileFormModal from '@/components/ProfileFormModal.vue'

const educationStatusOptions = [
  { value: 'ENROLLED', label: '재학' },
  { value: 'ON_LEAVE', label: '휴학' },
  { value: 'GRADUATED', label: '졸업' },
  { value: 'DROPPED_OUT', label: '중퇴' },
  { value: 'COMPLETED', label: '수료' },
]

const fieldSchemas = {
  학력: [
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
  경력: [
    { key: 'companyName', label: '회사명', type: 'text', placeholder: '회사 이름' },
    { key: 'position', label: '직책', type: 'text', placeholder: '직책/직무' },
    { key: 'startDate', label: '입사일', type: 'date' },
    { key: 'endDate', label: '퇴사일', type: 'date' },
    {
      key: 'description',
      label: '설명',
      type: 'textarea',
      placeholder: '주요 업무나 성과를 입력해 주세요',
    },
  ],
  프로젝트: [
    { key: 'projectName', label: '프로젝트명', type: 'text', placeholder: '프로젝트 이름' },
    { key: 'role', label: '역할', type: 'text', placeholder: '담당 역할' },
    { key: 'techStack', label: '기술 스택', type: 'text', placeholder: '사용한 기술' },
    {
      key: 'description',
      label: '설명',
      type: 'textarea',
      placeholder: '주요 내용이나 성과를 입력해 주세요',
    },
  ],
  '수상 경력': [
    { key: 'awardName', label: '수상명', type: 'text', placeholder: '수상명' },
    { key: 'organizer', label: '주최기관', type: 'text', placeholder: '주최기관' },
    { key: 'awardDate', label: '수상일', type: 'date' },
    {
      key: 'description',
      label: '설명',
      type: 'textarea',
      placeholder: '수상 내용이나 성과를 입력해 주세요',
    },
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
  return { title: item.projectName, period: item.role, extra: item.techStack }
}

function mapAward(item) {
  return {
    title: item.awardName,
    period: item.awardDate,
    extra: [item.organizer, item.description].filter(Boolean).join(' · '),
  }
}

const education = ref([])
const career = ref([])
const projects = ref([])
const certificates = ref([])
const awards = ref([])

const apiByCategory = {
  학력: { add: api.addEducation, update: api.updateEducation, remove: api.deleteEducation },
  '자격증 · 어학': {
    add: api.addCertificate,
    update: api.updateCertificate,
    remove: api.deleteCertificate,
  },
  경력: { add: api.addCareer, update: api.updateCareer, remove: api.deleteCareer },
  프로젝트: { add: api.addProject, update: api.updateProject, remove: api.deleteProject },
  '수상 경력': { add: api.addAward, update: api.updateAward, remove: api.deleteAward },
}

onMounted(async () => {
  try {
    const [educationItems, careerItems, projectItems, certificateItems, awardItems] =
      await Promise.all([
        api.getEducations(),
        api.getCareers(),
        api.getProjects(),
        api.getCertificates(),
        api.getAwards(),
      ])

    education.value = educationItems
    career.value = careerItems
    projects.value = projectItems
    certificates.value = certificateItems
    awards.value = awardItems
  } catch (error) {
    console.error('내 스펙을 불러오지 못했습니다.', error)
  }
})

async function removeItem(category, list, id) {
  try {
    await apiByCategory[category].remove(id)
    const index = list.findIndex((item) => item.id === id)
    if (index !== -1) list.splice(index, 1)
  } catch (error) {
    alert(error.message || '삭제에 실패했습니다.')
  }
}

const modalState = ref(null)

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
      list.push(await apiByCategory[category].add(formValue))
    } else {
      const updated = await apiByCategory[category].update(item.id, formValue)
      const index = list.findIndex((listItem) => listItem.id === item.id)
      if (index !== -1) list[index] = updated
    }
    closeModal()
  } catch (error) {
    alert(error.message || '저장에 실패했습니다.')
  }
}
</script>

<template>
  <main class="profile-page">
    <div class="profile-title">
      <h2>내 프로필</h2>
      <p>나의 경험과 역량을 등록해 AI 예상 합격률을 높여보세요.</p>
    </div>

    <div class="info-grid">
      <InfoCard title="학력" @add="openAddModal('학력', education)">
        <EditableTimeLineGroup
          :items="education"
          id-key="id"
          :map-item="mapEducation"
          @edit="(item) => openEditModal('학력', education, item)"
          @remove="(id) => removeItem('학력', education, id)"
        />
      </InfoCard>
      <InfoCard title="자격증 · 어학" @add="openAddModal('자격증 · 어학', certificates)">
        <EditableTimeLineGroup
          :items="certificates"
          id-key="id"
          :map-item="mapCertificate"
          @edit="(item) => openEditModal('자격증 · 어학', certificates, item)"
          @remove="(id) => removeItem('자격증 · 어학', certificates, id)"
        />
      </InfoCard>
      <InfoCard title="경력" @add="openAddModal('경력', career)">
        <EditableTimeLineGroup
          :items="career"
          id-key="id"
          :map-item="mapCareer"
          @edit="(item) => openEditModal('경력', career, item)"
          @remove="(id) => removeItem('경력', career, id)"
        />
      </InfoCard>
      <InfoCard title="수상 경력" @add="openAddModal('수상 경력', awards)">
        <EditableTimeLineGroup
          :items="awards"
          id-key="id"
          :map-item="mapAward"
          @edit="(item) => openEditModal('수상 경력', awards, item)"
          @remove="(id) => removeItem('수상 경력', awards, id)"
        />
      </InfoCard>
      <InfoCard class="project-card" title="프로젝트" @add="openAddModal('프로젝트', projects)">
        <EditableTimeLineGroup
          :items="projects"
          id-key="id"
          :map-item="mapProject"
          @edit="(item) => openEditModal('프로젝트', projects, item)"
          @remove="(id) => removeItem('프로젝트', projects, id)"
        />
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
    />
  </main>
</template>

<style scoped>
.profile-page {
  min-height: calc(100vh - 64px);
  background: #fafafa;
}
.profile-title {
  display: flex;
  align-items: baseline;
  gap: 10px;
  max-width: var(--page-content-width);
  margin: 0 auto;
  padding: var(--page-top-space) var(--page-gutter) 20px;
}
.profile-title h2 {
  margin: 0;
  color: #202124;
  font-size: 30px;
  font-weight: 700;
  letter-spacing: -0.07em;
}
.profile-title p {
  margin: 0;
  color: #858585;
  font-size: 13px;
}
.info-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 16px;
  align-items: start;
  max-width: var(--page-content-width);
  margin: 0 auto;
  padding: 0 var(--page-gutter) var(--page-bottom-space);
}
.project-card {
  grid-column: 1 / -1;
}

@media (max-width: 760px) {
  .profile-title {
    display: block;
  }
  .profile-title h2 {
    margin-bottom: 8px;
    font-size: 26px;
  }
  .info-grid {
    grid-template-columns: 1fr;
  }
  .project-card {
    grid-column: auto;
  }
}
</style>
