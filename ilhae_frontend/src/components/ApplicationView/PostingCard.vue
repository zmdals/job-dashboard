<script setup>
import { computed } from 'vue'

const props = defineProps({
  post: {
    type: Object,
    required: true,
  },
  score: {
    type: [Number, String],
    default: null,
  },

  starred: {
    type: Boolean,
    default: false,
  },
  applied: {
    type: Boolean,
    default: false,
  },
});

const emits = defineEmits([
  'emitRequestDetail',
  'emitRequestInfo',
  'emitRequestRelevance',
  'emitToggleStar',
  'emitApply',
])

const companyName = computed(() => props.post.companyName || props.post.company?.name || '회사명 미정')
const role = computed(() => [props.post.title, props.post.jobType].filter(Boolean).join(' · '))
const deadline = computed(() => {
  if (!props.post.deadline) return { date: '상시채용', dDay: '' }

  const date = new Date(`${String(props.post.deadline).slice(0, 10)}T00:00:00`)
  const today = new Date()
  today.setHours(0, 0, 0, 0)

  if (Number.isNaN(date.getTime())) {
    return { date: props.post.deadline, dDay: '' }
  }

  const days = Math.ceil((date - today) / 86400000)
  return {
    date: String(props.post.deadline).slice(0, 10),
    dDay: days >= 0 ? `D-${days}` : '마감',
  }
})

const emitRequestInfo= () => {
  emits('emitRequestInfo', props.post.id)
}

const emitRequestRelevance = () => {
  emits('emitRequestRelevance', props.post.id)
}
</script>

<template>
  <div class="postCard">
    <button class="job-company" type="button" @click="$emit('emitRequestDetail', post)">
      {{ companyName }}
      <small class="job-role">{{ role }}</small>
    </button>
    <button class="fit-rate" type="button" @click.stop="emitRequestRelevance">
      {{ score === null ? '—' : `${score}%` }}
      <small>AI 적합도</small>
    </button>
    <span class="job-deadline">
      {{ deadline.date }}
      <small v-if="deadline.dDay">{{ deadline.dDay }}</small>
    </span>
    <button class="company-info" type="button" @click.stop="emitRequestInfo">회사정보</button>
    <button class="favorite" :class="{ saved: starred }" type="button" @click="$emit('emitToggleStar', post.id)">
      {{ starred ? '♥ 저장됨' : '☆ 추가' }}
    </button>
    <button class="apply" :class="{ added: applied }" type="button" :disabled="applied" @click="$emit('emitApply', post.id)">
      {{ applied ? '지원완료' : '지원 추가' }}
    </button>
  </div>
</template>

<style scoped>
.postCard {
  display: grid;
  grid-template-columns: 1.5fr 115px 135px 1.1fr 88px 88px;
  gap: 14px;
  align-items: center;
  min-height: 88px;
  padding: 0 22px;
  border-bottom: 1px solid #e7e7e7;
}

button {
  font: inherit;
  cursor: pointer;
}

.job-company {
  padding: 0;
  border: 0;
  color: #202124;
  background: none;
  font-size: 13px;
  font-weight: 700;
  text-align: left;
}

.job-company:hover {
  color: #e31b23;
  text-decoration: underline;
}

.job-role,
.fit-rate small,
.job-deadline small {
  display: block;
  margin-top: 3px;
  color: #999;
  font-size: 10px;
  font-weight: 400;
  line-height: 1.55;
}

.fit-rate {
  padding: 0;
  border: 0;
  color: #e31b23;
  background: none;
  font-size: 16px;
  font-weight: 700;
  text-align: left;
}

.job-deadline {
  color: #777;
  font-size: 11px;
  line-height: 1.55;
}

.company-info,
.favorite,
.apply {
  padding: 8px 9px;
  border: 1px solid #ddd;
  border-radius: 5px;
  color: #777;
  background: #fff;
  font-size: 10px;
}

.company-info:hover {
  border-color: #e31b23;
  color: #e31b23;
}

.favorite.saved {
  border-color: #e31b23;
  color: #e31b23;
  background: #fff0f0;
}

.apply {
  border-color: #e31b23;
  color: #e31b23;
}

.apply.added {
  border-color: #ddd;
  color: #999;
  background: #f5f5f5;
  cursor: default;
}

@media (max-width: 760px) {
  .postCard {
    grid-template-columns: 1fr 84px 84px;
    gap: 8px;
    min-height: 104px;
    padding: 15px;
  }

  .fit-rate {
    grid-column: 2 / 4;
    grid-row: 1;
  }

  .job-deadline,
  .company-info {
    grid-column: 1;
  }

  .favorite {
    grid-column: 2;
    grid-row: 2;
  }

  .apply {
    grid-column: 3;
    grid-row: 2;
  }
}
</style>
