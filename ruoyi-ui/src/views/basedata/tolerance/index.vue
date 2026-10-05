<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="标准体系" prop="standard">
        <el-select v-model="queryParams.standard" placeholder="请选择标准体系" clearable style="width: 180px">
          <el-option v-for="item in standardOptions" :key="item" :label="item" :value="item" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd" v-hasPermi="['basedata:tolerance:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['basedata:tolerance:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" @click="handleExport" v-hasPermi="['basedata:tolerance:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="toleranceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="标准体系" align="center" prop="standard" width="140" />
      <el-table-column label="厚度范围(mm)" align="center" width="180">
        <template slot-scope="scope">{{ rangeText(scope.row.thicknessMin, scope.row.thicknessMax) }}</template>
      </el-table-column>
      <el-table-column label="宽度范围(mm)" align="center" width="180">
        <template slot-scope="scope">{{ rangeText(scope.row.widthMin, scope.row.widthMax) }}</template>
      </el-table-column>
      <el-table-column label="下偏差(mm)" align="center" prop="devLower" width="120" />
      <el-table-column label="上偏差(mm)" align="center" prop="devUpper" width="120" />
      <el-table-column label="是否启用" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === '0'" type="success">正常</el-tag>
          <el-tag v-else type="info">停用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="140" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['basedata:tolerance:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['basedata:tolerance:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改宽度公差标准对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="620px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="130px">
        <el-form-item label="标准体系" prop="standard">
          <el-select v-model="form.standard" placeholder="请选择或输入标准体系" filterable allow-create style="width: 100%">
            <el-option v-for="item in standardOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="厚度下限(mm，含)" prop="thicknessMin">
          <el-input-number v-model="form.thicknessMin" :precision="3" :step="0.01" :min="0" controls-position="right" style="width: 100%" />
        </el-form-item>
        <el-form-item label="厚度上限(mm，不含)" prop="thicknessMax">
          <el-input-number v-model="form.thicknessMax" :precision="3" :step="0.01" :min="0" controls-position="right" style="width: 100%" />
        </el-form-item>
        <el-form-item label="宽度下限(mm，含)" prop="widthMin">
          <el-input-number v-model="form.widthMin" :precision="2" :step="1" :min="0" controls-position="right" style="width: 100%" />
        </el-form-item>
        <el-form-item label="宽度上限(mm，不含)" prop="widthMax">
          <el-input-number v-model="form.widthMax" :precision="2" :step="1" :min="0" controls-position="right" style="width: 100%" />
        </el-form-item>
        <el-form-item label="下偏差(mm)" prop="devLower">
          <el-input-number v-model="form.devLower" :precision="3" :step="0.05" controls-position="right" style="width: 100%" />
        </el-form-item>
        <el-form-item label="上偏差(mm)" prop="devUpper">
          <el-input-number v-model="form.devUpper" :precision="3" :step="0.05" controls-position="right" style="width: 100%" />
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
import { listTolerance, getTolerance, delTolerance, addTolerance, updateTolerance } from "@/api/basedata/tolerance";

export default {
  name: "Tolerance",
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
      // 宽度公差标准表格数据
      toleranceList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 标准体系选项
      standardOptions: ["JIS G4305", "ASTM A480", "GB/T 4239", "无锡宝浦"],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        standard: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        standard: [{ required: true, message: "标准体系不能为空", trigger: "change" }]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 区间展示：[下限, 上限) */
    rangeText(min, max) {
      if (min != null && max != null) {
        return min + " ~ " + max;
      }
      if (min != null) {
        return "≥ " + min;
      }
      if (max != null) {
        return "< " + max;
      }
      return "-";
    },
    /** 查询宽度公差标准列表 */
    getList() {
      this.loading = true;
      listTolerance(this.queryParams).then(response => {
        this.toleranceList = response.rows;
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
        toleranceId: null,
        standard: null,
        thicknessMin: null,
        thicknessMax: null,
        widthMin: null,
        widthMax: null,
        devLower: null,
        devUpper: null,
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
      this.ids = selection.map(item => item.toleranceId);
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加宽度公差标准";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const toleranceId = row.toleranceId;
      getTolerance(toleranceId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改宽度公差标准";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.toleranceId != null) {
            updateTolerance(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTolerance(this.form).then(response => {
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
      const toleranceIds = row.toleranceId || this.ids;
      this.$modal.confirm('是否确认删除该公差标准数据？').then(function () {
        return delTolerance(toleranceIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('basedata/tolerance/export', {
        ...this.queryParams
      }, `tolerance_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
