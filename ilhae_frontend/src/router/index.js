import { createRouter, createWebHistory } from "vue-router";
import SwaggerDocs from "@/views/SwaggerDocs.vue";
import LoginView from "@/views/LoginView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "login",
      component: LoginView,
      meta: {hideHeader: true}
    },
    {
      path: "/home",
      name: "home",
      component: () => import("@/views/HomeView.vue"),
    },
    {
      path: "/signup",
      name: "signup",
      component: () => import("@/views/SignupView.vue"),
      meta: {hideHeader: true}
    },
    {
      path: "/postings",
      name: "postings",
      component: () => import("@/views/PostingsView.vue"),
    },
    {
      path: "/profile",
      name: "profile",
      component: () => import("@/views/ProfileView.vue"),
    },
    {
      path: "/docs",
      component: SwaggerDocs,
    },

  ],
});

export default router;
