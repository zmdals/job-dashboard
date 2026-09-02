import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import { enableMocking } from './mocks/enableMocking'

async function bootstrap() {
  try {
    await enableMocking()
  } catch (error) {
    console.error('MSW 초기화 실패:', error)
  }

  const app = createApp(App)

  app.use(createPinia())
  app.use(router)

  app.mount('#app')
}

bootstrap()