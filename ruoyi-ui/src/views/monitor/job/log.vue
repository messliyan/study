<template>
  <div class="app-container">
    <el-form :inline="true" :model="queryParams" label-width="68px" ref="queryForm"
             v-show="showSearch">
      <el-form-item label="任务名称" prop="jobName">
        <el-input
          @keyup.enter.native="handleQuery"
          clearable
          placeholder="请输入任务名称"
          size="small"
          style="width: 240px"
          v-model="queryParams.jobName"
        />
      </el-form-item>
      <el-form-item label="任务组名" prop="jobGroup">
        <el-select
          clearable
          placeholder="请任务组名"
          size="small"
          style="width: 240px"
          v-model="queryParams.jobGroup"
        >
          <el-option
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
            v-for="dict in jobGroupOptions"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="执行状态" prop="status">
        <el-select
          clearable
          placeholder="请选择执行状态"
          size="small"
          style="width: 240px"
          v-model="queryParams.status"
        >
          <el-option
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
            v-for="dict in statusOptions"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="执行时间">
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
          :disabled="multiple"
          @click="handleDelete"
          icon="el-icon-delete"
          plain
          size="mini"
          type="danger"
          v-hasPermi="['monitor:job:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          @click="handleClean"
          icon="el-icon-delete"
          plain
          size="mini"
          type="danger"
          v-hasPermi="['monitor:job:remove']"
        >清空
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          @click="handleExport"
          icon="el-icon-download"
          plain
          size="mini"
          type="warning"
          v-hasPermi="['monitor:job:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table :data="jobLogList" @selection-change="handleSelectionChange" v-loading="loading">
      <el-table-column align="center" type="selection" width="55"/>
      <el-table-column align="center" label="日志编号" prop="jobLogId" width="80"/>
      <el-table-column :show-overflow-tooltip="true" align="center" label="任务名称" prop="jobName"/>
      <el-table-column :formatter="jobGroupFormat" :show-overflow-tooltip="true" align="center" label="任务组名"
                       prop="jobGroup"/>
      <el-table-column :show-overflow-tooltip="true" align="center" label="调用目标字符串"
                       prop="invokeTarget"/>
      <el-table-column :show-overflow-tooltip="true" align="center" label="日志信息" prop="jobMessage"/>
      <el-table-column :formatter="statusFormat" align="center" label="执行状态" prop="status"/>
      <el-table-column align="center" label="执行时间" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
        <template slot-scope="scope">
          <el-button
            @click="handleView(scope.row)"
            icon="el-icon-view"
            size="mini"
            type="text"
            v-hasPermi="['monitor:job:query']"
          >详细
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

    <!-- 调度日志详细 -->
    <el-dialog :visible.sync="open" append-to-body title="调度日志详细" width="700px">
      <el-form :model="form" label-width="100px" ref="form" size="mini">
        <el-row>
          <el-col :span="12">
            <el-form-item label="日志序号：">{{ form.jobLogId }}</el-form-item>
            <el-form-item label="任务名称：">{{ form.jobName }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="任务分组：">{{ form.jobGroup }}</el-form-item>
            <el-form-item label="执行时间：">{{ form.createTime }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="调用方法：">{{ form.invokeTarget }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="日志信息：">{{ form.jobMessage }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="执行状态：">
              <div v-if="form.status == 0">正常</div>
              <div v-else-if="form.status == 1">失败</div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="异常信息：" v-if="form.status == 1">{{ form.exceptionInfo }}
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div class="dialog-footer" slot="footer">
        <el-button @click="open = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {cleanJobLog, delJobLog, listJobLog} from "@/api/monitor/jobLog";

  export default {
    name: "JobLog",
    data() {
      return {
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        // 调度日志表格数据
        jobLogList: [],
        // 是否显示弹出层
        open: false,
        // 日期范围
        dateRange: [],
        // 表单参数
        form: {},
        // 执行状态字典
        statusOptions: [],
        // 任务组名字典
        jobGroupOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          jobName: undefined,
          jobGroup: undefined,
          status: undefined
        }
      };
    },
    created() {
      this.getList();
      this.getDicts("sys_job_status").then(response => {
        this.statusOptions = response.data;
      });
      this.getDicts("sys_job_group").then(response => {
        this.jobGroupOptions = response.data;
      });
    },
    methods: {
      /** 查询调度日志列表 */
      getList() {
        this.loading = true;
        listJobLog(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
            this.jobLogList = response.rows;
            this.total = response.total;
            this.loading = false;
          }
        );
      },
      // 执行状态字典翻译
      statusFormat(row, column) {
        return this.selectDictLabel(this.statusOptions, row.status);
      },
      // 任务组名字典翻译
      jobGroupFormat(row, column) {
        return this.selectDictLabel(this.jobGroupOptions, row.jobGroup);
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
        this.ids = selection.map(item => item.jobLogId);
        this.multiple = !selection.length;
      },
      /** 详细按钮操作 */
      handleView(row) {
        this.open = true;
        this.form = row;
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const jobLogIds = this.ids;
        this.$confirm('是否确认删除调度日志编号为"' + jobLogIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delJobLog(jobLogIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
      },
      /** 清空按钮操作 */
      handleClean() {
        this.$confirm("是否确认清空所有调度日志数据项?", "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return cleanJobLog();
        }).then(() => {
          this.getList();
          this.msgSuccess("清空成功");
        })
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('schedule/job/log/export', {
          ...this.queryParams
        }, `log_${new Date().getTime()}.xlsx`)
      }
    }
  };
</script>
