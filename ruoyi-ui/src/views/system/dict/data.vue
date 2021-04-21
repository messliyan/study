<template>
  <div class="app-container">
    <el-form :inline="true" :model="queryParams" ref="queryForm" v-show="showSearch">
      <el-form-item label="字典名称" prop="dictType">
        <el-select size="small" v-model="queryParams.dictType">
          <el-option
            :key="item.dictId"
            :label="item.dictName"
            :value="item.dictType"
            v-for="item in typeOptions"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="字典标签" prop="dictLabel">
        <el-input
          @keyup.enter.native="handleQuery"
          clearable
          placeholder="请输入字典标签"
          size="small"
          v-model="queryParams.dictLabel"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select clearable placeholder="数据状态" size="small" v-model="queryParams.status">
          <el-option
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
            v-for="dict in statusOptions"
          />
        </el-select>
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
          v-hasPermi="['system:dict:add']"
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
          v-hasPermi="['system:dict:edit']"
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
          v-hasPermi="['system:dict:remove']"
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
          v-hasPermi="['system:dict:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table :data="dataList" @selection-change="handleSelectionChange" v-loading="loading">
      <el-table-column align="center" type="selection" width="55"/>
      <el-table-column align="center" label="字典编码" prop="dictCode"/>
      <el-table-column align="center" label="字典标签" prop="dictLabel"/>
      <el-table-column align="center" label="字典键值" prop="dictValue"/>
      <el-table-column align="center" label="字典排序" prop="dictSort"/>
      <el-table-column :formatter="statusFormat" align="center" label="状态" prop="status"/>
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
            v-hasPermi="['system:dict:edit']"
          >修改
          </el-button>
          <el-button
            @click="handleDelete(scope.row)"
            icon="el-icon-delete"
            size="mini"
            type="text"
            v-hasPermi="['system:dict:remove']"
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
        <el-form-item label="字典类型">
          <el-input :disabled="true" v-model="form.dictType"/>
        </el-form-item>
        <el-form-item label="数据标签" prop="dictLabel">
          <el-input placeholder="请输入数据标签" v-model="form.dictLabel"/>
        </el-form-item>
        <el-form-item label="数据键值" prop="dictValue">
          <el-input placeholder="请输入数据键值" v-model="form.dictValue"/>
        </el-form-item>
        <el-form-item label="显示排序" prop="dictSort">
          <el-input-number :min="0" controls-position="right" v-model="form.dictSort"/>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              :key="dict.dictValue"
              :label="dict.dictValue"
              v-for="dict in statusOptions"
            >{{dict.dictLabel}}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input placeholder="请输入内容" type="textarea" v-model="form.remark"></el-input>
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
  import {addData, delData, getData, listData, updateData} from "@/api/system/dict/data";
  import {getType, listType} from "@/api/system/dict/type";

  export default {
    name: "Data",
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
        // 字典表格数据
        dataList: [],
        // 默认字典类型
        defaultDictType: "",
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 状态数据字典
        statusOptions: [],
        // 类型数据字典
        typeOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          dictName: undefined,
          dictType: undefined,
          status: undefined
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          dictLabel: [
            {required: true, message: "数据标签不能为空", trigger: "blur"}
          ],
          dictValue: [
            {required: true, message: "数据键值不能为空", trigger: "blur"}
          ],
          dictSort: [
            {required: true, message: "数据顺序不能为空", trigger: "blur"}
          ]
        }
      };
    },
    created() {
      const dictId = this.$route.params && this.$route.params.dictId;
      this.getType(dictId);
      this.getTypeList();
      this.getDicts("sys_normal_disable").then(response => {
        this.statusOptions = response.data;
      });
    },
    methods: {
      /** 查询字典类型详细 */
      getType(dictId) {
        getType(dictId).then(response => {
          this.queryParams.dictType = response.data.dictType;
          this.defaultDictType = response.data.dictType;
          this.getList();
        });
      },
      /** 查询字典类型列表 */
      getTypeList() {
        listType().then(response => {
          this.typeOptions = response.rows;
        });
      },
      /** 查询字典数据列表 */
      getList() {
        this.loading = true;
        listData(this.queryParams).then(response => {
          this.dataList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 数据状态字典翻译
      statusFormat(row, column) {
        return this.selectDictLabel(this.statusOptions, row.status);
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          dictCode: undefined,
          dictLabel: undefined,
          dictValue: undefined,
          dictSort: 0,
          status: "0",
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
        this.resetForm("queryForm");
        this.queryParams.dictType = this.defaultDictType;
        this.handleQuery();
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加字典数据";
        this.form.dictType = this.queryParams.dictType;
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.dictCode)
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const dictCode = row.dictCode || this.ids
        getData(dictCode).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改字典数据";
        });
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.dictCode != undefined) {
              updateData(this.form).then(response => {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addData(this.form).then(response => {
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
        const dictCodes = row.dictCode || this.ids;
        this.$confirm('是否确认删除字典编码为"' + dictCodes + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delData(dictCodes);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('system/dict/data/export', {
          ...this.queryParams
        }, `data_${new Date().getTime()}.xlsx`)
      }
    }
  };
</script>
