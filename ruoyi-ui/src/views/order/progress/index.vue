<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="订单号" prop="orderNo">
        <el-input v-model="queryParams.orderNo" placeholder="请输入订单号" clearable style="width: 180px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="客户" prop="customerName">
        <el-input v-model="queryParams.customerName" placeholder="请输入客户名称" clearable style="width: 180px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="产品名称" prop="productName">
        <el-input v-model="queryParams.productName" placeholder="请输入产品名称" clearable style="width: 160px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="progressList">
      <el-table-column label="订单号" align="center" prop="orderNo" min-width="170" :show-overflow-tooltip="true" />
      <el-table-column label="日期" align="center" prop="orderDate" width="110" />
      <el-table-column label="客户名称" align="center" prop="customerName" min-width="160" :show-overflow-tooltip="true" />
      <el-table-column label="行数" align="center" prop="lineCount" width="70" />
      <el-table-column label="订单数量" align="right" prop="quantity" width="110" />
      <el-table-column label="订单金额(元)" align="right" width="130">
        <template slot-scope="scope">{{ fmtAmount(scope.row.amount) }}</template>
      </el-table-column>
      <el-table-column label="交期" align="center" prop="deliveryDate" width="110" />
      <el-table-column label="执行进度" align="center" min-width="330">
        <template slot-scope="scope">
          <div class="flow-line">
            <span class="flow-item" :class="{ done: Number(scope.row.workOrderCount) > 0 }">
              工单 {{ scope.row.workOrderCount || 0 }}
            </span>
            <i class="el-icon-right"></i>
            <span class="flow-item" :class="{ done: Number(scope.row.finishCoilCount) > 0 }">
              成品卷 {{ scope.row.finishCoilCount || 0 }}
            </span>
            <i class="el-icon-right"></i>
            <span class="flow-item" :class="{ done: Number(scope.row.shipmentCount) > 0 }">
              发货单 {{ scope.row.shipmentCount || 0 }}
            </span>
          </div>
          <div class="flow-sub">
            计划 {{ scope.row.plannedQty || 0 }} ／ 入库 {{ scope.row.finishWeight || 0 }} kg ／ 已发 {{ scope.row.shipWeight || 0 }} kg
          </div>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" width="100">
        <template slot-scope="scope">
          <el-tag :type="stage(scope.row).type">{{ stage(scope.row).text }}</el-tag>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <div class="tip-text">状态按「有没有工单 / 有没有成品入库 / 有没有发货」自动判断，发货数据来自发货单明细。</div>
  </div>
</template>

<script>
import { listOrderProgress } from "@/api/order/progress";

export default {
  name: "OrderProgress",
  data() {
    return {
      loading: true,
      showSearch: true,
      total: 0,
      progressList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNo: null,
        customerName: null,
        productName: null
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    fmtAmount(value) {
      if (value === null || value === undefined || value === "") return "";
      return Number(value).toLocaleString("zh-CN", { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    },
    /** 由工单/入库/发货数量推导执行阶段 */
    stage(row) {
      if (Number(row.shipmentCount) > 0) {
        return { text: "已发货", type: "success" };
      }
      if (Number(row.finishCoilCount) > 0) {
        return { text: "已入库", type: "warning" };
      }
      if (Number(row.workOrderCount) > 0) {
        return { text: "生产中", type: "primary" };
      }
      return { text: "未开工", type: "info" };
    },
    getList() {
      this.loading = true;
      listOrderProgress(this.queryParams).then(response => {
        this.progressList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
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
.flow-line {
  line-height: 24px;
}

.flow-item {
  color: #909399;
}

.flow-item.done {
  color: #303133;
  font-weight: bold;
}

.flow-sub {
  color: #909399;
  font-size: 12px;
  line-height: 18px;
}

.tip-text {
  color: #909399;
  font-size: 12px;
  padding-top: 8px;
}
</style>
