<template>
  <div class="app-container">
    <el-form :inline="true" :model="queryParams" label-width="68px" ref="queryForm"
             v-show="showSearch">
      <el-form-item label="参数名称" prop="configName">
        <el-input
          @keyup.enter.native="handleQuery"
          clearable
          placeholder="请输入参数名称"
          size="small"
          style="width: 240px"
          v-model="queryParams.configName"
        />
      </el-form-item>
      <el-form-item label="参数键名" prop="configKey">
        <el-input
          @keyup.enter.native="handleQuery"
          clearable
          placeholder="请输入参数键名"
          size="small"
          style="width: 240px"
          v-model="queryParams.configKey"
        />
      </el-form-item>
      <el-form-item label="系统内置" prop="configType">
        <el-select clearable placeholder="系统内置" size="small" v-model="queryParams.configType">
          <el-option
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
            v-for="dict in typeOptions"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          end-placeholder="结束日期"
          range-separator="-"
          size="small"
          start-placeholder="开始日期"
          style="width: 240px"
          type="daterange"
          v-model="dateRange"
          value-format="yyyy-MM-dd"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery" icon="el-icon-search" size="mini" type="primary">搜索
        </el-button>
        <el-button @click="resetQuery" icon="el-icon-refresh" size="mini">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          @click="handleAdd"
          icon="el-icon-plus"
          plain
          size="mini"
          type="primary"
          v-hasPermi="['system:config:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          :disabled="single"
          @click="handleUpdate"
          icon="el-icon-edit"
          plain
          size="mini"
          type="success"
          v-hasPermi="['system:config:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          :disabled="multiple"
          @click="handleDelete"
          icon="el-icon-delete"
          plain
          size="mini"
          type="danger"
          v-hasPermi="['system:config:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          @click="handleExport"
          icon="el-icon-download"
          plain
          size="mini"
          type="warning"
          v-hasPermi="['system:config:export']"
        >导出
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          @click="handleClearCache"
          icon="el-icon-refresh"
          plain
          size="mini"
          type="danger"
          v-hasPermi="['system:config:remove']"
        >清理缓存
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table :data="configList" @selection-change="handleSelectionChange" v-loading="loading">
      <el-table-column align="center" type="selection" width="55"/>
      <el-table-column align="center" label="参数主键" prop="configId"/>
      <el-table-column :show-overflow-tooltip="true" align="center" label="参数名称" prop="configName"/>
      <el-table-column :show-overflow-tooltip="true" align="center" label="参数键名" prop="configKey"/>
      <el-table-column align="center" label="参数键值" prop="configValue"/>
      <el-table-column :formatter="typeFormat" align="center" label="系统内置" prop="configType"/>
      <el-table-column :show-overflow-tooltip="true" align="center" label="备注" prop="remark"/>
      <el-table-column align="center" label="创建时间" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
        <template slot-scope="scope">
          <el-button
            @click="handleUpdate(scope.row)"
            icon="el-icon-edit"
            size="mini"
            type="text"
            v-hasPermi="['system:config:edit']"
          >修改
          </el-button>
          <el-button
            @click="handleDelete(scope.row)"
            icon="el-icon-delete"
            size="mini"
            type="text"
            v-hasPermi="['system:config:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      :limit.sync="queryParams.pageSize"
      :page.sync="queryParams.pageNum"
      :total="total"
      @pagination="getList"
      v-show="total>0"
    />

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
      <el-form :model="form" :rules="rules" label-width="80px" ref="form">
        <el-form-item label="参数名称" prop="configName">
          <el-input placeholder="请输入参数名称" v-model="form.configName"/>
        </el-form-item>
        <el-form-item label="参数键名" prop="configKey">
          <el-input placeholder="请输入参数键名" v-model="form.configKey"/>
        </el-form-item>
        <el-form-item label="参数键值" prop="configValue">
          <el-input placeholder="请输入参数键值" v-model="form.configValue"/>
        </el-form-item>
        <el-form-item label="系统内置" prop="configType">
          <el-radio-group v-model="form.configType">
            <el-radio
              :key="dict.dictValue"
              :label="dict.dictValue"
              v-for="dict in typeOptions"
            >{{dict.dictLabel}}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input placeholder="请输入内容" type="textarea" v-model="form.remark"/>
        </el-form-item>
      </el-form>
      <div class="dialog-footer" slot="footer">
        <el-button @click="submitForm" type="primary">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    addConfig,
    clearCache,
    delConfig,
    getConfig,
    listConfig,
    updateConfig
  } from "@/api/system/config";

  export default {
    name: "Config",
    data() {
      return {
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        // 参数表格数据
        configList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 类型数据字典
        typeOptions: [],
        // 日期范围
        dateRange: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          configName: undefined,
          configKey: undefined,
          configType: undefined
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          configName: [
            {required: true, message: "参数名称不能为空", trigger: "blur"}
          ],
          configKey: [
            {required: true, message: "参数键名不能为空", trigger: "blur"}
          ],
          configValue: [
            {required: true, message: "参数键值不能为空", trigger: "blur"}
          ]
        }
      };
    },
    created() {
      this.getList();
      this.getDicts("sys_yes_no").then(response => {
        this.typeOptions = response.data;
      });
    },
    methods: {
      /** 查询参数列表 */
      getList() {
        this.loading = true;
        listConfig(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
            this.configList = response.rows;
            this.total = response.total;
            this.loading = false;
          }
        );
      },
      // 参数系统内置字典翻译
      typeFormat(row, column) {
        return this.selectDictLabel(this.typeOptions, row.configType);
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          configId: undefined,
          configName: undefined,
          configKey: undefined,
          configValue: undefined,
          configType: "Y",
          remark: undefined
        };
        this.resetForm("form");
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.dateRange = [];
        this.resetForm("queryForm");
        this.handleQuery();
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加参数";
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.configId)
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const configId = row.configId || this.ids
        getConfig(configId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改参数";
        });
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.configId != undefined) {
              updateConfig(this.form).then(response => {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addConfig(this.form).then(response => {
                this.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              });
            }
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const configIds = row.configId || this.ids;
        this.$confirm('是否确认删除参数编号为"' + configIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delConfig(configIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('system/config/export', {
          ...this.queryParams
        }, `config_${new Date().getTime()}.xlsx`)
      },
      /** 清理缓存按钮操作 */
      handleClearCache() {
        clearCache().then(response => {
          this.msgSuccess("清理成功");
        });
      }
    }
  };
</script>
