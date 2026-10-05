<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="卷号" prop="coilNo">
        <el-input v-model="queryParams.coilNo" placeholder="请输入卷号" clearable style="width: 160px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="订单号" prop="orderNo">
        <el-input v-model="queryParams.orderNo" placeholder="请输入订单号" clearable style="width: 160px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="客户" prop="customerName">
        <el-input v-model="queryParams.customerName" placeholder="请输入客户名称" clearable style="width: 170px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="卷状态" prop="coilStatus">
        <el-select v-model="queryParams.coilStatus" placeholder="请选择" clearable style="width: 120px">
          <el-option label="在库" value="0" />
          <el-option label="已发货" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd" v-hasPermi="['inv:finishcoil:add']">入库登记</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-truck" :disabled="multiple" @click="handleShip" v-hasPermi="['inv:finishcoil:ship']">发货</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['inv:finishcoil:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" @click="handleExport" v-hasPermi="['inv:finishcoil:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="finishCoilList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="卷号" align="center" prop="coilNo" min-width="150" :show-overflow-tooltip="true" />
      <el-table-column label="卡号" align="center" prop="labelNo" width="110" />
      <el-table-column label="订单号" align="center" prop="orderNo" min-width="140" :show-overflow-tooltip="true" />
      <el-table-column label="客户名称" align="center" prop="customerName" min-width="160" :show-overflow-tooltip="true" />
      <el-table-column label="钢号" align="center" prop="material" width="90" />
      <el-table-column label="规格" align="center" prop="spec" width="110" />
      <el-table-column label="净重(kg)" align="center" prop="netWeight" width="100" />
      <el-table-column label="件数" align="center" prop="pieces" width="70" />
      <el-table-column label="库位" align="center" prop="location" width="90" />
      <el-table-column label="卷状态" align="center" prop="coilStatus" width="90">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.coilStatus === '0'" type="success">在库</el-tag>
          <el-tag v-else type="info">已发货</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="140" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['inv:finishcoil:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['inv:finishcoil:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 成品入库 / 修改 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="卷号" prop="coilNo">
              <el-input v-model="form.coilNo" placeholder="请输入卷号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="卡号" prop="labelNo">
              <el-input v-model="form.labelNo" placeholder="标签卡号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="订单号" prop="orderNo">
              <el-input v-model="form.orderNo" placeholder="关联销售订单号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="客户名称" prop="customerName">
              <el-input v-model="form.customerName" placeholder="请输入客户名称" />
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
            <el-form-item label="产品名称" prop="productName">
              <el-input v-model="form.productName" placeholder="请输入产品名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="钢号" prop="material">
              <el-input v-model="form.material" placeholder="如 SUS304" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="规格" prop="spec">
              <el-input v-model="form.spec" placeholder="如 0.23*213" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性能(硬度HV)" prop="hardness">
              <el-input v-model="form.hardness" placeholder="如 270~300" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="表面精度" prop="surfaceRa">
              <el-input v-model="form.surfaceRa" placeholder="如 Ra0.3-0.4" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="净重(kg)" prop="netWeight">
              <el-input-number v-model="form.netWeight" :precision="2" :step="10" :min="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="件数" prop="pieces">
              <el-input-number v-model="form.pieces" :precision="0" :min="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生产日期" prop="produceDate">
              <el-date-picker v-model="form.produceDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择生产日期" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检验员" prop="checker">
              <el-input v-model="form.checker" placeholder="请输入检验员" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="库位" prop="location">
              <el-input v-model="form.location" placeholder="如 B-01" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入库日期" prop="inDate">
              <el-date-picker v-model="form.inDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择入库日期" style="width: 100%" />
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

    <!-- 发货 -->
    <el-dialog title="成品发货" :visible.sync="shipOpen" width="640px" append-to-body>
      <el-alert
        :title="'本次将发货 ' + shipForm.coilCount + ' 卷，合计 ' + shipForm.totalWeightText + ' kg'"
        type="info"
        :closable="false"
        style="margin-bottom: 12px"
      />
      <el-form ref="shipFormRef" :model="shipForm" :rules="shipRules" label-width="110px">
        <el-form-item label="客户名称" prop="customerName">
          <el-input v-model="shipForm.customerName" placeholder="默认取所选卷的客户" />
        </el-form-item>
        <el-form-item label="关联订单号" prop="orderNo">
          <el-input v-model="shipForm.orderNo" placeholder="默认取所选卷的订单号" />
        </el-form-item>
        <el-form-item label="收货人" prop="receiver">
          <el-input v-model="shipForm.receiver" placeholder="请输入收货人" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="shipForm.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="送货地址" prop="address">
          <el-input v-model="shipForm.address" type="textarea" placeholder="请输入送货地址" />
        </el-form-item>
        <el-form-item label="发货日期" prop="shipmentDate">
          <el-date-picker v-model="shipForm.shipmentDate" type="date" value-format="yyyy-MM-dd" placeholder="默认当天" style="width: 100%" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="shipForm.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitShip">确 定</el-button>
        <el-button @click="shipOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listFinishCoil, getFinishCoil, delFinishCoil, addFinishCoil, updateFinishCoil } from "@/api/inv/finishCoil";
import { shipCoils } from "@/api/inv/shipment";
import { listMaterial } from "@/api/basedata/material";

export default {
  name: "FinishCoil",
  data() {
    return {
      loading: true,
      ids: [],
      multiple: true,
      showSearch: true,
      total: 0,
      finishCoilList: [],
      // 选中行原始数据（发货用）
      selectedRows: [],
      title: "",
      open: false,
      shipOpen: false,
      materialOptions: [],
      shipForm: {
        coilIds: [],
        coilCount: 0,
        totalWeightText: "0",
        customerName: null,
        orderNo: null,
        receiver: null,
        phone: null,
        address: null,
        shipmentDate: null,
        remark: null
      },
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        coilNo: null,
        orderNo: null,
        customerName: null,
        coilStatus: null
      },
      form: {},
      rules: {
        coilNo: [{ required: true, message: "卷号不能为空", trigger: "blur" }]
      },
      shipRules: {}
    };
  },
  created() {
    this.loadMaterialOptions();
    this.getList();
  },
  methods: {
    /** 加载物料下拉（物料主数据，仅正常状态） */
    loadMaterialOptions() {
      listMaterial({ pageNum: 1, pageSize: 500, status: "0" }).then(response => {
        this.materialOptions = response.rows || [];
      });
    },
    /** 选中物料后带出产品名称/钢号/规格/性能/表面精度 */
    onMaterialChange(code) {
      if (!code) {
        return;
      }
      const material = this.materialOptions.find(item => item.materialCode === code);
      if (!material) {
        return;
      }
      this.form.productName = material.productName;
      this.form.material = material.material;
      this.form.spec = material.spec;
      this.form.hardness = material.hardness;
      this.form.surfaceRa = material.surfaceRa;
    },
    getList() {
      this.loading = true;
      listFinishCoil(this.queryParams).then(response => {
        this.finishCoilList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    reset() {
      this.form = {
        coilId: null,
        coilNo: null,
        labelNo: null,
        orderNo: null,
        customerName: null,
        materialCode: null,
        productName: null,
        material: null,
        spec: null,
        hardness: null,
        surfaceRa: null,
        netWeight: null,
        pieces: null,
        produceDate: null,
        checker: null,
        location: null,
        inDate: null,
        coilStatus: "0",
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
      this.ids = selection.map(item => item.coilId);
      this.selectedRows = selection;
      this.multiple = !selection.length;
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "成品入库登记";
    },
    handleUpdate(row) {
      this.reset();
      const coilId = row.coilId;
      getFinishCoil(coilId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改成品卷";
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.coilId != null) {
            updateFinishCoil(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addFinishCoil(this.form).then(response => {
              this.$modal.msgSuccess("入库成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 打开发货弹窗 */
    handleShip() {
      const rows = this.selectedRows || [];
      if (rows.length === 0) {
        this.$modal.msgError("请先勾选要发货的成品卷");
        return;
      }
      const shipped = rows.filter(item => item.coilStatus === "1");
      if (shipped.length > 0) {
        this.$modal.msgError("卷号 " + shipped.map(item => item.coilNo).join("、") + " 已发货，请取消勾选");
        return;
      }
      let totalWeight = 0;
      rows.forEach(item => {
        totalWeight += Number(item.netWeight || 0);
      });
      this.shipForm = {
        coilIds: rows.map(item => item.coilId),
        coilCount: rows.length,
        totalWeightText: totalWeight.toFixed(2),
        customerName: rows[0].customerName || null,
        orderNo: rows[0].orderNo || null,
        receiver: null,
        phone: null,
        address: null,
        shipmentDate: null,
        remark: null
      };
      this.shipOpen = true;
    },
    /** 提交发货 */
    submitShip() {
      const payload = { ...this.shipForm };
      delete payload.coilCount;
      delete payload.totalWeightText;
      shipCoils(payload).then(() => {
        this.$modal.msgSuccess("发货成功");
        this.shipOpen = false;
        this.getList();
      });
    },
    handleDelete(row) {
      const coilIds = row.coilId || this.ids;
      this.$modal.confirm('是否确认删除该成品卷数据？').then(function () {
        return delFinishCoil(coilIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleExport() {
      this.download('inv/finishcoil/export', {
        ...this.queryParams
      }, `finish_coil_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
