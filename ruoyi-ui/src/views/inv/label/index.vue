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
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-printer" :disabled="multiple" @click="handlePrint" v-hasPermi="['inv:label:print']">打印标签</el-button>
      </el-col>
      <el-col :span="12">
        <span class="tip-text">勾选成品卷后点「打印标签」，浏览器打印预览里就是最终标签（每张 100mm × 70mm，一页两张）</span>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="coilList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="卷号(流水号)" align="center" prop="coilNo" min-width="150" :show-overflow-tooltip="true" />
      <el-table-column label="卡号" align="center" prop="labelNo" width="110" />
      <el-table-column label="客户名称" align="center" prop="customerName" min-width="170" :show-overflow-tooltip="true" />
      <el-table-column label="订单号" align="center" prop="orderNo" min-width="140" :show-overflow-tooltip="true" />
      <el-table-column label="钢号" align="center" prop="material" width="90" />
      <el-table-column label="规格" align="center" prop="spec" width="110" />
      <el-table-column label="性能(HV)" align="center" prop="hardness" width="100" />
      <el-table-column label="表面精度" align="center" prop="surfaceRa" width="110" />
      <el-table-column label="净重(kg)" align="center" prop="netWeight" width="100" />
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 打印区域：屏幕上藏在视口外，打印时只显示它 -->
    <div id="labelPrintArea" class="print-area">
      <div v-for="item in printRows" :key="item.coilId" class="label-card">
        <div class="label-title">{{ companyTitle }}</div>
        <div class="label-row"><span class="label-key">客户</span><span class="label-val">{{ item.customerName }}</span></div>
        <div class="label-row"><span class="label-key">卡号</span><span class="label-val">{{ item.labelNo }}</span></div>
        <div class="label-row"><span class="label-key">流水号</span><span class="label-val strong">{{ item.coilNo }}</span></div>
        <div class="label-row"><span class="label-key">规格</span><span class="label-val">{{ item.spec }}</span></div>
        <div class="label-row"><span class="label-key">钢号</span><span class="label-val">{{ item.material }}</span></div>
        <div class="label-row"><span class="label-key">性能</span><span class="label-val">{{ item.hardness }}</span></div>
        <div class="label-row"><span class="label-key">表面精度</span><span class="label-val">{{ item.surfaceRa }}</span></div>
        <div class="label-row"><span class="label-key">生产日期</span><span class="label-val">{{ item.produceDate }}</span></div>
        <div class="label-row">
          <span class="label-key">检验员</span><span class="label-val">{{ item.checker }}</span>
          <span class="label-key">件数</span><span class="label-val">{{ item.pieces }}</span>
        </div>
        <div class="label-row">
          <span class="label-key">净重</span><span class="label-val strong">{{ item.netWeight }} kg</span>
        </div>
      </div>
    </div>
  </div>
</template>

<style>
/* 屏幕上把打印区挪到视口外，打印时再放回来 */
.print-area {
  position: absolute;
  left: -10000px;
  top: 0;
}

@media print {
  body * {
    visibility: hidden;
  }

  #labelPrintArea,
  #labelPrintArea * {
    visibility: visible;
  }

  #labelPrintArea {
    position: absolute;
    left: 0;
    top: 0;
    width: 100%;
    padding: 6mm;
  }

  .label-card {
    page-break-inside: avoid;
    break-inside: avoid;
  }
}

.label-card {
  width: 100mm;
  height: 70mm;
  border: 1px solid #000;
  box-sizing: border-box;
  padding: 4mm;
  margin: 0 0 4mm 0;
  display: inline-block;
  vertical-align: top;
  font-family: "Microsoft YaHei", SimSun, sans-serif;
  color: #000;
}

.label-title {
  font-size: 14px;
  font-weight: bold;
  text-align: center;
  padding-bottom: 2mm;
  margin-bottom: 2mm;
  border-bottom: 1px solid #000;
}

.label-row {
  font-size: 11px;
  line-height: 1.7;
  white-space: nowrap;
  overflow: hidden;
}

.label-key {
  display: inline-block;
  width: 18mm;
  color: #333;
}

.label-val {
  display: inline-block;
  margin-right: 4mm;
}

.label-val.strong {
  font-weight: bold;
}
</style>

<script>
import { listFinishCoil } from "@/api/inv/finishCoil";

export default {
  name: "LabelPrint",
  data() {
    return {
      loading: true,
      multiple: true,
      showSearch: true,
      total: 0,
      coilList: [],
      printRows: [],
      // 标签抬头（可按需改）
      companyTitle: "无锡宝浦不锈钢有限公司　精密不锈钢带",
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        coilNo: null,
        orderNo: null,
        customerName: null
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listFinishCoil(this.queryParams).then(response => {
        this.coilList = response.rows;
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
      this.printRows = selection;
      this.multiple = !selection.length;
    },
    /** 打印：把选中卷渲染到打印区后调起浏览器打印 */
    handlePrint() {
      if (!this.printRows.length) {
        this.$modal.msgError("请先勾选要打印的成品卷");
        return;
      }
      this.$nextTick(() => {
        window.print();
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
</style>
