<script setup>
import OurBox from './OurBox.vue';

    defineProps({label: {type: String, default: ''},
    type: {type: String, default: 'text'},
    placeholder: {type: String, default: ''}})

    const modelValue = defineModel({type:String, default:''})
</script>

<template>
    <OurBox class="form-field">
        <label>{{ label }}</label>
        <input :type="type" :placeholder="placeholder" v-model="modelValue">
    </OurBox>
</template>

<style scoped>
</style>