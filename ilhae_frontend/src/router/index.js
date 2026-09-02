import { createRouter, createWebHistory } from 'vue-router'
import SwaggerDocs from '@/views/SwaggerDocs.vue'
import LoginView from '@/views/LoginView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/docs',
      component: SwaggerDocs,
    },
  ],
})

export default router
