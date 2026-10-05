<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="工序名称" prop="processName">
        <el-input v-model="queryParams.processName" placeholder="请输入工序名称" clearable style="width: 180px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="质检点" prop="isQc">
        <el-select v-model="queryParams.isQc" placeholder="请选择" clearable style="width: 120px">
          <el-option label="是" value="1" />
          <el-option label="否" value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd" v-hasPermi="['basedata:process:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['basedata:process:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" @click="handleExport" v-hasPermi="['basedata:process:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="processList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="工序号" align="center" prop="processNo" width="90" />
      <el-table-column label="工序名称" align="center" prop="processName" width="160" />
      <el-table-column label="质检点" align="center" prop="isQc" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isQc === '1'" type="warning">质检点</el-tag>
          <el-tag v-else type="info">普通工序</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="控制要点" align="center" prop="remark" :show-overflow-tooltip="true" min-width="300" />
      <el-table-column label="是否启用" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === '0'" type="success">正常</el-tag>
          <el-tag v-else type="info">停用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="140" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['basedata:process:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['basedata:process:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改工序对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="620px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="工序号" prop="processNo">
          <el-input-number v-model="form.processNo" :precision="0" :min="1" :max="9999" controls-position="right" style="width: 100%" />
        </el-form-item>
        <el-form-item label="工序名称" prop="processName">
          <el-input v-model="form.processName" placeholder="如 分剪" />
        </el-form-item>
        <el-form-item label="是否质检点" prop="isQc">
          <el-radio-group v-model="form.isQc">
            <el-radio label="1">是</el-radio>
            <el-radio label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="控制要点" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="如：宽度213±0.10mm、毛刺高度≤0.03mm" />
        </el-form-item>
        <el-form-item label="是否启用" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="0">正常</el-radio>
            <el-radio label="1">停用</el-radio>
          </el-radio-group>
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
import { listProcess, getProcess, delProcess, addProcess, updateProcess } from "@/api/basedata/process";

export default {
  name: "Process",
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
      // 工序表格数据
      processList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        processName: null,
        isQc: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        processNo: [{ required: true, message: "工序号不能为空", trigger: "blur" }],
        processName: [{ required: true, message: "工序名称不能为空", trigger: "blur" }]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询工序列表 */
    getList() {
      this.loading = true;
      listProcess(this.queryParams).then(response => {
        this.processList = response.rows;
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
        processId: null,
        processNo: null,
        processName: null,
        isQc: "0",
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
      this.ids = selection.map(item => item.processId);
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加工序";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const processId = row.processId;
      getProcess(processId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改工序";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.processId != null) {
            updateProcess(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addProcess(this.form).then(response => {
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
      const processIds = row.processId || this.ids;
      this.$modal.confirm('是否确认删除该工序数据？').then(function () {
        return delProcess(processIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('basedata/process/export', {
        ...this.queryParams
      }, `process_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
