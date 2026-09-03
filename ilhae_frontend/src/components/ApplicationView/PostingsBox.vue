<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { storeToRefs } from 'pinia'
import PostingCard from './PostingCard.vue'
import RequirementsCard from './RequirementsCard.vue'
import RelevanceCard from './RelevanceCard.vue'
import PostingDetailBox from '@/components/LegoBox/PostingDetailBox.vue'
import PostingListHeader from '@/components/LegoBox/PostingListHeader.vue'
import PostingPagination from '@/components/LegoBox/PostingPagination.vue'
import PostingTabs from '@/components/LegoBox/PostingTabs.vue'
import { useMeStore } from '@/stores/meStore'
import { usePostingsStore } from '@/stores/postingsStore'

const PAGE_SIZE = 3

const postingsStore = usePostingsStore()
const meStore = useMeStore()
const {
  postings,
  pagination,
  loading: postingsLoading,
  error: postingsError,
} = storeToRefs(postingsStore)
const { starredPostings } = storeToRefs(meStore)

const activeTab = ref('all')
const currentPage = ref(1)
const selectedPosting = ref(null)
const infoPosting = ref(null)
const relevancePosting = ref(null)

const filteredPostings = computed(() => {
  if (activeTab.value === 'starred') {
    return starredPostings.value
  }

  return postings.value
})

const visiblePostings = computed(() => {
  if (activeTab.value === 'all') {
    return postings.value
  }

  const start = (currentPage.value - 1) * PAGE_SIZE
  return filteredPostings.value.slice(start, start + PAGE_SIZE)
})

const paginationTotal = computed(() =>
  activeTab.value === 'all' ? pagination.value.totalElements : starredPostings.value.length,
)

watch(activeTab, () => {
  currentPage.value = 1
})

watch(starredPostings, (items) => {
  if (activeTab.value !== 'starred') return

  const lastPage = Math.max(1, Math.ceil(items.length / PAGE_SIZE))
  currentPage.value = Math.min(currentPage.value, lastPage)
})

watch(
  [activeTab, currentPage],
  async ([tab, page]) => {
    if (tab === 'all') {
      try {
        await postingsStore.ensurePostings({
          page: page - 1,
          size: PAGE_SIZE,
        })
      } catch {
        return
      }
    }
  },
  { immediate: true },
)

onMounted(() => Promise.allSettled([meStore.ensureApplications(), meStore.ensureStarredPostings()]))

async function apply(postingId) {
  if (!meStore.isApplied(postingId)) {
    try {
      await meStore.apply(postingId)
    } catch {
      return
    }
  }
}

async function toggleStar(postingId) {
  try {
    await meStore.toggleStar(postingId)
  } catch {
    return
  }
}

async function applySelected() {
  if (!selectedPosting.value) return
  await apply(selectedPosting.value.id)
}
</script>

<template>
  <main class="postings-page">
    <section class="postings-content">
      <header class="page-title">
        <h2>채용공고</h2>
        <p>내게 맞는 기회를 찾아보세요.</p>
      </header>

      <PostingTabs v-model="activeTab" />
      <PostingListHeader />

      <div class="posting-list">
        <p v-if="postingsLoading && !postings.length" class="list-state">
          채용공고를 불러오는 중입니다.
        </p>
        <p v-else-if="postingsError && !postings.length" class="list-state error">
          채용공고를 불러오지 못했습니다.
        </p>
        <p v-else-if="!filteredPostings.length" class="list-state">
          {{ activeTab === 'starred' ? '저장한 공고가 없습니다.' : '등록된 채용공고가 없습니다.' }}
        </p>
        <template v-else>
          <PostingCard
            v-for="posting in visiblePostings"
            :key="posting.id"
            :post="posting"
            :score="posting.relevanceScore"
            :starred="meStore.isStarred(posting.id)"
            :applied="meStore.isApplied(posting.id)"
            @emit-request-detail="selectedPosting = posting"
            @emit-request-info="infoPosting = posting"
            @emit-request-relevance="relevancePosting = posting"
            @emit-toggle-star="toggleStar"
            @emit-apply="apply"
          />
        </template>
      </div>

      <PostingPagination
        v-model="currentPage"
        :total-items="paginationTotal"
        :page-size="PAGE_SIZE"
      />
    </section>

    <PostingDetailBox
      v-if="selectedPosting"
      :posting="selectedPosting"
      :applied="meStore.isApplied(selectedPosting.id)"
      @close="selectedPosting = null"
      @apply="applySelected"
    />
    <RequirementsCard
      v-if="infoPosting"
      :company-id="infoPosting.companyId ?? infoPosting.company?.id"
      @emit-close-card="infoPosting = null"
    />
    <RelevanceCard
      v-if="relevancePosting"
      :posting="relevancePosting"
      @emit-close-card="relevancePosting = null"
    />
  </main>
</template>

<style scoped>
.postings-page {
  min-height: calc(100vh - 78px);
  background: #fafafa;
}

.postings-content {
  width: min(100% - 48px, 1060px);
  margin: 0 auto;
  padding: 48px 0 80px;
}

.page-title {
  margin-bottom: 30px;
}

.page-title h2 {
  margin: 0 0 8px;
  font-size: 30px;
  letter-spacing: -0.07em;
}

.page-title p {
  margin: 0;
  color: #858585;
  font-size: 13px;
}

.posting-list {
  border-top: 1px solid #333;
  background: #fff;
}

.list-state {
  margin: 0;
  padding: 45px;
  color: #999;
  text-align: center;
}

.list-state.error {
  color: #e31b23;
}

@media (max-width: 760px) {
  .postings-page {
    min-height: 100vh;
  }

  .postings-content {
    width: calc(100% - 32px);
    padding-top: 36px;
  }

  .page-title h2 {
    font-size: 26px;
  }
}
</style>
