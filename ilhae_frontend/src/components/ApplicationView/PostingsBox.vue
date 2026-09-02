<script setup>
import { computed, onMounted, ref } from "vue";
import OurBox from "../OurBox.vue";
import PostCard from "./PostingCard.vue";
import { api } from "@/api/client.js";

const allPosts = ref([]);
const starredPosts = ref([]);

const starredIds = computed(() => {
  return new Set(starredPosts.value.map((post) => post.id));
});

onMounted(async () => {
  const [posts, starred] = await Promise.all([api.getPostings(), api.getMyStarredPostings()]);

  allPosts.value = posts;
  starredPosts.value = starred;
});
</script>

<template>
  <OurBox>
    <div class="postContainer">
      <div class="postHeader" style="display: flex; flex-direction: row; margin: auto;">
        <p>공고이름</p>
        <p>직무적합도</p>
        <p>언제까지</p>
        <p>즐겨찾기</p>
        <p>회사정보</p>
        <p>지우기</p>
      </div>
      <PostCard
        v-for="post in allPosts"
        :key="post.id"
        :post="post"
        :starred="starredIds.has(post.id)"
      />
      <div class="postFooter" style="display: flex; flex-direction: row; margin: auto">
        내 정보 기준으로 계산된 에상 합격률입니다.
      </div>
    </div>
  </OurBox>
</template>

<style scoped></style>
