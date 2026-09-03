<script setup>
import { onMounted } from 'vue'
import SwaggerUI from 'swagger-ui-dist/swagger-ui-es-bundle'
import 'swagger-ui-dist/swagger-ui.css'

onMounted(() => {
  SwaggerUI({
    dom_id: '#swagger-ui',
    url: '/openapi/openapi.yaml',
  })
})
</script>

<template>
  <div id="swagger-ui"></div>
</template>