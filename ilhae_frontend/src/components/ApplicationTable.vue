<script setup>
defineProps({
  applications: {
    type: Array,
    required: true,
  },
})

const statusOptions = ['서류준비', '지원완료', '코딩테스트 (필기시험)', '1차면접', '2차면접', '최종면접', '탈락', '최종합격']
</script>

<template>
    <div class="app-head">
        <h3>내 지원 목록</h3>
        <span>총 {{ applications.length }}개</span>
    </div>
    <div class="app-table">
        <div class="table-row" v-for="app in applications" :key="app.id">
            <div class="company-name">
                <span class="company-logo">{{ app.company?.slice(0, 1) }}</span>
                {{ app.company }}
            </div>
            <div class="company-meta">{{ app.role }}</div>
            <div class="deadline">지원일 {{ app.announceDate }}</div>
            <select class="status-select" v-model="app.status">
                <option v-for="opt in statusOptions" :key="opt" :value="opt">{{ opt }}</option>
            </select>
            <button class="delete-btn" type="button">삭제</button>
        </div>
    </div>
</template>

<style scoped>
.app-head {
    display: flex;
    align-items: center;
    justify-content: space-between;
    max-width: 1060px;
    margin: 0 auto;
    padding: 0 24px 14px;
}

.app-head h3 {
    margin: 0;
    font-size: 17px;
    letter-spacing: -.05em;
    color: #202124;
}

.app-head span {
    color: #999;
    font-size: 11px;
}

.app-table {
    max-width: 1060px;
    margin: 0 auto 80px;
    padding: 0 24px;
    border-top: 1px solid #333;
    background: #fff;
}

.table-row {
    display: grid;
    grid-template-columns: 1.45fr 1fr 1fr minmax(110px, 1fr) 42px;
    align-items: center;
    gap: 20px;
    min-height: 78px;
    padding: 0 22px;
    border-bottom: 1px solid #e7e7e7;
}

.company-name {
    display: flex;
    align-items: center;
    gap: 12px;
    font-size: 13px;
    font-weight: 700;
    color: #202124;
}

.company-logo {
    display: grid;
    place-items: center;
    width: 34px;
    height: 34px;
    border-radius: 8px;
    color: #555;
    background: #f0f0f0;
    font-size: 11px;
}

.company-meta,
.deadline {
    color: #999;
    font-size: 11px;
}

.status-select {
    width: 100%;
    padding: 8px 9px;
    border: 1px solid #ddd;
    border-radius: 5px;
    color: #555;
    background: #fff;
    font-size: 11px;
}

.status-select:focus {
    border-color: #e31b23;
    outline: 0;
}

.delete-btn {
    padding: 0;
    border: 0;
    color: #999;
    background: none;
    font-size: 11px;
    cursor: pointer;
}

.delete-btn:hover {
    color: #e31b23;
}
</style>