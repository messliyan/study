<template>
  <div>
    <svg-icon @click="goto" icon-class="question"/>
  </div>
</template>

<script>
  export default {
    name: 'RuoYiDoc',
    data() {
      return {
        url: 'http://doc.ruoyi.vip/ruoyi-cloud'
      }
    },
    methods: {
      goto() {
        window.open(this.url)
      }
    }
  }
</script>
