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

async function handleSignup() {
  console.log("signup", { email: email.value, password: password.value });
  if (!email.value || !password.value) {
    errorMessage.value = "아이디와 비밀번호를 입력해주세요.";
    return;
  }
  loading.value = true;
  errorMessage.value = "";
  try {
    const response = await api.signup({
      id: email.value,
      password: password.value,
    });

    localStorage.setItem("accessToken", response.accessToken);

    console.log("signup success", response);

    // 회원가입 성공 후 이동할 페이지
    router.push("/");
  } catch (error) {
    console.error("signup failed", error);

    errorMessage.value = error.message || "회원가입에 실패했습니다.";
  } finally {
    loading.value = false;
  }
}
</script>

<template>
  <OurBox>
    <div class="signup-form">
      <h2>로그인</h2>
      <form @submit.prevent="handleSignup">
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
        <FormField
          label="이름"
          type="text"
          v-model="name"
          placeholder="이름을 입력하세요"
        ></FormField>
        <FormField
          label="전화번호"
          type="tel"
          placeholder="010-1234-5678"
          pattern="[0-9]{3}-[0-9]{3,4}-[0-9]{4}"
          v-model="name"
        ></FormField>
        <button type="submit" :disabled="loading">
          {{ loading ? "회원가입 중..." : "회원가입" }}
        </button>
      </form>
    </div>
  </OurBox>
</template>

<style scoped></style>
