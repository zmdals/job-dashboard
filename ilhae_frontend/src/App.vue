<script setup>
import { RouterView, useRoute } from 'vue-router';
import OurHeader from './components/OurHeader.vue';

const route = useRoute()
</script>

<template>
  <OurHeader v-if="!route.meta.hideHeader"/>
  <RouterView />
</template>

<style>
* {
  box-sizing: border-box;
}

body {
  margin: 0;
  color: #202124;
  font-family: Arial, "Apple SD Gothic Neo", sans-serif;
}

button,
input {
  font: inherit;
}
</style>
