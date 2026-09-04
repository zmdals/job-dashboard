<script setup>
import { computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'
import { useMeStore } from '@/stores/meStore'

const router = useRouter()
const authStore = useAuthStore()
const meStore = useMeStore()

const userName = computed(() => meStore.profile?.name || '김청년')

onMounted(() => {
  if (authStore.isAuthenticated) {
    meStore.ensureMe().catch(() => {})
  }
})

function gotoHome() {
  router.push('/home')
}
function gotoPosts() {
  router.push('/postings')
}
function gotoProfile() {
  router.push('/profile')
}
async function logout() {
  authStore.logout()
  meStore.reset()
  await router.replace({ name: 'login' })
}
</script>

<template>
  <header class="headerContainer">
    <button class="logo" type="button" aria-label="취업했음청년 홈" @click="gotoHome">
      <span class="logo-mark" aria-hidden="true">취</span>
      <span>취업했음청년</span>
    </button>

    <nav class="home-nav" aria-label="주요 메뉴">
      <button type="button" :class="{ active: $route.name === 'home' }" @click="gotoHome">지원현황</button>
      <button
        type="button"
        :class="{ active: ['postings', 'posting-report'].includes($route.name) }"
        @click="gotoPosts"
      >
        채용공고
      </button>
      <button type="button" :class="{ active: $route.name === 'profile' }" @click="gotoProfile">내프로필</button>
    </nav>

    <div class="user-menu">
      {{ userName }} 님
      <button class="logout" type="button" @click="logout">로그아웃</button>
    </div>
  </header>
</template>

<style scoped>
:global(*) {
  box-sizing: border-box;
}

:global(body) {
  margin: 0;
  color: #202124;
  font-family: Arial, "Apple SD Gothic Neo", sans-serif;
}

.headerContainer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 78px;
  padding: 0 7%;
  border-bottom: 1px solid #e7e7e7;
  background: #fff;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 0;
  border: 0;
  color: #202124;
  background: none;
  font: inherit;
  font-size: 14px;
  font-weight: 700;
  cursor: pointer;
}

.logo-mark {
  display: grid;
  place-items: center;
  width: 30px;
  height: 30px;
  border-radius: 8px;
  color: #fff;
  background: #e31b23;
  font-weight: 700;
}

.home-nav {
  display: flex;
  gap: 34px;
  margin-right: 34px;
  margin-left: auto;
}

.home-nav button {
  padding: 8px 0;
  border: 0;
  color: #888;
  background: none;
  font: inherit;
  font-size: 13px;
  cursor: pointer;
}

.home-nav button.active {
  color: #e31b23;
  font-weight: 700;
}

.user-menu {
  color: #666;
  font-size: 12px;
  white-space: nowrap;
}

.logout {
  margin-left: 15px;
  padding: 0;
  border: 0;
  color: #999;
  background: none;
  font: inherit;
  font-size: 11px;
  cursor: pointer;
}

.logout:hover,
.logout:focus-visible {
  color: #e31b23;
}

@media (max-width: 760px) {
  .headerContainer {
    height: auto;
    flex-wrap: wrap;
    gap: 18px;
    padding: 20px 24px;
  }

  .home-nav {
    order: 3;
    width: 100%;
    justify-content: space-between;
    margin: 0;
  }

  .user-menu {
    margin-left: auto;
  }
}
</style>
