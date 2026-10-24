<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="订单号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入订单号"
          clearable
          style="width: 180px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="客户名称" prop="customerName">
        <el-input
          v-model="queryParams.customerName"
          placeholder="请输入客户名称"
          clearable
          style="width: 200px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="产品名称" prop="productName">
        <el-input
          v-model="queryParams.productName"
          placeholder="请输入产品名称"
          clearable
          style="width: 180px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="日期" prop="orderDate">
        <el-date-picker
          v-model="queryParams.orderDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择日期"
          clearable
          style="width: 150px"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['order:sale:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['order:sale:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['order:sale:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['order:sale:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['order:sale:import']"
        >导入</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="saleOrderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="45" align="center" />
      <el-table-column label="订单号" align="center" prop="orderNo" min-width="120" :show-overflow-tooltip="true" />
      <el-table-column label="日期" align="center" prop="orderDate" min-width="95" />
      <el-table-column label="客户名称" align="center" prop="customerName" min-width="140" :show-overflow-tooltip="true" />
      <el-table-column label="材质" align="center" prop="material" min-width="75" :show-overflow-tooltip="true" />
      <el-table-column label="产品名称" align="center" prop="productName" min-width="140" :show-overflow-tooltip="true" />
      <el-table-column label="数量" align="right" min-width="95">
        <template slot-scope="scope">{{ fmtQty(scope.row.quantity) }}</template>
      </el-table-column>
      <el-table-column label="金额" align="right" min-width="115">
        <template slot-scope="scope">
          <span v-if="scope.row.amount !== null && scope.row.amount !== undefined && scope.row.amount !== ''">{{ fmtAmount(scope.row.amount) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="交期" align="center" prop="deliveryDate" min-width="95" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="115">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            title="详情"
            @click="handleDetail(scope.row)"
            v-hasPermi="['order:sale:query']"
          ></el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            title="修改"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['order:sale:edit']"
          ></el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            title="删除"
            @click="handleDelete(scope.row)"
            v-hasPermi="['order:sale:remove']"
          ></el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 新增 / 修改 -->
    <el-drawer
      :title="title"
      :visible.sync="open"
      size="62%"
      append-to-body
      :before-close="handleCloseDrawer"
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-divider content-position="left">客户信息</el-divider>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="客户名称" prop="customerName">
              <el-select
                v-model="form.customerName"
                placeholder="从企业档案选择，或直接输入"
                filterable
                allow-create
                default-first-option
                clearable
                style="width: 100%"
                @change="onCustomerChange"
              >
                <el-option v-for="item in customerOptions" :key="item.entId" :label="item.entName" :value="item.entName" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系人" prop="contactPerson">
              <el-input v-model="form.contactPerson" placeholder="请输入联系人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="contactPhone">
              <el-input v-model="form.contactPhone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系地址" prop="contactAddress">
              <el-input v-model="form.contactAddress" placeholder="请输入联系地址" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">订单信息</el-divider>
        <el-row :gutter="16">
          <el-col :span="8">
            <el-form-item label="日期" prop="orderDate">
              <el-date-picker
                v-model="form.orderDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="订单号" prop="orderNo">
              <el-input v-model="form.orderNo" disabled placeholder="保存后自动生成" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="交期" prop="deliveryDate">
              <el-date-picker
                v-model="form.deliveryDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择交期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">产品信息</el-divider>
        <el-row :gutter="16">
          <el-col :span="8">
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
                  v-for="item in filteredMaterialOptions"
                  :key="item.materialId"
                  :label="[item.materialCode, item.productName, item.material, item.spec].filter(Boolean).join(' · ')"
                  :value="item.materialCode"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="产品名称" prop="productName">
              <el-input v-model="form.productName" placeholder="请输入产品名称" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="材质" prop="material">
              <el-input v-model="form.material" placeholder="请输入材质" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="规格型号(mm)" prop="spec">
              <el-input v-model="form.spec" placeholder="例如 0.8×1219×2438" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="硬度(HV)" prop="hardness">
              <el-input v-model="form.hardness" placeholder="例如 180~220" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="抗拉(MPa)" prop="tensileStrength">
              <el-input v-model="form.tensileStrength" placeholder="例如 520~620" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="单位" prop="unit">
              <el-input v-model="form.unit" placeholder="例如 吨 / 张 / 米" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="数量" prop="quantity">
              <el-input-number
                v-model="form.quantity"
                :precision="3"
                :controls="false"
                placeholder="请输入数量"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="单价" prop="unitPrice">
              <el-input-number
                v-model="form.unitPrice"
                :precision="2"
                :controls="false"
                placeholder="请输入单价"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="金额" prop="amount">
              <el-input-number
                v-model="form.amount"
                :precision="2"
                :controls="false"
                placeholder="可自动计算或手动修改"
                style="width: 100%"
                @change="markAmountTouched"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">备注</el-divider>
        <el-row :gutter="16">
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input
                v-model="form.remark"
                type="textarea"
                :rows="3"
                placeholder="请输入备注"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div class="drawer-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-drawer>

    <!-- 详情 -->
    <el-drawer
      title="销售订单详情"
      :visible.sync="detailOpen"
      size="52%"
      append-to-body
    >
      <div class="detail-body">
        <el-divider content-position="left">客户信息</el-divider>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="客户名称">{{ detail.customerName }}</el-descriptions-item>
          <el-descriptions-item label="联系人">{{ detail.contactPerson }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ detail.contactPhone }}</el-descriptions-item>
          <el-descriptions-item label="联系地址">{{ detail.contactAddress }}</el-descriptions-item>
        </el-descriptions>

        <el-divider content-position="left">订单信息</el-divider>
        <el-descriptions :column="3" border>
          <el-descriptions-item label="日期">{{ detail.orderDate }}</el-descriptions-item>
          <el-descriptions-item label="订单号">{{ detail.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="交期">{{ detail.deliveryDate }}</el-descriptions-item>
        </el-descriptions>

        <el-divider content-position="left">产品信息</el-divider>
        <el-descriptions :column="3" border>
          <el-descriptions-item label="物料编码">{{ detail.materialCode }}</el-descriptions-item>
          <el-descriptions-item label="产品名称">{{ detail.productName }}</el-descriptions-item>
          <el-descriptions-item label="材质">{{ detail.material }}</el-descriptions-item>
          <el-descriptions-item label="规格型号(mm)">{{ detail.spec }}</el-descriptions-item>
          <el-descriptions-item label="硬度(HV)">{{ detail.hardness }}</el-descriptions-item>
          <el-descriptions-item label="抗拉(MPa)">{{ detail.tensileStrength }}</el-descriptions-item>
          <el-descriptions-item label="单位">{{ detail.unit }}</el-descriptions-item>
          <el-descriptions-item label="数量">{{ fmtQty(detail.quantity) }}</el-descriptions-item>
          <el-descriptions-item label="单价">{{ fmtAmount(detail.unitPrice) }}</el-descriptions-item>
          <el-descriptions-item label="金额">{{ fmtAmount(detail.amount) }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ detail.remark }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-drawer>

    <!-- 导入对话框 -->
    <el-dialog title="销售订单数据导入" :visible.sync="importOpen" width="420px" append-to-body>
      <el-form ref="importForm" :model="importForm" label-width="130px">
        <el-form-item label="更新已存在数据">
          <el-checkbox v-model="importForm.updateSupport">存在则更新</el-checkbox>
        </el-form-item>
        <el-form-item label="销售订单文件">
          <el-upload
            ref="upload"
            :auto-upload="false"
            :limit="1"
            accept=".xlsx, .xls"
            :file-list="fileList"
            :on-change="handleFileChange"
            :on-remove="handleFileRemove"
          >
            <el-button size="small" type="primary">选择文件</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="importFile">确 定</el-button>
        <el-button type="success" @click="downloadTemplate">下载模板</el-button>
        <el-button @click="importOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<style scoped>
.drawer-footer {
  text-align: center;
  padding-top: 10px;
}

.detail-body {
  padding-bottom: 20px;
}
</style>

<script>
import { listSaleOrder, getSaleOrder, delSaleOrder, addSaleOrder, updateSaleOrder, importSaleOrder, importTemplate } from "@/api/order/saleOrder"
import { listEnterprise } from "@/api/enterprise/enterprise"
import { listContact } from "@/api/enterprise/contact"
import { listMaterial } from "@/api/basedata/material"

export default {
  name: "SaleOrder",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 销售订单表格数据
      saleOrderList: [],
      // 客户下拉（企业档案）
      entOptions: [],
      // 物料下拉（物料主数据）
      materialOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示导入弹窗
      importOpen: false,
      // 导入参数
      importForm: { updateSupport: false },
      // 上传文件列表
      fileList: [],
      // 详情弹层
      detailOpen: false,
      detail: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNo: null,
        customerName: null,
        productName: null,
        orderDate: null
      },
      // 表单参数
      form: {},
      // 金额是否被手动修改过（true 后数量/单价变化不再覆盖）
      amountTouched: false,
      // 表单校验
      rules: {}
    }
  },
  computed: {
    // 导入数据中存在同名占位企业，下拉中合并显示
    customerOptions() {
      const byName = new Map()
      this.entOptions.forEach(item => {
        if (!item.entName) {
          return
        }
        const current = byName.get(item.entName)
        const isPlaceholder = value => (value.remark || "").startsWith("来自产品资料")
        if (!current || (isPlaceholder(current) && !isPlaceholder(item))) {
          byName.set(item.entName, item)
        }
      })
      return Array.from(byName.values()).sort((a, b) => a.entName.localeCompare(b.entName, "zh-CN"))
    },
    selectedCustomer() {
      return this.customerOptions.find(item => item.entName === this.form.customerName)
    },
    // 选中客户后只显示该客户的产品（物料编码以客户代码开头）
    filteredMaterialOptions() {
      const ent = this.selectedCustomer
      if (!ent || !ent.entCode) {
        return this.materialOptions
      }
      const rows = this.materialOptions.filter(item => (item.materialCode || "").startsWith(ent.entCode))
      return rows.length ? rows : this.materialOptions
    }
  },
  watch: {
    "form.quantity": function() {
      this.autoCalcAmount()
    },
    "form.unitPrice": function() {
      this.autoCalcAmount()
    }
  },
  created() {
    this.loadEntOptions()
    this.loadMaterialOptions()
    this.getList()
  },
  methods: {
    /** 加载客户下拉（企业档案） */
    loadEntOptions() {
      listEnterprise({ pageNum: 1, pageSize: 500 }).then(response => {
        this.entOptions = response.rows || []
      })
    },
    /** 加载物料下拉（物料主数据，仅正常状态） */
    loadMaterialOptions() {
      listMaterial({ pageNum: 1, pageSize: 2000, status: "0" }).then(response => {
        this.materialOptions = response.rows || []
      })
    },
    /** 选中客户后带出地址/电话，并取该企业的主要对接人 */
    onCustomerChange(name) {
      if (!name) {
        return
      }
      const ent = this.customerOptions.find(item => item.entName === name)
      if (!ent) {
        return
      }
      if (this.form.materialCode && !this.form.materialCode.startsWith(ent.entCode || "")) {
        this.form.materialCode = null
        this.form.productName = ""
        this.form.material = ""
        this.form.spec = ""
        this.form.hardness = ""
        this.form.tensileStrength = ""
        this.form.unit = ""
      }
      if (ent.address) {
        this.form.contactAddress = ent.address
      }
      if (ent.phone) {
        this.form.contactPhone = ent.phone
      }
      listContact({ entId: ent.entId, pageNum: 1, pageSize: 20 }).then(response => {
        const rows = response.rows || []
        const main = rows.find(item => item.isMain === "1") || rows[0]
        if (main) {
          this.form.contactPerson = main.contactName
          if (main.phone) {
            this.form.contactPhone = main.phone
          }
        }
      })
    },
    /** 选中物料后带出产品名称/材质/规格/硬度/抗拉/单位 */
    onMaterialChange(code) {
      if (!code) {
        return
      }
      const material = this.materialOptions.find(item => item.materialCode === code)
      if (!material) {
        return
      }
      this.form.productName = material.productName
      this.form.material = material.material
      this.form.spec = material.spec
      this.form.hardness = material.hardness
      this.form.tensileStrength = material.tensileStrength
      if (material.unit) {
        this.form.unit = material.unit
      }
    },
    /** 查询销售订单列表 */
    getList() {
      this.loading = true
      listSaleOrder(this.queryParams).then(response => {
        this.saleOrderList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 金额 = 数量 × 单价（自动计算）
    calcAmount() {
      const qty = this.form.quantity
      const price = this.form.unitPrice
      if (qty !== null && qty !== undefined && qty !== "" && price !== null && price !== undefined && price !== "") {
        this.form.amount = Number((qty * price).toFixed(2))
      } else {
        this.form.amount = null
      }
    },
    // 未手动改过金额时才自动重算
    autoCalcAmount() {
      if (!this.amountTouched) {
        this.calcAmount()
      }
    },
    // 手动修改金额后标记，避免被自动计算覆盖
    markAmountTouched() {
      this.amountTouched = true
    },
    // 编辑回显时：金额与数量×单价不一致，视为手动值，保留不覆盖
    syncAmountTouched() {
      const qty = this.form.quantity
      const price = this.form.unitPrice
      const amount = this.form.amount
      if (amount === null || amount === undefined || amount === "") {
        this.amountTouched = false
        return
      }
      if (qty === null || qty === undefined || qty === "" || price === null || price === undefined || price === "") {
        this.amountTouched = true
        return
      }
      const auto = Number((qty * price).toFixed(2))
      this.amountTouched = Math.abs(Number(amount) - auto) > 0.005
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        orderId: null,
        orderNo: null,
        orderDate: null,
        customerName: null,
        contactPerson: null,
        contactPhone: null,
        contactAddress: null,
        materialCode: null,
        productName: null,
        material: null,
        spec: null,
        hardness: null,
        tensileStrength: null,
        unit: null,
        quantity: null,
        unitPrice: null,
        amount: null,
        deliveryDate: null,
        remark: null
      }
      this.amountTouched = false
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.orderId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.title = "新增销售订单"
      this.open = true
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const orderId = row.orderId || this.ids
      this.title = "修改销售订单"
      this.open = true
      getSaleOrder(orderId).then(response => {
        this.form = response.data
        this.syncAmountTouched()
        this.$nextTick(() => {
          this.$refs.form.clearValidate()
        })
      })
    },
    /** 详情按钮操作 */
    handleDetail(row) {
      getSaleOrder(row.orderId).then(response => {
        this.detail = response.data || {}
        this.detailOpen = true
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.form.orderId != null) {
            updateSaleOrder(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addSaleOrder(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const orderIds = row.orderId || this.ids
      this.$modal.confirm('是否确认删除销售订单编号为"' + orderIds + '"的数据项？').then(function() {
        return delSaleOrder(orderIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('order/sale/export', {
        ...this.queryParams
      }, `sale_order_${new Date().getTime()}.xlsx`)
    },
    /** 导入按钮操作 */
    handleImport() {
      this.importOpen = true
      this.fileList = []
      this.importForm.updateSupport = false
      if (this.$refs.upload) {
        this.$refs.upload.clearFiles()
      }
    },
    /** 文件选择 */
    handleFileChange(file) {
      this.fileList = [file]
    },
    /** 文件移除 */
    handleFileRemove() {
      this.fileList = []
    },
    /** 提交导入 */
    importFile() {
      const file = this.fileList[0]
      if (!file) {
        this.$modal.msgError("请选择要导入的Excel文件")
        return
      }
      const formData = new FormData()
      formData.append("file", file.raw)
      formData.append("updateSupport", this.importForm.updateSupport)
      importSaleOrder(formData).then(response => {
        this.importOpen = false
        this.$modal.msgSuccess(response.msg)
        this.getList()
      })
    },
    /** 下载导入模板 */
    downloadTemplate() {
      importTemplate().then(response => {
        this.$modal.msgSuccess("模板下载成功")
        window.open(process.env.VUE_APP_BASE_API + response.data)
      })
    },
    handleCloseDrawer(done) {
      this.open = false
      done()
    },
    fmtQty(value) {
      if (value === null || value === undefined || value === "") return ""
      return Number(value).toLocaleString("zh-CN", { minimumFractionDigits: 0, maximumFractionDigits: 3 })
    },
    fmtAmount(value) {
      if (value === null || value === undefined || value === "") return ""
      return Number(value).toLocaleString("zh-CN", { minimumFractionDigits: 0, maximumFractionDigits: 2 })
    }
  }
}
</script>
