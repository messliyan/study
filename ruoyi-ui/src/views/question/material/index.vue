<template>
  <div class="app-container">
    <el-form :inline="true" :model="queryParams" label-width="68px" ref="queryForm"
             v-show="showSearch">
      <el-form-item label="资料名称" prop="materialName">
        <el-input
          @keyup.enter.native="handleQuery"
          clearable
          placeholder="请输入专题资料名称"
          size="small"
          v-model="queryParams.materialName"
        />
      </el-form-item>
      <el-form-item label="资料类型" prop="materialType">
        <el-select clearable placeholder="请选择资料类型" size="small" v-model="queryParams.materialType">
          <el-option
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictLabel"
            v-for="dict in materialTypeOptions"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="所属专题" prop="subjectId">
        <el-select clearable placeholder="请选择所属专题" size="small" v-model="queryParams.subjectId">
          <el-option
            :key="subject.subjectId"
            :label="subject.subjectName"
            :value="subject.subjectId"
            v-for="subject in materialSubjectOptions"
          ></el-option>
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
          v-hasPermi="['question:material:add']"
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
          v-hasPermi="['question:material:edit']"
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
          v-hasPermi="['question:material:remove']"
        >删除
        </el-button>
      </el-col>

      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table :data="materialList" @selection-change="handleSelectionChange" v-loading="loading">
      <el-table-column align="center" type="selection" width="55"/>
      <el-table-column align="center" label="专题资料编号" prop="materialId"/>
      <el-table-column align="center" label="专题资料名称" prop="materialName"/>
      <el-table-column align="center" label="专题资料类型" prop="materialType"/>
      <el-table-column align="center" label="创建时间" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{m}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
        <template slot-scope="scope">
          <el-button
            @click="handleUpdate(scope.row)"
            icon="el-icon-edit"
            size="mini"
            type="text"
            v-hasPermi="['question:material:edit']"
          >修改
          </el-button>
          <el-button
            @click="handleDelete(scope.row)"
            icon="el-icon-delete"
            size="mini"
            type="text"
            v-hasPermi="['question:material:remove']"
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

    <!-- 添加或修改专题资料对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
      <el-form :model="form" :rules="rules" label-width="80px" ref="form">

        <el-form-item label="所属专题" prop="subjectId">
          <el-select placeholder="请选择所属专题" v-model="form.subjectId">
            <el-option
              :key="subject.subjectId"
              :label="subject.subjectName"
              :value="subject.subjectId"
              v-for="subject in materialSubjectOptions"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="资料名称" prop="materialName">
          <el-input placeholder="请输入专题资料名称" v-model="form.materialName"/>
        </el-form-item>
        <el-form-item label="资料类型" prop="materialType">
          <el-select placeholder="请选择资料类型" v-model="form.materialType">
            <el-option
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictLabel"
              v-for="dict in materialTypeOptions"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="文件资料">
          <fileUpload v-model="form.url"/>
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
    addMaterial,
    delMaterial,
    getMaterial,
    listMaterial,
    updateMaterial
  } from "@/api/question/material";
  import {listSubject} from "@/api/question/subject";
  import FileUpload from '@/components/FileUpload';

  export default {
    name: "Material",
    components: {
      FileUpload,
    },
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
        // 专题资料表格数据
        materialList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 专题资料类型字典
        materialTypeOptions: [],
        // 专题资料专题字典
        materialSubjectOptions: [],

        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          materialName: null,
          materialType: null,
          subjectId: null,
        },
        // 字典查询参数
        queryParams2: {
          pageNum: 1,
          pageSize: 10000
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {}
      };
    },
    created() {
      this.getList();
      this.getSubject();
      this.getDicts("que_material_type").then(response => {
        this.materialTypeOptions = response.data;
      });
    },
    methods: {
      /** 查询专题资料列表 */
      getList() {
        this.loading = true;
        listMaterial(this.queryParams).then(response => {
          this.materialList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 查询专题字典
      getSubject() {
        listSubject(this.queryParams2).then(response => {
          this.materialSubjectOptions = response.rows;
        });
      },
      // 题目类型字典翻译
      materialTypeFormat(row, column) {
        return this.selectDictLabel(this.materialTypeOptions, row.materialType);
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          materialId: null,
          materialName: null,
          materialType: null,
          url: null,
          subjectId: null,
          deleteStatus: 0,
          createTime: null,
          updateTime: null,
          version: null
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
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.materialId)
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加专题资料";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const materialId = row.materialId || this.ids
        getMaterial(materialId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改专题资料";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.materialId != null) {
              updateMaterial(this.form).then(response => {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addMaterial(this.form).then(response => {
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
        const materialIds = row.materialId || this.ids;
        this.$confirm('是否确认删除专题资料编号为"' + materialIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delMaterial(materialIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('question/material/export', {
          ...this.queryParams
        }, `question_material.xlsx`)
      }
    }
  };
</script>
