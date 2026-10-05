<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="物料编码" prop="materialCode">
        <el-input v-model="queryParams.materialCode" placeholder="请输入物料编码" clearable style="width: 180px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="产品名称" prop="productName">
        <el-input v-model="queryParams.productName" placeholder="请输入产品名称" clearable style="width: 180px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="材质" prop="material">
        <el-input v-model="queryParams.material" placeholder="请输入材质" clearable style="width: 150px" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 120px">
          <el-option label="正常" value="0" />
          <el-option label="停用" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd" v-hasPermi="['basedata:material:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['basedata:material:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" @click="handleExport" v-hasPermi="['basedata:material:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="materialList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="物料编码" align="center" prop="materialCode" :show-overflow-tooltip="true" min-width="180" />
      <el-table-column label="产品名称" align="center" prop="productName" :show-overflow-tooltip="true" min-width="130" />
      <el-table-column label="材质" align="center" prop="material" width="100" />
      <el-table-column label="规格型号(mm)" align="center" prop="spec" width="130" />
      <el-table-column label="执行标准" align="center" prop="standard" width="110" />
      <el-table-column label="硬度(HV)" align="center" prop="hardness" width="100" />
      <el-table-column label="抗拉(MPa)" align="center" prop="tensileStrength" width="100" />
      <el-table-column label="单卷重(kg)" align="center" width="120">
        <template slot-scope="scope">{{ weightRange(scope.row) }}</template>
      </el-table-column>
      <el-table-column label="原料单价(元/kg)" align="center" prop="rawPrice" width="130" />
      <el-table-column label="单位" align="center" prop="unit" width="70" />
      <el-table-column label="状态" align="center" prop="status" width="80">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === '0'" type="success">正常</el-tag>
          <el-tag v-else type="info">停用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="140" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['basedata:material:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['basedata:material:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改物料主数据对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="780px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="物料编码" prop="materialCode">
              <el-input v-model="form.materialCode" placeholder="请输入物料编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="产品名称" prop="productName">
              <el-input v-model="form.productName" placeholder="请输入产品名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="材质" prop="material">
              <el-input v-model="form.material" placeholder="如 SUS304" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="规格型号(mm)" prop="spec">
              <el-input v-model="form.spec" placeholder="如 0.1*400" />
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
            <el-form-item label="卷内径(mm)" prop="coilInnerDia">
              <el-input-number v-model="form.coilInnerDia" :precision="2" :step="1" :min="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单位" prop="unit">
              <el-input v-model="form.unit" placeholder="如 kg" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行标准" prop="standard">
              <el-input v-model="form.standard" placeholder="如 JIS G4305" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="硬度(HV)" prop="hardness">
              <el-input v-model="form.hardness" placeholder="如 270~300" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="抗拉(MPa)" prop="tensileStrength">
              <el-input v-model="form.tensileStrength" placeholder="如 520~620" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="表面精度Ra" prop="surfaceRa">
              <el-input v-model="form.surfaceRa" placeholder="如 Ra0.3-0.4" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单卷重下限" prop="coilWeightMin">
              <el-input-number v-model="form.coilWeightMin" :precision="2" :step="10" :min="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单卷重上限" prop="coilWeightMax">
              <el-input-number v-model="form.coilWeightMax" :precision="2" :step="10" :min="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="包装方式" prop="packageCode">
              <el-input v-model="form.packageCode" placeholder="如 B01" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="原料单价(元/kg)" prop="rawPrice">
              <el-input-number v-model="form.rawPrice" :precision="2" :min="0" controls-position="right" style="width: 100%" />
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
import { listMaterial, getMaterial, delMaterial, addMaterial, updateMaterial } from "@/api/basedata/material";

export default {
  name: "Material",
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
      // 物料主数据表格数据
      materialList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        materialCode: null,
        productName: null,
        material: null,
        status: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        materialCode: [{ required: true, message: "物料编码不能为空", trigger: "blur" }],
        productName: [{ required: true, message: "产品名称不能为空", trigger: "blur" }]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 单卷重区间展示 */
    weightRange(row) {
      const min = row.coilWeightMin;
      const max = row.coilWeightMax;
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
    /** 查询物料主数据列表 */
    getList() {
      this.loading = true;
      listMaterial(this.queryParams).then(response => {
        this.materialList = response.rows;
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
        materialId: null,
        materialCode: null,
        productName: null,
        material: null,
        spec: null,
        thickness: null,
        width: null,
        coilInnerDia: null,
        unit: null,
        standard: null,
        hardness: null,
        tensileStrength: null,
        surfaceRa: null,
        coilWeightMin: null,
        coilWeightMax: null,
        packageCode: null,
        rawPrice: null,
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
      this.ids = selection.map(item => item.materialId);
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加物料主数据";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const materialId = row.materialId;
      getMaterial(materialId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改物料主数据";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.materialId != null) {
            updateMaterial(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMaterial(this.form).then(response => {
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
      const materialIds = row.materialId || this.ids;
      this.$modal.confirm('是否确认删除该物料数据？').then(function () {
        return delMaterial(materialIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('basedata/material/export', {
        ...this.queryParams
      }, `material_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
