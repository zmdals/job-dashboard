<script setup>
defineProps({
    label: String,
    modelValue: String,
    buttonText: String,
    hint: String,
    hintType: String,
})

defineEmits(['update:modelValue', 'action'])
</script>

<template>
    <div class="field">
        <label>{{ label }}</label>
        <div class="input-row">
            <input :value="modelValue" @input="$emit('update:modelValue', $event.target.value)">
            <button v-if="buttonText" @click="$emit('action')">{{ buttonText }}</button>
        </div>
        <span v-if="hint" :class="['hint', hintType]">{{ hint }}</span>
    </div>
</template>

<style scoped>
</style>