<script setup>
</script>

<template>
  <aside class="brand-panel">
    <div class="logo">커리어보드</div>

    <div class="content">
      <h1 class="headline">
        지원한 곳,<br />
        합격 가능성,<br />
        회사의 실체까지<br />
        한 화면에서.
      </h1>
      <p class="subtext">
        내 정보와 공고 자격요건을 비교해 예상 합격률을 계산하고,<br />
        회사의 논문·특허·보고서를 함께 확인합니다.
      </p>
    </div>

    <div class="stats">
      <div class="stat">
        <span>공고</span>
        <strong>12,480</strong>
      </div>
      <div class="stat">
        <span>기업 데이터</span>
        <strong>3,102</strong>
      </div>
      <div class="stat">
        <span>특허·논문</span>
        <strong>89만건</strong>
      </div>
    </div>
  </aside>
</template>

<style scoped>
.brand-panel {
  width: 50%;
  min-height: 100vh;
  box-sizing: border-box;
  background: #1c4532;
  color: #fff;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 48px 56px;
}

.logo {
  font-size: 15px;
  font-weight: 700;
  letter-spacing: -0.01em;
}

.headline {
  margin: 0;
  font-size: 40px;
  line-height: 1.35;
  font-weight: 700;
  letter-spacing: -0.01em;
}

.subtext {
  margin: 24px 0 0;
  font-size: 15px;
  line-height: 1.7;
  color: rgba(255, 255, 255, 0.72);
}

.stats {
  display: flex;
  gap: 32px;
}

.stat span {
  display: block;
  font-size: 13px;
  color: rgba(255, 255, 255, 0.6);
  margin-bottom: 4px;
}

.stat strong {
  font-size: 18px;
  font-weight: 700;
}

@media (max-width: 768px) {
  .brand-panel {
    width: 100%;
    min-height: auto;
    padding: 32px 24px;
  }
}
</style>