<script setup>
import AuthBrandPanel from '@/components/AuthBrandPanel.vue';
import SignupForm from '@/components/SignupForm.vue';

</script>

<template>
  <div class="signup-page">
    <AuthBrandPanel />
    <SignupForm />
  </div>
</template>

<style scoped>
.signup-page {
  display: flex;
  min-height: 100vh;
}
.signup-form {
  width: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>