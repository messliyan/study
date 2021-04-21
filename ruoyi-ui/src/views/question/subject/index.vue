<template>
  <div class="app-container">
    <el-form :inline="true" :model="queryParams" label-width="68px" ref="queryForm"
             v-show="showSearch">
      <el-form-item label="专题名称" prop="subjectName">
        <el-input
          @keyup.enter.native="handleQuery"
          clearable
          placeholder="请输入专题名称"
          size="small"
          v-model="queryParams.subjectName"
        />
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
          v-hasPermi="['question:subject:add']"
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
          v-hasPermi="['question:subject:edit']"
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
          v-hasPermi="['question:subject:remove']"
        >删除
        </el-button>
      </el-col>

      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table :data="subjectList" @selection-change="handleSelectionChange" v-loading="loading">
      <el-table-column align="center" type="selection" width="55"/>
      <el-table-column align="center" label="专题编号" prop="subjectId"/>
      <el-table-column align="center" label="专题名称" prop="subjectName"/>
      <el-table-column align="center" label="专题描述" prop="describe"/>
      <el-table-column align="center" label="专题封面" prop="icon">
        <template slot-scope="scope">
          　<img :src="scope.row.icon"/>
        </template>
      </el-table-column>
      <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
        <template slot-scope="scope">
          <el-button
            @click="handleUpdate(scope.row)"
            icon="el-icon-edit"
            size="mini"
            type="text"
            v-hasPermi="['question:subject:edit']"
          >修改
          </el-button>
          <el-button
            @click="handleDelete(scope.row)"
            icon="el-icon-delete"
            size="mini"
            type="text"
            v-hasPermi="['question:subject:remove']"
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

    <!-- 添加或修改专题数据对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
      <el-form :model="form" :rules="rules" label-width="80px" ref="form">
        <el-form-item label="专题名称" prop="subjectName">
          <el-input placeholder="请输入专题名称" v-model="form.subjectName"/>
        </el-form-item>
        <el-form-item label="专题描述">
          <el-input placeholder="请输入专题描述" v-model="form.describe"/>
        </el-form-item>
        <el-form-item label="专题封面">
          <imageUpload v-model="form.icon"/>
        </el-form-item>
        <el-form-item label="权重">
          <el-input placeholder="请输入权重" v-model="form.weight"/>
        </el-form-item>
        <el-form-item label="岗位">
          <el-select multiple placeholder="请选择" v-model="form.postIds">
            <el-option
              :disabled="item.status == 1"
              :key="item.postId"
              :label="item.postName"
              :value="item.postId"
              v-for="item in postOptions"
            ></el-option>
          </el-select>
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
    addSubject,
    delSubject,
    getSubject,
    listSubject,
    updateSubject
  } from "@/api/question/subject";
  import {getUser} from "@/api/system/user";
  import ImageUpload from '@/components/ImageUpload';

  export default {
    name: "Subject",
    components: {
      ImageUpload,
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
        // 专题数据表格数据
        subjectList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 岗位选项
        postOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          subjectName: null,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {}
      };
    },
    created() {
      this.getList();
      this.getPost();
    },
    methods: {
      /** 查询专题数据列表 */
      getList() {
        this.loading = true;
        listSubject(this.queryParams).then(response => {
          this.subjectList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 查询岗位
      getPost() {
        getUser().then(response => {
          this.postOptions = response.posts;
        });
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          subjectId: null,
          subjectName: null,
          weight: null,
          describe: null,
          subjectDetail: null,
          occupation: null,
          deleteStatus: 0,
          createTime: null,
          updateTime: null,
          version: null,
          postIds: []
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
        this.ids = selection.map(item => item.subjectId)
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加专题数据";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const subjectId = row.subjectId || this.ids
        getSubject(subjectId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改专题数据";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.subjectId != null) {
              updateSubject(this.form).then(response => {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addSubject(this.form).then(response => {
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
        const subjectIds = row.subjectId || this.ids;
        this.$confirm('是否确认删除专题数据编号为"' + subjectIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delSubject(subjectIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('question/subject/export', {
          ...this.queryParams
        }, `question_subject.xlsx`)
      }
    }
  };
</script>
