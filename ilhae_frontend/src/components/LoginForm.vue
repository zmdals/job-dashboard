<script setup>
import FormField from "./FormField.vue";
import OurBox from "./OurBox.vue";
import { ref } from "vue";
import { useRouter } from "vue-router";

import { api } from "@/api/client.js";

const router = useRouter();

const email = ref("");
const password = ref("");
//const emit = defineEmits(['login', 'signup'])
const errorMessage = ref("");
const loading = ref(false);
async function handleLogin() {
  console.log("login emit", { email: email.value, password: password.value });
  //emit('login', {email: email.value, password: password.value})
  if (!email.value || !password.value) {
    errorMessage.value = "아이디와 비밀번호를 입력해주세요.";
    return;
  }
  loading.value = true;
  errorMessage.value = "";
  try {
    const response = await api.login({
      id: email.value,
      password: password.value,
    })

    localStorage.setItem(
      'accessToken',
      response.accessToken,
    )

    console.log('login success', response)

    // 로그인 성공 후 이동할 페이지
    router.push('/home')
  } catch (error) {
    console.error('login failed', error)

    errorMessage.value =
      error.message ||
      '로그인에 실패했습니다.'
  } finally {
    loading.value = false
  }
}
function handleSignup() {router.push("/signup")}
</script>

<template>
  <OurBox>
    <div class="login-form">
      <h2>로그인</h2>
      <form @submit.prevent="handleLogin">
      <FormField
        label="이메일"
        type="email"
        v-model="email"
        placeholder="이메일을 입력하세요"
      ></FormField>
      <FormField
        label="비밀번호"
        type="password"
        v-model="password"
        placeholder="비밀번호를 입력하세요"
      ></FormField>
      <button
      type="submit"
      :disabled="loading"
    >
      {{ loading ? '로그인 중...' : '로그인' }}
    </button>
      <button @click="handleSignup">회원가입</button>
      </form>
    </div>
  </OurBox>
</template>

<style scoped></style>
