<template>
  <aside class="brand-panel">
    <div class="logo"><span class="logo-mark">취</span> 취업했음청년</div>

    <div class="intro">
      <small>YOUR CAREER, IN ONE PLACE</small>
      <h1>취업했음<span>청년</span></h1>
      <p>취준부터 첫 출근까지,<br />나의 취업 여정을 한눈에 관리해요.</p>
    </div>

    <div class="scene" aria-hidden="true">
      <div class="sun"></div>
      <div class="ground"></div>
      <div class="flag"></div>
      <div class="person">
        <div class="head"></div>
        <div class="hair"></div>
        <div class="body"></div>
        <div class="leg one"></div>
        <div class="leg two"></div>
      </div>
      <div class="note">이번 주 목표<strong>이력서 업데이트</strong></div>
    </div>

    <div class="caption">매일 한 걸음씩, 원하는 곳에 닿으니까.</div>
  </aside>
</template>

<style scoped>
.brand-panel {
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  min-width: 0;
  min-height: 100vh;
  padding: 38px 8%;
  overflow: hidden;
  background: #fff8f3;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 14px;
  font-weight: 700;
}

.logo-mark {
  display: grid;
  place-items: center;
  width: 30px;
  height: 30px;
  border-radius: 8px;
  color: #fff;
  background: #e31b23;
}

.intro {
  position: relative;
  z-index: 1;
  margin-top: 8vh;
}

.intro small {
  color: #e31b23;
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.12em;
}

h1 {
  margin: 16px 0 18px;
  font-size: clamp(44px, 5vw, 72px);
  line-height: 1;
  letter-spacing: -0.09em;
}

h1 span {
  color: #e31b23;
}

.intro p {
  margin: 0;
  color: #756c68;
  font-size: 15px;
  line-height: 1.7;
}

.scene {
  position: relative;
  height: 45vh;
  min-height: 270px;
  margin: 0 -5%;
}

.ground {
  position: absolute;
  right: -10%;
  bottom: -42%;
  width: 120%;
  height: 80%;
  border-radius: 50% 50% 0 0;
  background: #d9e4d2;
  transform: rotate(-5deg);
}

.sun {
  position: absolute;
  right: 21%;
  bottom: 48%;
  width: 74px;
  height: 74px;
  border-radius: 50%;
  background: #f6bd56;
}

.person {
  position: absolute;
  bottom: 13%;
  left: 48%;
  width: 92px;
  height: 185px;
}

.head {
  position: absolute;
  top: 0;
  left: 29px;
  width: 45px;
  height: 48px;
  border-radius: 50%;
  background: #e4a073;
}

.hair {
  position: absolute;
  top: -6px;
  left: 23px;
  width: 55px;
  height: 39px;
  border-radius: 50% 55% 25% 30%;
  background: #352c2c;
}

.body {
  position: absolute;
  top: 45px;
  left: 15px;
  width: 69px;
  height: 88px;
  border-radius: 28px 28px 10px 10px;
  background: #e31b23;
}

.leg {
  position: absolute;
  top: 124px;
  width: 18px;
  height: 66px;
  border-radius: 12px;
  background: #384653;
}

.leg.one { left: 29px; transform: rotate(7deg); }
.leg.two { left: 56px; transform: rotate(-7deg); }

.flag {
  position: absolute;
  right: 17%;
  bottom: 35%;
  width: 4px;
  height: 88px;
  background: #8a7066;
  transform: rotate(8deg);
}

.flag::before {
  position: absolute;
  top: 0;
  left: 3px;
  width: 48px;
  height: 29px;
  background: #e31b23;
  content: '';
  clip-path: polygon(0 0, 100% 0, 82% 50%, 100% 100%, 0 100%);
}

.note {
  position: absolute;
  top: 24%;
  right: 1%;
  padding: 12px 15px;
  border-radius: 9px;
  color: #514644;
  background: #fff;
  box-shadow: 0 8px 22px rgba(80, 50, 40, 0.09);
  font-size: 11px;
}

.note strong {
  display: block;
  margin-top: 4px;
  color: #e31b23;
  font-size: 14px;
}

.caption {
  color: #a18f86;
  font-size: 11px;
}

@media (max-width: 760px) {
  .brand-panel {
    min-height: 440px;
    padding: 25px 26px 22px;
  }

  .intro {
    margin-top: 54px;
  }

  .intro p {
    font-size: 13px;
  }

  .scene {
    height: 190px;
    min-height: 0;
  }

  .note {
    right: 0;
    transform: scale(0.85);
    transform-origin: right top;
  }
}
</style>
