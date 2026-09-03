<script setup>
import {ref} from 'vue'
    defineProps({title:String})

    const isEditing = ref(false)
    function toggleEdit() {
        isEditing.value = ! isEditing.value
    }
</script>

<template>
    <div class="info-card">
        <div class="card-header">
            <span class="title">{{ title }}</span>
            <span class="edit-link" @click="toggleEdit">
                {{ isEditing ? '완료' : '수정' }}
            </span>
        </div>
        <div class="card-body">
            <slot :editing="isEditing" />
        </div>
    </div>
</template>

<style scoped>
.info-card {
    background: #fff;
    border: 1px solid #ececec;
    border-radius: 12px;
    padding: 20px 22px;
}

.card-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding-bottom: 14px;
    margin-bottom: 14px;
    border-bottom: 1px solid #f0f0f0;
}

.card-header .title {
    font-size: 15px;
    font-weight: 700;
    color: #1a1d1f;
}

.card-header .edit-link {
    font-size: 13px;
    color: #1f6b4a;
    cursor: pointer;
}

.card-body {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
    min-height: 20px;
}
</style>