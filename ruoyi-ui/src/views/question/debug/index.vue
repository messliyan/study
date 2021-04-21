<template>
  <div class="app-container">
    <el-form :inline="true" :model="queryParams" label-width="68px" ref="queryForm"
             v-show="showSearch">
      <el-form-item label="纠错状态" prop="debugStatus">
        <el-select clearable placeholder="请选择纠错状态" size="small" v-model="queryParams.debugStatus">
          <el-option
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictLabel"
            v-for="dict in debugStatusOptions"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery" icon="el-icon-search" size="mini" type="primary">搜索
        </el-button>
        <el-button @click="resetQuery" icon="el-icon-refresh" size="mini">重置</el-button>
      </el-form-item>
    </el-form>


    <el-table :data="debugList" @selection-change="handleSelectionChange" v-loading="loading">
      <el-table-column align="center" type="selection" width="55"/>
      <el-table-column align="center" label="纠错管理编号" prop="debugId"/>
      <el-table-column align="center" label="纠错人" prop="debugMan"/>
      <el-table-column align="center" label="纠错题目" prop="storeName"/>
      <el-table-column align="center" label="纠错描述" prop="detail"/>
      <el-table-column align="center" label="错误类型" prop="type"/>
      <el-table-column align="center" label="纠错状态" prop="debugStatus"/>
      <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
        <template slot-scope="scope">
          <el-button
            @click="handleUpdate(scope.row)"
            icon="el-icon-edit"
            size="mini"
            type="text"
            v-hasPermi="['question:debug:edit']"
          >纠错
          </el-button>
          <el-button
            @click="handleDelete(scope.row)"
            icon="el-icon-delete"
            size="mini"
            type="text"
            v-hasPermi="['question:debug:remove']"
          >完成
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
      <el-form :model="store" :rules="rules" label-width="80px" ref="store">
        <el-form-item label="题目类型" prop="storeType">
          <el-select placeholder="请选择题目类型" v-model="store.storeType">
            <el-option
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictLabel"
              v-for="dict in storeTypeOptions"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="所属专题" prop="subject">
          <el-select placeholder="请选择所属专题" v-model="store.subject">
            <el-option
              :key="subject.subjectId"
              :label="subject.subjectName"
              :value="subject.subjectName"
              v-for="subject in storeSubjectOptions"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="题目内容" prop="storeContent">
          <el-input placeholder="请输入内容" type="textarea" v-model="store.storeContent"/>
        </el-form-item>
        <el-form-item label="题目答案" prop="answer">
          <el-input placeholder="请输入题目答案" type="textarea" v-model="store.answer"/>
        </el-form-item>
        <el-form-item label="题目解析" prop="parsing">
          <el-input placeholder="请输入题目解析" type="textarea" v-model="store.parsing"/>
        </el-form-item>
        <el-form-item label="题目选项">
        </el-form-item>
        <el-form-item label="选项A" prop="selectionA">
          <el-input placeholder="请输入选项A" v-model="store.selectionA"/>
        </el-form-item>
        <el-form-item label="选项B" prop="selectionB">
          <el-input placeholder="请输入选项B" v-model="store.selectionB"/>
        </el-form-item>
        <el-form-item label="选项C" prop="selectionC">
          <el-input placeholder="请输入选项C" v-model="store.selectionC"/>
        </el-form-item>
        <el-form-item label="选项D" prop="selectionD">
          <el-input placeholder="请输入选项D" v-model="store.selectionD"/>
        </el-form-item>
      </el-form>
      <div class="dialog-footer" slot="footer">
        <el-button @click="submitStore" type="primary">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {debug, delDebug, listDebug} from "@/api/question/debug";
  import {getStore, updateStore} from "@/api/question/store";
  import {listSubject} from "@/api/question/subject";

  export default {
    name: "Debug",
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
        rules: {}
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
        this.single = selection.length !== 1
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
          this.store.debugId = row.debugId || this.ids
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
        }).then(function () {
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
        }).then(function () {
          return debug(debugIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("纠错成功");
        })
      }
    }
  };
</script>
