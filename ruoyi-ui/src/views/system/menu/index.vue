<template>
  <div class="app-container">
    <el-form
      :inline="true"
      :model="queryParams"
      ref="queryForm"
      v-show="showSearch">
      <el-form-item
        label="菜单名称"
        prop="menuName">
        <el-input
          @keyup.enter.native="handleQuery"
          clearable
          placeholder="请输入菜单名称"
          size="small"
          v-model="queryParams.menuName"
          />
      </el-form-item>
      <el-form-item
        label="状态"
        prop="status">
        <el-select
          clearable
          placeholder="菜单状态"
          size="small"
          v-model="queryParams.status">
          <el-option
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
            v-for="dict in statusOptions"
            />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
          @click="handleQuery"
          icon="el-icon-search"
          size="mini"
          type="primary">搜索
        </el-button>
        <el-button
          @click="resetQuery"
          icon="el-icon-refresh"
          size="mini">重置
        </el-button>
      </el-form-item>
    </el-form>

    <el-row
      :gutter="10"
      class="mb8">
      <el-col :span="1.5">
        <el-button
          @click="handleAdd"
          icon="el-icon-plus"
          plain
          size="mini"
          type="primary"
          v-hasPermi="['system:menu:add']"
          >新增
        </el-button>
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      :data="menuList"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
      row-key="menuId"
      v-loading="loading"
      >
      <el-table-column
        :show-overflow-tooltip="true"
        label="菜单名称"
        prop="menuName"
        width="160"></el-table-column>
      <el-table-column
        align="center"
        label="图标"
        prop="icon"
        width="100">
        <template slot-scope="scope">
          <svg-icon :icon-class="scope.row.icon"/>
        </template>
      </el-table-column>
      <el-table-column
        label="排序"
        prop="orderNum"
        width="60"></el-table-column>
      <el-table-column
        :show-overflow-tooltip="true"
        label="权限标识"
        prop="perms"></el-table-column>
      <el-table-column
        :show-overflow-tooltip="true"
        label="组件路径"
        prop="component"></el-table-column>
      <el-table-column
        :formatter="statusFormat"
        label="状态"
        prop="status"
        width="80"></el-table-column>
      <el-table-column
        align="center"
        label="创建时间"
        prop="createTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        class-name="small-padding fixed-width"
        label="操作">
        <template slot-scope="scope">
          <el-button
            @click="handleUpdate(scope.row)"
            icon="el-icon-edit"
            size="mini"
            type="text"
            v-hasPermi="['system:menu:edit']"
            >修改
          </el-button>
          <el-button
            @click="handleAdd(scope.row)"
            icon="el-icon-plus"
            size="mini"
            type="text"
            v-hasPermi="['system:menu:add']"
            >新增
          </el-button>
          <el-button
            @click="handleDelete(scope.row)"
            icon="el-icon-delete"
            size="mini"
            type="text"
            v-hasPermi="['system:menu:remove']"
            >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改菜单对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      append-to-body
      width="600px">
      <el-form
        :model="form"
        :rules="rules"
        label-width="80px"
        ref="form">
        <el-row>
          <el-col :span="24">
            <el-form-item label="上级菜单">
              <treeselect
                :normalizer="normalizer"
                :options="menuOptions"
                :show-count="true"
                placeholder="选择上级菜单"
                v-model="form.parentId"
                />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item
              label="菜单类型"
              prop="menuType">
              <el-radio-group v-model="form.menuType">
                <el-radio label="M">目录</el-radio>
                <el-radio label="C">菜单</el-radio>
                <el-radio label="F">按钮</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item
              label="菜单图标"
              v-if="form.menuType != 'F'">
              <el-popover
                @show="$refs['iconSelect'].reset()"
                placement="bottom-start"
                trigger="click"
                width="460"
                >
                <IconSelect
                  @selected="selected"
                  ref="iconSelect"/>
                <el-input
                  placeholder="点击选择图标"
                  readonly
                  slot="reference"
                  v-model="form.icon">
                  <svg-icon
                    :icon-class="form.icon"
                    class="el-input__icon"
                    slot="prefix"
                    style="height: 32px;width: 16px;"
                    v-if="form.icon"
                    />
                  <i
                    class="el-icon-search el-input__icon"
                    slot="prefix"
                    v-else/>
                </el-input>
              </el-popover>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="菜单名称"
              prop="menuName">
              <el-input
                placeholder="请输入菜单名称"
                v-model="form.menuName"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="显示排序"
              prop="orderNum">
              <el-input-number
                :min="0"
                controls-position="right"
                v-model="form.orderNum"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="是否外链"
              v-if="form.menuType != 'F'">
              <el-radio-group v-model="form.isFrame">
                <el-radio label="0">是</el-radio>
                <el-radio label="1">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="路由地址"
              prop="path"
              v-if="form.menuType != 'F'">
              <el-input
                placeholder="请输入路由地址"
                v-model="form.path"/>
            </el-form-item>
          </el-col>
          <el-col
            :span="12"
            v-if="form.menuType == 'C'">
            <el-form-item
              label="组件路径"
              prop="component">
              <el-input
                placeholder="请输入组件路径"
                v-model="form.component"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="权限标识"
              v-if="form.menuType != 'M'">
              <el-input
                maxlength="50"
                placeholder="请权限标识"
                v-model="form.perms"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="显示状态"
              v-if="form.menuType != 'F'">
              <el-radio-group v-model="form.visible">
                <el-radio
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                  v-for="dict in visibleOptions"
                  >{{dict.dictLabel}}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="菜单状态"
              v-if="form.menuType != 'F'">
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
          <el-col :span="12">
            <el-form-item
              label="是否缓存"
              v-if="form.menuType == 'C'">
              <el-radio-group v-model="form.isCache">
                <el-radio label="0">缓存</el-radio>
                <el-radio label="1">不缓存</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div
        class="dialog-footer"
        slot="footer">
        <el-button
          @click="submitForm"
          type="primary">确 定
        </el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {addMenu, delMenu, getMenu, listMenu, updateMenu} from "@/api/system/menu";
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  import IconSelect from "@/components/IconSelect";

  export default {
    name: "Menu",
    components: {Treeselect, IconSelect},
    data() {
      return {
        // 遮罩层
        loading: true,
        // 显示搜索条件
        showSearch: true,
        // 菜单表格树数据
        menuList: [],
        // 菜单树选项
        menuOptions: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 显示状态数据字典
        visibleOptions: [],
        // 菜单状态数据字典
        statusOptions: [],
        // 查询参数
        queryParams: {
          menuName: undefined,
          visible: undefined
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          menuName: [
            {required: true, message: "菜单名称不能为空", trigger: "blur"}
          ],
          orderNum: [
            {required: true, message: "菜单顺序不能为空", trigger: "blur"}
          ],
          path: [
            {required: true, message: "路由地址不能为空", trigger: "blur"}
          ]
        }
      };
    },
    created() {
      this.getList();
      this.getDicts("sys_show_hide").then(response => {
        this.visibleOptions = response.data;
      });
      this.getDicts("sys_normal_disable").then(response => {
        this.statusOptions = response.data;
      });
    },
    methods: {
      // 选择图标
      selected(name) {
        this.form.icon = name;
      },
      /** 查询菜单列表 */
      getList() {
        this.loading = true;
        listMenu(this.queryParams).then(response => {
          this.menuList = this.handleTree(response.data, "menuId");
          this.loading = false;
        });
      },
      /** 转换菜单数据结构 */
      normalizer(node) {
        if (node.children && !node.children.length) {
          delete node.children;
        }
        return {
          id: node.menuId,
          label: node.menuName,
          children: node.children
        };
      },
      /** 查询菜单下拉树结构 */
      getTreeselect() {
        listMenu().then(response => {
          this.menuOptions = [];
          const menu = {menuId: 0, menuName: '主类目', children: []};
          menu.children = this.handleTree(response.data, "menuId");
          this.menuOptions.push(menu);
        });
      },
      // 显示状态字典翻译
      visibleFormat(row, column) {
        if (row.menuType == "F") {
          return "";
        }
        return this.selectDictLabel(this.visibleOptions, row.visible);
      },
      // 菜单状态字典翻译
      statusFormat(row, column) {
        if (row.menuType == "F") {
          return "";
        }
        return this.selectDictLabel(this.statusOptions, row.status);
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          menuId: undefined,
          parentId: 0,
          menuName: undefined,
          icon: undefined,
          menuType: "M",
          orderNum: undefined,
          isFrame: "1",
          isCache: "0",
          visible: "0",
          status: "0"
        };
        this.resetForm("form");
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      /** 新增按钮操作 */
      handleAdd(row) {
        this.reset();
        this.getTreeselect();
        if (row != null && row.menuId) {
          this.form.parentId = row.menuId;
        } else {
          this.form.parentId = 0;
        }
        this.open = true;
        this.title = "添加菜单";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        this.getTreeselect();
        getMenu(row.menuId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改菜单";
        });
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.menuId != undefined) {
              updateMenu(this.form).then(response => {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addMenu(this.form).then(response => {
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
        this.$confirm('是否确认删除名称为"' + row.menuName + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delMenu(row.menuId);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
      }
    }
  };
</script>
