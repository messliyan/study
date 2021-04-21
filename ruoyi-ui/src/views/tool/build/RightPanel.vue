<template>
  <div class="right-board">
    <el-tabs class="center-tabs" v-model="currentTab">
      <el-tab-pane label="组件属性" name="field"/>
      <el-tab-pane label="表单属性" name="form"/>
    </el-tabs>
    <div class="field-box">
      <a :href="documentLink" class="document-link" target="_blank" title="查看组件文档">
        <i class="el-icon-link"/>
      </a>
      <el-scrollbar class="right-scrollbar">
        <!-- 组件属性 -->
        <el-form label-width="90px" size="small" v-show="currentTab==='field' && showField">
          <el-form-item label="组件类型" v-if="activeData.changeTag">
            <el-select
              :style="{width: '100%'}"
              @change="tagChange"
              placeholder="请选择组件类型"
              v-model="activeData.tagIcon"
            >
              <el-option-group :key="group.label" :label="group.label" v-for="group in tagList">
                <el-option
                  :key="item.label"
                  :label="item.label"
                  :value="item.tagIcon"
                  v-for="item in group.options"
                >
                  <svg-icon :icon-class="item.tagIcon" class="node-icon"/>
                  <span> {{ item.label }}</span>
                </el-option>
              </el-option-group>
            </el-select>
          </el-form-item>
          <el-form-item label="字段名" v-if="activeData.vModel!==undefined">
            <el-input placeholder="请输入字段名（v-model）" v-model="activeData.vModel"/>
          </el-form-item>
          <el-form-item label="组件名" v-if="activeData.componentName!==undefined">
            {{ activeData.componentName }}
          </el-form-item>
          <el-form-item label="标题" v-if="activeData.label!==undefined">
            <el-input placeholder="请输入标题" v-model="activeData.label"/>
          </el-form-item>
          <el-form-item label="占位提示" v-if="activeData.placeholder!==undefined">
            <el-input placeholder="请输入占位提示" v-model="activeData.placeholder"/>
          </el-form-item>
          <el-form-item label="开始占位" v-if="activeData['start-placeholder']!==undefined">
            <el-input placeholder="请输入占位提示" v-model="activeData['start-placeholder']"/>
          </el-form-item>
          <el-form-item label="结束占位" v-if="activeData['end-placeholder']!==undefined">
            <el-input placeholder="请输入占位提示" v-model="activeData['end-placeholder']"/>
          </el-form-item>
          <el-form-item label="表单栅格" v-if="activeData.span!==undefined">
            <el-slider :marks="{12:''}" :max="24" :min="1" @change="spanChange"
                       v-model="activeData.span"/>
          </el-form-item>
          <el-form-item label="栅格间隔" v-if="activeData.layout==='rowFormItem'">
            <el-input-number :min="0" placeholder="栅格间隔" v-model="activeData.gutter"/>
          </el-form-item>
          <el-form-item label="布局模式" v-if="activeData.layout==='rowFormItem'">
            <el-radio-group v-model="activeData.type">
              <el-radio-button label="default"/>
              <el-radio-button label="flex"/>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="水平排列"
                        v-if="activeData.justify!==undefined&&activeData.type==='flex'">
            <el-select :style="{width: '100%'}" placeholder="请选择水平排列" v-model="activeData.justify">
              <el-option
                :key="index"
                :label="item.label"
                :value="item.value"
                v-for="(item, index) in justifyOptions"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="垂直排列" v-if="activeData.align!==undefined&&activeData.type==='flex'">
            <el-radio-group v-model="activeData.align">
              <el-radio-button label="top"/>
              <el-radio-button label="middle"/>
              <el-radio-button label="bottom"/>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="标签宽度" v-if="activeData.labelWidth!==undefined">
            <el-input placeholder="请输入标签宽度" type="number" v-model.number="activeData.labelWidth"/>
          </el-form-item>
          <el-form-item label="组件宽度" v-if="activeData.style&&activeData.style.width!==undefined">
            <el-input clearable placeholder="请输入组件宽度" v-model="activeData.style.width"/>
          </el-form-item>
          <el-form-item label="默认值" v-if="activeData.vModel!==undefined">
            <el-input
              :value="setDefaultValue(activeData.defaultValue)"
              @input="onDefaultValueInput"
              placeholder="请输入默认值"
            />
          </el-form-item>
          <el-form-item label="至少应选" v-if="activeData.tag==='el-checkbox-group'">
            <el-input-number
              :min="0"
              :value="activeData.min"
              @input="$set(activeData, 'min', $event?$event:undefined)"
              placeholder="至少应选"
            />
          </el-form-item>
          <el-form-item label="最多可选" v-if="activeData.tag==='el-checkbox-group'">
            <el-input-number
              :min="0"
              :value="activeData.max"
              @input="$set(activeData, 'max', $event?$event:undefined)"
              placeholder="最多可选"
            />
          </el-form-item>
          <el-form-item label="前缀" v-if="activeData.prepend!==undefined">
            <el-input placeholder="请输入前缀" v-model="activeData.prepend"/>
          </el-form-item>
          <el-form-item label="后缀" v-if="activeData.append!==undefined">
            <el-input placeholder="请输入后缀" v-model="activeData.append"/>
          </el-form-item>
          <el-form-item label="前图标" v-if="activeData['prefix-icon']!==undefined">
            <el-input placeholder="请输入前图标名称" v-model="activeData['prefix-icon']">
              <el-button @click="openIconsDialog('prefix-icon')" icon="el-icon-thumb" slot="append">
                选择
              </el-button>
            </el-input>
          </el-form-item>
          <el-form-item label="后图标" v-if="activeData['suffix-icon'] !== undefined">
            <el-input placeholder="请输入后图标名称" v-model="activeData['suffix-icon']">
              <el-button @click="openIconsDialog('suffix-icon')" icon="el-icon-thumb" slot="append">
                选择
              </el-button>
            </el-input>
          </el-form-item>
          <el-form-item label="选项分隔符" v-if="activeData.tag === 'el-cascader'">
            <el-input placeholder="请输入选项分隔符" v-model="activeData.separator"/>
          </el-form-item>
          <el-form-item label="最小行数" v-if="activeData.autosize !== undefined">
            <el-input-number :min="1" placeholder="最小行数" v-model="activeData.autosize.minRows"/>
          </el-form-item>
          <el-form-item label="最大行数" v-if="activeData.autosize !== undefined">
            <el-input-number :min="1" placeholder="最大行数" v-model="activeData.autosize.maxRows"/>
          </el-form-item>
          <el-form-item label="最小值" v-if="activeData.min !== undefined">
            <el-input-number placeholder="最小值" v-model="activeData.min"/>
          </el-form-item>
          <el-form-item label="最大值" v-if="activeData.max !== undefined">
            <el-input-number placeholder="最大值" v-model="activeData.max"/>
          </el-form-item>
          <el-form-item label="步长" v-if="activeData.step !== undefined">
            <el-input-number placeholder="步数" v-model="activeData.step"/>
          </el-form-item>
          <el-form-item label="精度" v-if="activeData.tag === 'el-input-number'">
            <el-input-number :min="0" placeholder="精度" v-model="activeData.precision"/>
          </el-form-item>
          <el-form-item label="按钮位置" v-if="activeData.tag === 'el-input-number'">
            <el-radio-group v-model="activeData['controls-position']">
              <el-radio-button label="">
                默认
              </el-radio-button>
              <el-radio-button label="right">
                右侧
              </el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="最多输入" v-if="activeData.maxlength !== undefined">
            <el-input placeholder="请输入字符长度" v-model="activeData.maxlength">
              <template slot="append">
                个字符
              </template>
            </el-input>
          </el-form-item>
          <el-form-item label="开启提示" v-if="activeData['active-text'] !== undefined">
            <el-input placeholder="请输入开启提示" v-model="activeData['active-text']"/>
          </el-form-item>
          <el-form-item label="关闭提示" v-if="activeData['inactive-text'] !== undefined">
            <el-input placeholder="请输入关闭提示" v-model="activeData['inactive-text']"/>
          </el-form-item>
          <el-form-item label="开启值" v-if="activeData['active-value'] !== undefined">
            <el-input
              :value="setDefaultValue(activeData['active-value'])"
              @input="onSwitchValueInput($event, 'active-value')"
              placeholder="请输入开启值"
            />
          </el-form-item>
          <el-form-item label="关闭值" v-if="activeData['inactive-value'] !== undefined">
            <el-input
              :value="setDefaultValue(activeData['inactive-value'])"
              @input="onSwitchValueInput($event, 'inactive-value')"
              placeholder="请输入关闭值"
            />
          </el-form-item>
          <el-form-item
            label="时间类型"
            v-if="activeData.type !== undefined && 'el-date-picker' === activeData.tag"
          >
            <el-select
              :style="{ width: '100%' }"
              @change="dateTypeChange"
              placeholder="请选择时间类型"
              v-model="activeData.type"
            >
              <el-option
                :key="index"
                :label="item.label"
                :value="item.value"
                v-for="(item, index) in dateOptions"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="文件字段名" v-if="activeData.name !== undefined">
            <el-input placeholder="请输入上传文件字段名" v-model="activeData.name"/>
          </el-form-item>
          <el-form-item label="文件类型" v-if="activeData.accept !== undefined">
            <el-select
              :style="{ width: '100%' }"
              clearable
              placeholder="请选择文件类型"
              v-model="activeData.accept"
            >
              <el-option label="图片" value="image/*"/>
              <el-option label="视频" value="video/*"/>
              <el-option label="音频" value="audio/*"/>
              <el-option label="excel" value=".xls,.xlsx"/>
              <el-option label="word" value=".doc,.docx"/>
              <el-option label="pdf" value=".pdf"/>
              <el-option label="txt" value=".txt"/>
            </el-select>
          </el-form-item>
          <el-form-item label="文件大小" v-if="activeData.fileSize !== undefined">
            <el-input placeholder="请输入文件大小" v-model.number="activeData.fileSize">
              <el-select :style="{ width: '66px' }" slot="append" v-model="activeData.sizeUnit">
                <el-option label="KB" value="KB"/>
                <el-option label="MB" value="MB"/>
                <el-option label="GB" value="GB"/>
              </el-select>
            </el-input>
          </el-form-item>
          <el-form-item label="上传地址" v-if="activeData.action !== undefined">
            <el-input clearable placeholder="请输入上传地址" v-model="activeData.action"/>
          </el-form-item>
          <el-form-item label="列表类型" v-if="activeData['list-type'] !== undefined">
            <el-radio-group size="small" v-model="activeData['list-type']">
              <el-radio-button label="text">
                text
              </el-radio-button>
              <el-radio-button label="picture">
                picture
              </el-radio-button>
              <el-radio-button label="picture-card">
                picture-card
              </el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-form-item
            label="按钮文字"
            v-if="activeData.buttonText !== undefined"
            v-show="'picture-card' !== activeData['list-type']"
          >
            <el-input placeholder="请输入按钮文字" v-model="activeData.buttonText"/>
          </el-form-item>
          <el-form-item label="分隔符" v-if="activeData['range-separator'] !== undefined">
            <el-input placeholder="请输入分隔符" v-model="activeData['range-separator']"/>
          </el-form-item>
          <el-form-item label="时间段" v-if="activeData['picker-options'] !== undefined">
            <el-input
              placeholder="请输入时间段"
              v-model="activeData['picker-options'].selectableRange"
            />
          </el-form-item>
          <el-form-item label="时间格式" v-if="activeData.format !== undefined">
            <el-input
              :value="activeData.format"
              @input="setTimeValue($event)"
              placeholder="请输入时间格式"
            />
          </el-form-item>
          <template
            v-if="['el-checkbox-group', 'el-radio-group', 'el-select'].indexOf(activeData.tag) > -1">
            <el-divider>选项</el-divider>
            <draggable
              :animation="340"
              :list="activeData.options"
              group="selectItem"
              handle=".option-drag"
            >
              <div :key="index" class="select-item" v-for="(item, index) in activeData.options">
                <div class="select-line-icon option-drag">
                  <i class="el-icon-s-operation"/>
                </div>
                <el-input placeholder="选项名" size="small" v-model="item.label"/>
                <el-input
                  :value="item.value"
                  @input="setOptionValue(item, $event)"
                  placeholder="选项值"
                  size="small"
                />
                <div @click="activeData.options.splice(index, 1)"
                     class="close-btn select-line-icon">
                  <i class="el-icon-remove-outline"/>
                </div>
              </div>
            </draggable>
            <div style="margin-left: 20px;">
              <el-button
                @click="addSelectItem"
                icon="el-icon-circle-plus-outline"
                style="padding-bottom: 0"
                type="text"
              >
                添加选项
              </el-button>
            </div>
            <el-divider/>
          </template>

          <template v-if="['el-cascader'].indexOf(activeData.tag) > -1">
            <el-divider>选项</el-divider>
            <el-form-item label="数据类型">
              <el-radio-group size="small" v-model="activeData.dataType">
                <el-radio-button label="dynamic">
                  动态数据
                </el-radio-button>
                <el-radio-button label="static">
                  静态数据
                </el-radio-button>
              </el-radio-group>
            </el-form-item>

            <template v-if="activeData.dataType === 'dynamic'">
              <el-form-item label="标签键名">
                <el-input placeholder="请输入标签键名" v-model="activeData.labelKey"/>
              </el-form-item>
              <el-form-item label="值键名">
                <el-input placeholder="请输入值键名" v-model="activeData.valueKey"/>
              </el-form-item>
              <el-form-item label="子级键名">
                <el-input placeholder="请输入子级键名" v-model="activeData.childrenKey"/>
              </el-form-item>
            </template>

            <el-tree
              :data="activeData.options"
              :expand-on-click-node="false"
              :render-content="renderContent"
              draggable
              node-key="id"
              v-if="activeData.dataType === 'static'"
            />
            <div style="margin-left: 20px" v-if="activeData.dataType === 'static'">
              <el-button
                @click="addTreeItem"
                icon="el-icon-circle-plus-outline"
                style="padding-bottom: 0"
                type="text"
              >
                添加父级
              </el-button>
            </div>
            <el-divider/>
          </template>

          <el-form-item label="选项样式" v-if="activeData.optionType !== undefined">
            <el-radio-group v-model="activeData.optionType">
              <el-radio-button label="default">
                默认
              </el-radio-button>
              <el-radio-button label="button">
                按钮
              </el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="开启颜色" v-if="activeData['active-color'] !== undefined">
            <el-color-picker v-model="activeData['active-color']"/>
          </el-form-item>
          <el-form-item label="关闭颜色" v-if="activeData['inactive-color'] !== undefined">
            <el-color-picker v-model="activeData['inactive-color']"/>
          </el-form-item>

          <el-form-item label="允许半选" v-if="activeData['allow-half'] !== undefined">
            <el-switch v-model="activeData['allow-half']"/>
          </el-form-item>
          <el-form-item label="辅助文字" v-if="activeData['show-text'] !== undefined">
            <el-switch @change="rateTextChange" v-model="activeData['show-text']"/>
          </el-form-item>
          <el-form-item label="显示分数" v-if="activeData['show-score'] !== undefined">
            <el-switch @change="rateScoreChange" v-model="activeData['show-score']"/>
          </el-form-item>
          <el-form-item label="显示间断点" v-if="activeData['show-stops'] !== undefined">
            <el-switch v-model="activeData['show-stops']"/>
          </el-form-item>
          <el-form-item label="范围选择" v-if="activeData.range !== undefined">
            <el-switch @change="rangeChange" v-model="activeData.range"/>
          </el-form-item>
          <el-form-item
            label="是否带边框"
            v-if="activeData.border !== undefined && activeData.optionType === 'default'"
          >
            <el-switch v-model="activeData.border"/>
          </el-form-item>
          <el-form-item label="颜色格式" v-if="activeData.tag === 'el-color-picker'">
            <el-select
              :style="{ width: '100%' }"
              @change="colorFormatChange"
              placeholder="请选择颜色格式"
              v-model="activeData['color-format']"
            >
              <el-option
                :key="index"
                :label="item.label"
                :value="item.value"
                v-for="(item, index) in colorFormatOptions"
              />
            </el-select>
          </el-form-item>
          <el-form-item
            label="选项尺寸"
            v-if="activeData.size !== undefined &&
              (activeData.optionType === 'button' ||
                activeData.border ||
                activeData.tag === 'el-color-picker')"
          >
            <el-radio-group v-model="activeData.size">
              <el-radio-button label="medium">
                中等
              </el-radio-button>
              <el-radio-button label="small">
                较小
              </el-radio-button>
              <el-radio-button label="mini">
                迷你
              </el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="输入统计" v-if="activeData['show-word-limit'] !== undefined">
            <el-switch v-model="activeData['show-word-limit']"/>
          </el-form-item>
          <el-form-item label="严格步数" v-if="activeData.tag === 'el-input-number'">
            <el-switch v-model="activeData['step-strictly']"/>
          </el-form-item>
          <el-form-item label="是否多选" v-if="activeData.tag === 'el-cascader'">
            <el-switch v-model="activeData.props.props.multiple"/>
          </el-form-item>
          <el-form-item label="展示全路径" v-if="activeData.tag === 'el-cascader'">
            <el-switch v-model="activeData['show-all-levels']"/>
          </el-form-item>
          <el-form-item label="可否筛选" v-if="activeData.tag === 'el-cascader'">
            <el-switch v-model="activeData.filterable"/>
          </el-form-item>
          <el-form-item label="能否清空" v-if="activeData.clearable !== undefined">
            <el-switch v-model="activeData.clearable"/>
          </el-form-item>
          <el-form-item label="显示提示" v-if="activeData.showTip !== undefined">
            <el-switch v-model="activeData.showTip"/>
          </el-form-item>
          <el-form-item label="多选文件" v-if="activeData.multiple !== undefined">
            <el-switch v-model="activeData.multiple"/>
          </el-form-item>
          <el-form-item label="自动上传" v-if="activeData['auto-upload'] !== undefined">
            <el-switch v-model="activeData['auto-upload']"/>
          </el-form-item>
          <el-form-item label="是否只读" v-if="activeData.readonly !== undefined">
            <el-switch v-model="activeData.readonly"/>
          </el-form-item>
          <el-form-item label="是否禁用" v-if="activeData.disabled !== undefined">
            <el-switch v-model="activeData.disabled"/>
          </el-form-item>
          <el-form-item label="是否可搜索" v-if="activeData.tag === 'el-select'">
            <el-switch v-model="activeData.filterable"/>
          </el-form-item>
          <el-form-item label="是否多选" v-if="activeData.tag === 'el-select'">
            <el-switch @change="multipleChange" v-model="activeData.multiple"/>
          </el-form-item>
          <el-form-item label="是否必填" v-if="activeData.required !== undefined">
            <el-switch v-model="activeData.required"/>
          </el-form-item>

          <template v-if="activeData.layoutTree">
            <el-divider>布局结构树</el-divider>
            <el-tree
              :data="[activeData]"
              :props="layoutTreeProps"
              default-expand-all
              draggable
              node-key="renderKey"
            >
              <span slot-scope="{ node, data }">
                <span class="node-label">
                  <svg-icon :icon-class="data.tagIcon" class="node-icon"/>
                  {{ node.label }}
                </span>
              </span>
            </el-tree>
          </template>

          <template v-if="activeData.layout === 'colFormItem'">
            <el-divider>正则校验</el-divider>
            <div
              :key="index"
              class="reg-item"
              v-for="(item, index) in activeData.regList"
            >
              <span @click="activeData.regList.splice(index, 1)" class="close-btn">
                <i class="el-icon-close"/>
              </span>
              <el-form-item label="表达式">
                <el-input placeholder="请输入正则" v-model="item.pattern"/>
              </el-form-item>
              <el-form-item label="错误提示" style="margin-bottom:0">
                <el-input placeholder="请输入错误提示" v-model="item.message"/>
              </el-form-item>
            </div>
            <div style="margin-left: 20px">
              <el-button @click="addReg" icon="el-icon-circle-plus-outline" type="text">
                添加规则
              </el-button>
            </div>
          </template>
        </el-form>
        <!-- 表单属性 -->
        <el-form label-width="90px" size="small" v-show="currentTab === 'form'">
          <el-form-item label="表单名">
            <el-input placeholder="请输入表单名（ref）" v-model="formConf.formRef"/>
          </el-form-item>
          <el-form-item label="表单模型">
            <el-input placeholder="请输入数据模型" v-model="formConf.formModel"/>
          </el-form-item>
          <el-form-item label="校验模型">
            <el-input placeholder="请输入校验模型" v-model="formConf.formRules"/>
          </el-form-item>
          <el-form-item label="表单尺寸">
            <el-radio-group v-model="formConf.size">
              <el-radio-button label="medium">
                中等
              </el-radio-button>
              <el-radio-button label="small">
                较小
              </el-radio-button>
              <el-radio-button label="mini">
                迷你
              </el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="标签对齐">
            <el-radio-group v-model="formConf.labelPosition">
              <el-radio-button label="left">
                左对齐
              </el-radio-button>
              <el-radio-button label="right">
                右对齐
              </el-radio-button>
              <el-radio-button label="top">
                顶部对齐
              </el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="标签宽度">
            <el-input-number placeholder="标签宽度" v-model="formConf.labelWidth"/>
          </el-form-item>
          <el-form-item label="栅格间隔">
            <el-input-number :min="0" placeholder="栅格间隔" v-model="formConf.gutter"/>
          </el-form-item>
          <el-form-item label="禁用表单">
            <el-switch v-model="formConf.disabled"/>
          </el-form-item>
          <el-form-item label="表单按钮">
            <el-switch v-model="formConf.formBtns"/>
          </el-form-item>
          <el-form-item label="显示未选中组件边框">
            <el-switch v-model="formConf.unFocusedComponentBorder"/>
          </el-form-item>
        </el-form>
      </el-scrollbar>
    </div>

    <treeNode-dialog :visible.sync="dialogVisible" @commit="addNode" title="添加选项"/>
    <icons-dialog :current="activeData[currentIconModel]" :visible.sync="iconsVisible"
                  @select="setIcon"/>
  </div>
</template>

<script>
  import {isArray} from 'util'
  import TreeNodeDialog from './TreeNodeDialog'
  import {isNumberStr} from '@/utils/index'
  import IconsDialog from './IconsDialog'
  import {inputComponents, selectComponents} from '@/utils/generator/config'

  const dateTimeFormat = {
    date: 'yyyy-MM-dd',
    week: 'yyyy 第 WW 周',
    month: 'yyyy-MM',
    year: 'yyyy',
    datetime: 'yyyy-MM-dd HH:mm:ss',
    daterange: 'yyyy-MM-dd',
    monthrange: 'yyyy-MM',
    datetimerange: 'yyyy-MM-dd HH:mm:ss'
  }

  export default {
    components: {
      TreeNodeDialog,
      IconsDialog
    },
    props: ['showField', 'activeData', 'formConf'],
    data() {
      return {
        currentTab: 'field',
        currentNode: null,
        dialogVisible: false,
        iconsVisible: false,
        currentIconModel: null,
        dateTypeOptions: [
          {
            label: '日(date)',
            value: 'date'
          },
          {
            label: '周(week)',
            value: 'week'
          },
          {
            label: '月(month)',
            value: 'month'
          },
          {
            label: '年(year)',
            value: 'year'
          },
          {
            label: '日期时间(datetime)',
            value: 'datetime'
          }
        ],
        dateRangeTypeOptions: [
          {
            label: '日期范围(daterange)',
            value: 'daterange'
          },
          {
            label: '月范围(monthrange)',
            value: 'monthrange'
          },
          {
            label: '日期时间范围(datetimerange)',
            value: 'datetimerange'
          }
        ],
        colorFormatOptions: [
          {
            label: 'hex',
            value: 'hex'
          },
          {
            label: 'rgb',
            value: 'rgb'
          },
          {
            label: 'rgba',
            value: 'rgba'
          },
          {
            label: 'hsv',
            value: 'hsv'
          },
          {
            label: 'hsl',
            value: 'hsl'
          }
        ],
        justifyOptions: [
          {
            label: 'start',
            value: 'start'
          },
          {
            label: 'end',
            value: 'end'
          },
          {
            label: 'center',
            value: 'center'
          },
          {
            label: 'space-around',
            value: 'space-around'
          },
          {
            label: 'space-between',
            value: 'space-between'
          }
        ],
        layoutTreeProps: {
          label(data, node) {
            return data.componentName || `${data.label}: ${data.vModel}`
          }
        }
      }
    },
    computed: {
      documentLink() {
        return (
          this.activeData.document
          || 'https://element.eleme.cn/#/zh-CN/component/installation'
        )
      },
      dateOptions() {
        if (
          this.activeData.type !== undefined
          && this.activeData.tag === 'el-date-picker'
        ) {
          if (this.activeData['start-placeholder'] === undefined) {
            return this.dateTypeOptions
          }
          return this.dateRangeTypeOptions
        }
        return []
      },
      tagList() {
        return [
          {
            label: '输入型组件',
            options: inputComponents
          },
          {
            label: '选择型组件',
            options: selectComponents
          }
        ]
      }
    },
    methods: {
      addReg() {
        this.activeData.regList.push({
          pattern: '',
          message: ''
        })
      },
      addSelectItem() {
        this.activeData.options.push({
          label: '',
          value: ''
        })
      },
      addTreeItem() {
        ++this.idGlobal
        this.dialogVisible = true
        this.currentNode = this.activeData.options
      },
      renderContent(h, {node, data, store}) {
        return (
          < div
      class
        = "custom-tree-node" >
          < span > {node.label} < /span>
          < span
      class
        = "node-operation" >
          < i
        on - click = {()
      =>
        this.append(data)
      }
      class
        = "el-icon-plus"
        title = "添加"
          > < /i>
          < i
        on - click = {()
      =>
        this.remove(node, data)
      }
      class
        = "el-icon-delete"
        title = "删除"
          > < /i>
          < /span>
          < /div>
      )
      },
      append(data) {
        if (!data.children) {
          this.$set(data, 'children', [])
        }
        this.dialogVisible = true
        this.currentNode = data.children
      },
      remove(node, data) {
        const {parent} = node
        const children = parent.data.children || parent.data
        const index = children.findIndex(d => d.id === data.id)
        children.splice(index, 1)
      },
      addNode(data) {
        this.currentNode.push(data)
      },
      setOptionValue(item, val) {
        item.value = isNumberStr(val) ? +val : val
      },
      setDefaultValue(val) {
        if (Array.isArray(val)) {
          return val.join(',')
        }
        if (['string', 'number'].indexOf(val) > -1) {
          return val
        }
        if (typeof val === 'boolean') {
          return `${val}`
        }
        return val
      },
      onDefaultValueInput(str) {
        if (isArray(this.activeData.defaultValue)) {
          // 数组
          this.$set(
            this.activeData,
            'defaultValue',
            str.split(',').map(val => (isNumberStr(val) ? +val : val))
          )
        } else if (['true', 'false'].indexOf(str) > -1) {
          // 布尔
          this.$set(this.activeData, 'defaultValue', JSON.parse(str))
        } else {
          // 字符串和数字
          this.$set(
            this.activeData,
            'defaultValue',
            isNumberStr(str) ? +str : str
          )
        }
      },
      onSwitchValueInput(val, name) {
        if (['true', 'false'].indexOf(val) > -1) {
          this.$set(this.activeData, name, JSON.parse(val))
        } else {
          this.$set(this.activeData, name, isNumberStr(val) ? +val : val)
        }
      },
      setTimeValue(val, type) {
        const valueFormat = type === 'week' ? dateTimeFormat.date : val
        this.$set(this.activeData, 'defaultValue', null)
        this.$set(this.activeData, 'value-format', valueFormat)
        this.$set(this.activeData, 'format', val)
      },
      spanChange(val) {
        this.formConf.span = val
      },
      multipleChange(val) {
        this.$set(this.activeData, 'defaultValue', val ? [] : '')
      },
      dateTypeChange(val) {
        this.setTimeValue(dateTimeFormat[val], val)
      },
      rangeChange(val) {
        this.$set(
          this.activeData,
          'defaultValue',
          val ? [this.activeData.min, this.activeData.max] : this.activeData.min
        )
      },
      rateTextChange(val) {
        if (val) {
          this.activeData['show-score'] = false
        }
      },
      rateScoreChange(val) {
        if (val) {
          this.activeData['show-text'] = false
        }
      },
      colorFormatChange(val) {
        this.activeData.defaultValue = null
        this.activeData['show-alpha'] = val.indexOf('a') > -1
        this.activeData.renderKey = +new Date() // 更新renderKey,重新渲染该组件
      },
      openIconsDialog(model) {
        this.iconsVisible = true
        this.currentIconModel = model
      },
      setIcon(val) {
        this.activeData[this.currentIconModel] = val
      },
      tagChange(tagIcon) {
        let target = inputComponents.find(item => item.tagIcon === tagIcon)
        if (!target) {
          target = selectComponents.find(item => item.tagIcon === tagIcon)
        }
        this.$emit('tag-change', target)
      }
    }
  }
</script>

<style lang="scss" scoped>
  .right-board {
    width: 350px;
    position: absolute;
    right: 0;
    top: 0;
    padding-top: 3px;

    .field-box {
      position: relative;
      height: calc(100vh - 42px);
      box-sizing: border-box;
      overflow: hidden;
    }

    .el-scrollbar {
      height: 100%;
    }
  }

  .select-item {
    display: flex;
    border: 1px dashed #fff;
    box-sizing: border-box;

    & .close-btn {
      cursor: pointer;
      color: #f56c6c;
    }

    & .el-input + .el-input {
      margin-left: 4px;
    }
  }

  .select-item + .select-item {
    margin-top: 4px;
  }

  .select-item.sortable-chosen {
    border: 1px dashed #409eff;
  }

  .select-line-icon {
    line-height: 32px;
    font-size: 22px;
    padding: 0 4px;
    color: #777;
  }

  .option-drag {
    cursor: move;
  }

  .time-range {
    .el-date-editor {
      width: 227px;
    }

    ::v-deep .el-icon-time {
      display: none;
    }
  }

  .document-link {
    position: absolute;
    display: block;
    width: 26px;
    height: 26px;
    top: 0;
    left: 0;
    cursor: pointer;
    background: #409eff;
    z-index: 1;
    border-radius: 0 0 6px 0;
    text-align: center;
    line-height: 26px;
    color: #fff;
    font-size: 18px;
  }

  .node-label {
    font-size: 14px;
  }

  .node-icon {
    color: #bebfc3;
  }
</style>
