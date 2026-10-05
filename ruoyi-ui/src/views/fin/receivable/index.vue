<template>
  <div class="app-container">
    <el-alert
      title="应收汇总：按客户合并销售订单金额与收款登记，未收金额 = 订单金额 − 已收金额；账龄按该客户最早订单日期计算"
      type="info"
      :closable="false"
      style="margin-bottom: 12px"
    />

    <el-row :gutter="10" class="mb8">
      <el-col :span="6">
        <el-input v-model="keyword" placeholder="按客户名称筛选" clearable prefix-icon="el-icon-search" />
      </el-col>
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-refresh" @click="getList">刷新</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="filteredList" show-summary :summary-method="summaryMethod">
      <el-table-column label="客户名称" align="center" prop="customerName" min-width="200" :show-overflow-tooltip="true" />
      <el-table-column label="订单金额(元)" align="right" prop="orderAmount" width="150">
        <template slot-scope="scope">{{ fmtAmount(scope.row.orderAmount) }}</template>
      </el-table-column>
      <el-table-column label="已收金额(元)" align="right" prop="receiptAmount" width="150">
        <template slot-scope="scope">{{ fmtAmount(scope.row.receiptAmount) }}</template>
      </el-table-column>
      <el-table-column label="未收金额(元)" align="right" prop="unreceivedAmount" width="150">
        <template slot-scope="scope">
          <span :style="{ color: Number(scope.row.unreceivedAmount) > 0 ? '#c0392b' : '#67c23a', fontWeight: 'bold' }">
            {{ fmtAmount(scope.row.unreceivedAmount) }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="最早订单日期" align="center" prop="earliestOrderDate" width="130" />
      <el-table-column label="账龄(天)" align="center" prop="ageDays" width="110">
        <template slot-scope="scope">
          <el-tag v-if="Number(scope.row.ageDays) >= 90" type="danger">{{ scope.row.ageDays }}</el-tag>
          <el-tag v-else-if="Number(scope.row.ageDays) >= 30" type="warning">{{ scope.row.ageDays }}</el-tag>
          <el-tag v-else type="info">{{ scope.row.ageDays }}</el-tag>
        </template>
      </el-table-column>
    </el-table>

    <div class="tip-text">提示：账龄超过 30 / 90 天的客户会标黄 / 标红，方便先催这些。</div>
  </div>
</template>

<script>
import { receivableSummary } from "@/api/fin/receipt";

export default {
  name: "Receivable",
  data() {
    return {
      loading: true,
      keyword: "",
      list: []
    };
  },
  computed: {
    filteredList() {
      if (!this.keyword) {
        return this.list;
      }
      return this.list.filter(item => (item.customerName || "").indexOf(this.keyword) > -1);
    }
  },
  created() {
    this.getList();
  },
  methods: {
    fmtAmount(value) {
      if (value === null || value === undefined || value === "") return "0.00";
      return Number(value).toLocaleString("zh-CN", { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    },
    getList() {
      this.loading = true;
      receivableSummary().then(response => {
        this.list = response.rows || [];
        this.loading = false;
      });
    },
    /** 表格合计行 */
    summaryMethod(param) {
      const { columns, data } = param;
      const sums = [];
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = "合计";
          return;
        }
        const prop = column.property;
        if (["orderAmount", "receiptAmount", "unreceivedAmount"].indexOf(prop) > -1) {
          const total = data.reduce((acc, row) => acc + Number(row[prop] || 0), 0);
          sums[index] = total.toLocaleString("zh-CN", { minimumFractionDigits: 2, maximumFractionDigits: 2 });
        } else {
          sums[index] = "";
        }
      });
      return sums;
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
