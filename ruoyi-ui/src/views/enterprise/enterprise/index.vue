<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="统一社会信用代码" prop="entCode">
        <el-input
          v-model="queryParams.entCode"
          placeholder="请输入统一社会信用代码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="企业名称" prop="entName">
        <el-input
          v-model="queryParams.entName"
          placeholder="请输入企业名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所属行业" prop="industry">
        <el-select v-model="queryParams.industry" placeholder="请选择所属行业" clearable>
          <el-option
            v-for="dict in industryOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="企业类型" prop="entType">
        <el-select v-model="queryParams.entType" placeholder="请选择企业类型" clearable>
          <el-option
            v-for="dict in entTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="在营" value="0" />
          <el-option label="注销" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['enterprise:enterprise:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['enterprise:enterprise:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['enterprise:enterprise:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['enterprise:enterprise:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['enterprise:enterprise:import']"
        >导入</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="enterpriseList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="企业ID" align="center" prop="entId" />
      <el-table-column label="统一社会信用代码" align="center" prop="entCode" />
      <el-table-column label="企业名称" align="center" prop="entName" :show-overflow-tooltip="true" />
      <el-table-column label="所属行业" align="center" prop="industry" />
      <el-table-column label="企业类型" align="center" prop="entType" />
      <el-table-column label="企业规模" align="center" prop="scale" />
      <el-table-column label="法定代表人" align="center" prop="legalPerson" />
      <el-table-column label="注册资本(万元)" align="center" prop="registeredCapital" />
      <el-table-column label="员工人数" align="center" prop="employeeCount" />
      <el-table-column label="状态" align="center" prop="status" width="90">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status == 0" type="success">在营</el-tag>
          <el-tag v-else type="danger">注销</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleDetail(scope.row)"
            v-hasPermi="['enterprise:enterprise:query']"
          >详情</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['enterprise:enterprise:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['enterprise:enterprise:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改企业档案对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="统一社会信用代码" prop="entCode">
          <el-input v-model="form.entCode" placeholder="请输入统一社会信用代码" maxlength="18" />
        </el-form-item>
        <el-form-item label="企业名称" prop="entName">
          <el-input v-model="form.entName" placeholder="请输入企业名称" />
        </el-form-item>
        <el-form-item label="所属行业" prop="industry">
          <el-select v-model="form.industry" placeholder="请选择所属行业" style="width: 100%">
            <el-option
              v-for="dict in industryOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="企业类型" prop="entType">
          <el-select v-model="form.entType" placeholder="请选择企业类型" style="width: 100%">
            <el-option
              v-for="dict in entTypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="企业规模" prop="scale">
          <el-select v-model="form.scale" placeholder="请选择企业规模" style="width: 100%">
            <el-option
              v-for="dict in scaleOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="法定代表人" prop="legalPerson">
          <el-input v-model="form.legalPerson" placeholder="请输入法定代表人" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="成立日期" prop="establishDate">
          <el-date-picker
            v-model="form.establishDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择成立日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="注册资本(万元)" prop="registeredCapital">
          <el-input-number v-model="form.registeredCapital" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="员工人数" prop="employeeCount">
          <el-input-number v-model="form.employeeCount" :min="0" :precision="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="0">在营</el-radio>
            <el-radio label="1">注销</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="注册地址" prop="address">
          <el-input v-model="form.address" type="textarea" placeholder="请输入注册地址" />
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

    <!-- 导入对话框 -->
    <el-dialog title="企业数据导入" :visible.sync="importOpen" width="420px" append-to-body>
      <el-form ref="importForm" :model="importForm" label-width="110px">
        <el-form-item label="更新已存在数据">
          <el-checkbox v-model="importForm.updateSupport">存在则更新</el-checkbox>
        </el-form-item>
        <el-form-item label="企业数据文件">
          <el-upload
            ref="upload"
            :auto-upload="false"
            :limit="1"
            accept=".xlsx, .xls"
            :file-list="fileList"
            :on-change="handleFileChange"
            :on-remove="handleFileRemove"
          >
            <el-button size="small" type="primary">选择文件</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="importFile">确 定</el-button>
        <el-button type="success" @click="downloadTemplate">下载模板</el-button>
        <el-button @click="importOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listEnterprise, getEnterprise, delEnterprise, addEnterprise, updateEnterprise, importEnterprise, importTemplate } from "@/api/enterprise/enterprise";

export default {
  name: "Enterprise",
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
      // 企业档案表格数据
      enterpriseList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示导入弹窗
      importOpen: false,
      // 导入参数
      importForm: { updateSupport: false },
      // 上传文件列表
      fileList: [],
      // 字典
      industryOptions: [],
      entTypeOptions: [],
      scaleOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        entCode: null,
        entName: null,
        industry: null,
        entType: null,
        status: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        entCode: [{ required: true, message: "统一社会信用代码不能为空", trigger: "blur" }],
        entName: [{ required: true, message: "企业名称不能为空", trigger: "blur" }]
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("enterprise_industry").then(response => {
      this.industryOptions = response.data;
    });
    this.getDicts("enterprise_type").then(response => {
      this.entTypeOptions = response.data;
    });
    this.getDicts("enterprise_scale").then(response => {
      this.scaleOptions = response.data;
    });
  },
  activated() {
    // keep-alive 页面每次进入都会触发：读取 URL 状态参数（首页卡片跳转）
    const qs = this.$route.query.status;
    if (qs !== undefined) {
      // status=all 表示全部，0/1 表示在营/注销
      this.queryParams.status = (qs === "all") ? null : qs;
      this.getList();
    }
  },
  methods: {
    /** 查询企业档案列表 */
    getList() {
      this.loading = true;
      listEnterprise(this.queryParams).then(response => {
        this.enterpriseList = response.rows;
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
        entId: null,
        entCode: null,
        entName: null,
        industry: null,
        entType: null,
        scale: null,
        address: null,
        legalPerson: null,
        phone: null,
        email: null,
        establishDate: null,
        registeredCapital: null,
        employeeCount: null,
        status: "0",
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
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.entId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加企业档案";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const entId = row.entId || this.ids;
      getEnterprise(entId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改企业档案";
      });
    },
    /** 详情按钮操作 */
    handleDetail(row) {
      this.$router.push("/enterprise/enterprise/detail/" + row.entId);
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.entId != null) {
            updateEnterprise(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addEnterprise(this.form).then(response => {
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
      const entIds = row.entId || this.ids;
      this.$modal.confirm('是否确认删除企业档案编号为"' + entIds + '"的数据项？').then(function () {
        return delEnterprise(entIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('enterprise/enterprise/export', {
        ...this.queryParams
      }, `enterprise_${new Date().getTime()}.xlsx`);
    },
    /** 导入按钮操作 */
    handleImport() {
      this.importOpen = true;
      this.fileList = [];
      this.importForm.updateSupport = false;
      if (this.$refs.upload) {
        this.$refs.upload.clearFiles();
      }
    },
    /** 文件选择 */
    handleFileChange(file) {
      this.fileList = [file];
    },
    /** 文件移除 */
    handleFileRemove() {
      this.fileList = [];
    },
    /** 提交导入 */
    importFile() {
      const file = this.fileList[0];
      if (!file) {
        this.$modal.msgError("请选择要导入的Excel文件");
        return;
      }
      const formData = new FormData();
      formData.append("file", file.raw);
      formData.append("updateSupport", this.importForm.updateSupport);
      importEnterprise(formData).then(response => {
        this.importOpen = false;
        this.$modal.msgSuccess(response.msg);
        this.getList();
      });
    },
    /** 下载导入模板 */
    downloadTemplate() {
      importTemplate().then(response => {
        this.$modal.msgSuccess("模板下载成功");
        window.open(process.env.VUE_APP_BASE_API + response.data);
      });
    }
  }
};
</script>
