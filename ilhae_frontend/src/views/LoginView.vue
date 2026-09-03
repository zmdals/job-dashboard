<script setup>
import AuthBrandPanel from '@/components/AuthBrandPanel.vue'
import LoginForm from '@/components/LoginForm.vue'
</script>

<template>
  <main class="auth-page">
    <AuthBrandPanel />
    <LoginForm />
  </main>
</template>

<style scoped>
.auth-page {
  display: grid;
  grid-template-columns: 1fr 1fr;
  min-height: 100vh;
}

@media (max-width: 760px) {
  .auth-page {
    display: block;
  }
}
</style>
