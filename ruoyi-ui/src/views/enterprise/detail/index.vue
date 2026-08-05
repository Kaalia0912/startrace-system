<template>
  <div class="app-container">
    <el-page-header @back="$router.back()" :content="enterprise.entName || '企业详情'">
    </el-page-header>

    <!-- 基本信息 -->
    <el-card shadow="never" header="基本信息" style="margin-top: 16px">
      <el-descriptions :column="3" border>
        <el-descriptions-item label="企业名称">{{ enterprise.entName }}</el-descriptions-item>
        <el-descriptions-item label="统一社会信用代码">{{ enterprise.entCode }}</el-descriptions-item>
        <el-descriptions-item label="所属行业">
          <dict-tag :options="industryOptions" :value="enterprise.industry" />
        </el-descriptions-item>
        <el-descriptions-item label="企业类型">
          <dict-tag :options="entTypeOptions" :value="enterprise.entType" />
        </el-descriptions-item>
        <el-descriptions-item label="企业规模">
          <dict-tag :options="scaleOptions" :value="enterprise.scale" />
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <dict-tag :options="statusOptions" :value="enterprise.status" />
        </el-descriptions-item>
        <el-descriptions-item label="法定代表人">{{ enterprise.legalPerson || '-' }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ enterprise.phone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ enterprise.email || '-' }}</el-descriptions-item>
        <el-descriptions-item label="成立日期">{{ enterprise.establishDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="注册资本(万元)">{{ enterprise.registeredCapital != null ? enterprise.registeredCapital : '-' }}</el-descriptions-item>
        <el-descriptions-item label="员工人数">{{ enterprise.employeeCount != null ? enterprise.employeeCount : '-' }}</el-descriptions-item>
        <el-descriptions-item label="注册地址" :span="3">{{ enterprise.address || '-' }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="3">{{ enterprise.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <!-- 联系人 -->
    <el-card shadow="never" header="企业联系人" style="margin-top: 16px">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" plain icon="el-icon-plus" @click="handleAddContact" v-hasPermi="['enterprise:contact:add']">新增联系人</el-button>
        </el-col>
      </el-row>
      <el-table v-loading="contactLoading" :data="contactList">
        <el-table-column label="姓名" align="center" prop="contactName" width="100" />
        <el-table-column label="职务" align="center" prop="position" width="120" />
        <el-table-column label="手机号" align="center" prop="phone" width="130" />
        <el-table-column label="邮箱" align="center" prop="email" :show-overflow-tooltip="true" min-width="150" />
        <el-table-column label="微信" align="center" prop="wechat" width="120" />
        <el-table-column label="主要对接人" align="center" prop="isMain" width="110">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.isMain === '1'" type="success">主要对接</el-tag>
            <el-tag v-else type="info">普通</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="140">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleEditContact(scope.row)" v-hasPermi="['enterprise:contact:edit']">修改</el-button>
            <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelContact(scope.row)" v-hasPermi="['enterprise:contact:remove']">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增联系人对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="open" width="520px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
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
        <el-button type="primary" @click="submitContact">确 定</el-button>
        <el-button @click="open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getEnterprise } from "@/api/enterprise/enterprise";
import { listContactByEnt, addContact, updateContact, getContact, delContact } from "@/api/enterprise/contact";

export default {
  name: "EnterpriseDetail",
  data() {
    return {
      entId: null,
      enterprise: {},
      contactList: [],
      contactLoading: true,
      open: false,
      dialogTitle: "新增联系人",
      form: {},
      statusOptions: [],
      industryOptions: [],
      entTypeOptions: [],
      scaleOptions: [],
      rules: {
        contactName: [{ required: true, message: "联系人姓名不能为空", trigger: "blur" }]
      }
    };
  },
  created() {
    this.entId = this.$route.params.entId;
    this.loadDicts();
    this.loadEnterprise();
    this.loadContacts();
  },
  methods: {
    loadDicts() {
      this.getDicts("enterprise_status").then(response => { this.statusOptions = response.data; });
      this.getDicts("enterprise_industry").then(response => { this.industryOptions = response.data; });
      this.getDicts("enterprise_type").then(response => { this.entTypeOptions = response.data; });
      this.getDicts("enterprise_scale").then(response => { this.scaleOptions = response.data; });
    },
    loadEnterprise() {
      getEnterprise(this.entId).then(response => {
        this.enterprise = response.data;
      });
    },
    loadContacts() {
      this.contactLoading = true;
      listContactByEnt(this.entId).then(response => {
        this.contactList = response.data || [];
        this.contactLoading = false;
      });
    },
    handleAddContact() {
      this.form = { entId: Number(this.entId), isMain: "0" };
      this.dialogTitle = "新增联系人";
      this.open = true;
    },
    handleEditContact(row) {
      getContact(row.contactId).then(response => {
        this.form = response.data;
        this.dialogTitle = "修改联系人";
        this.open = true;
      });
    },
    submitContact() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.contactId != null) {
            updateContact(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.loadContacts();
            });
          } else {
            addContact(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.loadContacts();
            });
          }
        }
      });
    },
    handleDelContact(row) {
      this.$modal.confirm('是否确认删除联系人"' + row.contactName + '"？').then(() => {
        return delContact(row.contactId);
      }).then(() => {
        this.loadContacts();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    }
  }
};
</script>
