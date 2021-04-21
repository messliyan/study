<template>
  <div class="component-upload-image">
    <el-upload
      :action="uploadImgUrl"
      :before-upload="handleBeforeUpload"
      :headers="headers"
      :on-error="handleUploadError"
      :on-success="handleUploadSuccess"
      :show-file-list="false"
      list-type="picture-card"
      name="file"
      style="display: inline-block; vertical-align: top"
    >
      <el-image :src="value" v-if="!value">
        <div class="image-slot" slot="error">
          <i class="el-icon-plus"/>
        </div>
      </el-image>
      <div class="image" v-else>
        <el-image :src="value" :style="`width:150px;height:150px;`" fit="fill"/>
        <div class="mask">
          <div class="actions">
            <span @click.stop="dialogVisible = true" title="预览">
              <i class="el-icon-zoom-in"/>
            </span>
            <span @click.stop="removeImage" title="移除">
              <i class="el-icon-delete"/>
            </span>
          </div>
        </div>
      </div>
    </el-upload>
    <el-dialog :visible.sync="dialogVisible" append-to-body title="预览" width="800">
      <img :src="value" style="display: block; max-width: 100%; margin: 0 auto;">
    </el-dialog>
  </div>
</template>

<script>
  import {getToken} from "@/utils/auth";

  export default {
    data() {
      return {
        dialogVisible: false,
        uploadImgUrl: process.env.VUE_APP_BASE_API + "/file/upload", // 上传的图片服务器地址
        headers: {
          Authorization: "Bearer " + getToken(),
        },
      };
    },
    props: {
      value: {
        type: String,
        default: "",
      },
    },
    methods: {
      removeImage() {
        this.$emit("input", "");
      },
      handleUploadSuccess(res) {
        this.$emit("input", res.data.url);
        this.loading.close();
      },
      handleBeforeUpload() {
        this.loading = this.$loading({
          lock: true,
          text: "上传中",
          background: "rgba(0, 0, 0, 0.7)",
        });
      },
      handleUploadError() {
        this.$message({
          type: "error",
          message: "上传失败",
        });
        this.loading.close();
      },
    },
    watch: {},
  };
</script>

<style lang="scss" scoped>
  .image {
    position: relative;

    .mask {
      opacity: 0;
      position: absolute;
      top: 0;
      width: 100%;
      background-color: rgba(0, 0, 0, 0.5);
      transition: all 0.3s;
    }

    &:hover .mask {
      opacity: 1;
    }
  }
</style>
