<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="钢种" prop="steelGrade">
        <el-input v-model="queryParams.steelGrade" placeholder="请输入钢种" clearable style="width: 160px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="state">
        <el-select v-model="queryParams.state" placeholder="请选择状态" clearable style="width: 140px">
          <el-option v-for="item in stateOptions" :key="item" :label="item" :value="item" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd" v-hasPermi="['basedata:property:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['basedata:property:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" @click="handleExport" v-hasPermi="['basedata:property:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="propertyList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="钢种" align="center" prop="steelGrade" width="120" />
      <el-table-column label="状态" align="center" prop="state" width="100" />
      <el-table-column label="屈服下限(MPa)" align="center" prop="yieldMin" width="130" />
      <el-table-column label="抗拉下限(MPa)" align="center" prop="tensileMin" width="130" />
      <el-table-column label="延伸率下限(%)" align="center" prop="elongationMin" width="130" />
      <el-table-column label="硬度HV" align="center" width="120">
        <template slot-scope="scope">{{ hvRange(scope.row) }}</template>
      </el-table-column>
      <el-table-column label="是否启用" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === '0'" type="success">正常</el-tag>
          <el-tag v-else type="info">停用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="140" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['basedata:property:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['basedata:property:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改钢种性能标准对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="620px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="130px">
        <el-form-item label="钢种" prop="steelGrade">
          <el-input v-model="form.steelGrade" placeholder="如 SUS304" />
        </el-form-item>
        <el-form-item label="状态" prop="state">
          <el-select v-model="form.state" placeholder="请选择状态" filterable allow-create style="width: 100%">
            <el-option v-for="item in stateOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="屈服强度下限(MPa)" prop="yieldMin">
          <el-input-number v-model="form.yieldMin" :precision="2" :min="0" controls-position="right" style="width: 100%" />
        </el-form-item>
        <el-form-item label="抗拉强度下限(MPa)" prop="tensileMin">
          <el-input-number v-model="form.tensileMin" :precision="2" :min="0" controls-position="right" style="width: 100%" />
        </el-form-item>
        <el-form-item label="延伸率下限(%)" prop="elongationMin">
          <el-input-number v-model="form.elongationMin" :precision="2" :min="0" controls-position="right" style="width: 100%" />
        </el-form-item>
        <el-form-item label="硬度下限HV" prop="hvMin">
          <el-input-number v-model="form.hvMin" :precision="2" :min="0" controls-position="right" style="width: 100%" />
        </el-form-item>
        <el-form-item label="硬度上限HV" prop="hvMax">
          <el-input-number v-model="form.hvMax" :precision="2" :min="0" controls-position="right" style="width: 100%" />
        </el-form-item>
        <el-form-item label="是否启用" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="0">正常</el-radio>
            <el-radio label="1">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listSteelProperty, getSteelProperty, delSteelProperty, addSteelProperty, updateSteelProperty } from "@/api/basedata/steelProperty";

export default {
  name: "SteelProperty",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 钢种性能标准表格数据
      propertyList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 常用状态选项
      stateOptions: ["ANN", "1/4H", "1/2H", "3/4H", "FH", "EH", "SH", "H"],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        steelGrade: null,
        state: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        steelGrade: [{ required: true, message: "钢种不能为空", trigger: "blur" }],
        state: [{ required: true, message: "状态不能为空", trigger: "change" }]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 硬度区间展示 */
    hvRange(row) {
      const min = row.hvMin;
      const max = row.hvMax;
      if (min != null && max != null) {
        return min + "~" + max;
      }
      if (min != null) {
        return "≥" + min;
      }
      if (max != null) {
        return "≤" + max;
      }
      return "-";
    },
    /** 查询钢种性能标准列表 */
    getList() {
      this.loading = true;
      listSteelProperty(this.queryParams).then(response => {
        this.propertyList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        propertyId: null,
        steelGrade: null,
        state: null,
        yieldMin: null,
        tensileMin: null,
        elongationMin: null,
        hvMin: null,
        hvMax: null,
        status: "0",
        remark: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryRef");
      this.handleQuery();
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.propertyId);
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加钢种性能标准";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const propertyId = row.propertyId;
      getSteelProperty(propertyId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改钢种性能标准";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.propertyId != null) {
            updateSteelProperty(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addSteelProperty(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const propertyIds = row.propertyId || this.ids;
      this.$modal.confirm('是否确认删除该性能标准数据？').then(function () {
        return delSteelProperty(propertyIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('basedata/property/export', {
        ...this.queryParams
      }, `steel_property_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
