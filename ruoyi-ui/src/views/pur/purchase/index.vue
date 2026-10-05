<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="采购单号" prop="purchaseNo">
        <el-input v-model="queryParams.purchaseNo" placeholder="请输入采购单号" clearable style="width: 180px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="供应商" prop="supplier">
        <el-input v-model="queryParams.supplier" placeholder="请输入供应商" clearable style="width: 170px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择" clearable style="width: 130px">
          <el-option label="待收货" value="0" />
          <el-option label="已收货" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd" v-hasPermi="['pur:order:add']">新增采购</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['pur:order:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" @click="handleExport" v-hasPermi="['pur:order:export']">导出</el-button>
      </el-col>
      <el-col :span="10">
        <span class="tip-text">点「收货」会自动在原料卷料里生成一条台账（卷号 = 采购单号）</span>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="purchaseList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="采购单号" align="center" prop="purchaseNo" min-width="175" :show-overflow-tooltip="true" />
      <el-table-column label="采购日期" align="center" prop="purchaseDate" width="110" />
      <el-table-column label="供应商" align="center" prop="supplier" min-width="150" :show-overflow-tooltip="true" />
      <el-table-column label="材质" align="center" prop="material" width="90" />
      <el-table-column label="规格" align="center" prop="spec" width="110" />
      <el-table-column label="重量(kg)" align="right" prop="weight" width="110" />
      <el-table-column label="单价(元/kg)" align="right" prop="unitPrice" width="120" />
      <el-table-column label="金额(元)" align="right" width="130">
        <template slot-scope="scope">{{ fmtAmount(scope.row.amount) }}</template>
      </el-table-column>
      <el-table-column label="预计到货" align="center" prop="expectDate" width="110" />
      <el-table-column label="状态" align="center" prop="status" width="90">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === '1'" type="success">已收货</el-tag>
          <el-tag v-else type="warning">待收货</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="190" fixed="right">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.status !== '1'"
            size="mini"
            type="text"
            icon="el-icon-download"
            @click="handleReceive(scope.row)"
            v-hasPermi="['pur:order:receive']"
          >收货</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['pur:order:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['pur:order:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="780px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="采购单号" prop="purchaseNo">
              <el-input v-model="form.purchaseNo" placeholder="自动生成，可修改" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="供应商" prop="supplier">
              <el-input v-model="form.supplier" placeholder="请输入供应商" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="物料编码" prop="materialCode">
              <el-select
                v-model="form.materialCode"
                placeholder="从物料主数据选择，或直接输入"
                filterable
                allow-create
                default-first-option
                clearable
                style="width: 100%"
                @change="onMaterialChange"
              >
                <el-option
                  v-for="item in materialOptions"
                  :key="item.materialId"
                  :label="item.materialCode + (item.material ? '（' + item.material + ' ' + (item.spec || '') + '）' : '')"
                  :value="item.materialCode"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="材质" prop="material">
              <el-input v-model="form.material" placeholder="选物料后自动带出" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="规格" prop="spec">
              <el-input v-model="form.spec" placeholder="选物料后自动带出" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数量" prop="quantity">
              <el-input-number v-model="form.quantity" :precision="3" :min="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="重量(kg)" prop="weight">
              <el-input-number v-model="form.weight" :precision="2" :min="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单价(元/kg)" prop="unitPrice">
              <el-input-number v-model="form.unitPrice" :precision="2" :min="0" controls-position="right" style="width: 100%" @change="calcAmount" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="金额(元)" prop="amount">
              <el-input-number v-model="form.amount" :precision="2" :min="0" controls-position="right" style="width: 100%" placeholder="可按重量×单价自动算" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="采购日期" prop="purchaseDate">
              <el-date-picker v-model="form.purchaseDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预计到货" prop="expectDate">
              <el-date-picker v-model="form.expectDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listPurchase, getPurchase, delPurchase, addPurchase, updatePurchase, receivePurchase } from "@/api/pur/purchase";
import { listMaterial } from "@/api/basedata/material";

export default {
  name: "PurOrder",
  data() {
    return {
      loading: true,
      ids: [],
      multiple: true,
      showSearch: true,
      total: 0,
      purchaseList: [],
      materialOptions: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        purchaseNo: null,
        supplier: null,
        status: null
      },
      form: {},
      rules: {
        purchaseNo: [{ required: true, message: "采购单号不能为空", trigger: "blur" }]
      }
    };
  },
  created() {
    this.loadMaterialOptions();
    this.getList();
  },
  methods: {
    loadMaterialOptions() {
      listMaterial({ pageNum: 1, pageSize: 500, status: "0" }).then(response => {
        this.materialOptions = response.rows || [];
      });
    },
    onMaterialChange(code) {
      const material = this.materialOptions.find(item => item.materialCode === code);
      if (!material) {
        return;
      }
      this.form.material = material.material;
      this.form.spec = material.spec;
      if (material.rawPrice && !this.form.unitPrice) {
        this.form.unitPrice = material.rawPrice;
        this.calcAmount();
      }
    },
    /** 金额 = 重量 × 单价 */
    calcAmount() {
      const weight = this.form.weight;
      const price = this.form.unitPrice;
      if (weight && price) {
        this.form.amount = Number((weight * price).toFixed(2));
      }
    },
    fmtAmount(value) {
      if (value === null || value === undefined || value === "") return "";
      return Number(value).toLocaleString("zh-CN", { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    },
    getList() {
      this.loading = true;
      listPurchase(this.queryParams).then(response => {
        this.purchaseList = response.rows;
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
        purchaseId: null,
        purchaseNo: "CG" + d.getFullYear() + pad(d.getMonth() + 1) + pad(d.getDate()) + pad(d.getHours()) + pad(d.getMinutes()) + pad(d.getSeconds()),
        supplier: null,
        materialCode: null,
        material: null,
        spec: null,
        quantity: null,
        weight: null,
        unitPrice: null,
        amount: null,
        purchaseDate: null,
        expectDate: null,
        status: "0",
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
      this.ids = selection.map(item => item.purchaseId);
      this.multiple = !selection.length;
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "新增采购单";
    },
    handleUpdate(row) {
      this.reset();
      getPurchase(row.purchaseId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改采购单";
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.purchaseId != null) {
            updatePurchase(this.form).then(() => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPurchase(this.form).then(() => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 收货入库 */
    handleReceive(row) {
      this.$modal.confirm('确认收货？系统将生成原料卷台账（卷号 ' + row.purchaseNo + '）').then(() => {
        return receivePurchase(row.purchaseId);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("收货成功，已生成原料卷台账");
      }).catch(() => {});
    },
    handleDelete(row) {
      const purchaseIds = row.purchaseId || this.ids;
      this.$modal.confirm('是否确认删除该采购单？').then(function () {
        return delPurchase(purchaseIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleExport() {
      this.download('pur/order/export', {
        ...this.queryParams
      }, `purchase_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

<style scoped>
.tip-text {
  color: #909399;
  font-size: 12px;
  line-height: 32px;
}
</style>
