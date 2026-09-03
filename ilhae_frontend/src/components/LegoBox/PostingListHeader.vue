<template>
  <div class="posting-list-header" aria-hidden="true">
    <span>회사 / 포지션</span>
    <span>예상 합격률</span>
    <span>마감기한</span>
    <span>회사정보</span>
    <span>선호공고</span>
    <span>지원</span>
  </div>
</template>

<style scoped>
.posting-list-header {
  display: grid;
  grid-template-columns: 1.5fr 115px 135px 1.1fr 88px 88px;
  gap: 14px;
  padding: 11px 22px;
  color: #999;
  background: #fafafa;
  font-size: 10px;
}

@media (max-width: 760px) {
  .posting-list-header {
    display: none;
  }
}
</style>
