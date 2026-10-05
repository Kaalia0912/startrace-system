<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="检验单号" prop="inspectionNo">
        <el-input v-model="queryParams.inspectionNo" placeholder="请输入检验单号" clearable style="width: 170px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="卷号" prop="coilNo">
        <el-input v-model="queryParams.coilNo" placeholder="请输入卷号" clearable style="width: 150px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="钢种" prop="steelGrade">
        <el-input v-model="queryParams.steelGrade" placeholder="如 SUS304" clearable style="width: 130px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="判定" prop="judgeResult">
        <el-select v-model="queryParams.judgeResult" placeholder="请选择" clearable style="width: 120px">
          <el-option label="合格" value="0" />
          <el-option label="不合格" value="1" />
          <el-option label="让步接收" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd" v-hasPermi="['qc:inspection:add']">新增检验</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['qc:inspection:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" @click="handleExport" v-hasPermi="['qc:inspection:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="inspectionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="检验单号" align="center" prop="inspectionNo" min-width="175" :show-overflow-tooltip="true" />
      <el-table-column label="来源" align="center" prop="source" width="90">
        <template slot-scope="scope">{{ sourceText(scope.row.source) }}</template>
      </el-table-column>
      <el-table-column label="卷号" align="center" prop="coilNo" min-width="140" :show-overflow-tooltip="true" />
      <el-table-column label="钢种" align="center" prop="steelGrade" width="90" />
      <el-table-column label="状态" align="center" prop="state" width="80" />
      <el-table-column label="判定结果" align="center" prop="judgeResult" width="110">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.judgeResult === '0'" type="success">合格</el-tag>
          <el-tag v-else-if="scope.row.judgeResult === '1'" type="danger">不合格</el-tag>
          <el-tag v-else-if="scope.row.judgeResult === '2'" type="warning">让步接收</el-tag>
          <el-tag v-else type="info">未判定</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="检验员" align="center" prop="inspector" width="100" />
      <el-table-column label="检验日期" align="center" prop="inspectDate" width="110" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetail(scope.row)">详情</el-button>
          <el-button size="mini" type="text" icon="el-icon-printer" @click="handleReport(scope.row)" v-hasPermi="['qc:inspection:report']">质保书</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['qc:inspection:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['qc:inspection:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 新增 / 修改 -->
    <el-dialog :title="title" :visible.sync="open" width="820px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-divider content-position="left">基本信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="检验单号" prop="inspectionNo">
              <el-input v-model="form.inspectionNo" placeholder="自动生成，可修改" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检验来源" prop="source">
              <el-select v-model="form.source" style="width: 100%">
                <el-option label="来料检验" value="0" />
                <el-option label="过程检验" value="1" />
                <el-option label="成品检验" value="2" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="订单号" prop="orderNo">
              <el-input v-model="form.orderNo" placeholder="关联订单号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="卷号" prop="coilNo">
              <el-input v-model="form.coilNo" placeholder="关联成品卷/原料卷号" />
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
            <el-form-item label="钢种" prop="steelGrade">
              <el-input v-model="form.steelGrade" placeholder="如 SUS304（判定用）" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="state">
              <el-select v-model="form.state" placeholder="请选择状态（判定用）" filterable allow-create clearable style="width: 100%">
                <el-option v-for="item in stateOptions" :key="item" :label="item" :value="item" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="公差标准" prop="standard">
              <el-select v-model="form.standard" placeholder="宽度判定用" filterable allow-create clearable style="width: 100%">
                <el-option v-for="item in standardOptions" :key="item" :label="item" :value="item" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">实测值（保存后自动判定）</el-divider>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="实测厚度(mm)" prop="thicknessVal">
              <el-input-number v-model="form.thicknessVal" :precision="3" :step="0.01" :min="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="名义宽度(mm)" prop="nominalWidth">
              <el-input-number v-model="form.nominalWidth" :precision="2" :min="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="实测宽度(mm)" prop="widthVal">
              <el-input-number v-model="form.widthVal" :precision="2" :min="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="实测屈服(MPa)" prop="yieldVal">
              <el-input-number v-model="form.yieldVal" :precision="2" :min="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="实测抗拉(MPa)" prop="tensileVal">
              <el-input-number v-model="form.tensileVal" :precision="2" :min="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="实测延伸率(%)" prop="elongationVal">
              <el-input-number v-model="form.elongationVal" :precision="2" :min="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="实测硬度HV" prop="hvVal">
              <el-input-number v-model="form.hvVal" :precision="2" :min="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="实测表面精度" prop="surfaceRaVal">
              <el-input v-model="form.surfaceRaVal" placeholder="如 Ra0.3-0.4" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="判定结果" prop="judgeResult">
              <el-select v-model="form.judgeResult" placeholder="留空由系统判定" clearable style="width: 100%">
                <el-option label="合格" value="0" />
                <el-option label="不合格" value="1" />
                <el-option label="让步接收" value="2" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="检验员" prop="inspector">
              <el-input v-model="form.inspector" placeholder="请输入检验员" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="检验日期" prop="inspectDate">
              <el-date-picker v-model="form.inspectDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" style="width: 100%" />
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

    <!-- 详情（含判定明细） -->
    <el-drawer title="检验单详情" :visible.sync="detailOpen" size="52%" append-to-body>
      <div class="detail-body">
        <el-alert
          :title="'判定结果：' + resultText(detail.judgeResult)"
          :type="detail.judgeResult === '0' ? 'success' : (detail.judgeResult === '1' ? 'error' : 'warning')"
          :closable="false"
          style="margin-bottom: 12px"
        />
        <el-descriptions :column="2" border>
          <el-descriptions-item label="检验单号">{{ detail.inspectionNo }}</el-descriptions-item>
          <el-descriptions-item label="检验来源">{{ sourceText(detail.source) }}</el-descriptions-item>
          <el-descriptions-item label="卷号">{{ detail.coilNo }}</el-descriptions-item>
          <el-descriptions-item label="订单号">{{ detail.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="钢种 / 状态">{{ detail.steelGrade }} / {{ detail.state }}</el-descriptions-item>
          <el-descriptions-item label="公差标准">{{ detail.standard }}</el-descriptions-item>
          <el-descriptions-item label="实测厚度">{{ detail.thicknessVal }} mm</el-descriptions-item>
          <el-descriptions-item label="宽度(名义/实测)">{{ detail.nominalWidth }} / {{ detail.widthVal }} mm</el-descriptions-item>
          <el-descriptions-item label="屈服">{{ detail.yieldVal }} MPa</el-descriptions-item>
          <el-descriptions-item label="抗拉">{{ detail.tensileVal }} MPa</el-descriptions-item>
          <el-descriptions-item label="延伸率">{{ detail.elongationVal }} %</el-descriptions-item>
          <el-descriptions-item label="硬度">{{ detail.hvVal }} HV</el-descriptions-item>
          <el-descriptions-item label="表面精度">{{ detail.surfaceRaVal }}</el-descriptions-item>
          <el-descriptions-item label="检验员">{{ detail.inspector }}</el-descriptions-item>
          <el-descriptions-item label="检验日期">{{ detail.inspectDate }}</el-descriptions-item>
          <el-descriptions-item label="备注">{{ detail.remark }}</el-descriptions-item>
        </el-descriptions>
        <el-divider content-position="left">判定明细</el-divider>
        <div class="judge-detail">{{ detail.judgeDetail }}</div>
      </div>
    </el-drawer>

    <!-- 质保书打印区 -->
    <div id="reportPrintArea" class="print-area">
      <div class="report-title">无锡宝浦不锈钢有限公司</div>
      <div class="report-sub">产 品 质 量 保 证 书</div>
      <table class="report-table">
        <tr><td class="k">质保书编号</td><td class="v">{{ detail.inspectionNo }}</td><td class="k">检验日期</td><td class="v">{{ detail.inspectDate }}</td></tr>
        <tr><td class="k">订单号</td><td class="v">{{ detail.orderNo }}</td><td class="k">卷号</td><td class="v">{{ detail.coilNo }}</td></tr>
        <tr><td class="k">钢种</td><td class="v">{{ detail.steelGrade }}</td><td class="k">状态</td><td class="v">{{ detail.state }}</td></tr>
        <tr><td class="k">厚度(mm)</td><td class="v">{{ detail.thicknessVal }}</td><td class="k">宽度(名义/实测)mm</td><td class="v">{{ detail.nominalWidth }} / {{ detail.widthVal }}</td></tr>
        <tr><td class="k">屈服(MPa)</td><td class="v">{{ detail.yieldVal }}</td><td class="k">抗拉(MPa)</td><td class="v">{{ detail.tensileVal }}</td></tr>
        <tr><td class="k">延伸率(%)</td><td class="v">{{ detail.elongationVal }}</td><td class="k">硬度(HV)</td><td class="v">{{ detail.hvVal }}</td></tr>
        <tr><td class="k">表面精度</td><td class="v">{{ detail.surfaceRaVal }}</td><td class="k">公差标准</td><td class="v">{{ detail.standard }}</td></tr>
        <tr><td class="k">检验结论</td><td class="v" colspan="3">{{ resultText(detail.judgeResult) }}</td></tr>
        <tr><td class="k">判定明细</td><td class="v" colspan="3">{{ detail.judgeDetail }}</td></tr>
      </table>
      <div class="report-foot">
        <span>检验员：{{ detail.inspector }}</span>
        <span>签发日期：{{ detail.inspectDate }}</span>
      </div>
    </div>
  </div>
</template>

<style>
.print-area {
  position: absolute;
  left: -10000px;
  top: 0;
}

@media print {
  body * {
    visibility: hidden;
  }

  #reportPrintArea,
  #reportPrintArea * {
    visibility: visible;
  }

  #reportPrintArea {
    position: absolute;
    left: 0;
    top: 0;
    width: 100%;
    padding: 10mm;
  }
}

.report-title {
  font-size: 18px;
  font-weight: bold;
  text-align: center;
  font-family: "Microsoft YaHei", SimSun, sans-serif;
}

.report-sub {
  font-size: 15px;
  letter-spacing: 4px;
  text-align: center;
  margin: 4px 0 12px 0;
  font-family: "Microsoft YaHei", SimSun, sans-serif;
}

.report-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 12px;
  font-family: "Microsoft YaHei", SimSun, sans-serif;
}

.report-table td {
  border: 1px solid #000;
  padding: 6px 8px;
}

.report-table td.k {
  width: 120px;
  background: #f2f2f2;
}

.report-foot {
  margin-top: 16px;
  font-size: 12px;
  display: flex;
  justify-content: space-between;
  font-family: "Microsoft YaHei", SimSun, sans-serif;
}
</style>

<script>
import { listInspection, getInspection, delInspection, addInspection, updateInspection } from "@/api/qc/inspection";
import { listMaterial } from "@/api/basedata/material";

export default {
  name: "Inspection",
  data() {
    return {
      loading: true,
      ids: [],
      multiple: true,
      showSearch: true,
      total: 0,
      inspectionList: [],
      materialOptions: [],
      detailOpen: false,
      detail: {},
      title: "",
      open: false,
      stateOptions: ["ANN", "1/4H", "1/2H", "3/4H", "FH", "EH", "SH", "H"],
      standardOptions: ["JIS G4305", "ASTM A480", "GB/T 4239", "无锡宝浦"],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        inspectionNo: null,
        coilNo: null,
        steelGrade: null,
        judgeResult: null
      },
      form: {},
      rules: {
        inspectionNo: [{ required: true, message: "检验单号不能为空", trigger: "blur" }]
      }
    };
  },
  created() {
    this.loadMaterialOptions();
    this.getList();
  },
  methods: {
    sourceText(v) {
      return { "0": "来料检验", "1": "过程检验", "2": "成品检验" }[v] || "";
    },
    resultText(v) {
      return { "0": "合格", "1": "不合格", "2": "让步接收" }[v] || "未判定";
    },
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
      this.form.steelGrade = material.material;
      this.form.nominalWidth = material.width;
      this.form.thicknessVal = material.thickness;
    },
    getList() {
      this.loading = true;
      listInspection(this.queryParams).then(response => {
        this.inspectionList = response.rows;
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
        inspectionId: null,
        inspectionNo: "JY" + d.getFullYear() + pad(d.getMonth() + 1) + pad(d.getDate()) + pad(d.getHours()) + pad(d.getMinutes()) + pad(d.getSeconds()),
        source: "2",
        orderNo: null,
        coilNo: null,
        materialCode: null,
        steelGrade: null,
        state: null,
        standard: "无锡宝浦",
        thicknessVal: null,
        nominalWidth: null,
        widthVal: null,
        yieldVal: null,
        tensileVal: null,
        elongationVal: null,
        hvVal: null,
        surfaceRaVal: null,
        judgeResult: null,
        inspector: null,
        inspectDate: null,
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
      this.ids = selection.map(item => item.inspectionId);
      this.multiple = !selection.length;
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "新增质量检验单";
    },
    handleUpdate(row) {
      this.reset();
      getInspection(row.inspectionId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改质量检验单";
      });
    },
    /** 详情：展示判定明细 */
    handleDetail(row) {
      getInspection(row.inspectionId).then(response => {
        this.detail = response.data || {};
        this.detailOpen = true;
      });
    },
    /** 质保书打印 */
    handleReport(row) {
      getInspection(row.inspectionId).then(response => {
        this.detail = response.data || {};
        this.$nextTick(() => {
          window.print();
        });
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          const isEdit = this.form.inspectionId != null;
          const action = isEdit ? updateInspection(this.form) : addInspection(this.form);
          action.then(() => {
            this.open = false;
            this.getList();
            // 回查该单，把自动判定结果直接展示出来
            listInspection({ pageNum: 1, pageSize: 1, inspectionNo: this.form.inspectionNo }).then(res => {
              if (res.rows && res.rows.length) {
                this.detail = res.rows[0];
                this.detailOpen = true;
                this.$modal.msgSuccess("保存成功，判定结果：" + this.resultText(this.detail.judgeResult));
              } else {
                this.$modal.msgSuccess("保存成功");
              }
            });
          });
        }
      });
    },
    handleDelete(row) {
      const inspectionIds = row.inspectionId || this.ids;
      this.$modal.confirm('是否确认删除该检验单？').then(function () {
        return delInspection(inspectionIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleExport() {
      this.download('qc/inspection/export', {
        ...this.queryParams
      }, `inspection_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

<style scoped>
.detail-body {
  padding-bottom: 20px;
}

.judge-detail {
  line-height: 2;
  font-size: 14px;
  color: #303133;
}
</style>
