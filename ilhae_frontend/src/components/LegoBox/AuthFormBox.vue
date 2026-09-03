<script setup>
import OurBox from '@/components/OurBox.vue'

defineProps({
  eyebrow: {
    type: String,
    default: 'WELCOME',
  },
  title: {
    type: String,
    required: true,
  },
  description: {
    type: String,
    required: true,
  },
})
</script>

<template>
  <OurBox class="auth-form-box" padding-class="" :shadow="false">
    <header>
      <small>{{ eyebrow }}</small>
      <h2>{{ title }}</h2>
      <p>{{ description }}</p>
    </header>
    <slot />
  </OurBox>
</template>

<style scoped>
.auth-form-box {
  width: min(100%, 390px);
  border-radius: 0;
}

header {
  margin-bottom: 30px;
}

small {
  color: #e31b23;
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.1em;
}

h2 {
  margin: 13px 0 8px;
  font-size: 28px;
  letter-spacing: -0.07em;
}

p {
  margin: 0;
  color: #858585;
  font-size: 13px;
}
</style>
