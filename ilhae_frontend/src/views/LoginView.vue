<script setup>
import AuthBrandPanel from '@/components/AuthBrandPanel.vue';
import LoginForm from '@/components/LoginForm.vue';
</script>

<template>
  <div class="login-page">
    <AuthBrandPanel />
    <LoginForm />
  </div>
</template>

<style scoped>
.login-page {
  display: flex;
  min-height: 100vh;
}
.login-form {
  width: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>