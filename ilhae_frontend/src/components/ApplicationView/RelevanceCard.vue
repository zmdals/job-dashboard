<script setup>
import { onMounted, ref } from "vue";
import OurBox from "../OurBox.vue";
import { api } from "@/api/client.js";

const props = defineProps({
    id:{type:String, required:true}
})

const relevance = ref("")

onMounted(async () => {
    relevance.value = await api.getPostingRelevance(props.id);
})

const emits = defineEmits(["emitCloseCard"]);

const emitCloseCard = () => {
  emits("emitCloseCard");
};
</script>

<template>
  <OurBox bg-color="#ffffff" class="reqCardContainer">
    <div>
      <button @click.stop="emitCloseCard">X</button>
      <div>
        <p>{{relevance}}</p>
      </div>
    </div>
  </OurBox>
</template>

<style scoped>
.reqCardContainer {
  position: fixed;
  z-index: 1000;
  inset: 0;
  margin: auto;
  height: 500px;
  width: 500px;
}
</style>
