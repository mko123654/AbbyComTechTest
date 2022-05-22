<template>
  <div class="banner-wrapper">
    <div class="banner-title-wrapper">
      <div class="title-line-wrapper" style="opacity: 1; transform: translate(0px, 0px);">
        <div class="title-line" style="transform: translateX(-34px);"></div>
      </div>
      <h1 class="" style="opacity: 1; transform: translate(0px, 0px);">Abby.com 線上考試系統</h1>
      <p style="opacity: 1; transform: translate(0px, 0px);">
        <span>歡迎來到面試技術考題系統，祝各位考生考試順利!!</span>
      </p>
    </div>
    <div class="banner-image-wrapper" style="opacity: 1;">
      <a-carousel arrows autoplay>
        <div v-for="i in 2" :key="i">
          <img :src="`/home/cover${i}.png`" style="height: 100%"/>
        </div>
      </a-carousel>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Banner'
}
</script>

<style lang="less">
  @import "home";
</style>
