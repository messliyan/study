<template>
  <div class="app-container">
    <el-form :inline="true" :model="queryParams" label-width="68px" ref="queryForm"
             v-show="showSearch">
      <el-form-item label="题目内容" prop="storeContent">
        <el-input placeholder="请输入题目内容" v-model="queryParams.storeContent"/>
        </el-select>
      </el-form-item>
      <el-form-item label="题目类型" prop="storeType">
        <el-select clearable placeholder="请选择题目类型" size="small" v-model="queryParams.storeType">
          <el-option
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictLabel"
            v-for="dict in storeTypeOptions"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="所属专题" prop="subject">
        <el-select clearable placeholder="请选择所属专题" size="small" v-model="queryParams.subject">
          <el-option
            :key="subject.subjectId"
            :label="subject.subjectName"
            :value="subject.subjectName"
            v-for="subject in storeSubjectOptions"
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
          v-hasPermi="['question:store:add']"
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
          v-hasPermi="['question:store:edit']"
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
          v-hasPermi="['question:store:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          @click="handleImport"
          icon="el-icon-upload2"
          plain
          size="mini"
          type="info"
          v-hasPermi="['question:store:import']"
        >导入
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          @click="handleExport"
          icon="el-icon-download"
          plain
          size="mini"
          type="warning"
          v-hasPermi="['question:store:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table :data="storeList" @selection-change="handleSelectionChange" v-loading="loading">
      <el-table-column align="center" type="selection" width="55"/>
      <el-table-column align="left" label="题目编号" prop="storeId"/>
      <el-table-column align="left" label="题目类型" prop="storeType"/>
      <el-table-column align="center" label="题目内容" prop="storeContent"/>
      <el-table-column align="center" label="题目答案" prop="answer"/>
      <el-table-column align="center" label="题目解析" prop="parsing"/>
      <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
        <template slot-scope="scope">
          <el-button
            @click="handleUpdate(scope.row)"
            icon="el-icon-edit"
            size="mini"
            type="text"
            v-hasPermi="['question:store:edit']"
          >修改
          </el-button>
          <el-button
            @click="handleDelete(scope.row)"
            icon="el-icon-delete"
            size="mini"
            type="text"
            v-hasPermi="['question:store:remove']"
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

    <!-- 添加或修改题库数据对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
      <el-form :model="form" :rules="rules" label-width="80px" ref="form">
        <el-form-item label="题目类型" prop="storeType">
          <el-select placeholder="请选择题目类型" v-model="form.storeType">
            <el-option
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictLabel"
              v-for="dict in storeTypeOptions"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="所属专题" prop="subject">
          <el-select placeholder="请选择所属专题" v-model="form.subject">
            <el-option
              :key="subject.subjectId"
              :label="subject.subjectName"
              :value="subject.subjectName"
              v-for="subject in storeSubjectOptions"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="题目内容" prop="storeContent">
          <el-input placeholder="请输入内容" type="textarea" v-model="form.storeContent"/>
        </el-form-item>
        <el-form-item label="题目答案" prop="answer">
          <el-input placeholder="请输入题目答案" type="textarea" v-model="form.answer"/>
        </el-form-item>
        <el-form-item label="题目解析" prop="parsing">
          <el-input placeholder="请输入题目解析" type="textarea" v-model="form.parsing"/>
        </el-form-item>
        <el-form-item label="题目选项">
        </el-form-item>
        <el-form-item label="选项A" prop="selectionA">
          <el-input placeholder="请输入选项A" v-model="form.selectionA"/>
        </el-form-item>
        <el-form-item label="选项B" prop="selectionB">
          <el-input placeholder="请输入选项B" v-model="form.selectionB"/>
        </el-form-item>
        <el-form-item label="选项C" prop="selectionC">
          <el-input placeholder="请输入选项C" v-model="form.selectionC"/>
        </el-form-item>
        <el-form-item label="选项D" prop="selectionD">
          <el-input placeholder="请输入选项D" v-model="form.selectionD"/>
        </el-form-item>
      </el-form>
      <div class="dialog-footer" slot="footer">
        <el-button @click="submitForm" type="primary">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 用户导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" append-to-body width="400px">
      <el-upload
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :auto-upload="false"
        :disabled="upload.isUploading"
        :headers="upload.headers"
        :limit="1"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        accept=".xlsx, .xls"
        drag
        ref="upload"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">
          将文件拖到此处，或
          <em>点击上传</em>
        </div>
        <div class="el-upload__tip" slot="tip">
          <el-checkbox v-model="upload.updateSupport"/>
          是否更新已经存在的用户数据
          <el-link @click="importTemplate" style="font-size:12px" type="info">下载模板</el-link>
        </div>
        <div class="el-upload__tip" slot="tip" style="color:red">提示：仅允许导入“xls”或“xlsx”格式文件！</div>
      </el-upload>
      <div class="dialog-footer" slot="footer">
        <el-button @click="submitFileForm" type="primary">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {addStore, delStore, getStore, listStore, updateStore} from "@/api/question/store";
  import {listSubject} from "@/api/question/subject";
  import {getToken} from "@/utils/auth";

  export default {
    name: "Store",
    components: {},
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
        // 题库数据表格数据
        storeList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 题目类型字典
        storeTypeOptions: [],
        // 题目专题字典
        storeSubjectOptions: [],
        // 用户导入参数
        upload: {
          // 是否显示弹出层（用户导入）
          open: false,
          // 弹出层标题（用户导入）
          title: "",
          // 是否禁用上传
          isUploading: false,
          // 是否更新已经存在的用户数据
          updateSupport: 0,
          // 设置上传的请求头部
          headers: {Authorization: "Bearer " + getToken()},
          // 上传的地址
          url: process.env.VUE_APP_BASE_API + "question/store/importData"
        },
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          storeType: null,
          storeContent: null,
          subject: null,
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
      this.getDicts("que_store_type").then(response => {
        this.storeTypeOptions = response.data;
      });
    },
    methods: {
      /** 查询题库数据列表 */
      getList() {
        this.loading = true;
        listStore(this.queryParams).then(response => {
          this.storeList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 查询专题字典
      getSubject() {
        listSubject(this.queryParams2).then(response => {
          this.storeSubjectOptions = response.rows;
        });
      },
      // 题目类型字典翻译
      storeTypeFormat(row, column) {
        return this.selectDictLabel(this.storeTypeOptions, row.storeType);
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          storeId: null,
          storeType: null,
          storeContent: null,
          storeDetail: null,
          answer: null,
          parsing: null,
          selectionA: null,
          selectionB: null,
          selectionC: null,
          selectionD: null,
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
        this.ids = selection.map(item => item.storeId)
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加题库数据";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const storeId = row.storeId || this.ids
        getStore(storeId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改题库数据";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.storeId != null) {
              updateStore(this.form).then(response => {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addStore(this.form).then(response => {
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
        const storeIds = row.storeId || this.ids;
        this.$confirm('是否确认删除题库数据编号为"' + storeIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delStore(storeIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('question/store/export', {
          ...this.queryParams
        }, `题库Export_${new Date().getTime()}.xlsx`)
      },
      /** 导入按钮操作 */
      handleImport() {
        this.upload.title = "用户导入";
        this.upload.open = true;
      },
      /** 下载模板操作 */
      importTemplate() {
        this.download('question/store/importTemplate', {
          ...this.queryParams
        }, `题库模板Template_${new Date().getTime()}.xlsx`)
      },
      // 文件上传中处理
      handleFileUploadProgress(event, file, fileList) {
        this.upload.isUploading = true;
      },
      // 文件上传成功处理
      handleFileSuccess(response, file, fileList) {
        this.upload.open = false;
        this.upload.isUploading = false;
        this.$refs.upload.clearFiles();
        this.$alert(response.msg, "导入结果", {dangerouslyUseHTMLString: true});
        this.getList();
      },
      // 提交上传文件
      submitFileForm() {
        this.$refs.upload.submit();
      }
    }
  };
</script>
