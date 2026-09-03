<script setup>
    defineProps({
        label:{type:String, required:true},
        meta:String,
        dashed: Boolean
    })
</script>

<template>
    <span class="chip" :class="{dashed}">
        {{ label }} <span v-if="meta" class="meta">{{ meta }}</span>
    </span>
</template>

<style scoped>
.chip {
    display: inline-flex;
    align-items: center;
    gap: 6px;
    padding: 6px 14px;
    border: 1px solid #ececec;
    border-radius: 999px;
    background: #f7f7f8;
    font-size: 13px;
    color: #1a1d1f;
}

.chip .meta {
    color: #9aa0a6;
    font-size: 12px;
}

.chip.dashed {
    background: transparent;
    border-style: dashed;
    color: #9aa0a6;
    cursor: pointer;
}
</style>