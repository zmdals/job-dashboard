<script setup>
import { RouterView, useRoute } from 'vue-router'
import OurHeader from './components/OurHeader.vue'

const route = useRoute()
</script>

<template>
  <OurHeader v-if="!route.meta.hideHeader" />
  <RouterView />
</template>

<style>
:root {
  --page-content-width: 1060px;
  --page-gutter: 24px;
  --page-top-space: 56px;
  --page-bottom-space: 80px;
}

* {
  box-sizing: border-box;
}

body {
  margin: 0;
  color: #202124;
  font-family: Arial, 'Apple SD Gothic Neo', sans-serif;
}

button,
input {
  font: inherit;
}

@media (max-width: 760px) {
  :root {
    --page-top-space: 36px;
  }
}

@media (max-width: 480px) {
  :root {
    --page-gutter: 16px;
    --page-top-space: 32px;
  }
}
</style>
