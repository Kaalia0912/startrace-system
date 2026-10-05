<template>
  <div class="app-container">
    <el-card shadow="never" class="mb8">
      <el-form :inline="true" label-width="70px">
        <el-form-item label="卷号">
          <el-input
            v-model="coilNo"
            placeholder="输入成品卷号，回车查询"
            clearable
            style="width: 260px"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery" v-hasPermi="['trace:coil:query']">追溯</el-button>
        </el-form-item>
        <el-form-item>
          <span class="tip-text">输入成品卷号，一路查到订单、工序、检验和发货记录</span>
        </el-form-item>
      </el-form>
    </el-card>

    <div v-if="tracable">
      <el-card shadow="never" class="mb8">
        <div slot="header">① 成品卷</div>
        <el-descriptions :column="3" border>
          <el-descriptions-item label="卷号">{{ coil.coilNo }}</el-descriptions-item>
          <el-descriptions-item label="卡号">{{ coil.labelNo }}</el-descriptions-item>
          <el-descriptions-item label="订单号">{{ coil.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="客户">{{ coil.customerName }}</el-descriptions-item>
          <el-descriptions-item label="钢号 / 规格">{{ coil.material }} / {{ coil.spec }}</el-descriptions-item>
          <el-descriptions-item label="性能(HV) / 表面">{{ coil.hardness }} / {{ coil.surfaceRa }}</el-descriptions-item>
          <el-descriptions-item label="净重(kg)">{{ coil.netWeight }}</el-descriptions-item>
          <el-descriptions-item label="件数">{{ coil.pieces }}</el-descriptions-item>
          <el-descriptions-item label="生产日期">{{ coil.produceDate }}</el-descriptions-item>
          <el-descriptions-item label="检验员">{{ coil.checker }}</el-descriptions-item>
          <el-descriptions-item label="库位">{{ coil.location }}</el-descriptions-item>
          <el-descriptions-item label="卷状态">{{ coil.coilStatus === '1' ? '已发货' : '在库' }}</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <el-card shadow="never" class="mb8">
        <div slot="header">② 销售订单（共 {{ orders.length }} 条）</div>
        <el-table :data="orders" border size="mini">
          <el-table-column label="订单号" prop="orderNo" min-width="150" />
          <el-table-column label="日期" prop="orderDate" width="110" />
          <el-table-column label="客户" prop="customerName" min-width="140" />
          <el-table-column label="材质" prop="material" width="90" />
          <el-table-column label="规格" prop="spec" width="110" />
          <el-table-column label="硬度(HV)" prop="hardness" width="100" />
          <el-table-column label="抗拉(MPa)" prop="tensileStrength" width="110" />
          <el-table-column label="数量" prop="quantity" width="90" />
          <el-table-column label="交期" prop="deliveryDate" width="110" />
        </el-table>
      </el-card>

      <el-card shadow="never" class="mb8">
        <div slot="header">③ 工序报工（共 {{ processRecords.length }} 条）</div>
        <el-table :data="processRecords" border size="mini">
          <el-table-column label="工单号" prop="workOrderNo" min-width="160" />
          <el-table-column label="工序号" prop="processNo" width="80" />
          <el-table-column label="工序名称" prop="processName" width="130" />
          <el-table-column label="机台" prop="machine" width="110" />
          <el-table-column label="操作人" prop="operator" width="100" />
          <el-table-column label="投入(kg)" prop="inputWeight" width="100" />
          <el-table-column label="产出(kg)" prop="outputWeight" width="100" />
          <el-table-column label="损耗(kg)" prop="lossWeight" width="100" />
        </el-table>
        <div v-if="!processRecords.length" class="empty-text">该订单暂无工序报工记录</div>
      </el-card>

      <el-card shadow="never" class="mb8">
        <div slot="header">④ 质量检验（共 {{ inspections.length }} 条）</div>
        <el-table :data="inspections" border size="mini">
          <el-table-column label="检验单号" prop="inspectionNo" min-width="170" />
          <el-table-column label="来源" width="90">
            <template slot-scope="scope">{{ sourceText(scope.row.source) }}</template>
          </el-table-column>
          <el-table-column label="钢种/状态" width="120">
            <template slot-scope="scope">{{ scope.row.steelGrade }} / {{ scope.row.state }}</template>
          </el-table-column>
          <el-table-column label="屈服" prop="yieldVal" width="90" />
          <el-table-column label="抗拉" prop="tensileVal" width="90" />
          <el-table-column label="延伸率" prop="elongationVal" width="90" />
          <el-table-column label="硬度" prop="hvVal" width="90" />
          <el-table-column label="判定" width="100">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.judgeResult === '0'" type="success" size="mini">合格</el-tag>
              <el-tag v-else-if="scope.row.judgeResult === '1'" type="danger" size="mini">不合格</el-tag>
              <el-tag v-else-if="scope.row.judgeResult === '2'" type="warning" size="mini">让步</el-tag>
              <el-tag v-else type="info" size="mini">未判定</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="检验员" prop="inspector" width="100" />
        </el-table>
        <div v-if="!inspections.length" class="empty-text">该卷暂无检验记录</div>
      </el-card>

      <el-card shadow="never">
        <div slot="header">⑤ 发货记录（共 {{ shipments.length }} 条）</div>
        <el-table :data="shipments" border size="mini">
          <el-table-column label="发货单号" prop="shipmentNo" min-width="170" />
          <el-table-column label="发货日期" prop="shipmentDate" width="110" />
          <el-table-column label="客户" prop="customerName" min-width="140" />
          <el-table-column label="收货人" prop="receiver" width="100" />
          <el-table-column label="送货地址" prop="address" min-width="180" :show-overflow-tooltip="true" />
          <el-table-column label="状态" width="90">
            <template slot-scope="scope">{{ scope.row.status === '1' ? '已签收' : '已发货' }}</template>
          </el-table-column>
        </el-table>
        <div v-if="!shipments.length" class="empty-text">该卷尚未发货</div>
      </el-card>
    </div>

    <el-empty v-else description="输入卷号后点击「追溯」"></el-empty>
  </div>
</template>

<script>
import { traceCoil } from "@/api/trace/coil";

export default {
  name: "CoilTrace",
  data() {
    return {
      coilNo: null,
      tracable: false,
      coil: {},
      orders: [],
      processRecords: [],
      inspections: [],
      shipments: []
    };
  },
  methods: {
    sourceText(v) {
      return { "0": "来料", "1": "过程", "2": "成品" }[v] || "";
    },
    handleQuery() {
      if (!this.coilNo) {
        this.$modal.msgError("请输入卷号");
        return;
      }
      traceCoil(this.coilNo).then(response => {
        const data = response.data || {};
        this.coil = data.coil || {};
        this.orders = data.orders || [];
        this.processRecords = data.processRecords || [];
        this.inspections = data.inspections || [];
        this.shipments = data.shipments || [];
        this.tracable = true;
      }).catch(() => {
        this.tracable = false;
      });
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

.empty-text {
  color: #909399;
  font-size: 12px;
  padding: 8px 0;
}
</style>
