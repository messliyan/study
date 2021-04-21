<template>
  <div :class="{'collapse':collapse}" :style="{ backgroundColor: sideTheme === 'theme-dark' ? variables.menuBg : variables.menuLightBg }"
       class="sidebar-logo-container">
    <transition name="sidebarLogoFade">
      <router-link class="sidebar-logo-link" key="collapse" to="/" v-if="collapse">
        <img :src="logo" class="sidebar-logo" v-if="logo">
        <h1 :style="{ color: sideTheme === 'theme-dark' ? variables.sidebarTitle : variables.sidebarLightTitle }" class="sidebar-title"
            v-else>
          {{ title }} </h1>
      </router-link>
      <router-link class="sidebar-logo-link" key="expand" to="/" v-else>
        <img :src="logo" class="sidebar-logo" v-if="logo">
        <h1 :style="{ color: sideTheme === 'theme-dark' ? variables.sidebarTitle : variables.sidebarLightTitle }"
            class="sidebar-title">
          {{ title }} </h1>
      </router-link>
    </transition>
  </div>
</template>

<script>
  import logoImg from '@/assets/logo/logo.png'
  import variables from '@/assets/styles/variables.scss'

  export default {
    name: 'SidebarLogo',
    props: {
      collapse: {
        type: Boolean,
        required: true
      }
    },
    computed: {
      variables() {
        return variables;
      },
      sideTheme() {
        return this.$store.state.settings.sideTheme
      }
    },
    data() {
      return {
        title: '若依管理系统',
        logo: logoImg
      }
    }
  }
</script>

<style lang="scss" scoped>
  .sidebarLogoFade-enter-active {
    transition: opacity 1.5s;
  }

  .sidebarLogoFade-enter,
  .sidebarLogoFade-leave-to {
    opacity: 0;
  }

  .sidebar-logo-container {
    position: relative;
    width: 100%;
    height: 50px;
    line-height: 50px;
    background: #2b2f3a;
    text-align: center;
    overflow: hidden;

    & .sidebar-logo-link {
      height: 100%;
      width: 100%;

      & .sidebar-logo {
        width: 32px;
        height: 32px;
        vertical-align: middle;
        margin-right: 12px;
      }

      & .sidebar-title {
        display: inline-block;
        margin: 0;
        color: #fff;
        font-weight: 600;
        line-height: 50px;
        font-size: 14px;
        font-family: Avenir, Helvetica Neue, Arial, Helvetica, sans-serif;
        vertical-align: middle;
      }
    }

    &.collapse {
      .sidebar-logo {
        margin-right: 0px;
      }
    }
  }
</style>
