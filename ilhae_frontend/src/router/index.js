import { createRouter, createWebHistory } from "vue-router";
import SwaggerDocs from "@/views/SwaggerDocs.vue";
import LoginView from "@/views/LoginView.vue";
import { useAuthStore } from "@/stores/authStore";

// 서버 로그인 구현 전까지 비활성화. 준비되면 VITE_AUTH_GUARD_ENABLED=true로 복구한다.
const AUTH_GUARD_ENABLED = import.meta.env.VITE_AUTH_GUARD_ENABLED === "true";

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
      meta: { requiresAuth: true },
    },
    {
      path: "/signup",
      name: "signup",
      redirect: { path: "/", query: { mode: "signup" } },
    },
    {
      path: "/postings",
      name: "postings",
      component: () => import("@/views/PostingsView.vue"),
      meta: { requiresAuth: true },
    },
    {
      path: "/postings/:companyId",
      name: "posting-report",
      component: () => import("@/views/DetailView.vue"),
      meta: { requiresAuth: true },
    },
    {
      path: "/profile",
      name: "profile",
      component: () => import("@/views/ProfileView.vue"),
      meta: { requiresAuth: true },
    },
    {
      path: "/docs",
      component: SwaggerDocs,
    },

  ],
});

router.beforeEach((to) => {
  if (!AUTH_GUARD_ENABLED) return true;

  const authStore = useAuthStore();

  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    return {
      name: "login",
      query: { redirect: to.fullPath },
    };
  }

  if (to.name === "login" && authStore.isAuthenticated) {
    const redirect = to.query.redirect;

    if (
      typeof redirect === "string" &&
      redirect.startsWith("/") &&
      !redirect.startsWith("//")
    ) {
      return redirect;
    }

    return { name: "home" };
  }
});

export default router;
