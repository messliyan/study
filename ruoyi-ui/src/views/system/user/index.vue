<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--部门数据-->
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-input
            clearable
            placeholder="请输入部门名称"
            prefix-icon="el-icon-search"
            size="small"
            style="margin-bottom: 20px"
            v-model="deptName"
          />
        </div>
        <div class="head-container">
          <el-tree
            :data="deptOptions"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            :props="defaultProps"
            @node-click="handleNodeClick"
            default-expand-all
            ref="tree"
          />
        </div>
      </el-col>
      <!--用户数据-->
      <el-col :span="20" :xs="24">
        <el-form :inline="true" :model="queryParams" label-width="68px" ref="queryForm"
                 v-show="showSearch">
          <el-form-item label="用户名称" prop="userName">
            <el-input
              @keyup.enter.native="handleQuery"
              clearable
              placeholder="请输入用户名称"
              size="small"
              style="width: 240px"
              v-model="queryParams.userName"
            />
          </el-form-item>
          <el-form-item label="手机号码" prop="phonenumber">
            <el-input
              @keyup.enter.native="handleQuery"
              clearable
              placeholder="请输入手机号码"
              size="small"
              style="width: 240px"
              v-model="queryParams.phonenumber"
            />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select
              clearable
              placeholder="用户状态"
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
              v-hasPermi="['system:user:add']"
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
              v-hasPermi="['system:user:edit']"
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
              v-hasPermi="['system:user:remove']"
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
              v-hasPermi="['system:user:import']"
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
              v-hasPermi="['system:user:export']"
            >导出
            </el-button>
          </el-col>
          <right-toolbar :columns="columns" :showSearch.sync="showSearch"
                         @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table :data="userList" @selection-change="handleSelectionChange" v-loading="loading">
          <el-table-column align="center" type="selection" width="50"/>
          <el-table-column align="center" key="userId" label="用户编号" prop="userId"
                           v-if="columns[0].visible"/>
          <el-table-column :show-overflow-tooltip="true" align="center" key="userName" label="用户名称"
                           prop="userName" v-if="columns[1].visible"/>
          <el-table-column :show-overflow-tooltip="true" align="center" key="nickName" label="用户昵称"
                           prop="nickName" v-if="columns[2].visible"/>
          <el-table-column :show-overflow-tooltip="true" align="center" key="deptName" label="部门"
                           prop="dept.deptName" v-if="columns[3].visible"/>
          <el-table-column align="center" key="phonenumber" label="手机号码" prop="phonenumber"
                           v-if="columns[4].visible" width="120"/>
          <el-table-column align="center" key="status" label="状态" v-if="columns[5].visible">
            <template slot-scope="scope">
              <el-switch
                @change="handleStatusChange(scope.row)"
                active-value="0"
                inactive-value="1"
                v-model="scope.row.status"
              ></el-switch>
            </template>
          </el-table-column>
          <el-table-column align="center" label="创建时间" prop="createTime" v-if="columns[6].visible"
                           width="160">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            class-name="small-padding fixed-width"
            label="操作"
            width="160"
          >
            <template slot-scope="scope">
              <el-button
                @click="handleUpdate(scope.row)"
                icon="el-icon-edit"
                size="mini"
                type="text"
                v-hasPermi="['system:user:edit']"
              >修改
              </el-button>
              <el-button
                @click="handleDelete(scope.row)"
                icon="el-icon-delete"
                size="mini"
                type="text"
                v-hasPermi="['system:user:remove']"
                v-if="scope.row.userId !== 1"
              >删除
              </el-button>
              <el-button
                @click="handleResetPwd(scope.row)"
                icon="el-icon-key"
                size="mini"
                type="text"
                v-hasPermi="['system:user:resetPwd']"
              >重置
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
      </el-col>
    </el-row>

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="600px">
      <el-form :model="form" :rules="rules" label-width="80px" ref="form">
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户昵称" prop="nickName">
              <el-input placeholder="请输入用户昵称" v-model="form.nickName"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="归属部门" prop="deptId">
              <treeselect :options="deptOptions" :show-count="true" placeholder="请选择归属部门"
                          v-model="form.deptId"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="手机号码" prop="phonenumber">
              <el-input maxlength="11" placeholder="请输入手机号码" v-model="form.phonenumber"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input maxlength="50" placeholder="请输入邮箱" v-model="form.email"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户名称" prop="userName" v-if="form.userId == undefined">
              <el-input placeholder="请输入用户名称" v-model="form.userName"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户密码" prop="password" v-if="form.userId == undefined">
              <el-input placeholder="请输入用户密码" type="password" v-model="form.password"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户性别">
              <el-select placeholder="请选择" v-model="form.sex">
                <el-option
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                  v-for="dict in sexOptions"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
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
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="岗位">
              <el-select multiple placeholder="请选择" v-model="form.postIds">
                <el-option
                  :disabled="item.status == 1"
                  :key="item.postId"
                  :label="item.postName"
                  :value="item.postId"
                  v-for="item in postOptions"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="角色">
              <el-select multiple placeholder="请选择" v-model="form.roleIds">
                <el-option
                  :disabled="item.status == 1"
                  :key="item.roleId"
                  :label="item.roleName"
                  :value="item.roleId"
                  v-for="item in roleOptions"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input placeholder="请输入内容" type="textarea" v-model="form.remark"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
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
  import {
    addUser,
    changeUserStatus,
    delUser,
    getUser,
    listUser,
    resetUserPwd,
    updateUser
  } from "@/api/system/user";
  import {getToken} from "@/utils/auth";
  import {treeselect} from "@/api/system/dept";
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";

  export default {
    name: "User",
    components: {Treeselect},
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
        // 用户表格数据
        userList: null,
        // 弹出层标题
        title: "",
        // 部门树选项
        deptOptions: undefined,
        // 是否显示弹出层
        open: false,
        // 部门名称
        deptName: undefined,
        // 默认密码
        initPassword: undefined,
        // 日期范围
        dateRange: [],
        // 状态数据字典
        statusOptions: [],
        // 性别状态字典
        sexOptions: [],
        // 岗位选项
        postOptions: [],
        // 角色选项
        roleOptions: [],
        // 表单参数
        form: {},
        defaultProps: {
          children: "children",
          label: "label"
        },
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
          url: process.env.VUE_APP_BASE_API + "/system/user/importData"
        },
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          userName: undefined,
          phonenumber: undefined,
          status: undefined,
          deptId: undefined
        },
        // 列信息
        columns: [
          {key: 0, label: `用户编号`, visible: true},
          {key: 1, label: `用户名称`, visible: true},
          {key: 2, label: `用户昵称`, visible: true},
          {key: 3, label: `部门`, visible: true},
          {key: 4, label: `手机号码`, visible: true},
          {key: 5, label: `状态`, visible: true},
          {key: 6, label: `创建时间`, visible: true}
        ],
        // 表单校验
        rules: {
          userName: [
            {required: true, message: "用户名称不能为空", trigger: "blur"}
          ],
          nickName: [
            {required: true, message: "用户昵称不能为空", trigger: "blur"}
          ],
          password: [
            {required: true, message: "用户密码不能为空", trigger: "blur"}
          ],
          email: [
            {
              type: "email",
              message: "'请输入正确的邮箱地址",
              trigger: ["blur", "change"]
            }
          ],
          phonenumber: [
            {
              pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
              message: "请输入正确的手机号码",
              trigger: "blur"
            }
          ]
        }
      };
    },
    watch: {
      // 根据名称筛选部门树
      deptName(val) {
        this.$refs.tree.filter(val);
      }
    },
    created() {
      this.getList();
      this.getTreeselect();
      this.getDicts("sys_normal_disable").then(response => {
        this.statusOptions = response.data;
      });
      this.getDicts("sys_user_sex").then(response => {
        this.sexOptions = response.data;
      });
      this.getConfigKey("sys.user.initPassword").then(response => {
        this.initPassword = response.msg;
      });
    },
    methods: {
      /** 查询用户列表 */
      getList() {
        this.loading = true;
        listUser(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
            this.userList = response.rows;
            this.total = response.total;
            this.loading = false;
          }
        );
      },
      /** 查询部门下拉树结构 */
      getTreeselect() {
        treeselect().then(response => {
          this.deptOptions = response.data;
        });
      },
      // 筛选节点
      filterNode(value, data) {
        if (!value) {
          return true;
        }
        return data.label.indexOf(value) !== -1;
      },
      // 节点单击事件
      handleNodeClick(data) {
        this.queryParams.deptId = data.id;
        this.getList();
      },
      // 用户状态修改
      handleStatusChange(row) {
        let text = row.status === "0" ? "启用" : "停用";
        this.$confirm('确认要"' + text + '""' + row.userName + '"用户吗?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return changeUserStatus(row.userId, row.status);
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
      // 表单重置
      reset() {
        this.form = {
          userId: undefined,
          deptId: undefined,
          userName: undefined,
          nickName: undefined,
          password: undefined,
          phonenumber: undefined,
          email: undefined,
          sex: undefined,
          status: "0",
          remark: undefined,
          postIds: [],
          roleIds: []
        };
        this.resetForm("form");
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.page = 1;
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
        this.ids = selection.map(item => item.userId);
        this.single = selection.length != 1;
        this.multiple = !selection.length;
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.getTreeselect();
        getUser().then(response => {
          this.postOptions = response.posts;
          this.roleOptions = response.roles;
          this.open = true;
          this.title = "添加用户";
          this.form.password = this.initPassword;
        });
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        this.getTreeselect();
        const userId = row.userId || this.ids;
        getUser(userId).then(response => {
          this.form = response.data;
          this.postOptions = response.posts;
          this.roleOptions = response.roles;
          this.form.postIds = response.postIds;
          this.form.roleIds = response.roleIds;
          this.open = true;
          this.title = "修改用户";
          this.form.password = "";
        });
      },
      /** 重置密码按钮操作 */
      handleResetPwd(row) {
        this.$prompt('请输入"' + row.userName + '"的新密码', "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消"
        }).then(({value}) => {
          resetUserPwd(row.userId, value).then(response => {
            this.msgSuccess("修改成功，新密码是：" + value);
          });
        }).catch(() => {
        });
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.userId != undefined) {
              updateUser(this.form).then(response => {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addUser(this.form).then(response => {
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
        const userIds = row.userId || this.ids;
        this.$confirm('是否确认删除用户编号为"' + userIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delUser(userIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('system/user/export', {
          ...this.queryParams
        }, `user_${new Date().getTime()}.xlsx`)
      },
      /** 导入按钮操作 */
      handleImport() {
        this.upload.title = "用户导入";
        this.upload.open = true;
      },
      /** 下载模板操作 */
      importTemplate() {
        this.download('system/user/importTemplate', {
          ...this.queryParams
        }, `user_${new Date().getTime()}.xlsx`)
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
