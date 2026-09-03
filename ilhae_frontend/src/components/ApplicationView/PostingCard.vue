<script setup>
const props = defineProps({
  post: {
    type: Object,
    required: true,
  },

  starred: {
    type: Boolean,
    default: false,
  },
});

const emits = defineEmits(["emitRequestInfo"])

const emitRequestInfo= () => {
  emits('emitRequestInfo', props.post.id)
}

</script>

<template>
  <div class="postCard">
    <p>{{ post.title }}</p>

    <p>
      {{ props.starred ? "★" : "☆" }}
    </p>

    <div @click.stop="emitRequestInfo">{{ post.id }}</div>

  </div>
</template>
