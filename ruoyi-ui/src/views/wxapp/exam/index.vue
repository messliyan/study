<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="考试名称" prop="examName">
        <el-input
          v-model="queryParams.examName"
          placeholder="请输入考试名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="考试状态" prop="examStatus">
        <el-select v-model="queryParams.examStatus" placeholder="请选择考试状态" clearable size="small">
          <el-option
            v-for="dict in examStatusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictLabel"
          />
        </el-select>
      </el-form-item>
     <el-form-item label="创建时间">
        <el-date-picker
          v-model="dateRange"
          size="small"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['wxapp:exam:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['wxapp:exam:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['wxapp:exam:remove']"
        >删除</el-button>
      </el-col>
  
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="examList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="考试编号" align="center" prop="examId" />
      <el-table-column label="考试名称" align="center" prop="examName" />
      <el-table-column label="考试状态" align="center" prop="examStatus" />
      <el-table-column label="考试范围" width="280" align="center"  >
       <template slot-scope="scope"  >
          <span v-for="subject in examSubjectOptions" :key="subject.subjectId">
             <span v-for="subjectId in scope.row.subjectIds" :key="subjectId">
              <span v-if="subjectId == subject.subjectId">{{subject.subjectName}}<br/></span>
             </span>
          </span>
        </template>
      </el-table-column>
      <el-table-column label="考题总数" align="center" prop="total" />
      <el-table-column label="通过题数" align="center" prop="passes" />
      <el-table-column label="考试时长" align="center" prop="duration" />
      <el-table-column label="考试人数" align="center" prop="candidate" >
         <template slot-scope="scope"  >
             <span>{{scope.row.candidate.length}}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="255" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-if="scope.row.examStatus == '未开始'"
            size="mini"
            type="text"
            icon="el-icon-refresh"
            @click="handleDelete(scope.row)"
            v-hasPermi="['wxapp:exam:edit']"
          >考试设置</el-button>
          <el-button v-if="scope.row.examStatus == '未开始'"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['wxapp:exam:edit']"
          >修改</el-button>
          <el-button v-if="scope.row.examStatus == '未开始'"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['wxapp:exam:remove']"
          >删除</el-button>
           <el-button v-if="scope.row.examStatus == '已结束'"
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleDelete(scope.row)"
            v-hasPermi="['wxapp:exam:edit']"
          >查看考生报表</el-button>
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

    <!-- 添加或修改考试设置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="考试名称" prop="examName">
          <el-input v-model="form.examName" placeholder="请输入考试名称" />
        </el-form-item>
         <el-form-item label="考试范围">
              <el-select v-model="form.subjectIds" multiple placeholder="请选择">
                <el-option
                  v-for="subject in examSubjectOptions"
                  :key="subject.subjectId"
                  :label="subject.subjectName"
                  :value="subject.subjectId"
                  :disabled="subject.status == 1"
                ></el-option>
              </el-select>
         </el-form-item>
        <el-form-item label="考题总数" prop="total">
          <el-input-number v-model="form.total" placeholder="请输入考题总数"></el-input-number>
        </el-form-item>
        <el-form-item label="通过题数" prop="passes">
          <el-input-number v-model="form.passes" placeholder="请输入通过题数"></el-input-number>
        </el-form-item>
        <el-form-item label="考试人员">
           <el-checkbox v-model="deptExpand" @change="handleCheckedTreeExpand($event, 'dept')">展开/折叠</el-checkbox>
          <el-checkbox v-model="deptNodeAll" @change="handleCheckedTreeNodeAll($event, 'dept')">全选/全不选</el-checkbox>
          <el-checkbox v-model="form.deptCheckStrictly" @change="handleCheckedTreeConnect($event, 'dept')">父子联动</el-checkbox>
          <el-tree
            class="tree-border"
            :data="deptOptions"
            show-checkbox
            default-expand-all
            ref="dept"
            node-key="id"
            :check-strictly="!form.deptCheckStrictly"
            empty-text="加载中，请稍后"
            :props="defaultProps"
          ></el-tree>
        </el-form-item>
       <el-form-item label="考试时长" prop="duration">
          <el-input-number v-model="form.duration" placeholder="请输入考试时长"></el-input-number>
        </el-form-item>
        <el-form-item label="日期范围" prop="dateRange">
          <el-date-picker type="datetimerange" v-model="dateRange2" format="yyyy-MM-dd HH:mm"
            value-format="yyyy-MM-dd HH:mm" :style="{width: '100%'}" start-placeholder="开始日期"
            end-placeholder="结束日期" range-separator="至" clearable>
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listExam, getExam, delExam, addExam, updateExam } from "@/api/wxapp/exam";
import { listSubject } from "@/api/question/subject";
import { treeselect as deptTreeselect, roleDeptTreeselect } from "@/api/system/dept";
export default {
  name: "Exam",
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
      // 考试设置表格数据
      examList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
       // 日期范围
      dateRange: [],
      dateRange2: [],
      // 专题选项
      examSubjectOptions: [],
      // 考试状态字典
      examStatusOptions: [],
       // 是否显示弹出层（数据权限）
      deptExpand: true,
      deptNodeAll: false,

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        examName: null,
        examStatus: null,
        examStart: null,
        examEnd: null,
      },
       // 部门列表
      deptOptions: [],
       // 字典查询参数
      queryParams2: {
        pageNum: 1,
        pageSize: 10000
      },
      // 表单参数
      form: {
      },
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
    this.getSubject();
    this.getDicts("wxapp_exam_status").then(response => {
      this.examStatusOptions = response.data;
    });
  },
  methods: {
    /** 查询考试设置列表 */
    getList() {
      this.loading = true;
      listExam(this.addDateRange(this.queryParams, this.dateRange)).then(
        response => {
        this.examList = response.rows;
        this.total = response.total;
        this.loading = false;
        }
      );
    },
    /** 查询部门树结构 */
    getDeptTreeselect() {
      deptTreeselect().then(response => {
        this.deptOptions = response.data;
      });
    },
    // 所有部门节点数据
    getDeptAllCheckedKeys() {
      // 目前被选中的部门节点
      let checkedKeys = this.$refs.dept.getCheckedKeys();
      // 半选中的部门节点
      let halfCheckedKeys = this.$refs.dept.getHalfCheckedKeys();
      checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
      return checkedKeys;
    },
    /** 根据角色ID查询部门树结构 */
    getRoleDeptTreeselect(roleId) {
      return roleDeptTreeselect(roleId).then(response => {
        this.deptOptions = response.depts;
        return response;
      });
    },
    // 查询专题字典
    getSubject() {
      listSubject(this.queryParams2).then(response => {
        this.examSubjectOptions = response.rows;
      });
    },
    // 考试状态字典翻译
    examStatusFormat(row, column) {
      return this.selectDictLabel(this.examStatusOptions, row.examStatus);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.deptExpand = true,
      this.deptNodeAll = false,
      this.form = {
        examId: null,
        examName: null,
        examStatus: null,
        subjectIds: [],
        total: null,
        passes: null,
        duration: null,
        candidate: null,
        examStart: null,
        examEnd: null,
        deleteStatus: 0,
        createTime: null,
        updateTime: null,
        version: null,
        subjectIds: [],
        deptIds: [],
        deptCheckStrictly: true
      };
      this.dateRange2 = [];
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.examId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },

     // 树权限（展开/折叠）
    handleCheckedTreeExpand(value, type) {
        let treeList = this.deptOptions;
        for (let i = 0; i < treeList.length; i++) {
          this.$refs.dept.store.nodesMap[treeList[i].id].expanded = value;
        }
      
    },
    // 树权限（全选/全不选）
    handleCheckedTreeNodeAll(value, type) {
         if (type == 'dept') {
        this.$refs.dept.setCheckedNodes(value ? this.deptOptions: []);
       }
    },
    // 树权限（父子联动）
    handleCheckedTreeConnect(value, type) {
        this.form.deptCheckStrictly = value ? true: false;

    },


    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加考试设置";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const roleDeptTreeselect = this.getRoleDeptTreeselect(1);
      const examId = row.examId || this.ids
      getExam(examId).then(response => {
        this.form = response.data;
        this.dateRange2.push(this.form.examStart);
        this.dateRange2.push(this.form.examEnd);
        this.open = true;
         this.$nextTick(() => {
          roleDeptTreeselect.then(res => {
            this.$refs.dept.setCheckedKeys(res.checkedKeys);
          });
        });
        this.title = "修改考试设置";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.examId != null) {
            updateExam(this.addDateRange(this.form, this.dateRange2)).then(
            response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addExam(this.form).then(response => {
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
      const examIds = row.examId || this.ids;
      this.$confirm('是否确认删除考试设置编号为"' + examIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delExam(examIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 考生设置操作 */
    handleCandidate(row) {
        this.reset();
        const examId = row.examId || this.ids
      getExam(examId).then(response => {
        this.form = response.data;
        this.dateRange2.push(this.form.examStart);
        this.dateRange2.push(this.form.examEnd);
        this.open = true;
        this.title = "考试设置";
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('wxapp/exam/export', {
        ...this.queryParams
      }, `wxapp_exam.xlsx`)
    }
  }
};
</script>