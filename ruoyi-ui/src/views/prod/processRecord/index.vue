<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="工单号" prop="workOrderNo">
        <el-input v-model="queryParams.workOrderNo" placeholder="请输入工单号" clearable style="width: 180px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="工序" prop="processName">
        <el-input v-model="queryParams.processName" placeholder="请输入工序名称" clearable style="width: 150px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="操作人" prop="operator">
        <el-input v-model="queryParams.operator" placeholder="请输入操作人" clearable style="width: 130px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd" v-hasPermi="['prod:record:add']">报工</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['prod:record:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" @click="handleExport" v-hasPermi="['prod:record:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="recordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="工单号" align="center" prop="workOrderNo" min-width="170" :show-overflow-tooltip="true" />
      <el-table-column label="工序号" align="center" prop="processNo" width="80" />
      <el-table-column label="工序名称" align="center" prop="processName" width="130" />
      <el-table-column label="机台" align="center" prop="machine" width="110" />
      <el-table-column label="操作人" align="center" prop="operator" width="100" />
      <el-table-column label="开始时间" align="center" prop="startTime" min-width="150" />
      <el-table-column label="结束时间" align="center" prop="endTime" min-width="150" />
      <el-table-column label="投入(kg)" align="center" prop="inputWeight" width="100" />
      <el-table-column label="产出(kg)" align="center" prop="outputWeight" width="100" />
      <el-table-column label="损耗(kg)" align="center" prop="lossWeight" width="100" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="140" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['prod:record:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['prod:record:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="760px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="工单号" prop="workOrderNo">
              <el-select
                v-model="form.workOrderNo"
                placeholder="选择工单，或直接输入"
                filterable
                allow-create
                default-first-option
                clearable
                style="width: 100%"
                @change="onWorkOrderChange"
              >
                <el-option v-for="item in workOrderOptions" :key="item.workOrderId" :label="item.workOrderNo" :value="item.workOrderNo" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="工序" prop="processNo">
              <el-select
                v-model="form.processNo"
                placeholder="从工序库选择，或直接输入"
                filterable
                allow-create
                clearable
                style="width: 100%"
                @change="onProcessChange"
              >
                <el-option v-for="item in processOptions" :key="item.processId" :label="item.processNo + ' ' + item.processName" :value="item.processNo" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="工序名称" prop="processName">
              <el-input v-model="form.processName" placeholder="选工序后自动带出" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="机台" prop="machine">
              <el-input v-model="form.machine" placeholder="如 分剪机 1#" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="操作人" prop="operator">
              <el-input v-model="form.operator" placeholder="请输入操作人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="开始时间" prop="startTime">
              <el-date-picker v-model="form.startTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间" prop="endTime">
              <el-date-picker v-model="form.endTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="投入重量(kg)" prop="inputWeight">
              <el-input-number v-model="form.inputWeight" :precision="2" :min="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="产出重量(kg)" prop="outputWeight">
              <el-input-number v-model="form.outputWeight" :precision="2" :min="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="损耗重量(kg)" prop="lossWeight">
              <el-input-number v-model="form.lossWeight" :precision="2" controls-position="right" style="width: 100%" placeholder="不填自动算" />
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
import { listProcessRecord, getProcessRecord, delProcessRecord, addProcessRecord, updateProcessRecord } from "@/api/prod/processRecord";
import { listWorkOrder } from "@/api/prod/workOrder";
import { listProcess } from "@/api/basedata/process";

export default {
  name: "ProcessRecord",
  data() {
    return {
      loading: true,
      ids: [],
      multiple: true,
      showSearch: true,
      total: 0,
      recordList: [],
      workOrderOptions: [],
      processOptions: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        workOrderNo: null,
        processName: null,
        operator: null
      },
      form: {},
      rules: {
        workOrderNo: [{ required: true, message: "工单号不能为空", trigger: "change" }],
        processNo: [{ required: true, message: "工序不能为空", trigger: "change" }]
      }
    };
  },
  created() {
    this.loadWorkOrderOptions();
    this.loadProcessOptions();
    this.getList();
  },
  methods: {
    loadWorkOrderOptions() {
      listWorkOrder({ pageNum: 1, pageSize: 500 }).then(response => {
        this.workOrderOptions = response.rows || [];
      });
    },
    loadProcessOptions() {
      listProcess({ pageNum: 1, pageSize: 200, status: "0" }).then(response => {
        this.processOptions = response.rows || [];
      });
    },
    onWorkOrderChange(no) {
      const wo = this.workOrderOptions.find(item => item.workOrderNo === no);
      if (wo) {
        this.form.workOrderId = wo.workOrderId;
      }
    },
    onProcessChange(no) {
      const p = this.processOptions.find(item => String(item.processNo) === String(no));
      if (p) {
        this.form.processName = p.processName;
      }
    },
    getList() {
      this.loading = true;
      listProcessRecord(this.queryParams).then(response => {
        this.recordList = response.rows;
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
        recordId: null,
        workOrderId: null,
        workOrderNo: null,
        processNo: null,
        processName: null,
        machine: null,
        operator: null,
        startTime: null,
        endTime: null,
        inputWeight: null,
        outputWeight: null,
        lossWeight: null,
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
      this.ids = selection.map(item => item.recordId);
      this.multiple = !selection.length;
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "工序报工";
    },
    handleUpdate(row) {
      this.reset();
      getProcessRecord(row.recordId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改报工记录";
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.recordId != null) {
            updateProcessRecord(this.form).then(() => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addProcessRecord(this.form).then(() => {
              this.$modal.msgSuccess("报工成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleDelete(row) {
      const recordIds = row.recordId || this.ids;
      this.$modal.confirm('是否确认删除该报工记录？').then(function () {
        return delProcessRecord(recordIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleExport() {
      this.download('prod/record/export', {
        ...this.queryParams
      }, `process_record_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
