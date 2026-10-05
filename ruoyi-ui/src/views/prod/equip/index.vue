<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="设备编号" prop="equipCode">
        <el-input v-model="queryParams.equipCode" placeholder="请输入设备编号" clearable style="width: 160px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="设备名称" prop="equipName">
        <el-input v-model="queryParams.equipName" placeholder="请输入设备名称" clearable style="width: 160px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="设备类型" prop="equipType">
        <el-select v-model="queryParams.equipType" placeholder="请选择" clearable style="width: 150px">
          <el-option label="生产设备" value="0" />
          <el-option label="表面处理" value="1" />
          <el-option label="检测设备" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd" v-hasPermi="['prod:equip:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['prod:equip:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" @click="handleExport" v-hasPermi="['prod:equip:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="equipList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="设备编号" align="center" prop="equipCode" width="120" />
      <el-table-column label="设备名称" align="center" prop="equipName" min-width="140" :show-overflow-tooltip="true" />
      <el-table-column label="设备类型" align="center" prop="equipType" width="110">
        <template slot-scope="scope">{{ typeText(scope.row.equipType) }}</template>
      </el-table-column>
      <el-table-column label="型号" align="center" prop="model" width="120" />
      <el-table-column label="存放位置" align="center" prop="location" width="120" />
      <el-table-column label="购置日期" align="center" prop="buyDate" width="110" />
      <el-table-column label="校准周期(天)" align="center" prop="checkCycleDays" width="120" />
      <el-table-column label="上次校准" align="center" prop="lastCheckDate" width="110" />
      <el-table-column label="校准状态" align="center" width="170">
        <template slot-scope="scope">
          <el-tag :type="checkStatus(scope.row).type">{{ checkStatus(scope.row).text }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="90">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === '0'" type="success">正常</el-tag>
          <el-tag v-else type="info">停用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="140" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['prod:equip:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['prod:equip:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="720px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设备编号" prop="equipCode">
              <el-input v-model="form.equipCode" placeholder="如 EQ-T01" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备名称" prop="equipName">
              <el-input v-model="form.equipName" placeholder="如 维氏硬度计" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备类型" prop="equipType">
              <el-select v-model="form.equipType" style="width: 100%">
                <el-option label="生产设备" value="0" />
                <el-option label="表面处理" value="1" />
                <el-option label="检测设备" value="2" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="型号" prop="model">
              <el-input v-model="form.model" placeholder="请输入型号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="存放位置" prop="location">
              <el-input v-model="form.location" placeholder="如 检测室" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="购置日期" prop="buyDate">
              <el-date-picker v-model="form.buyDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="校准周期(天)" prop="checkCycleDays">
              <el-input-number v-model="form.checkCycleDays" :precision="0" :min="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="上次校准" prop="lastCheckDate">
              <el-date-picker v-model="form.lastCheckDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio label="0">正常</el-radio>
                <el-radio label="1">停用</el-radio>
              </el-radio-group>
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
import { listEquip, getEquip, delEquip, addEquip, updateEquip } from "@/api/prod/equip";

export default {
  name: "EquipLedger",
  data() {
    return {
      loading: true,
      ids: [],
      multiple: true,
      showSearch: true,
      total: 0,
      equipList: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        equipCode: null,
        equipName: null,
        equipType: null
      },
      form: {},
      rules: {
        equipCode: [{ required: true, message: "设备编号不能为空", trigger: "blur" }],
        equipName: [{ required: true, message: "设备名称不能为空", trigger: "blur" }]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    typeText(v) {
      return { "0": "生产设备", "1": "表面处理", "2": "检测设备" }[v] || "";
    },
    /** 校准到期状态：超期 / 30 天内到期 / 正常 */
    checkStatus(row) {
      if (!row.checkCycleDays || !row.lastCheckDate) {
        return { text: "未设置校准", type: "info" };
      }
      const last = new Date(row.lastCheckDate);
      const next = new Date(last.getTime() + row.checkCycleDays * 24 * 3600 * 1000);
      const nextText = next.toISOString().slice(0, 10);
      const days = Math.floor((new Date() - next) / (24 * 3600 * 1000));
      if (days > 0) {
        return { text: "已超期 " + days + " 天", type: "danger" };
      }
      if (days > -30) {
        return { text: "还有 " + (-days) + " 天到期", type: "warning" };
      }
      return { text: "正常（" + nextText + "）", type: "success" };
    },
    getList() {
      this.loading = true;
      listEquip(this.queryParams).then(response => {
        this.equipList = response.rows;
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
        equipId: null,
        equipCode: null,
        equipName: null,
        equipType: "0",
        model: null,
        location: null,
        buyDate: null,
        checkCycleDays: null,
        lastCheckDate: null,
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
      this.ids = selection.map(item => item.equipId);
      this.multiple = !selection.length;
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "新增设备";
    },
    handleUpdate(row) {
      this.reset();
      getEquip(row.equipId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改设备";
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.equipId != null) {
            updateEquip(this.form).then(() => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addEquip(this.form).then(() => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleDelete(row) {
      const equipIds = row.equipId || this.ids;
      this.$modal.confirm('是否确认删除该设备？').then(function () {
        return delEquip(equipIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    handleExport() {
      this.download('prod/equip/export', {
        ...this.queryParams
      }, `equip_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
