<template>
  <div class="app-container">
    <el-form :inline="true" :model="queryParams" ref="queryForm" v-show="showSearch">
      <el-form-item label="角色名称" prop="roleName">
        <el-input
          @keyup.enter.native="handleQuery"
          clearable
          placeholder="请输入角色名称"
          size="small"
          style="width: 240px"
          v-model="queryParams.roleName"
        />
      </el-form-item>
      <el-form-item label="权限字符" prop="roleKey">
        <el-input
          @keyup.enter.native="handleQuery"
          clearable
          placeholder="请输入权限字符"
          size="small"
          style="width: 240px"
          v-model="queryParams.roleKey"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select
          clearable
          placeholder="角色状态"
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
          v-hasPermi="['system:role:add']"
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
          v-hasPermi="['system:role:edit']"
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
          v-hasPermi="['system:role:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          @click="handleExport"
          icon="el-icon-download"
          plain
          size="mini"
          type="warning"
          v-hasPermi="['system:role:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table :data="roleList" @selection-change="handleSelectionChange" v-loading="loading">
      <el-table-column align="center" type="selection" width="55"/>
      <el-table-column label="角色编号" prop="roleId" width="120"/>
      <el-table-column :show-overflow-tooltip="true" label="角色名称" prop="roleName" width="150"/>
      <el-table-column :show-overflow-tooltip="true" label="权限字符" prop="roleKey" width="150"/>
      <el-table-column label="显示顺序" prop="roleSort" width="100"/>
      <el-table-column align="center" label="状态" width="100">
        <template slot-scope="scope">
          <el-switch
            @change="handleStatusChange(scope.row)"
            active-value="0"
            inactive-value="1"
            v-model="scope.row.status"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column align="center" label="创建时间" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
        <template slot-scope="scope">
          <el-button
            @click="handleUpdate(scope.row)"
            icon="el-icon-edit"
            size="mini"
            type="text"
            v-hasPermi="['system:role:edit']"
          >修改
          </el-button>
          <el-button
            @click="handleDataScope(scope.row)"
            icon="el-icon-circle-check"
            size="mini"
            type="text"
            v-hasPermi="['system:role:edit']"
          >数据权限
          </el-button>
          <el-button
            @click="handleDelete(scope.row)"
            icon="el-icon-delete"
            size="mini"
            type="text"
            v-hasPermi="['system:role:remove']"
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

    <!-- 添加或修改角色配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
      <el-form :model="form" :rules="rules" label-width="80px" ref="form">
        <el-form-item label="角色名称" prop="roleName">
          <el-input placeholder="请输入角色名称" v-model="form.roleName"/>
        </el-form-item>
        <el-form-item label="权限字符" prop="roleKey">
          <el-input placeholder="请输入权限字符" v-model="form.roleKey"/>
        </el-form-item>
        <el-form-item label="角色顺序" prop="roleSort">
          <el-input-number :min="0" controls-position="right" v-model="form.roleSort"/>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio
              :key="dict.dictValue"
              :label="dict.dictValue"
              v-for="dict in statusOptions"
            >{{dict.dictLabel}}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="菜单权限">
          <el-checkbox @change="handleCheckedTreeExpand($event, 'menu')" v-model="menuExpand">
            展开/折叠
          </el-checkbox>
          <el-checkbox @change="handleCheckedTreeNodeAll($event, 'menu')" v-model="menuNodeAll">
            全选/全不选
          </el-checkbox>
          <el-checkbox @change="handleCheckedTreeConnect($event, 'menu')"
                       v-model="form.menuCheckStrictly">父子联动
          </el-checkbox>
          <el-tree
            :check-strictly="!form.menuCheckStrictly"
            :data="menuOptions"
            :props="defaultProps"
            class="tree-border"
            empty-text="加载中，请稍后"
            node-key="id"
            ref="menu"
            show-checkbox
          ></el-tree>
        </el-form-item>
        <el-form-item label="备注">
          <el-input placeholder="请输入内容" type="textarea" v-model="form.remark"></el-input>
        </el-form-item>
      </el-form>
      <div class="dialog-footer" slot="footer">
        <el-button @click="submitForm" type="primary">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 分配角色数据权限对话框 -->
    <el-dialog :title="title" :visible.sync="openDataScope" append-to-body width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="角色名称">
          <el-input :disabled="true" v-model="form.roleName"/>
        </el-form-item>
        <el-form-item label="权限字符">
          <el-input :disabled="true" v-model="form.roleKey"/>
        </el-form-item>
        <el-form-item label="权限范围">
          <el-select @change="dataScopeSelectChange" v-model="form.dataScope">
            <el-option
              :key="item.value"
              :label="item.label"
              :value="item.value"
              v-for="item in dataScopeOptions"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="数据权限" v-show="form.dataScope == 2">
          <el-checkbox @change="handleCheckedTreeExpand($event, 'dept')" v-model="deptExpand">
            展开/折叠
          </el-checkbox>
          <el-checkbox @change="handleCheckedTreeNodeAll($event, 'dept')" v-model="deptNodeAll">
            全选/全不选
          </el-checkbox>
          <el-checkbox @change="handleCheckedTreeConnect($event, 'dept')"
                       v-model="form.deptCheckStrictly">父子联动
          </el-checkbox>
          <el-tree
            :check-strictly="!form.deptCheckStrictly"
            :data="deptOptions"
            :props="defaultProps"
            class="tree-border"
            default-expand-all
            empty-text="加载中，请稍后"
            node-key="id"
            ref="dept"
            show-checkbox
          ></el-tree>
        </el-form-item>
      </el-form>
      <div class="dialog-footer" slot="footer">
        <el-button @click="submitDataScope" type="primary">确 定</el-button>
        <el-button @click="cancelDataScope">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    addRole,
    changeRoleStatus,
    dataScope,
    delRole,
    getRole,
    listRole,
    updateRole
  } from "@/api/system/role";
  import {roleMenuTreeselect, treeselect as menuTreeselect} from "@/api/system/menu";
  import {roleDeptTreeselect, treeselect as deptTreeselect} from "@/api/system/dept";

  export default {
    name: "Role",
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
        // 角色表格数据
        roleList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 是否显示弹出层（数据权限）
        openDataScope: false,
        menuExpand: false,
        menuNodeAll: false,
        deptExpand: true,
        deptNodeAll: false,
        // 日期范围
        dateRange: [],
        // 状态数据字典
        statusOptions: [],
        // 数据范围选项
        dataScopeOptions: [
          {
            value: "1",
            label: "全部数据权限"
          },
          {
            value: "2",
            label: "自定数据权限"
          },
          {
            value: "3",
            label: "本部门数据权限"
          },
          {
            value: "4",
            label: "本部门及以下数据权限"
          },
          {
            value: "5",
            label: "仅本人数据权限"
          }
        ],
        // 菜单列表
        menuOptions: [],
        // 部门列表
        deptOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          roleName: undefined,
          roleKey: undefined,
          status: undefined
        },
        // 表单参数
        form: {},
        defaultProps: {
          children: "children",
          label: "label"
        },
        // 表单校验
        rules: {
          roleName: [
            {required: true, message: "角色名称不能为空", trigger: "blur"}
          ],
          roleKey: [
            {required: true, message: "权限字符不能为空", trigger: "blur"}
          ],
          roleSort: [
            {required: true, message: "角色顺序不能为空", trigger: "blur"}
          ]
        }
      };
    },
    created() {
      this.getList();
      this.getDicts("sys_normal_disable").then(response => {
        this.statusOptions = response.data;
      });
    },
    methods: {
      /** 查询角色列表 */
      getList() {
        this.loading = true;
        listRole(this.addDateRange(this.queryParams, this.dateRange)).then(
          response => {
            this.roleList = response.rows;
            this.total = response.total;
            this.loading = false;
          }
        );
      },
      /** 查询菜单树结构 */
      getMenuTreeselect() {
        menuTreeselect().then(response => {
          this.menuOptions = response.data;
        });
      },
      /** 查询部门树结构 */
      getDeptTreeselect() {
        deptTreeselect().then(response => {
          this.deptOptions = response.data;
        });
      },
      // 所有菜单节点数据
      getMenuAllCheckedKeys() {
        // 目前被选中的菜单节点
        let checkedKeys = this.$refs.menu.getCheckedKeys();
        // 半选中的菜单节点
        let halfCheckedKeys = this.$refs.menu.getHalfCheckedKeys();
        checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
        return checkedKeys;
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
      /** 根据角色ID查询菜单树结构 */
      getRoleMenuTreeselect(roleId) {
        return roleMenuTreeselect(roleId).then(response => {
          this.menuOptions = response.menus;
          return response;
        });
      },
      /** 根据角色ID查询部门树结构 */
      getRoleDeptTreeselect(roleId) {
        return roleDeptTreeselect(roleId).then(response => {
          this.deptOptions = response.depts;
          return response;
        });
      },
      // 角色状态修改
      handleStatusChange(row) {
        let text = row.status === "0" ? "启用" : "停用";
        this.$confirm('确认要"' + text + '""' + row.roleName + '"角色吗?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return changeRoleStatus(row.roleId, row.status);
        }).then(() => {
          this.msgSuccess(text + "成功");
        }).catch(function () {
          row.status = row.status === "0" ? "1" : "0";
        });
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 取消按钮（数据权限）
      cancelDataScope() {
        this.openDataScope = false;
        this.reset();
      },
      // 表单重置
      reset() {
        if (this.$refs.menu != undefined) {
          this.$refs.menu.setCheckedKeys([]);
        }
        this.menuExpand = false,
          this.menuNodeAll = false,
          this.deptExpand = true,
          this.deptNodeAll = false,
          this.form = {
            roleId: undefined,
            roleName: undefined,
            roleKey: undefined,
            roleSort: 0,
            status: "0",
            menuIds: [],
            deptIds: [],
            menuCheckStrictly: true,
            deptCheckStrictly: true,
            remark: undefined
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
        this.dateRange = [];
        this.resetForm("queryForm");
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.roleId)
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      // 树权限（展开/折叠）
      handleCheckedTreeExpand(value, type) {
        if (type == 'menu') {
          let treeList = this.menuOptions;
          for (let i = 0; i < treeList.length; i++) {
            this.$refs.menu.store.nodesMap[treeList[i].id].expanded = value;
          }
        } else if (type == 'dept') {
          let treeList = this.deptOptions;
          for (let i = 0; i < treeList.length; i++) {
            this.$refs.dept.store.nodesMap[treeList[i].id].expanded = value;
          }
        }
      },
      // 树权限（全选/全不选）
      handleCheckedTreeNodeAll(value, type) {
        if (type == 'menu') {
          this.$refs.menu.setCheckedNodes(value ? this.menuOptions : []);
        } else if (type == 'dept') {
          this.$refs.dept.setCheckedNodes(value ? this.deptOptions : []);
        }
      },
      // 树权限（父子联动）
      handleCheckedTreeConnect(value, type) {
        if (type == 'menu') {
          this.form.menuCheckStrictly = value ? true : false;
        } else if (type == 'dept') {
          this.form.deptCheckStrictly = value ? true : false;
        }
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.getMenuTreeselect();
        this.open = true;
        this.title = "添加角色";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const roleId = row.roleId || this.ids
        const roleMenu = this.getRoleMenuTreeselect(roleId);
        getRole(roleId).then(response => {
          this.form = response.data;
          this.open = true;
          this.$nextTick(() => {
            roleMenu.then(res => {
              let checkedKeys = res.checkedKeys
              checkedKeys.forEach((v) => {
                this.$nextTick(() => {
                  this.$refs.menu.setChecked(v, true, false);
                })
              })
            });
          });
          this.title = "修改角色";
        });
      },
      /** 选择角色权限范围触发 */
      dataScopeSelectChange(value) {
        if (value !== '2') {
          this.$refs.dept.setCheckedKeys([]);
        }
      },
      /** 分配数据权限操作 */
      handleDataScope(row) {
        this.reset();
        const roleDeptTreeselect = this.getRoleDeptTreeselect(row.roleId);
        getRole(row.roleId).then(response => {
          this.form = response.data;
          this.openDataScope = true;
          this.$nextTick(() => {
            roleDeptTreeselect.then(res => {
              this.$refs.dept.setCheckedKeys(res.checkedKeys);
            });
          });
          this.title = "分配数据权限";
        });
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.roleId != undefined) {
              this.form.menuIds = this.getMenuAllCheckedKeys();
              updateRole(this.form).then(response => {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              this.form.menuIds = this.getMenuAllCheckedKeys();
              addRole(this.form).then(response => {
                this.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              });
            }
          }
        });
      },
      /** 提交按钮（数据权限） */
      submitDataScope: function () {
        if (this.form.roleId != undefined) {
          this.form.deptIds = this.getDeptAllCheckedKeys();
          dataScope(this.form).then(response => {
            this.msgSuccess("修改成功");
            this.openDataScope = false;
            this.getList();
          });
        }
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const roleIds = row.roleId || this.ids;
        this.$confirm('是否确认删除角色编号为"' + roleIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delRole(roleIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('system/role/export', {
          ...this.queryParams
        }, `role_${new Date().getTime()}.xlsx`)
      }
    }
  };
</script>
