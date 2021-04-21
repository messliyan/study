<template>
  <div>
    <svg-icon @click="goto" icon-class="github"/>
  </div>
</template>

<script>
  export default {
    name: 'RuoYiGit',
    data() {
      return {
        url: 'https://gitee.com/y_project/RuoYi-Cloud'
      }
    },
    methods: {
      goto() {
        window.open(this.url)
      }
    }
  }
</script>
