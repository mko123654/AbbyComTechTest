<template>
  <div class="home-page page1">
    <div class="home-page-wrapper" id="page1-wrapper">
      <div class="page1-bg" style="transform: translate(0px, 200.953px);">Feature</div>
      <h1 style="font-size: 60px !important">【強力徵才以下職位】</h1>
      <div class="title-line-wrapper page1-line"></div>
        <list :data-source="features" />
    </div>
  </div>
</template>

<script>
import List from './List'
const featuresCN = [
  {
    title: '前端工程師',
    content: '需求：Vue/vuex/bootstrap...等前端技術',
    src: '/home/job_frontend.PNG'
  },
  {
    title: '後端工程師',
    content: '需求：JAVA/Spring框架...等後端技術',
    src: '/home/job_backend.PNG'
  },
  {
    title: 'DBA工程師',
    content: '需求：MySQL/Oracle...等資料庫技術',
    src: '/home/job_database.PNG'
  },
  {
    title: 'DevOps雲端工程師',
    content: '需求：AWS/GCP/Azure...等CI/CD技術',
    src: '/home/job_devops.PNG'
  },
  {
    title: 'APP工程師',
    content: '需求：IOS/Android...等APP技術',
    src: '/home/job_app.PNG'
  },
  {
    title: 'QA工程師',
    content: '需求：黑箱/白箱...等測試技術',
    src: '/home/job_qa.PNG'
  }
]

export default {
  name: 'Page1',
  components: {
    List
  },
  data () {
    return {
      features: featuresCN
    }
  },
  created () {
    this.updateFeatures()
  },
  methods: {
    updateFeatures () {
      const arr = featuresCN
      const newArr = [[], [], []]
      arr.forEach((item, index) => {
        newArr[Math.floor(index / 3)].push(item)
      })
      this.features = newArr
    }
  }
}
</script>
