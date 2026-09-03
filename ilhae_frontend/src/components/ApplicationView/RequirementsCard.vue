<script setup>
import { onMounted, ref } from "vue";
import OurBox from "../OurBox.vue";
import { api } from "@/api/client.js";

const props = defineProps({
  id: { type: String, required: true },
});

const requirements = ref("");

onMounted(async () => {
  requirements.value = await api.getPostingInfo(props.id);
});

const emits = defineEmits(["emitCloseCard"]);

const emitCloseCard = () => {
  emits("emitCloseCard");
};
</script>

<template>
  <teleport to="body">
    <OurBox bg-color="#ffffff" class="reqCardContainer">
      <div>
        <button @click.stop="emitCloseCard">X</button>
        <div>
          <p>{{ requirements }}</p>
        </div>
      </div>
    </OurBox>
  </teleport>
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
