<script setup>
import { computed, onMounted, ref } from "vue";
import OurBox from "../OurBox.vue";
import PostCard from "./PostingCard.vue";
import { api } from "@/api/client.js";
import RequirementsCard from "./RequirementsCard.vue";
import RelevanceCard from "./RelevanceCard.vue";

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

const showRequirements = ref(false)
const targetRequirements = ref("")
const showRelevance = ref(false)
const handleRequestInfo = (postId) => {
  showRequirements.value = true
  targetRequirements.value = postId
}
const handeCloseInfo = () => {
  showRequirements.value = false
  showRelevance.value = false
  targetRequirements.value = ""
}
const handleRelevance = (postId) => {
  showRelevance.value = true
  targetRequirements.value = postId
}
</script>

<template>
  <OurBox>
    <div class="postContainer">
      <RequirementsCard v-if="showRequirements" @emitCloseCard="handeCloseInfo" :id="targetRequirements" />
      <RelevanceCard v-if="showRelevance" @emitCloseCard="handeCloseInfo" :id="targetRequirements" />
      
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
        @emitRequestInfo="handleRequestInfo"
        @emitRequestRelevance="handleRelevance"
      />
      <div class="postFooter" style="display: flex; flex-direction: row; margin: auto">
        내 정보 기준으로 계산된 에상 합격률입니다.
      </div>
    </div>
  </OurBox>
</template>

<style scoped>
.postHeader p {
  padding: 20px;
}
</style>
