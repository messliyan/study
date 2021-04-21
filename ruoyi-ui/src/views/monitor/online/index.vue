<template>
  <div class="app-container">
    <el-form :inline="true" :model="queryParams" label-width="68px" ref="queryForm">
      <el-form-item label="登录地址" prop="ipaddr">
        <el-input
          @keyup.enter.native="handleQuery"
          clearable
          placeholder="请输入登录地址"
          size="small"
          v-model="queryParams.ipaddr"
        />
      </el-form-item>
      <el-form-item label="用户名称" prop="userName">
        <el-input
          @keyup.enter.native="handleQuery"
          clearable
          placeholder="请输入用户名称"
          size="small"
          v-model="queryParams.userName"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery" icon="el-icon-search" size="mini" type="primary">搜索
        </el-button>
        <el-button @click="resetQuery" icon="el-icon-refresh" size="mini">重置</el-button>
      </el-form-item>

    </el-form>
    <el-table
      :data="list.slice((pageNum-1)*pageSize,pageNum*pageSize)"
      style="width: 100%;"
      v-loading="loading"
    >
      <el-table-column align="center" label="序号" type="index">
        <template slot-scope="scope">
          <span>{{(pageNum - 1) * pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column :show-overflow-tooltip="true" align="center" label="会话编号" prop="tokenId"/>
      <el-table-column :show-overflow-tooltip="true" align="center" label="登录名称" prop="userName"/>
      <el-table-column :show-overflow-tooltip="true" align="center" label="主机" prop="ipaddr"/>
      <el-table-column align="center" label="登录时间" prop="loginTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.loginTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
        <template slot-scope="scope">
          <el-button
            @click="handleForceLogout(scope.row)"
            icon="el-icon-delete"
            size="mini"
            type="text"
            v-hasPermi="['monitor:online:forceLogout']"
          >强退
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination :limit.sync="pageSize" :page.sync="pageNum" :total="total" v-show="total>0"/>
  </div>
</template>

<script>
  import {forceLogout, list} from "@/api/monitor/online";

  export default {
    name: "Online",
    data() {
      return {
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 表格数据
        list: [],
        pageNum: 1,
        pageSize: 10,
        // 查询参数
        queryParams: {
          ipaddr: undefined,
          userName: undefined
        }
      };
    },
    created() {
      this.getList();
    },
    methods: {
      /** 查询登录日志列表 */
      getList() {
        this.loading = true;
        list(this.queryParams).then(response => {
          this.list = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      /** 强退按钮操作 */
      handleForceLogout(row) {
        this.$confirm('是否确认强退名称为"' + row.userName + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return forceLogout(row.tokenId);
        }).then(() => {
          this.getList();
          this.msgSuccess("强退成功");
        })
      }
    }
  };
</script>

