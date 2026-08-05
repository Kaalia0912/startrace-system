<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="所属企业" prop="entId">
        <el-select v-model="queryParams.entId" placeholder="请选择企业" clearable filterable style="width: 200px">
          <el-option v-for="item in entOptions" :key="item.entId" :label="item.entName" :value="item.entId" />
        </el-select>
      </el-form-item>
      <el-form-item label="联系人姓名" prop="contactName">
        <el-input v-model="queryParams.contactName" placeholder="请输入联系人姓名" clearable style="width: 160px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="职务" prop="position">
        <el-input v-model="queryParams.position" placeholder="请输入职务" clearable style="width: 160px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="主要对接人" prop="isMain">
        <el-select v-model="queryParams.isMain" placeholder="请选择" clearable style="width: 120px">
          <el-option label="是" value="1" />
          <el-option label="否" value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd" v-hasPermi="['enterprise:contact:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['enterprise:contact:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" @click="handleExport" v-hasPermi="['enterprise:contact:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="contactList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="所属企业" align="center" prop="entName" :show-overflow-tooltip="true" min-width="150" />
      <el-table-column label="联系人姓名" align="center" prop="contactName" width="100" />
      <el-table-column label="职务" align="center" prop="position" width="110" />
      <el-table-column label="手机号" align="center" prop="phone" width="120" />
      <el-table-column label="邮箱" align="center" prop="email" :show-overflow-tooltip="true" min-width="140" />
      <el-table-column label="微信" align="center" prop="wechat" width="110" />
      <el-table-column label="主要对接人" align="center" prop="isMain" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isMain === '1'" type="success">主要对接</el-tag>
          <el-tag v-else type="info">普通</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="140">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['enterprise:contact:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['enterprise:contact:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改联系人对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="560px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="所属企业" prop="entId">
          <el-select v-model="form.entId" placeholder="请选择所属企业" filterable style="width: 100%">
            <el-option v-for="item in entOptions" :key="item.entId" :label="item.entName" :value="item.entId" />
          </el-select>
        </el-form-item>
        <el-form-item label="联系人姓名" prop="contactName">
          <el-input v-model="form.contactName" placeholder="请输入联系人姓名" />
        </el-form-item>
        <el-form-item label="职务" prop="position">
          <el-input v-model="form.position" placeholder="请输入职务" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" maxlength="11" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="微信" prop="wechat">
          <el-input v-model="form.wechat" placeholder="请输入微信号" />
        </el-form-item>
        <el-form-item label="主要对接人" prop="isMain">
          <el-radio-group v-model="form.isMain">
            <el-radio label="1">是</el-radio>
            <el-radio label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
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
import { listContact, getContact, delContact, addContact, updateContact } from "@/api/enterprise/contact";
import { listEnterprise } from "@/api/enterprise/enterprise";

export default {
  name: "Contact",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 企业联系人表格数据
      contactList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 企业下拉数据
      entOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        entId: null,
        contactName: null,
        position: null,
        isMain: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        entId: [{ required: true, message: "所属企业不能为空", trigger: "change" }],
        contactName: [{ required: true, message: "联系人姓名不能为空", trigger: "blur" }]
      }
    };
  },
  created() {
    this.loadEntOptions();
    this.getList();
  },
  methods: {
    /** 加载企业下拉 */
    loadEntOptions() {
      listEnterprise({ pageNum: 1, pageSize: 500 }).then(response => {
        this.entOptions = response.rows;
      });
    },
    /** 查询联系人列表 */
    getList() {
      this.loading = true;
      listContact(this.queryParams).then(response => {
        this.contactList = response.rows;
        this.total = response.total;
        this.loading = false;
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
        contactId: null,
        entId: null,
        contactName: null,
        position: null,
        phone: null,
        email: null,
        wechat: null,
        isMain: "0",
        remark: null
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
      this.resetForm("queryRef");
      this.handleQuery();
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.contactId);
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加企业联系人";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const contactId = row.contactId;
      getContact(contactId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改企业联系人";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.contactId != null) {
            updateContact(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addContact(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const contactIds = row.contactId || this.ids;
      this.$modal.confirm('是否确认删除该联系人数据？').then(function () {
        return delContact(contactIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('enterprise/contact/export', {
        ...this.queryParams
      }, `contact_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
