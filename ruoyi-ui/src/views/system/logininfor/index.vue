<template>
  <div class="app-container">
    <el-form :inline="true" :model="queryParams" label-width="68px" ref="queryForm"
             v-show="showSearch">
      <el-form-item label="登录地址" prop="ipaddr">
        <el-input
          @keyup.enter.native="handleQuery"
          clearable
          placeholder="请输入登录地址"
          size="small"
          style="width: 240px;"
          v-model="queryParams.ipaddr"
        />
      </el-form-item>
      <el-form-item label="用户名称" prop="userName">
        <el-input
          @keyup.enter.native="handleQuery"
          clearable
          placeholder="请输入用户名称"
          size="small"
          style="width: 240px;"
          v-model="queryParams.userName"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select
          clearable
          placeholder="登录状态"
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
      <el-form-item label="登录时间">
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
          v-hasPermi="['system:logininfor:remove']"
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
          v-hasPermi="['system:logininfor:remove']"
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
          v-hasPermi="['system:logininfor:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table :data="list" @selection-change="handleSelectionChange" v-loading="loading">
      <el-table-column align="center" type="selection" width="55"/>
      <el-table-column align="center" label="访问编号" prop="infoId"/>
      <el-table-column align="center" label="用户名称" prop="userName"/>
      <el-table-column :show-overflow-tooltip="true" align="center" label="地址" prop="ipaddr"
                       width="130"/>
      <el-table-column :formatter="statusFormat" align="center" label="状态" prop="status"/>
      <el-table-column align="center" label="描述" prop="msg"/>
      <el-table-column align="center" label="访问时间" prop="accessTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.accessTime) }}</span>
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
  </div>
</template>

<script>
  import {cleanLogininfor, delLogininfor, list} from "@/api/system/logininfor";

  export default {
    name: "Logininfor",
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
        // 表格数据
        list: [],
        // 状态数据字典
        statusOptions: [],
        // 日期范围
        dateRange: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          ipaddr: undefined,
          userName: undefined,
          status: undefined
        }
      };
    },
    created() {
      this.getList();
      this.getDicts("sys_common_status").then(response => {
        this.statusOptions = response.data;
      });
    },
    methods: {
      /** 查询登录日志列表 */
      getList() {
        this.loading = true;
        list(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
            this.list = response.rows;
            this.total = response.total;
            this.loading = false;
          }
        );
      },
      // 登录状态字典翻译
      statusFormat(row, column) {
        return this.selectDictLabel(this.statusOptions, row.status);
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
        this.ids = selection.map(item => item.infoId)
        this.multiple = !selection.length
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const infoIds = row.infoId || this.ids;
        this.$confirm('是否确认删除访问编号为"' + infoIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delLogininfor(infoIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
      },
      /** 清空按钮操作 */
      handleClean() {
        this.$confirm('是否确认清空所有登录日志数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return cleanLogininfor();
        }).then(() => {
          this.getList();
          this.msgSuccess("清空成功");
        })
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('system/logininfor/export', {
          ...this.queryParams
        }, `logininfor_${new Date().getTime()}.xlsx`)
      }
    }
  };
</script>

