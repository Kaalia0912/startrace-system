<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="发货单号" prop="shipmentNo">
        <el-input v-model="queryParams.shipmentNo" placeholder="请输入发货单号" clearable style="width: 180px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="客户" prop="customerName">
        <el-input v-model="queryParams.customerName" placeholder="请输入客户名称" clearable style="width: 170px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="订单号" prop="orderNo">
        <el-input v-model="queryParams.orderNo" placeholder="请输入订单号" clearable style="width: 160px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择" clearable style="width: 120px">
          <el-option label="已发货" value="0" />
          <el-option label="已签收" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['inv:shipment:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" @click="handleExport" v-hasPermi="['inv:shipment:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="shipmentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="发货单号" align="center" prop="shipmentNo" min-width="180" :show-overflow-tooltip="true" />
      <el-table-column label="发货日期" align="center" prop="shipmentDate" width="110" />
      <el-table-column label="客户名称" align="center" prop="customerName" min-width="160" :show-overflow-tooltip="true" />
      <el-table-column label="订单号" align="center" prop="orderNo" min-width="140" :show-overflow-tooltip="true" />
      <el-table-column label="收货人" align="center" prop="receiver" width="100" />
      <el-table-column label="总重(kg)" align="center" prop="totalWeight" width="110" />
      <el-table-column label="总件数" align="center" prop="totalPieces" width="90" />
      <el-table-column label="状态" align="center" prop="status" width="90">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === '1'" type="success">已签收</el-tag>
          <el-tag v-else type="warning">已发货</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="170" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetail(scope.row)">详情</el-button>
          <el-button
            v-if="scope.row.status !== '1'"
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleSign(scope.row)"
            v-hasPermi="['inv:shipment:edit']"
          >签收</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['inv:shipment:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 发货单详情 -->
    <el-drawer title="发货单详情" :visible.sync="detailOpen" size="55%" append-to-body>
      <div class="detail-body">
        <el-divider content-position="left">单头信息</el-divider>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="发货单号">{{ detail.shipmentNo }}</el-descriptions-item>
          <el-descriptions-item label="发货日期">{{ detail.shipmentDate }}</el-descriptions-item>
          <el-descriptions-item label="客户名称">{{ detail.customerName }}</el-descriptions-item>
          <el-descriptions-item label="订单号">{{ detail.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="收货人">{{ detail.receiver }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ detail.phone }}</el-descriptions-item>
          <el-descriptions-item label="送货地址" :span="2">{{ detail.address }}</el-descriptions-item>
          <el-descriptions-item label="总重(kg)">{{ detail.totalWeight }}</el-descriptions-item>
          <el-descriptions-item label="总件数">{{ detail.totalPieces }}</el-descriptions-item>
          <el-descriptions-item label="状态">{{ detail.status === '1' ? '已签收' : '已发货' }}</el-descriptions-item>
          <el-descriptions-item label="备注">{{ detail.remark }}</el-descriptions-item>
        </el-descriptions>

        <el-divider content-position="left">发货明细</el-divider>
        <el-table :data="detail.items || []" border>
          <el-table-column label="卷号" align="center" prop="coilNo" min-width="150" />
          <el-table-column label="物料编码" align="center" prop="materialCode" min-width="150" />
          <el-table-column label="产品名称" align="center" prop="productName" min-width="130" />
          <el-table-column label="规格" align="center" prop="spec" width="110" />
          <el-table-column label="净重(kg)" align="center" prop="netWeight" width="100" />
          <el-table-column label="件数" align="center" prop="pieces" width="80" />
        </el-table>
      </div>
    </el-drawer>
  </div>
</template>

<style scoped>
.detail-body {
  padding-bottom: 20px;
}
</style>

<script>
import { listShipment, getShipment, updateShipment, delShipment } from "@/api/inv/shipment";

export default {
  name: "Shipment",
  data() {
    return {
      loading: true,
      ids: [],
      multiple: true,
      showSearch: true,
      total: 0,
      shipmentList: [],
      detailOpen: false,
      detail: {},
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        shipmentNo: null,
        customerName: null,
        orderNo: null,
        status: null
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listShipment(this.queryParams).then(response => {
        this.shipmentList = response.rows;
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
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.shipmentId);
      this.multiple = !selection.length;
    },
    /** 查看详情（含明细） */
    handleDetail(row) {
      getShipment(row.shipmentId).then(response => {
        this.detail = response.data || {};
        this.detailOpen = true;
      });
    },
    /** 标记签收 */
    handleSign(row) {
      this.$modal.confirm('确认将该发货单标记为已签收？').then(() => {
        return updateShipment({ shipmentId: row.shipmentId, status: "1" });
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("已标记签收");
      }).catch(() => {});
    },
    handleDelete(row) {
      const shipmentIds = row.shipmentId || this.ids;
      this.$modal.confirm('是否确认删除该发货单？（不影响已发货的成品卷状态）').then(function () {
        return delShipment(shipmentIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleExport() {
      this.download('inv/shipment/export', {
        ...this.queryParams
      }, `shipment_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
