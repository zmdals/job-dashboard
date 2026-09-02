<script setup>
import FormField from './FormField.vue';
import OurBox from './OurBox.vue';
import {ref} from 'vue'

const email=ref('')
const password=ref('')
const emit = defineEmits(['login', 'signup'])
function handleLogin(){
    console.log('login emit', { email: email.value, password: password.value })
    emit('login', {email: email.value, password: password.value})
}

</script>

<template>
    <OurBox>
        <div class="login-form">
        <h2>로그인</h2>
        <FormField label="이메일" type="email" v-model="email" placeholder="이메일을 입려하세요"></FormField>
        <FormField label="비밀번호" type="password" v-model="password" placeholder="비밀번호를 입력하세요"></FormField>
        <button @click="handleLogin">로그인</button>
        <button @click="emit('signup')">회원가입</button>
    </div>
    </OurBox>
    
</template>

<style scoped>
</style>