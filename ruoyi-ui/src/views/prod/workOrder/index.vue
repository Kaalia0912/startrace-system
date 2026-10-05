<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="工单号" prop="workOrderNo">
        <el-input v-model="queryParams.workOrderNo" placeholder="请输入工单号" clearable style="width: 180px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="订单号" prop="orderNo">
        <el-input v-model="queryParams.orderNo" placeholder="请输入订单号" clearable style="width: 170px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择" clearable style="width: 130px">
          <el-option label="待生产" value="0" />
          <el-option label="生产中" value="1" />
          <el-option label="已完成" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd" v-hasPermi="['prod:workorder:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['prod:workorder:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" @click="handleExport" v-hasPermi="['prod:workorder:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="workOrderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="工单号" align="center" prop="workOrderNo" min-width="180" :show-overflow-tooltip="true" />
      <el-table-column label="订单号" align="center" prop="orderNo" min-width="150" :show-overflow-tooltip="true" />
      <el-table-column label="物料编码" align="center" prop="materialCode" min-width="160" :show-overflow-tooltip="true" />
      <el-table-column label="产品名称" align="center" prop="productName" min-width="130" :show-overflow-tooltip="true" />
      <el-table-column label="规格" align="center" prop="spec" width="110" />
      <el-table-column label="计划数量" align="center" prop="quantity" width="100" />
      <el-table-column label="计划开工" align="center" prop="planStartDate" width="110" />
      <el-table-column label="计划完工" align="center" prop="planEndDate" width="110" />
      <el-table-column label="状态" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === '0'" type="info">待生产</el-tag>
          <el-tag v-else-if="scope.row.status === '1'" type="warning">生产中</el-tag>
          <el-tag v-else type="success">已完成</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="140" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['prod:workorder:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['prod:workorder:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="760px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="工单号" prop="workOrderNo">
              <el-input v-model="form.workOrderNo" placeholder="留空可点右侧生成" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="来源订单号" prop="orderNo">
              <el-input v-model="form.orderNo" placeholder="可填销售订单号" />
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
              <el-input v-model="form.productName" placeholder="选物料后自动带出" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="规格" prop="spec">
              <el-input v-model="form.spec" placeholder="选物料后自动带出" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划数量" prop="quantity">
              <el-input-number v-model="form.quantity" :precision="3" :min="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划开工" prop="planStartDate">
              <el-date-picker v-model="form.planStartDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划完工" prop="planEndDate">
              <el-date-picker v-model="form.planEndDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" style="width: 100%">
                <el-option label="待生产" value="0" />
                <el-option label="生产中" value="1" />
                <el-option label="已完成" value="2" />
              </el-select>
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
import { listWorkOrder, getWorkOrder, delWorkOrder, addWorkOrder, updateWorkOrder } from "@/api/prod/workOrder";
import { listMaterial } from "@/api/basedata/material";

export default {
  name: "WorkOrder",
  data() {
    return {
      loading: true,
      ids: [],
      multiple: true,
      showSearch: true,
      total: 0,
      workOrderList: [],
      materialOptions: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        workOrderNo: null,
        orderNo: null,
        status: null
      },
      form: {},
      rules: {
        workOrderNo: [{ required: true, message: "工单号不能为空", trigger: "blur" }]
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
      if (!code) {
        return;
      }
      const material = this.materialOptions.find(item => item.materialCode === code);
      if (!material) {
        return;
      }
      this.form.productName = material.productName;
      this.form.spec = material.spec;
    },
    getList() {
      this.loading = true;
      listWorkOrder(this.queryParams).then(response => {
        this.workOrderList = response.rows;
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
        workOrderId: null,
        workOrderNo: null,
        orderNo: null,
        materialCode: null,
        productName: null,
        spec: null,
        quantity: null,
        planStartDate: null,
        planEndDate: null,
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
      this.ids = selection.map(item => item.workOrderId);
      this.multiple = !selection.length;
    },
    handleAdd() {
      this.reset();
      const d = new Date();
      const pad = n => String(n).padStart(2, "0");
      this.form.workOrderNo = "GD" + d.getFullYear() + pad(d.getMonth() + 1) + pad(d.getDate()) + pad(d.getHours()) + pad(d.getMinutes()) + pad(d.getSeconds());
      this.open = true;
      this.title = "新增生产工单";
    },
    handleUpdate(row) {
      this.reset();
      getWorkOrder(row.workOrderId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改生产工单";
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.workOrderId != null) {
            updateWorkOrder(this.form).then(() => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addWorkOrder(this.form).then(() => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleDelete(row) {
      const workOrderIds = row.workOrderId || this.ids;
      this.$modal.confirm('是否确认删除该工单？').then(function () {
        return delWorkOrder(workOrderIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleExport() {
      this.download('prod/workorder/export', {
        ...this.queryParams
      }, `work_order_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
