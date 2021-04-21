<template>
  <!-- 导入表 -->
  <el-dialog :visible.sync="visible" append-to-body title="导入表" top="5vh" width="800px">
    <el-form :inline="true" :model="queryParams" ref="queryForm">
      <el-form-item label="表名称" prop="tableName">
        <el-input
          @keyup.enter.native="handleQuery"
          clearable
          placeholder="请输入表名称"
          size="small"
          v-model="queryParams.tableName"
        />
      </el-form-item>
      <el-form-item label="表描述" prop="tableComment">
        <el-input
          @keyup.enter.native="handleQuery"
          clearable
          placeholder="请输入表描述"
          size="small"
          v-model="queryParams.tableComment"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery" icon="el-icon-search" size="mini" type="primary">搜索
        </el-button>
        <el-button @click="resetQuery" icon="el-icon-refresh" size="mini">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row>
      <el-table :data="dbTableList" @row-click="clickRow" @selection-change="handleSelectionChange"
                height="260px" ref="table">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column :show-overflow-tooltip="true" label="表名称"
                         prop="tableName"></el-table-column>
        <el-table-column :show-overflow-tooltip="true" label="表描述"
                         prop="tableComment"></el-table-column>
        <el-table-column label="创建时间" prop="createTime"></el-table-column>
        <el-table-column label="更新时间" prop="updateTime"></el-table-column>
      </el-table>
      <pagination
        :limit.sync="queryParams.pageSize"
        :page.sync="queryParams.pageNum"
        :total="total"
        @pagination="getList"
        v-show="total>0"
      />
    </el-row>
    <div class="dialog-footer" slot="footer">
      <el-button @click="handleImportTable" type="primary">确 定</el-button>
      <el-button @click="visible = false">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import {importTable, listDbTable} from "@/api/tool/gen";

  export default {
    data() {
      return {
        // 遮罩层
        visible: false,
        // 选中数组值
        tables: [],
        // 总条数
        total: 0,
        // 表数据
        dbTableList: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          tableName: undefined,
          tableComment: undefined
        }
      };
    },
    methods: {
      // 显示弹框
      show() {
        this.getList();
        this.visible = true;
      },
      clickRow(row) {
        this.$refs.table.toggleRowSelection(row);
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.tables = selection.map(item => item.tableName);
      },
      // 查询表数据
      getList() {
        listDbTable(this.queryParams).then(res => {
          if (res.code === 200) {
            this.dbTableList = res.rows;
            this.total = res.total;
          }
        });
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
      /** 导入按钮操作 */
      handleImportTable() {
        importTable({tables: this.tables.join(",")}).then(res => {
          this.msgSuccess(res.msg);
          if (res.code === 200) {
            this.visible = false;
            this.$emit("ok");
          }
        });
      }
    }
  };
</script>
