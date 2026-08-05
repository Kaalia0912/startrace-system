<template>
  <div class="dashboard-home">
    <!-- 顶部横幅 -->
    <div class="hero-banner">
      <div class="hero-overlay"></div>
      <div class="hero-content">
        <h2 class="hero-greet">{{ greeting }}，{{ name }}</h2>
        <p class="hero-date">{{ today }} · 欢迎回来</p>
      </div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="16" class="mt16">
      <el-col :span="6" v-for="item in statCards" :key="item.label">
        <el-card shadow="hover" class="stat-card" @click.native="goStat(item.path)">
          <div class="stat-card-inner">
            <div class="stat-icon" :style="{ background: item.bg, color: item.color }">
              <i :class="item.icon"></i>
            </div>
            <div class="stat-info">
              <div class="stat-num">{{ item.value }}</div>
              <div class="stat-label">{{ item.label }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 快捷入口 -->
    <el-row :gutter="16" class="mt16">
      <el-col :span="8" v-for="entry in quickEntries" :key="entry.title">
        <el-card shadow="hover" class="quick-card" @click.native="$router.push(entry.path)">
          <div class="quick-inner">
            <div class="quick-icon" :style="{ background: entry.bg, color: entry.color }">
              <i :class="entry.icon"></i>
            </div>
            <div class="quick-text">
              <div class="quick-title">{{ entry.title }}</div>
              <div class="quick-desc">{{ entry.desc }}</div>
            </div>
            <i class="el-icon-arrow-right quick-arrow"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 最近企业 + 行业分布 -->
    <el-row :gutter="16" class="mt16">
      <el-col :span="14">
        <el-card shadow="never" header="最近新增企业">
          <el-table :data="recentList" size="small" @row-click="goDetail" class="recent-table">
            <el-table-column label="企业名称" prop="entName" :show-overflow-tooltip="true" min-width="140" />
            <el-table-column label="行业" prop="industry" width="100">
              <template slot-scope="scope">
                <dict-tag :options="industryOptions" :value="scope.row.industry" />
              </template>
            </el-table-column>
            <el-table-column label="类型" prop="entType" width="110">
              <template slot-scope="scope">
                <dict-tag :options="entTypeOptions" :value="scope.row.entType" />
              </template>
            </el-table-column>
            <el-table-column label="注册资本(万)" prop="registeredCapital" width="110" />
            <el-table-column label="操作" width="60" align="center">
              <template slot-scope="scope">
                <el-button size="mini" type="text" @click.stop="goDetail(scope.row)">详情</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never" header="行业分布">
          <div ref="industryChartRef" class="mini-chart"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from "echarts";
import { getStats } from "@/api/enterprise/dashboard";
import { listEnterprise } from "@/api/enterprise/enterprise";
import { listContact } from "@/api/enterprise/contact";

export default {
  name: "Index",
  data() {
    return {
      name: "",
      today: "",
      greeting: "",
      summary: {},
      contactTotal: 0,
      recentList: [],
      industryOptions: [],
      entTypeOptions: [],
      statCards: [
        { label: "企业总数", value: 0, icon: "el-icon-office-building", color: "#2b6de8", bg: "#2b6de822", path: "/enterprise/enterprise?status=all" },
        { label: "联系人", value: 0, icon: "el-icon-user", color: "#5b8ff0", bg: "#5b8ff022", path: "/enterprise/contact" },
        { label: "在营企业", value: 0, icon: "el-icon-circle-check", color: "#e3be5a", bg: "#e3be5a22", path: "/enterprise/enterprise?status=0" },
        { label: "覆盖行业", value: 0, icon: "el-icon-collection", color: "#c99a2e", bg: "#c99a2e22", path: "/enterprise/dashboard" }
      ],
      quickEntries: [
        { title: "企业档案", desc: "管理企业基本信息", icon: "el-icon-office-building", color: "#2b6de8", bg: "#2b6de81a", path: "/enterprise/enterprise" },
        { title: "企业联系人", desc: "维护企业联系人", icon: "el-icon-user", color: "#5b8ff0", bg: "#5b8ff01a", path: "/enterprise/contact" },
        { title: "统计看板", desc: "数据图表分析", icon: "el-icon-pie-chart", color: "#e3be5a", bg: "#e3be5a1a", path: "/enterprise/dashboard" }
      ]
    };
  },
  created() {
    this.initGreeting();
    this.loadDicts();
    this.loadData();
  },
  mounted() {
    this.$nextTick(() => {});
  },
  methods: {
    initGreeting() {
      this.name = this.$store.getters.nickName || this.$store.getters.name || "admin";
      const now = new Date();
      const h = now.getHours();
      if (h < 6) this.greeting = "夜深了";
      else if (h < 9) this.greeting = "早上好";
      else if (h < 12) this.greeting = "上午好";
      else if (h < 14) this.greeting = "中午好";
      else if (h < 18) this.greeting = "下午好";
      else this.greeting = "晚上好";
      this.today = now.getFullYear() + "年" + (now.getMonth() + 1) + "月" + now.getDate() + "日";
    },
    loadDicts() {
      this.getDicts("enterprise_industry").then(response => {
        this.industryOptions = response.data;
        if (this.pendingIndustry) {
          this.renderIndustryChart(this.pendingIndustry);
        }
      });
      this.getDicts("enterprise_type").then(response => {
        this.entTypeOptions = response.data;
      });
    },
    loadData() {
      // 统计
      getStats().then(response => {
        const data = response;
        this.summary = data.summary || {};
        const status = data.status || [];
        const statusMap = Object.fromEntries(status.map(s => [s.name, Number(s.value)]));
        this.statCards[0].value = this.summary.total || 0;
        this.statCards[2].value = statusMap["0"] || 0;
        this.statCards[3].value = this.summary.industryCount || 0;
        // 行业迷你图
        this.pendingIndustry = data.industry || [];
        this.renderIndustryChart(this.pendingIndustry);
      });
      // 联系人总数
      listContact({ pageNum: 1, pageSize: 1 }).then(response => {
        this.statCards[1].value = response.total || 0;
      });
      // 最近企业
      listEnterprise({ pageNum: 1, pageSize: 5 }).then(response => {
        this.recentList = response.rows || [];
      });
    },
    renderIndustryChart(industry) {
      if (!this.$refs.industryChartRef) return;
      const chart = echarts.init(this.$refs.industryChartRef);
      const dict = Object.fromEntries(this.industryOptions.map(d => [d.dictValue, d.dictLabel]));
      const indData = industry.map(item => ({ name: dict[item.name] || item.name || "未填写", value: Number(item.value) }));
      chart.setOption({
        tooltip: { trigger: "axis" },
        grid: { left: 40, right: 16, top: 16, bottom: 30 },
        xAxis: {
          type: "category",
          data: indData.map(d => d.name),
          axisLabel: { rotate: 30, interval: 0, fontSize: 10 }
        },
        yAxis: { type: "value", minInterval: 1 },
        series: [{
          type: "bar",
          barWidth: 22,
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: "#e3be5a" },
              { offset: 1, color: "#c99a2e" }
            ]),
            borderRadius: [4, 4, 0, 0]
          },
          label: { show: true, position: "top", fontSize: 10 },
          data: indData.map(d => d.value)
        }]
      });
    },
    goStat(path) {
      if (path) this.$router.push(path);
    },
    goDetail(row) {
      this.$router.push("/enterprise/enterprise/detail/" + row.entId);
    }
  }
};
</script>

<style scoped>
.dashboard-home {
  margin: -20px;
}
/* 顶部横幅 */
.hero-banner {
  position: relative;
  height: 180px;
  background: url("/img/home.jpg") center/cover no-repeat;
  border-radius: 0;
  overflow: hidden;
}
.hero-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(90deg, rgba(8, 18, 45, 0.82) 0%, rgba(8, 18, 45, 0.45) 60%, rgba(8, 18, 45, 0.25) 100%);
}
.hero-content {
  position: relative;
  z-index: 1;
  padding: 46px 32px;
}
.hero-greet {
  margin: 0 0 10px 0;
  color: #fff;
  font-size: 30px;
  font-weight: 800;
  letter-spacing: 2px;
  text-shadow: 0 2px 14px rgba(0, 0, 0, 0.55);
}
.hero-date {
  margin: 0;
  color: #e3be5a;
  font-size: 14px;
  letter-spacing: 1px;
}
/* 间距 */
.mt16 {
  margin-top: 16px;
}
/* 统计卡片 */
.stat-card {
  cursor: pointer;
  border-radius: 12px;
  transition: transform 0.2s;
}
.stat-card:hover {
  transform: translateY(-3px);
}
.stat-card-inner {
  display: flex;
  align-items: center;
  gap: 14px;
}
.stat-icon {
  width: 52px;
  height: 52px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 26px;
  flex-shrink: 0;
}
.stat-num {
  font-size: 26px;
  font-weight: 800;
  line-height: 1.2;
  color: #1f2d3d;
}
.stat-label {
  color: #909399;
  font-size: 13px;
}
/* 快捷入口 */
.quick-card {
  cursor: pointer;
  border-radius: 12px;
  transition: transform 0.2s, box-shadow 0.2s;
}
.quick-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}
.quick-inner {
  display: flex;
  align-items: center;
  gap: 14px;
}
.quick-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  flex-shrink: 0;
}
.quick-title {
  font-size: 16px;
  font-weight: 700;
  color: #1f2d3d;
}
.quick-desc {
  font-size: 12px;
  color: #909399;
  margin-top: 3px;
}
.quick-arrow {
  margin-left: auto;
  color: #c0c4cc;
}
/* 迷你图 */
.mini-chart {
  width: 100%;
  height: 285px;
}
.recent-table {
  cursor: pointer;
}
</style>
