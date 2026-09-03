<script setup>
import { onMounted, ref } from "vue";
import OurBox from "../OurBox.vue";
import { api } from "@/api/client.js";

const props = defineProps({
    id:{type:String, required:true}
})

const requirements = ref("")

onMounted(async () => {
    requirements.value = await api.getPostingInfo(props.id);
})

const emits = defineEmits(["emitCloseCard"]);

const emitCloseCard = () => {
  emits("emitCloseCard");
};
</script>

<template>
  <OurBox bg-color="#0101010" class="reqCardContainer">
    <div>
      <button @click.stop="emitCloseCard">X</button>
      <div>
        <p>{{requirements}}</p>
      </div>
    </div>
  </OurBox>
</template>

<style scoped>
.reqCardContainer {
  position: absolute;
  z-index: 1000;
  right: 10%;
  height: 100px;
  width: 100px;
}
</style>
