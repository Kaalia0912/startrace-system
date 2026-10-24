<template>
  <div class="business-home" v-loading="loading">
    <section class="masthead">
      <div class="masthead-copy">
        <div class="eyebrow"><span></span>STARTrace · 经营工作台</div>
        <h1>{{ greeting }}，{{ displayName }}</h1>
        <p>{{ today }}，订单、客户与交期信息已汇总。</p>
      </div>
      <div class="masthead-actions">
        <el-button icon="el-icon-document" @click="openOrders">订单台账</el-button>
        <el-button type="primary" icon="el-icon-plus" @click="openSale('add')">新增订单</el-button>
      </div>
    </section>

    <section class="metric-grid" aria-label="本月经营指标">
      <button
        v-for="item in metrics"
        :key="item.label"
        type="button"
        class="metric-card"
        :class="'metric-card--' + item.tone"
        @click="openOrders"
      >
        <span class="metric-topline">
          <span class="metric-icon"><i :class="item.icon"></i></span>
          <span class="metric-label">{{ item.label }}</span>
        </span>
        <strong class="metric-value" :title="item.title">{{ item.value }}</strong>
        <span class="metric-hint">{{ item.hint }}</span>
      </button>
    </section>

    <div class="work-grid">
      <el-card shadow="never" class="panel recent-panel">
        <div slot="header" class="panel-header">
          <div>
            <h2>近期订单</h2>
            <p>按订单日期和录入顺序展示最近 6 条</p>
          </div>
          <el-button type="text" @click="openOrders">查看全部</el-button>
        </div>

        <el-table :data="recentOrders" class="order-table" @row-click="openRecentOrder">
          <el-table-column label="订单号" prop="orderNo" min-width="150" :show-overflow-tooltip="true" />
          <el-table-column label="客户" prop="customerName" min-width="150" :show-overflow-tooltip="true" />
          <el-table-column label="产品" prop="productName" min-width="150" :show-overflow-tooltip="true" />
          <el-table-column label="数量" min-width="100" align="right">
            <template slot-scope="scope">{{ formatQuantity(scope.row) }}</template>
          </el-table-column>
          <el-table-column label="金额（元）" min-width="120" align="right">
            <template slot-scope="scope">{{ formatAmount(scope.row.amount) }}</template>
          </el-table-column>
          <el-table-column label="交期" prop="deliveryDate" min-width="105" />
          <el-table-column label="" width="54" align="right">
            <template>
              <i class="el-icon-arrow-right row-arrow"></i>
            </template>
          </el-table-column>

          <div slot="empty" class="empty-state">
            <i class="el-icon-document-add"></i>
            <strong>还没有销售订单</strong>
            <p>录入第一张订单后，经营数据会自动汇总到这里。</p>
            <el-button type="primary" plain size="small" @click.stop="openSale('add')">新增订单</el-button>
          </div>
        </el-table>
      </el-card>

      <aside class="side-stack">
        <el-card shadow="never" class="panel attention-panel">
          <div slot="header" class="panel-header compact-header">
            <div>
              <h2>需要关注</h2>
              <p>订单资料与交期提示</p>
            </div>
          </div>

          <button
            v-if="incompleteOrderCount > 0"
            type="button"
            class="attention-row attention-row--warning"
            @click="openIncompleteOrders"
          >
            <span class="attention-icon"><i class="el-icon-warning-outline"></i></span>
            <span class="attention-copy">
              <strong>{{ incompleteOrderCount }} 张订单资料待完善</strong>
              <small>客户、金额或交期至少一项未填写</small>
            </span>
          </button>
          <div v-else class="attention-row attention-row--clear">
            <span class="attention-icon"><i class="el-icon-circle-check"></i></span>
            <span class="attention-copy">
              <strong>订单基础资料完整</strong>
              <small>当前没有客户、金额或交期缺失提示</small>
            </span>
          </div>

          <div class="delivery-note">
            <span>未来 30 天交期</span>
            <strong>{{ upcomingDeliveryCount }}</strong>
            <small>按交期日期统计，不包含订单完成状态</small>
          </div>
        </el-card>

        <el-card shadow="never" class="panel action-panel">
          <div slot="header" class="panel-header compact-header">
            <div>
              <h2>快捷操作</h2>
              <p>从经营工作台直接开始</p>
            </div>
          </div>

          <div class="action-grid">
            <button
              v-for="item in actionEntries"
              :key="item.label"
              type="button"
              class="action-tile"
              @click="handleAction(item)"
            >
              <span class="action-icon"><i :class="item.icon"></i></span>
              <span class="action-copy">
                <strong>{{ item.label }}</strong>
                <small>{{ item.description }}</small>
              </span>
              <i class="el-icon-arrow-right action-arrow"></i>
            </button>
          </div>
        </el-card>
      </aside>
    </div>
  </div>
</template>

<script>
import { getSaleOrderDashboard } from "@/api/order/saleOrder"

export default {
  name: "Index",
  data() {
    return {
      loading: true,
      dashboard: {},
      recentOrders: [],
      today: "",
      greeting: "",
      actionEntries: [
        { label: "新增销售订单", description: "录入客户与产品信息", icon: "el-icon-plus", path: "/order/sale", action: "add" },
        { label: "批量导入订单", description: "下载模板后批量录入", icon: "el-icon-upload2", path: "/order/sale", action: "import" },
        { label: "维护物料主数据", description: "统一产品、材质与规格", icon: "el-icon-box", path: "/basedata/material" },
        { label: "查看客户档案", description: "维护客户与联系人", icon: "el-icon-office-building", path: "/enterprise/enterprise" }
      ]
    }
  },
  computed: {
    displayName() {
      return this.$store.getters.nickName || this.$store.getters.name || "用户"
    },
    incompleteOrderCount() {
      return Number(this.dashboard.incompleteOrderCount || 0)
    },
    upcomingDeliveryCount() {
      return Number(this.dashboard.upcomingDeliveryCount || 0)
    },
    metrics() {
      const monthOrderCount = Number(this.dashboard.monthOrderCount || 0)
      const monthAmount = Number(this.dashboard.monthAmount || 0)
      const monthCustomerCount = Number(this.dashboard.monthCustomerCount || 0)
      return [
        {
          label: "本月订单",
          value: this.formatCount(monthOrderCount),
          hint: this.compareHint(monthOrderCount, this.dashboard.lastMonthOrderCount, "上月无订单"),
          icon: "el-icon-document",
          tone: "blue"
        },
        {
          label: "本月订单额（元）",
          value: this.formatCompactAmount(monthAmount),
          title: this.formatAmount(monthAmount),
          hint: this.compareHint(monthAmount, this.dashboard.lastMonthAmount, "上月无金额"),
          icon: "el-icon-coin",
          tone: "amber"
        },
        {
          label: "本月客户",
          value: this.formatCount(monthCustomerCount),
          hint: "按客户名称去重统计",
          icon: "el-icon-user",
          tone: "teal"
        },
        {
          label: "30 天内交期",
          value: this.formatCount(this.upcomingDeliveryCount),
          hint: "从今天起 30 天内的订单",
          icon: "el-icon-date",
          tone: "slate"
        }
      ]
    }
  },
  created() {
    this.initGreeting()
    this.loadDashboard()
  },
  methods: {
    initGreeting() {
      const now = new Date()
      const hour = now.getHours()
      this.greeting = hour < 6 ? "夜深了" : hour < 12 ? "上午好" : hour < 14 ? "中午好" : hour < 18 ? "下午好" : "晚上好"
      const weekdays = ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"]
      this.today = `${now.getFullYear()}年${now.getMonth() + 1}月${now.getDate()}日 · ${weekdays[now.getDay()]}`
    },
    loadDashboard() {
      this.loading = true
      getSaleOrderDashboard().then(response => {
        const data = response.data || {}
        this.dashboard = data
        this.recentOrders = data.recentOrders || []
      }).catch(() => {
        this.dashboard = {}
        this.recentOrders = []
      }).finally(() => {
        this.loading = false
      })
    },
    openOrders() {
      this.$router.push({ path: "/order/sale" })
    },
    openIncompleteOrders() {
      this.$router.push({ path: "/order/sale", query: { incompleteOnly: "true" } })
    },
    openSale(action) {
      this.$router.push({ path: "/order/sale", query: { action, t: Date.now() } })
    },
    openRecentOrder(row) {
      const query = row.orderNo ? { orderNo: row.orderNo } : {}
      this.$router.push({ path: "/order/sale", query })
    },
    handleAction(item) {
      if (item.action) {
        this.openSale(item.action)
        return
      }
      this.$router.push({ path: item.path })
    },
    compareHint(current, previous, emptyHint) {
      const currentValue = Number(current || 0)
      const previousValue = Number(previous || 0)
      if (previousValue <= 0) {
        return emptyHint
      }
      const percent = ((currentValue - previousValue) / previousValue) * 100
      if (Math.abs(percent) < 0.05) {
        return "与上月持平"
      }
      return `较上月${percent > 0 ? "增加" : "减少"} ${Math.abs(percent).toFixed(1)}%`
    },
    formatCount(value) {
      return Number(value || 0).toLocaleString("zh-CN")
    },
    formatCompactAmount(value) {
      const amount = Number(value || 0)
      if (amount >= 100000000) {
        return `${(amount / 100000000).toFixed(2)} 亿`
      }
      if (amount >= 10000) {
        return `${(amount / 10000).toFixed(1)} 万`
      }
      return this.formatAmount(amount)
    },
    formatAmount(value) {
      if (value === null || value === undefined || value === "") {
        return ""
      }
      return Number(value).toLocaleString("zh-CN", { minimumFractionDigits: 2, maximumFractionDigits: 2 })
    },
    formatQuantity(row) {
      if (row.quantity === null || row.quantity === undefined || row.quantity === "") {
        return ""
      }
      const quantity = Number(row.quantity).toLocaleString("zh-CN", { maximumFractionDigits: 3 })
      return row.unit ? `${quantity} ${row.unit}` : quantity
    }
  }
}
</script>

<style scoped>
.business-home {
  --ink-900: #17212b;
  --ink-700: #34424f;
  --ink-500: #687784;
  --line: #e3e8ed;
  --surface: #ffffff;
  --page: #f3f5f7;
  --blue: #1f5f9f;
  --blue-soft: #eaf2fa;
  --amber: #b77820;
  --amber-soft: #fbf3e5;
  --teal: #27776b;
  --teal-soft: #eaf5f2;
  min-height: calc(100vh - 84px);
  padding: 20px;
  color: var(--ink-900);
  background:
    linear-gradient(135deg, rgba(31, 95, 159, 0.04), transparent 34%),
    var(--page);
}

.masthead {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 32px;
  min-height: 170px;
  padding: 32px 36px;
  overflow: hidden;
  color: #fff;
  background: linear-gradient(112deg, #17212b 0%, #243441 62%, #354653 100%);
  border-radius: 10px;
}

.masthead::after {
  position: absolute;
  right: 18%;
  bottom: -88px;
  width: 320px;
  height: 220px;
  content: "";
  border: 1px solid rgba(255, 255, 255, 0.09);
  border-radius: 50%;
  box-shadow: 0 0 0 34px rgba(255, 255, 255, 0.035), 0 0 0 68px rgba(255, 255, 255, 0.02);
}

.masthead-copy,
.masthead-actions {
  position: relative;
  z-index: 1;
}

.eyebrow {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 16px;
  color: #d8b66e;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.16em;
}

.eyebrow span {
  width: 28px;
  height: 2px;
  background: #d8b66e;
}

.masthead h1 {
  margin: 0 0 10px;
  font-size: clamp(25px, 2.4vw, 34px);
  line-height: 1.2;
  letter-spacing: 0.02em;
}

.masthead p {
  margin: 0;
  color: rgba(255, 255, 255, 0.7);
  font-size: 14px;
}

.masthead-actions {
  display: flex;
  flex-shrink: 0;
  gap: 10px;
}

.masthead-actions .el-button {
  min-height: 40px;
  padding-inline: 18px;
  border-color: rgba(255, 255, 255, 0.24);
  color: #fff;
  background: rgba(255, 255, 255, 0.06);
}

.masthead-actions .el-button:hover,
.masthead-actions .el-button:focus {
  border-color: rgba(255, 255, 255, 0.52);
  color: #fff;
  background: rgba(255, 255, 255, 0.12);
}

.masthead-actions .el-button--primary {
  border-color: #d8b66e;
  color: #17212b;
  background: #e3c37d;
}

.masthead-actions .el-button--primary:hover,
.masthead-actions .el-button--primary:focus {
  border-color: #edd49b;
  color: #17212b;
  background: #edd49b;
}

.metric-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 14px;
  margin-top: 14px;
}

.metric-card {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  min-height: 152px;
  padding: 20px;
  text-align: left;
  background: var(--surface);
  border: 1px solid var(--line);
  border-radius: 8px;
  box-shadow: 0 1px 2px rgba(23, 33, 43, 0.025);
  cursor: pointer;
  transition: transform 160ms ease, border-color 160ms ease, box-shadow 160ms ease;
}

.metric-card:hover {
  border-color: #c8d1d9;
  box-shadow: 0 8px 22px rgba(23, 33, 43, 0.08);
  transform: translateY(-2px);
}

.metric-card:focus-visible,
.action-tile:focus-visible,
.attention-row:focus-visible {
  outline: 2px solid var(--blue);
  outline-offset: 2px;
}

.metric-topline {
  display: flex;
  align-items: center;
  gap: 10px;
  width: 100%;
}

.metric-icon {
  display: grid;
  width: 34px;
  height: 34px;
  place-items: center;
  border-radius: 7px;
  font-size: 17px;
}

.metric-label {
  color: var(--ink-500);
  font-size: 13px;
  font-weight: 600;
}

.metric-value {
  margin-top: 18px;
  font-size: clamp(27px, 2.6vw, 36px);
  font-variant-numeric: tabular-nums;
  line-height: 1;
  letter-spacing: -0.02em;
}

.metric-hint {
  margin-top: auto;
  padding-top: 14px;
  color: #7e8c98;
  font-size: 12px;
}

.metric-card--blue .metric-icon {
  color: var(--blue);
  background: var(--blue-soft);
}

.metric-card--amber .metric-icon {
  color: var(--amber);
  background: var(--amber-soft);
}

.metric-card--teal .metric-icon {
  color: var(--teal);
  background: var(--teal-soft);
}

.metric-card--slate .metric-icon {
  color: var(--ink-700);
  background: #edf1f4;
}

.work-grid {
  display: grid;
  grid-template-columns: minmax(0, 1.8fr) minmax(300px, 0.8fr);
  gap: 14px;
  margin-top: 14px;
}

.panel {
  border: 1px solid var(--line);
  border-radius: 8px;
}

.panel >>> .el-card__header {
  padding: 18px 20px;
  border-bottom-color: var(--line);
}

.panel >>> .el-card__body {
  padding: 0;
}

.panel-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.panel-header h2 {
  margin: 0;
  color: var(--ink-900);
  font-size: 16px;
  font-weight: 700;
}

.panel-header p {
  margin: 5px 0 0;
  color: var(--ink-500);
  font-size: 12px;
}

.order-table {
  cursor: pointer;
}

.order-table >>> th.el-table__cell {
  height: 43px;
  color: #5d6b77;
  font-size: 12px;
  font-weight: 600;
  background: #fafbfc;
}

.order-table >>> td.el-table__cell {
  height: 54px;
  color: #26333e;
}

.order-table >>> .el-table__row:hover > td.el-table__cell {
  background: #f5f8fa;
}

.row-arrow {
  color: #a8b3bc;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 46px 20px 40px;
  color: var(--ink-500);
}

.empty-state > i {
  margin-bottom: 14px;
  color: #a8b4bd;
  font-size: 38px;
}

.empty-state strong {
  color: var(--ink-700);
  font-size: 15px;
}

.empty-state p {
  margin: 7px 0 16px;
  font-size: 13px;
}

.side-stack {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.compact-header {
  padding-bottom: 0;
}

.attention-panel >>> .el-card__body,
.action-panel >>> .el-card__body {
  padding: 16px 18px 18px;
}

.attention-row {
  display: flex;
  align-items: center;
  gap: 12px;
  width: 100%;
  min-height: 68px;
  padding: 12px;
  text-align: left;
  border: 1px solid transparent;
  border-radius: 7px;
}

button.attention-row {
  cursor: pointer;
}

.attention-row--warning {
  color: #74460d;
  background: #fdf6e9;
  border-color: #f1ddb6;
}

.attention-row--clear {
  color: #1e6257;
  background: #f0f8f5;
  border-color: #d6ebe5;
}

.attention-icon {
  display: grid;
  width: 34px;
  height: 34px;
  flex: 0 0 34px;
  place-items: center;
  font-size: 20px;
}

.attention-copy {
  display: flex;
  min-width: 0;
  flex-direction: column;
  gap: 4px;
}

.attention-copy strong {
  font-size: 13px;
}

.attention-copy small {
  color: inherit;
  font-size: 12px;
  line-height: 1.45;
  opacity: 0.72;
}

.delivery-note {
  display: grid;
  grid-template-columns: 1fr auto;
  gap: 4px 12px;
  margin-top: 16px;
  padding: 15px 2px 0;
  border-top: 1px solid var(--line);
}

.delivery-note span {
  color: var(--ink-500);
  font-size: 13px;
}

.delivery-note strong {
  color: var(--ink-900);
  font-size: 20px;
  line-height: 1;
}

.delivery-note small {
  grid-column: 1 / -1;
  margin-top: 5px;
  color: #87949f;
  font-size: 11px;
}

.action-grid {
  display: grid;
  gap: 4px;
}

.action-tile {
  display: flex;
  align-items: center;
  gap: 11px;
  width: 100%;
  min-height: 58px;
  padding: 8px 6px;
  text-align: left;
  background: transparent;
  border: 0;
  border-radius: 6px;
  cursor: pointer;
}

.action-tile:hover {
  background: #f5f7f9;
}

.action-icon {
  display: grid;
  width: 34px;
  height: 34px;
  flex: 0 0 34px;
  place-items: center;
  color: var(--ink-700);
  background: #eef2f5;
  border-radius: 6px;
  font-size: 16px;
}

.action-copy {
  display: flex;
  min-width: 0;
  flex: 1;
  flex-direction: column;
  gap: 3px;
}

.action-copy strong {
  color: #26333e;
  font-size: 13px;
}

.action-copy small {
  overflow: hidden;
  color: #85919b;
  font-size: 11px;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.action-arrow {
  color: #b1bbc3;
}

@media (max-width: 1180px) {
  .metric-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .work-grid {
    grid-template-columns: 1fr;
  }

  .side-stack {
    display: grid;
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 720px) {
  .business-home {
    padding: 12px;
  }

  .masthead {
    align-items: flex-start;
    flex-direction: column;
    min-height: 0;
    padding: 26px 22px;
  }

  .masthead-actions {
    width: 100%;
  }

  .masthead-actions .el-button {
    flex: 1;
  }

  .metric-grid,
  .side-stack {
    grid-template-columns: 1fr;
  }

  .metric-card {
    min-height: 138px;
  }
}

@media (prefers-reduced-motion: reduce) {
  .metric-card {
    transition: none;
  }
}
</style>
