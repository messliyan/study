<template>
  <div :style="'height:'+ height" v-loading="loading">
    <iframe :src="src" frameborder="no" scrolling="auto" style="width: 100%;height: 100%"/>
  </div>
</template>
<script>
  export default {
    name: "Druid",
    data() {
      return {
        src: process.env.VUE_APP_BASE_API + "/druid/index.html",
        height: document.documentElement.clientHeight - 94.5 + "px;",
        loading: true
      };
    },
    mounted: function () {
      setTimeout(() => {
        this.loading = false;
      }, 230);
      const that = this;
      window.onresize = function temp() {
        that.height = document.documentElement.clientHeight - 94.5 + "px;";
      };
    }
  };
</script>
