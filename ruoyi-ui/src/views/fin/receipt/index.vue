<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="收款单号" prop="receiptNo">
        <el-input v-model="queryParams.receiptNo" placeholder="请输入收款单号" clearable style="width: 170px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="客户" prop="customerName">
        <el-input v-model="queryParams.customerName" placeholder="请输入客户名称" clearable style="width: 170px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="订单号" prop="orderNo">
        <el-input v-model="queryParams.orderNo" placeholder="请输入订单号" clearable style="width: 160px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="收款方式" prop="payMethod">
        <el-select v-model="queryParams.payMethod" placeholder="请选择" clearable style="width: 130px">
          <el-option v-for="item in payMethods" :key="item" :label="item" :value="item" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd" v-hasPermi="['fin:receipt:add']">收款登记</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['fin:receipt:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" @click="handleExport" v-hasPermi="['fin:receipt:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="receiptList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="收款单号" align="center" prop="receiptNo" min-width="175" :show-overflow-tooltip="true" />
      <el-table-column label="收款日期" align="center" prop="receiptDate" width="110" />
      <el-table-column label="客户名称" align="center" prop="customerName" min-width="170" :show-overflow-tooltip="true" />
      <el-table-column label="订单号" align="center" prop="orderNo" min-width="150" :show-overflow-tooltip="true" />
      <el-table-column label="收款金额(元)" align="right" prop="amount" width="130">
        <template slot-scope="scope">{{ fmtAmount(scope.row.amount) }}</template>
      </el-table-column>
      <el-table-column label="收款方式" align="center" prop="payMethod" width="110" />
      <el-table-column label="经手人" align="center" prop="handler" width="100" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="140" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['fin:receipt:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['fin:receipt:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="640px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="收款单号" prop="receiptNo">
          <el-input v-model="form.receiptNo" placeholder="自动生成，可修改" />
        </el-form-item>
        <el-form-item label="客户名称" prop="customerName">
          <el-select
            v-model="form.customerName"
            placeholder="从企业档案选择，或直接输入"
            filterable
            allow-create
            default-first-option
            clearable
            style="width: 100%"
          >
            <el-option v-for="item in entOptions" :key="item.entId" :label="item.entName" :value="item.entName" />
          </el-select>
        </el-form-item>
        <el-form-item label="关联订单号" prop="orderNo">
          <el-input v-model="form.orderNo" placeholder="请输入订单号" />
        </el-form-item>
        <el-form-item label="收款日期" prop="receiptDate">
          <el-date-picker v-model="form.receiptDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" style="width: 100%" />
        </el-form-item>
        <el-form-item label="收款金额(元)" prop="amount">
          <el-input-number v-model="form.amount" :precision="2" :min="0" controls-position="right" style="width: 100%" />
        </el-form-item>
        <el-form-item label="收款方式" prop="payMethod">
          <el-select v-model="form.payMethod" style="width: 100%">
            <el-option v-for="item in payMethods" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="经手人" prop="handler">
          <el-input v-model="form.handler" placeholder="请输入经手人" />
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
import { listReceipt, getReceipt, delReceipt, addReceipt, updateReceipt } from "@/api/fin/receipt";
import { listEnterprise } from "@/api/enterprise/enterprise";

export default {
  name: "FinReceipt",
  data() {
    return {
      loading: true,
      ids: [],
      multiple: true,
      showSearch: true,
      total: 0,
      receiptList: [],
      entOptions: [],
      payMethods: ["电汇", "银行承兑", "现金", "转账", "其他"],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        receiptNo: null,
        customerName: null,
        orderNo: null,
        payMethod: null
      },
      form: {},
      rules: {
        receiptNo: [{ required: true, message: "收款单号不能为空", trigger: "blur" }],
        customerName: [{ required: true, message: "客户名称不能为空", trigger: "change" }]
      }
    };
  },
  created() {
    this.loadEntOptions();
    this.getList();
  },
  methods: {
    loadEntOptions() {
      listEnterprise({ pageNum: 1, pageSize: 500 }).then(response => {
        this.entOptions = response.rows || [];
      });
    },
    fmtAmount(value) {
      if (value === null || value === undefined || value === "") return "";
      return Number(value).toLocaleString("zh-CN", { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    },
    getList() {
      this.loading = true;
      listReceipt(this.queryParams).then(response => {
        this.receiptList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    reset() {
      const d = new Date();
      const pad = n => String(n).padStart(2, "0");
      this.form = {
        receiptId: null,
        receiptNo: "SK" + d.getFullYear() + pad(d.getMonth() + 1) + pad(d.getDate()) + pad(d.getHours()) + pad(d.getMinutes()) + pad(d.getSeconds()),
        customerName: null,
        orderNo: null,
        receiptDate: null,
        amount: null,
        payMethod: "电汇",
        handler: null,
        remark: null
      };
      this.resetForm("form");
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.resetForm("queryRef");
      this.handleQuery();
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.receiptId);
      this.multiple = !selection.length;
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "收款登记";
    },
    handleUpdate(row) {
      this.reset();
      getReceipt(row.receiptId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改收款登记";
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.receiptId != null) {
            updateReceipt(this.form).then(() => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addReceipt(this.form).then(() => {
              this.$modal.msgSuccess("登记成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleDelete(row) {
      const receiptIds = row.receiptId || this.ids;
      this.$modal.confirm('是否确认删除该收款记录？').then(function () {
        return delReceipt(receiptIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleExport() {
      this.download('fin/receipt/export', {
        ...this.queryParams
      }, `receipt_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
