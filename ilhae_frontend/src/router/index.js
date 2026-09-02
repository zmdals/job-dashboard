import { createRouter, createWebHistory } from 'vue-router'
import SwaggerDocs from '@/views/SwaggerDocs.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/docs',
      component: SwaggerDocs,
    }
  ],
})

export default router
