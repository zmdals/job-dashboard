import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import { enableMocking } from './mocks/enableMocking'

async function bootstrap() {
  await enableMocking()

  createApp(App)
    .use(createPinia())
    .mount('#app')
}

bootstrap()
