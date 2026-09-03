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

const emits = defineEmits(["emitRequestInfo", "emitRequestRelevance"])

const emitRequestInfo= () => {
  emits('emitRequestInfo', props.post.id)
}

const emitRequestRelevance = () => {
  emits('emitRequestRelevance', props.post.id)
}

</script>

<template>
  <div class="postCard">
    <p>{{ post.title }}</p>

    <p>
      {{ props.starred ? "★" : "☆" }}
    </p>

    <p @click.stop="emitRequestRelevance">75%</p>

    <p @click.stop="emitRequestInfo">{{ post.id }}</p>

  </div>
</template>

<style scoped>
.postCard {
  display: flex;
  flex-direction: row;
}
.postCard * {
  padding: 20px;
}
</style>