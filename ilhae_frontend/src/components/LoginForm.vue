<script setup>
import { computed, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import AuthFormBox from '@/components/LegoBox/AuthFormBox.vue'
import AuthTabs from '@/components/LegoBox/AuthTabs.vue'
import FormField from '@/components/FormField.vue'
import { useAuthStore } from '@/stores/authStore'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const mode = ref(route.query.mode === 'signup' ? 'signup' : 'login')
const loginEmail = ref('')
const loginPassword = ref('')
const rememberLogin = ref(false)
const signupId = ref('')
const signupEmail = ref('')
const signupName = ref('')
const signupPassword = ref('')
const errorMessage = ref('')
const successMessage = ref('')

const heading = computed(() => mode.value === 'login'
  ? { eyebrow: 'WELCOME', title: '다시 만났네요', description: '오늘도 취업했음청년과 함께 시작해요.' }
  : { eyebrow: 'JOIN US', title: '함께 시작해요', description: '나의 취업 여정을 한곳에서 관리해 보세요.' })

watch(mode, (value) => {
  errorMessage.value = ''
  void router.replace({ query: value === 'signup' ? { mode: 'signup' } : {} })
})

watch(
  () => route.query.mode,
  (value) => {
    mode.value = value === 'signup' ? 'signup' : 'login'
  },
)

async function handleLogin() {
  errorMessage.value = ''
  successMessage.value = ''

  try {
    await authStore.login(loginEmail.value, loginPassword.value, rememberLogin.value)

    const redirect = route.query.redirect
    const destination =
      typeof redirect === 'string' &&
      redirect.startsWith('/') &&
      !redirect.startsWith('//')
        ? redirect
        : '/home'

    await router.replace(destination)
  } catch (error) {
    errorMessage.value = error.message || '로그인에 실패했습니다.'
  }
}

async function handleSignup() {
  errorMessage.value = ''
  successMessage.value = ''

  try {
    await authStore.signup({
      id: signupId.value,
      email: signupEmail.value,
      name: signupName.value,
      password: signupPassword.value,
    })

    loginEmail.value = signupEmail.value
    mode.value = 'login'
    successMessage.value = '회원가입이 완료되었습니다. 로그인해 주세요.'
  } catch (error) {
    errorMessage.value = error.message || '회원가입에 실패했습니다.'
  }
}
</script>

<template>
  <section class="form-side">
    <AuthFormBox v-bind="heading">
      <AuthTabs v-model="mode" />

      <form v-if="mode === 'login'" @submit.prevent="handleLogin">
        <FormField v-model="loginEmail" label="이메일" type="email" placeholder="이메일을 입력해 주세요" autocomplete="username" required />
        <FormField v-model="loginPassword" label="비밀번호" type="password" placeholder="비밀번호를 입력해 주세요" autocomplete="current-password" required />
        <div class="options">
          <label><input v-model="rememberLogin" type="checkbox" /> 로그인 상태 유지</label>
          <button type="button">비밀번호 찾기</button>
        </div>
        <p v-if="errorMessage" class="message error" role="alert">{{ errorMessage }}</p>
        <p v-if="successMessage" class="message success" role="status">{{ successMessage }}</p>
        <button class="submit" type="submit" :disabled="authStore.loading">
          {{ authStore.loading ? '로그인 중...' : '로그인하기' }}
        </button>
      </form>

      <form v-else @submit.prevent="handleSignup">
        <FormField v-model="signupId" label="아이디" placeholder="사용할 아이디를 입력해 주세요" autocomplete="username" required />
        <FormField v-model="signupEmail" label="이메일" type="email" placeholder="이메일을 입력해 주세요" autocomplete="email" required />
        <FormField v-model="signupName" label="이름" placeholder="이름을 입력해 주세요" autocomplete="name" required />
        <FormField v-model="signupPassword" label="비밀번호" type="password" placeholder="비밀번호를 입력해 주세요" autocomplete="new-password" required />
        <p v-if="errorMessage" class="message error" role="alert">{{ errorMessage }}</p>
        <button class="submit" type="submit" :disabled="authStore.loading">
          {{ authStore.loading ? '가입 중...' : '회원가입하기' }}
        </button>
      </form>
    </AuthFormBox>
  </section>
</template>

<style scoped>
.form-side {
  display: grid;
  place-items: center;
  min-width: 0;
  padding: 40px 9%;
  background: #fff;
}

.options {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin: -1px 0 25px;
  color: #858585;
  font-size: 11px;
}

.options label {
  display: flex;
  gap: 6px;
  align-items: center;
}

.options button {
  padding: 0;
  border: 0;
  color: #e31b23;
  background: none;
  font: inherit;
}

.submit {
  width: 100%;
  height: 52px;
  border: 0;
  border-radius: 7px;
  color: #fff;
  background: #e31b23;
  font: inherit;
  font-size: 13px;
  font-weight: 700;
  cursor: pointer;
}

.submit:hover:not(:disabled) {
  background: #c9161d;
}

.submit:disabled {
  opacity: 0.65;
  cursor: wait;
}

.message {
  margin: -8px 0 14px;
  font-size: 11px;
}

.message.error { color: #e31b23; }
.message.success { color: #55705b; }

@media (max-width: 760px) {
  .form-side {
    padding: 48px 26px 60px;
  }
}
</style>
