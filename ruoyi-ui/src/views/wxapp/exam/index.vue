<template>
  <div class="app-container">
    <el-form :inline="true" :model="queryParams" label-width="68px" ref="queryForm"
             v-show="showSearch">
      <el-form-item label="考试名称" prop="examName">
        <el-input
          @keyup.enter.native="handleQuery"
          clearable
          placeholder="请输入考试名称"
          size="small"
          v-model="queryParams.examName"
        />
      </el-form-item>
      <el-form-item label="考试状态" prop="examStatus">
        <el-select clearable placeholder="请选择考试状态" size="small" v-model="queryParams.examStatus">
          <el-option
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictLabel"
            v-for="dict in examStatusOptions"
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
          v-hasPermi="['wxapp:exam:add']"
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
          v-hasPermi="['wxapp:exam:edit']"
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
          v-hasPermi="['wxapp:exam:remove']"
        >删除
        </el-button>
      </el-col>

      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table :data="examList" @selection-change="handleSelectionChange" v-loading="loading">
      <el-table-column align="center" type="selection" width="55"/>
      <el-table-column align="center" label="考试编号" prop="examId"/>
      <el-table-column align="center" label="考试名称" prop="examName"/>
      <el-table-column align="center" label="考试状态" prop="examStatus"/>
      <el-table-column align="center" label="考试范围" width="280">
        <template slot-scope="scope">
          <span :key="subject.subjectId" v-for="subject in examSubjectOptions">
             <span :key="subjectId" v-for="subjectId in scope.row.subjectIds">
              <span v-if="subjectId == subject.subjectId">{{subject.subjectName}}<br/></span>
             </span>
          </span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="考题总数" prop="total"/>
      <el-table-column align="center" label="通过题数" prop="passes"/>
      <el-table-column align="center" label="考试时长" prop="duration"/>
      <el-table-column align="center" label="考试人数" prop="candidateIds">
        <template slot-scope="scope">
          <span>{{scope.row.candidateIds.length}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" class-name="small-padding fixed-width" label="操作" width="255">
        <template slot-scope="scope">
          <!-- <el-button v-if="scope.row.examStatus == '未开始'"
            size="mini"
            type="text"
            icon="el-icon-refresh"
            @click="handleDelete(scope.row)"
            v-hasPermi="['wxapp:exam:edit']"
          >考试设置</el-button> -->
          <el-button @click="handleUpdate(scope.row)"
                     icon="el-icon-edit"
                     size="mini"
                     type="text"
                     v-hasPermi="['wxapp:exam:edit']"
                     v-if="scope.row.examStatus == '未开始'"
          >修改
          </el-button>
          <el-button @click="handleDelete(scope.row)"
                     icon="el-icon-delete"
                     size="mini"
                     type="text"
                     v-hasPermi="['wxapp:exam:remove']"
                     v-if="scope.row.examStatus == '未开始'"
          >删除
          </el-button>
          <el-button @click="handleDelete(scope.row)"
                     icon="el-icon-view"
                     size="mini"
                     type="text"
                     v-hasPermi="['wxapp:exam:edit']"
                     v-if="scope.row.examStatus == '已结束'"
          >查看考生报表
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

    <!-- 添加或修改考试设置对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
      <el-form :model="form" :rules="rules" label-width="80px" ref="form">
        <el-form-item label="考试名称" prop="examName">
          <el-input placeholder="请输入考试名称" v-model="form.examName"/>
        </el-form-item>
        <el-form-item label="考试范围">
          <el-select multiple placeholder="请选择" v-model="form.subjectIds">
            <el-option
              :disabled="subject.status == 1"
              :key="subject.subjectId"
              :label="subject.subjectName"
              :value="subject.subjectId"
              v-for="subject in examSubjectOptions"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="考题总数" prop="total">
          <el-input-number placeholder="请输入考题总数" v-model="form.total"></el-input-number>
        </el-form-item>
        <el-form-item label="通过题数" prop="passes">
          <el-input-number placeholder="请输入通过题数" v-model="form.passes"></el-input-number>
        </el-form-item>
        <el-form-item label="考试人员">
          <!-- <el-checkbox v-model="deptExpand" @change="handleCheckedTreeExpand($event, 'dept')">展开/折叠</el-checkbox>
          <el-checkbox v-model="deptNodeAll" @change="handleCheckedTreeNodeAll($event, 'dept')">全选/全不选</el-checkbox>
          <el-checkbox v-model="form.deptCheckStrictly" @change="handleCheckedTreeConnect($event, 'dept')">父子联动</el-checkbox> -->
          <el-tree
            :check-strictly="false"
            :data="deptOptions"
            :props="defaultProps"
            class="tree-border"
            empty-text="加载中，请稍后"
            node-key="id"
            ref="dept"
            show-checkbox
          ></el-tree>
        </el-form-item>
        <el-form-item label="考试时长" prop="duration">
          <el-time-picker :picker-options='{"selectableRange":"00:00:00-23:59:59"}' clearable format="HH:mm:ss"
                          placeholder="请输入考试时长"
                          v-model="form.duration" value-format="HH:mm:ss">
          </el-time-picker>
        </el-form-item>
        <el-form-item label="日期范围" prop="dateRange">
          <el-date-picker clearable end-placeholder="结束日期" format="yyyy-MM-dd HH:mm"
                          range-separator="至" start-placeholder="开始日期"
                          type="datetimerange" v-model="dateRange2" value-format="yyyy-MM-dd HH:mm">
          </el-date-picker>
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
  import {addExam, delExam, getExam, listExam, updateExam} from "@/api/wxapp/exam";
  import {listSubject} from "@/api/question/subject";
  import {treeselectUser as deptTreeselect, treeselectUser} from "@/api/system/dept";

  export default {
    name: "Exam",
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
        form: {},
        defaultProps: {
          children: "children",
          label: "label"
        },
        // 表单校验
        rules: {}
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
      gettreeselectUser(roleId) {
        return treeselectUser(roleId).then(response => {
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
            examStart: null,
            examEnd: null,
            deleteStatus: 0,
            createTime: null,
            updateTime: null,
            version: null,
            subjectIds: [],
            candidateIds: [],
            deptCheckStrictly: false
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
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },

      //  // 树权限（展开/折叠）
      // handleCheckedTreeExpand(value, type) {
      //     let treeList = this.deptOptions;
      //     for (let i = 0; i < treeList.length; i++) {
      //       this.$refs.dept.store.nodesMap[treeList[i].id].expanded = value;
      //     }

      // },
      // // 树权限（全选/全不选）
      // handleCheckedTreeNodeAll(value, type) {
      //      if (type == 'dept') {
      //     this.$refs.dept.setCheckedNodes(value ? this.deptOptions: []);
      //    }
      // },
      // // 树权限（父子联动）
      // handleCheckedTreeConnect(value, type) {
      //     this.form.deptCheckStrictly = value ? true: false;

      // },

      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        const treeselectUser = this.gettreeselectUser(1);
        this.open = true;
        this.$nextTick(() => {
          treeselectUser.then(res => {
            this.$refs.dept.setCheckedKeys(res.checkedKeys);
          });
        });
        this.title = "添加考试设置";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const treeselectUser = this.gettreeselectUser(1);
        const examId = row.examId || this.ids
        getExam(examId).then(response => {
          this.form = response.data;
          this.dateRange2.push(this.form.examStart);
          this.dateRange2.push(this.form.examEnd);
          this.open = true;
          this.$nextTick(() => {
            treeselectUser.then(res => {
              this.$refs.dept.setCheckedKeys(res.checkedKeys);
            });
          });
          this.title = "修改考试设置";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {

          if (this.dateRange2 == undefined || this.dateRange2 == null) {
            this.msgError("日期不能为空");
          } else {
            if (valid) {
              if (this.form.examId != null) {
                this.form.candidateIds = this.getDeptAllCheckedKeys();
                updateExam(this.addDateRange(this.form, this.dateRange2)).then(
                  response => {
                    this.msgSuccess("修改成功");
                    this.open = false;
                    this.getList();
                  });
              } else {
                addExam(this.addDateRange(this.form, this.dateRange2)).then(response => {
                  this.msgSuccess("新增成功");
                  this.open = false;
                  this.getList();
                });
              }
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
        }).then(function () {
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
