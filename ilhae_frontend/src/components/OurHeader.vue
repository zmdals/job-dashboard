<script setup>
import { useRouter } from "vue-router";

const router = useRouter();

function gotoHome() {
  router.push("/home");
}
function gotoPosts() {
  router.push("/postings");
}
function gotoProfile() {
  router.push("/profile");
}
</script>

<template>
  <div class="headerContainer">
    <div @click="gotoHome">HOME</div>
    <div @click="gotoPosts">POSTS</div>
    <div @click="gotoProfile">PROFILE</div>
  </div>
</template>

<style scoped>
.headerContainer {
  display: flex;
  flex-direction: row;
}
.headerContainer div {
  padding: 20px;
}
</style>
