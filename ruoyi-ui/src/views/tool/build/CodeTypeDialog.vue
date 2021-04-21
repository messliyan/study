<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :modal-append-to-body="false"
      @close="onClose"
      @open="onOpen"
      v-bind="$attrs"
      v-on="$listeners"
      width="500px"
    >
      <el-row :gutter="15">
        <el-form
          :model="formData"
          :rules="rules"
          label-width="100px"
          ref="elForm"
          size="medium"
        >
          <el-col :span="24">
            <el-form-item label="生成类型" prop="type">
              <el-radio-group v-model="formData.type">
                <el-radio-button
                  :disabled="item.disabled"
                  :key="index"
                  :label="item.value"
                  v-for="(item, index) in typeOptions"
                >
                  {{ item.label }}
                </el-radio-button>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="文件名" prop="fileName" v-if="showFileName">
              <el-input clearable placeholder="请输入文件名" v-model="formData.fileName"/>
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>

      <div slot="footer">
        <el-button @click="close">
          取消
        </el-button>
        <el-button @click="handelConfirm" type="primary">
          确定
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  export default {
    inheritAttrs: false,
    props: ['showFileName'],
    data() {
      return {
        formData: {
          fileName: undefined,
          type: 'file'
        },
        rules: {
          fileName: [{
            required: true,
            message: '请输入文件名',
            trigger: 'blur'
          }],
          type: [{
            required: true,
            message: '生成类型不能为空',
            trigger: 'change'
          }]
        },
        typeOptions: [{
          label: '页面',
          value: 'file'
        }, {
          label: '弹窗',
          value: 'dialog'
        }]
      }
    },
    computed: {},
    watch: {},
    mounted() {
    },
    methods: {
      onOpen() {
        if (this.showFileName) {
          this.formData.fileName = `${+new Date()}.vue`
        }
      },
      onClose() {
      },
      close(e) {
        this.$emit('update:visible', false)
      },
      handelConfirm() {
        this.$refs.elForm.validate(valid => {
          if (!valid) {
            return
          }
          this.$emit('confirm', {...this.formData})
          this.close()
        })
      }
    }
  }
</script>
