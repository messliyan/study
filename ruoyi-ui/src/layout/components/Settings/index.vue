<template>
  <div class="drawer-container">
    <div>
      <div class="setting-drawer-content">
        <div class="setting-drawer-title">
          <h3 class="drawer-title">主题风格设置</h3>
        </div>
        <div class="setting-drawer-block-checbox">
          <div @click="handleTheme('theme-dark')" class="setting-drawer-block-checbox-item">
            <img alt="dark" src="@/assets/images/dark.svg">
            <div class="setting-drawer-block-checbox-selectIcon" style="display: block;"
                 v-if="sideTheme === 'theme-dark'">
              <i aria-label="图标: check" class="anticon anticon-check">
                <svg :fill="theme" aria-hidden="true" class="" data-icon="check"
                     focusable="false" height="1em"
                     viewBox="64 64 896 896" width="1em">
                  <path
                    d="M912 190h-69.9c-9.8 0-19.1 4.5-25.1 12.2L404.7 724.5 207 474a32 32 0 0 0-25.1-12.2H112c-6.7 0-10.4 7.7-6.3 12.9l273.9 347c12.8 16.2 37.4 16.2 50.3 0l488.4-618.9c4.1-5.1.4-12.8-6.3-12.8z"/>
                </svg>
              </i>
            </div>
          </div>
          <div @click="handleTheme('theme-light')" class="setting-drawer-block-checbox-item">
            <img alt="light" src="@/assets/images/light.svg">
            <div class="setting-drawer-block-checbox-selectIcon" style="display: block;"
                 v-if="sideTheme === 'theme-light'">
              <i aria-label="图标: check" class="anticon anticon-check">
                <svg :fill="theme" aria-hidden="true" class="" data-icon="check"
                     focusable="false" height="1em"
                     viewBox="64 64 896 896" width="1em">
                  <path
                    d="M912 190h-69.9c-9.8 0-19.1 4.5-25.1 12.2L404.7 724.5 207 474a32 32 0 0 0-25.1-12.2H112c-6.7 0-10.4 7.7-6.3 12.9l273.9 347c12.8 16.2 37.4 16.2 50.3 0l488.4-618.9c4.1-5.1.4-12.8-6.3-12.8z"/>
                </svg>
              </i>
            </div>
          </div>
        </div>

        <div class="drawer-item">
          <span>主题颜色</span>
          <theme-picker @change="themeChange"
                        style="float: right;height: 26px;margin: -3px 8px 0 0;"/>
        </div>
      </div>

      <el-divider/>

      <h3 class="drawer-title">系统布局配置</h3>

      <div class="drawer-item">
        <span>开启 Tags-Views</span>
        <el-switch class="drawer-switch" v-model="tagsView"/>
      </div>

      <div class="drawer-item">
        <span>固定 Header</span>
        <el-switch class="drawer-switch" v-model="fixedHeader"/>
      </div>

      <div class="drawer-item">
        <span>显示 Logo</span>
        <el-switch class="drawer-switch" v-model="sidebarLogo"/>
      </div>

    </div>
  </div>
</template>

<script>
  import ThemePicker from '@/components/ThemePicker'

  export default {
    components: {ThemePicker},
    data() {
      return {}
    },
    computed: {
      theme() {
        return this.$store.state.settings.theme
      },
      sideTheme() {
        return this.$store.state.settings.sideTheme
      },
      fixedHeader: {
        get() {
          return this.$store.state.settings.fixedHeader
        },
        set(val) {
          this.$store.dispatch('settings/changeSetting', {
            key: 'fixedHeader',
            value: val
          })
        }
      },
      tagsView: {
        get() {
          return this.$store.state.settings.tagsView
        },
        set(val) {
          this.$store.dispatch('settings/changeSetting', {
            key: 'tagsView',
            value: val
          })
        }
      },
      sidebarLogo: {
        get() {
          return this.$store.state.settings.sidebarLogo
        },
        set(val) {
          this.$store.dispatch('settings/changeSetting', {
            key: 'sidebarLogo',
            value: val
          })
        }
      },
    },
    methods: {
      themeChange(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'theme',
          value: val
        })
      },
      handleTheme(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'sideTheme',
          value: val
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  .setting-drawer-content {
    .setting-drawer-title {
      margin-bottom: 12px;
      color: rgba(0, 0, 0, .85);
      font-size: 14px;
      line-height: 22px;
      font-weight: bold;
    }

    .setting-drawer-block-checbox {
      display: flex;
      justify-content: flex-start;
      align-items: center;
      margin-top: 10px;
      margin-bottom: 20px;

      .setting-drawer-block-checbox-item {
        position: relative;
        margin-right: 16px;
        border-radius: 2px;
        cursor: pointer;

        img {
          width: 48px;
          height: 48px;
        }

        .setting-drawer-block-checbox-selectIcon {
          position: absolute;
          top: 0;
          right: 0;
          width: 100%;
          height: 100%;
          padding-top: 15px;
          padding-left: 24px;
          color: #1890ff;
          font-weight: 700;
          font-size: 14px;
        }
      }
    }
  }

  .drawer-container {
    padding: 24px;
    font-size: 14px;
    line-height: 1.5;
    word-wrap: break-word;

    .drawer-title {
      margin-bottom: 12px;
      color: rgba(0, 0, 0, .85);
      font-size: 14px;
      line-height: 22px;
    }

    .drawer-item {
      color: rgba(0, 0, 0, .65);
      font-size: 14px;
      padding: 12px 0;
    }

    .drawer-switch {
      float: right
    }
  }
</style>
