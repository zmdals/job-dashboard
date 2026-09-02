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
</script>

<template>
  <div class="postCard">
    <p>{{ post.title }}</p>

    <p>
      {{ props.starred ? "★" : "☆" }}
    </p>
  </div>
</template>
