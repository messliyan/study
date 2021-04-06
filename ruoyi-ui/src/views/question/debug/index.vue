<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="纠错状态" prop="debugStatus">
        <el-select v-model="queryParams.debugStatus" placeholder="请选择纠错状态" clearable size="small">
           <el-option
            v-for="dict in debugStatusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictLabel"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    

    <el-table v-loading="loading" :data="debugList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="纠错管理编号" align="center" prop="debugId" />
      <el-table-column label="纠错人" align="center" prop="debugMan" />
      <el-table-column label="纠错题目" align="center" prop="storeName" />
      <el-table-column label="纠错描述" align="center" prop="detail" />
      <el-table-column label="错误类型" align="center" prop="type" />
      <el-table-column label="纠错状态" align="center" prop="debugStatus" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
       <template slot-scope="scope">
        <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['question:debug:edit']"
          >纠错</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['question:debug:remove']"
          >完成</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

  <!-- 添加或修改题库数据对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="store" :model="store" :rules="rules" label-width="80px">
        <el-form-item label="题目类型" prop="storeType">
          <el-select v-model="store.storeType" placeholder="请选择题目类型">
            <el-option
              v-for="dict in storeTypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictLabel"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="所属专题" prop="subject">
         <el-select v-model="store.subject" placeholder="请选择所属专题">
          <el-option
              v-for="subject in storeSubjectOptions"
              :key="subject.subjectId"
              :label="subject.subjectName"
              :value="subject.subjectName"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="题目内容" prop="storeContent">
          <el-input v-model="store.storeContent" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="题目答案" prop="answer">
          <el-input v-model="store.answer"  type="textarea" placeholder="请输入题目答案" />
        </el-form-item>
         <el-form-item label="题目解析" prop="parsing">
          <el-input v-model="store.parsing"  type="textarea" placeholder="请输入题目解析" />
        </el-form-item>
         <el-form-item label="题目选项" >
        </el-form-item>
        <el-form-item label="选项A" prop="selectionA">
          <el-input v-model="store.selectionA" placeholder="请输入选项A" />
        </el-form-item>
        <el-form-item label="选项B" prop="selectionB">
          <el-input v-model="store.selectionB" placeholder="请输入选项B" />
        </el-form-item>
        <el-form-item label="选项C" prop="selectionC">
          <el-input v-model="store.selectionC" placeholder="请输入选项C" />
        </el-form-item>  
         <el-form-item label="选项D" prop="selectionD">
          <el-input v-model="store.selectionD" placeholder="请输入选项D" />
        </el-form-item>  
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitStore">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listDebug, getDebug, delDebug, addDebug, updateDebug,debug } from "@/api/question/debug";
import {  getStore,  addStore, updateStore } from "@/api/question/store";
import { listSubject } from "@/api/question/subject";
export default {
  name: "Debug",
  components: {
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
      // 纠错管理表格数据
      debugList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 纠错状态字典
      debugStatusOptions: [],
       // 题目类型字典
      storeTypeOptions: [],
      // 题目专题字典
      storeSubjectOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        debugDetail: null,
        debugStatus: null,
      },
       // 字典查询参数
      queryParams2: {
        pageNum: 1,
        pageSize: 10000
      },
      // 表单参数
      form: {},
      //纠错题目单参数
      store: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
    this.getSubject();
     this.getDicts("que_debug_status").then(response => {
      this.debugStatusOptions = response.data;
    });
    this.getDicts("que_store_type").then(response => {
      this.storeTypeOptions = response.data;
    });
  },
  methods: {
    /** 查询纠错管理列表 */
    getList() {
      this.loading = true;
      listDebug(this.queryParams).then(response => {
        this.debugList = response.rows;
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
     // 纠错类型字典翻译
    materialTypeFormat(row, column) {
      return this.selectDictLabel(this.debugStatusOptions, row.materialType);
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
        debugId: null,
        debugMan: null,
        debugDetail: null,
        debugStatus: null,
        storeId: null,
        storeName: null,
        detail: null,
        type: null,
        deleteStatus: 0,
        createTime: null,
        updateTime: null,
        version: null
      };
      this.store = {
        debugId: null,
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
      this.ids = selection.map(item => item.debugId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加纠错管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const storeId = row.storeId 
      getStore(storeId).then(response => {
         this.store = response.data;
         this.store.debugId=row.debugId || this.ids
         this.open = true;
         this.title = "修改纠错管理";
      });
    },
    /** 提交按钮 */
    submitStore() {
      this.$refs["store"].validate(valid => {
        if (valid) {
            updateStore(this.store).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              debug(this.store.debugId);
              this.getList();
            });
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const debugIds = row.debugId || this.ids;
      this.$confirm('是否确认纠错管理编号为"' + debugIds + '"的数据项不再修改?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delDebug(debugIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("纠错成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('question/debug/export', {
        ...this.queryParams
      }, `question_debug.xlsx`)
    },
     /** 纠错按钮操作 */
    handleDebug(row) {
      const debugIds = row.debugId || this.ids;
      this.$confirm('是否确认纠错编号为"' + debugIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return debug(debugIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("纠错成功");
        })
    }
  }
};
</script>