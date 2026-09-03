<script setup>
import { computed, useId } from 'vue'
import OurBox from './OurBox.vue'

defineOptions({ inheritAttrs: false })

const props = defineProps({
  id: { type: String, default: '' },
  label: { type: String, default: '' },
  type: { type: String, default: 'text' },
  placeholder: { type: String, default: '' },
})

const modelValue = defineModel({ type: String, default: '' })
const generatedId = useId()
const fieldId = computed(() => props.id || generatedId)
</script>

<template>
  <OurBox class="form-field" padding-class="" :shadow="false">
    <label :for="fieldId">{{ label }}</label>
    <input
      :id="fieldId"
      v-model="modelValue"
      v-bind="$attrs"
      :type="type"
      :placeholder="placeholder"
    />
  </OurBox>
</template>

<style scoped>
.form-field {
  overflow: visible;
  border-radius: 0;
}

label {
  display: block;
  margin: 0 0 8px;
  font-size: 12px;
  font-weight: 700;
}

input {
  width: 100%;
  height: 50px;
  margin-bottom: 19px;
  padding: 0 14px;
  border: 1px solid #e7e7e7;
  border-radius: 7px;
  outline: 0;
  font: inherit;
  font-size: 13px;
}

input:focus {
  border-color: #e31b23;
  box-shadow: 0 0 0 3px #fff0f0;
}

input::placeholder {
  color: #b7b7b7;
}
</style>
