<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :modal-append-to-body="false"
      @close="onClose"
      @open="onOpen"
      v-bind="$attrs"
      v-on="$listeners"
    >
      <el-row :gutter="0">
        <el-form
          :model="formData"
          :rules="rules"
          label-width="100px"
          ref="elForm"
          size="small"
        >
          <el-col :span="24">
            <el-form-item
              label="选项名"
              prop="label"
            >
              <el-input
                clearable
                placeholder="请输入选项名"
                v-model="formData.label"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item
              label="选项值"
              prop="value"
            >
              <el-input
                clearable
                placeholder="请输入选项值"
                v-model="formData.value"
              >
                <el-select
                  :style="{width: '100px'}"
                  slot="append"
                  v-model="dataType"
                >
                  <el-option
                    :disabled="item.disabled"
                    :key="index"
                    :label="item.label"
                    :value="item.value"
                    v-for="(item, index) in dataTypeOptions"
                  />
                </el-select>
              </el-input>
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>
      <div slot="footer">
        <el-button
          @click="handelConfirm"
          type="primary"
        >
          确定
        </el-button>
        <el-button @click="close">
          取消
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import {isNumberStr} from '@/utils/index'

  export default {
    components: {},
    inheritAttrs: false,
    props: [],
    data() {
      return {
        id: 100,
        formData: {
          label: undefined,
          value: undefined
        },
        rules: {
          label: [
            {
              required: true,
              message: '请输入选项名',
              trigger: 'blur'
            }
          ],
          value: [
            {
              required: true,
              message: '请输入选项值',
              trigger: 'blur'
            }
          ]
        },
        dataType: 'string',
        dataTypeOptions: [
          {
            label: '字符串',
            value: 'string'
          },
          {
            label: '数字',
            value: 'number'
          }
        ]
      }
    },
    computed: {},
    watch: {
      // eslint-disable-next-line func-names
      'formData.value': function (val) {
        this.dataType = isNumberStr(val) ? 'number' : 'string'
      }
    },
    created() {
    },
    mounted() {
    },
    methods: {
      onOpen() {
        this.formData = {
          label: undefined,
          value: undefined
        }
      },
      onClose() {
      },
      close() {
        this.$emit('update:visible', false)
      },
      handelConfirm() {
        this.$refs.elForm.validate(valid => {
          if (!valid) {
            return
          }
          if (this.dataType === 'number') {
            this.formData.value = parseFloat(this.formData.value)
          }
          this.formData.id = this.id++
          this.$emit('commit', this.formData)
          this.close()
        })
      }
    }
  }
</script>
