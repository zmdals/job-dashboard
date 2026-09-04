<script setup>
import OurBox from '@/components/OurBox.vue'

defineProps({
  title: {
    type: String,
    required: true,
  },
  subtitle: {
    type: String,
    default: '',
  },
  wide: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(['close'])

function closeOnBackdrop(event) {
  if (event.target === event.currentTarget) {
    emit('close')
  }
}
</script>

<template>
  <Teleport to="body">
    <div class="modal-backdrop" role="presentation" @click="closeOnBackdrop">
      <OurBox
        class="modal-box"
        :class="{ wide }"
        padding-class=""
        role="dialog"
        aria-modal="true"
        :aria-label="title"
      >
        <header class="modal-head">
          <div>
            <h3>{{ title }}</h3>
            <p v-if="subtitle">{{ subtitle }}</p>
          </div>
          <button class="close" type="button" aria-label="닫기" @click="emit('close')">×</button>
        </header>
        <slot />
        <footer v-if="$slots.actions" class="modal-actions">
          <slot name="actions" />
        </footer>
      </OurBox>
    </div>
  </Teleport>
</template>

<style scoped>
.modal-backdrop {
  position: fixed;
  z-index: 1000;
  inset: 0;
  display: grid;
  place-items: center;
  padding: 24px;
  background: rgba(20, 20, 20, 0.35);
}

.modal-box {
  width: min(100%, 560px);
  max-height: 90vh;
  padding: 30px;
  overflow: auto;
  border-radius: 10px;
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.15);
}

.modal-box.wide {
  width: min(100%, 960px);
}

.modal-head {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  margin-bottom: 25px;
}

h3 {
  margin: 0 0 7px;
  font-size: 22px;
  letter-spacing: -0.06em;
}

p {
  margin: 0;
  color: #888;
  font-size: 12px;
}

.close {
  border: 0;
  color: #999;
  background: none;
  font: inherit;
  font-size: 20px;
  cursor: pointer;
}

.modal-actions {
  display: flex;
  gap: 8px;
  margin-top: 20px;
}

.modal-actions :deep(button),
.modal-actions :deep(a) {
  flex: 1;
  display: grid;
  place-items: center;
  height: 42px;
  border: 1px solid #e7e7e7;
  border-radius: 5px;
  color: #202124;
  background: #fff;
  font: inherit;
  font-size: 12px;
  text-decoration: none;
  cursor: pointer;
}

.modal-actions :deep(.primary) {
  border-color: #e31b23;
  color: #fff;
  background: #e31b23;
}

.modal-actions :deep(button:disabled) {
  border-color: #ddd;
  color: #aaa;
  background: #f3f3f3;
  cursor: not-allowed;
}

@media (max-width: 760px) {
  .modal-box {
    padding: 22px;
  }
}
</style>
