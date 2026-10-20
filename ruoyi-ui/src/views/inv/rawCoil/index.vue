<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="卷号" prop="coilNo">
        <el-input v-model="queryParams.coilNo" placeholder="请输入卷号" clearable style="width: 170px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="材质" prop="material">
        <el-input v-model="queryParams.material" placeholder="请输入材质" clearable style="width: 140px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="检验状态" prop="qcStatus">
        <el-select v-model="queryParams.qcStatus" placeholder="请选择" clearable style="width: 130px">
          <el-option label="待检" value="0" />
          <el-option label="合格" value="1" />
          <el-option label="不合格" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item label="卷状态" prop="coilStatus">
        <el-select v-model="queryParams.coilStatus" placeholder="请选择" clearable style="width: 130px">
          <el-option label="在库" value="0" />
          <el-option label="已开卷" value="1" />
          <el-option label="已用完" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd" v-hasPermi="['inv:rawcoil:add']">入库登记</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['inv:rawcoil:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" @click="handleExport" v-hasPermi="['inv:rawcoil:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="rawCoilList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="卷号" align="center" prop="coilNo" min-width="160" :show-overflow-tooltip="true" />
      <el-table-column label="材质" align="center" prop="material" width="90" />
      <el-table-column label="规格" align="center" prop="spec" width="110" />
      <el-table-column label="净重(kg)" align="center" prop="netWeight" width="100" />
      <el-table-column label="硬度(HV)" align="center" prop="hardness" width="100" />
      <el-table-column label="供应商" align="center" prop="supplier" min-width="140" :show-overflow-tooltip="true" />
      <el-table-column label="库位" align="center" prop="location" width="90" />
      <el-table-column label="检验状态" align="center" prop="qcStatus" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.qcStatus === '1'" type="success">合格</el-tag>
          <el-tag v-else-if="scope.row.qcStatus === '2'" type="danger">不合格</el-tag>
          <el-tag v-else type="info">待检</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="卷状态" align="center" prop="coilStatus" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.coilStatus === '0'" type="success">在库</el-tag>
          <el-tag v-else-if="scope.row.coilStatus === '1'" type="warning">已开卷</el-tag>
          <el-tag v-else type="info">已用完</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="140" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['inv:rawcoil:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['inv:rawcoil:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改原料卷料对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="760px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="卷号" prop="coilNo">
              <el-input v-model="form.coilNo" placeholder="请输入卷号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="供应商" prop="supplier">
              <el-input v-model="form.supplier" placeholder="请输入供应商" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="材质" prop="material">
              <el-input v-model="form.material" placeholder="如 SUS304" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="规格型号(mm)" prop="spec">
              <el-input v-model="form.spec" placeholder="如 0.23*213" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="厚度(mm)" prop="thickness">
              <el-input-number v-model="form.thickness" :precision="3" :step="0.01" :min="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="宽度(mm)" prop="width">
              <el-input-number v-model="form.width" :precision="2" :step="1" :min="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="净重(kg)" prop="netWeight">
              <el-input-number v-model="form.netWeight" :precision="2" :step="10" :min="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="卷内径(mm)" prop="innerDia">
              <el-input-number v-model="form.innerDia" :precision="2" :step="1" :min="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="硬度(HV)" prop="hardness">
              <el-input v-model="form.hardness" placeholder="如 250~310" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="到货日期" prop="arriveDate">
              <el-date-picker v-model="form.arriveDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择到货日期" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="库位" prop="location">
              <el-input v-model="form.location" placeholder="如 A-01" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检验状态" prop="qcStatus">
              <el-select v-model="form.qcStatus" style="width: 100%">
                <el-option label="待检" value="0" />
                <el-option label="合格" value="1" />
                <el-option label="不合格" value="2" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="卷状态" prop="coilStatus">
              <el-select v-model="form.coilStatus" style="width: 100%">
                <el-option label="在库" value="0" />
                <el-option label="已开卷" value="1" />
                <el-option label="已用完" value="2" />
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
import { listRawCoil, getRawCoil, delRawCoil, addRawCoil, updateRawCoil } from "@/api/inv/rawCoil";

export default {
  name: "RawCoil",
  data() {
    return {
      loading: true,
      ids: [],
      multiple: true,
      showSearch: true,
      total: 0,
      rawCoilList: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        coilNo: null,
        material: null,
        qcStatus: null,
        coilStatus: null
      },
      form: {},
      rules: {
        coilNo: [{ required: true, message: "卷号不能为空", trigger: "blur" }]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listRawCoil(this.queryParams).then(response => {
        this.rawCoilList = response.rows;
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
        supplier: null,
        material: null,
        spec: null,
        thickness: null,
        width: null,
        netWeight: null,
        innerDia: null,
        hardness: null,
        arriveDate: null,
        location: null,
        qcStatus: "0",
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
      this.multiple = !selection.length;
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "原料入库登记";
    },
    handleUpdate(row) {
      this.reset();
      const coilId = row.coilId;
      getRawCoil(coilId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改原料卷料";
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.coilId != null) {
            updateRawCoil(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRawCoil(this.form).then(response => {
              this.$modal.msgSuccess("入库成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleDelete(row) {
      const coilIds = row.coilId || this.ids;
      this.$modal.confirm('是否确认删除该原料卷数据？').then(function () {
        return delRawCoil(coilIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleExport() {
      this.download('inv/rawcoil/export', {
        ...this.queryParams
      }, `raw_coil_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
