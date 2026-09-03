<script setup>
import { RouterView, useRoute } from 'vue-router';
import OurHeader from './components/OurHeader.vue';

const route = useRoute()
</script>

<template>
  <OurHeader v-if="!route.meta.hideHeader"/>
  <RouterView />
</template>

<style scoped></style>
