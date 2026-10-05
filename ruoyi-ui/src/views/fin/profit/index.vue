<template>
  <div class="app-container">
    <el-alert type="info" :closable="false" style="margin-bottom: 12px">
      <template slot="title">
        毛利 = 订单金额 − 原料成本（成品净重 × 物料原料单价）− 加工成本（成品净重 × 加工费单价 {{ processCost }} 元/kg）。
        原料单价在「基础数据 → 物料主数据」维护，加工费单价在「系统管理 → 参数设置」里改（键名 erp.process.cost.per.kg）。
      </template>
    </el-alert>

    <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="70px">
      <el-form-item label="订单号" prop="orderNo">
        <el-input v-model="queryParams.orderNo" placeholder="请输入订单号" clearable style="width: 180px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="客户" prop="customerName">
        <el-input v-model="queryParams.customerName" placeholder="请输入客户名称" clearable style="width: 180px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="profitList" show-summary :summary-method="summaryMethod">
      <el-table-column label="订单号" align="center" prop="orderNo" min-width="170" :show-overflow-tooltip="true" />
      <el-table-column label="客户名称" align="center" prop="customerName" min-width="160" :show-overflow-tooltip="true" />
      <el-table-column label="订单金额(元)" align="right" width="130">
        <template slot-scope="scope">{{ fmt(scope.row.orderAmount) }}</template>
      </el-table-column>
      <el-table-column label="成品净重(kg)" align="right" width="130">
        <template slot-scope="scope">{{ fmt(scope.row.finishWeight) }}</template>
      </el-table-column>
      <el-table-column label="原料成本(元)" align="right" width="130">
        <template slot-scope="scope">{{ fmt(scope.row.materialCost) }}</template>
      </el-table-column>
      <el-table-column label="加工成本(元)" align="right" width="130">
        <template slot-scope="scope">{{ fmt(scope.row.processCost) }}</template>
      </el-table-column>
      <el-table-column label="毛利(元)" align="right" width="130">
        <template slot-scope="scope">
          <span :style="{ color: Number(scope.row.profit) > 0 ? '#67c23a' : '#c0392b', fontWeight: 'bold' }">
            {{ fmt(scope.row.profit) }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="毛利率" align="center" width="110">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.profitRate === undefined">{{ "-" }}</el-tag>
          <el-tag v-else :type="Number(scope.row.profitRate) >= 20 ? 'success' : (Number(scope.row.profitRate) >= 0 ? 'warning' : 'danger')">
            {{ scope.row.profitRate }}%
          </el-tag>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <div class="tip-text">
      只有已经入库的成品卷才计入原料成本与加工成本——订单还没入库时，毛利等于订单金额，属于未核算状态，请以入库后再看为准。
    </div>
  </div>
</template>

<script>
import { listProfit, getProcessCost } from "@/api/fin/profit";

export default {
  name: "Profit",
  data() {
    return {
      loading: true,
      total: 0,
      profitList: [],
      processCost: "0.00",
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNo: null,
        customerName: null
      }
    };
  },
  created() {
    this.loadRate();
    this.getList();
  },
  methods: {
    loadRate() {
      getProcessCost().then(response => {
        this.processCost = Number(response.data || 0).toFixed(2);
      });
    },
    fmt(value) {
      if (value === null || value === undefined || value === "") return "0.00";
      return Number(value).toLocaleString("zh-CN", { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    },
    getList() {
      this.loading = true;
      listProfit(this.queryParams).then(response => {
        this.profitList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    summaryMethod(param) {
      const { columns, data } = param;
      const sums = [];
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = "合计";
          return;
        }
        const prop = column.property;
        if (["orderAmount", "finishWeight", "materialCost", "processCost", "profit"].indexOf(prop) > -1) {
          const total = data.reduce((acc, row) => acc + Number(row[prop] || 0), 0);
          sums[index] = total.toLocaleString("zh-CN", { minimumFractionDigits: 2, maximumFractionDigits: 2 });
        } else {
          sums[index] = "";
        }
      });
      return sums;
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.resetForm("queryRef");
      this.handleQuery();
    }
  }
};
</script>

<style scoped>
.tip-text {
  color: #909399;
  font-size: 12px;
  padding-top: 8px;
}
</style>
