<template>
  <div class="app-container">
    <!-- 统计卡片 -->
    <el-row :gutter="16" class="mb8">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background: #2b6de822; color: #2b6de8"><i class="el-icon-office-building"></i></div>
            <div class="stat-info">
              <div class="stat-num">{{ summary.total || 0 }}</div>
              <div class="stat-label">企业总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background: #5b8ff022; color: #5b8ff0"><i class="el-icon-money"></i></div>
            <div class="stat-info">
              <div class="stat-num">{{ formatCapital(summary.totalCapital) }}</div>
              <div class="stat-label">总注册资本(万元)</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background: #e3be5a22; color: #e3be5a"><i class="el-icon-user"></i></div>
            <div class="stat-info">
              <div class="stat-num">{{ summary.avgEmployee || 0 }}</div>
              <div class="stat-label">平均员工数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background: #c99a2e22; color: #c99a2e"><i class="el-icon-collection"></i></div>
            <div class="stat-info">
              <div class="stat-num">{{ summary.industryCount || 0 }}</div>
              <div class="stat-label">覆盖行业数</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区 -->
    <el-row :gutter="16">
      <el-col :span="12">
        <el-card shadow="hover" header="企业类型占比">
          <div ref="typeChartRef" class="chart-box"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover" header="企业规模分布">
          <div ref="scaleChartRef" class="chart-box"></div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="16" style="margin-top: 16px">
      <el-col :span="16">
        <el-card shadow="hover" header="行业分布">
          <div ref="industryChartRef" class="chart-box"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" header="经营状态">
          <div ref="statusChartRef" class="chart-box"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from "echarts";
import { getStats } from "@/api/enterprise/dashboard";

export default {
  name: "Dashboard",
  data() {
    return {
      summary: {},
      entTypeDict: {},
      industryDict: {},
      scaleDict: {},
      statusDict: {}
    };
  },
  mounted() {
    this.loadDicts();
    window.addEventListener("resize", this.handleResize);
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.handleResize);
  },
  methods: {
    loadDicts() {
      this.getDicts("enterprise_type").then(response => {
        this.entTypeDict = Object.fromEntries(response.data.map(d => [d.dictValue, d.dictLabel]));
        this.loadStats();
      });
      this.getDicts("enterprise_industry").then(response => {
        this.industryDict = Object.fromEntries(response.data.map(d => [d.dictValue, d.dictLabel]));
      });
      this.getDicts("enterprise_scale").then(response => {
        this.scaleDict = Object.fromEntries(response.data.map(d => [d.dictValue, d.dictLabel]));
      });
      this.getDicts("enterprise_status").then(response => {
        this.statusDict = Object.fromEntries(response.data.map(d => [d.dictValue, d.dictLabel]));
      });
    },
    mapName(list, dict) {
      return list.map(item => ({ name: dict[item.name] || item.name || "未填写", value: Number(item.value) }));
    },
    formatCapital(val) {
      if (val === null || val === undefined) return 0;
      const n = Number(val);
      if (n >= 10000) return (n / 10000).toFixed(1) + "万";
      return Math.round(n);
    },
    handleResize() {
      ["typeChartRef", "scaleChartRef", "industryChartRef", "statusChartRef"].forEach(name => {
        const el = this.$refs[name];
        if (el && echarts.getInstanceByDom(el)) {
          echarts.getInstanceByDom(el).resize();
        }
      });
    },
    loadStats() {
      getStats().then(response => {
        const data = response;
        this.summary = data.summary || {};

        const typeChart = echarts.init(this.$refs.typeChartRef);
        typeChart.setOption({
          color: ["#2b6de8", "#f0d68a", "#5b8ff0", "#e9c46a", "#8fb0f5"],
          tooltip: { trigger: "item" },
          legend: { bottom: 0 },
          series: [{
            type: "pie",
            radius: ["40%", "65%"],
            center: ["50%", "45%"],
            avoidLabelOverlap: true,
            itemStyle: { borderRadius: 6, borderColor: "#fff", borderWidth: 2 },
            label: { formatter: "{b}: {c}" },
            data: this.mapName(data.entType || [], this.entTypeDict)
          }]
        });

        const scaleChart = echarts.init(this.$refs.scaleChartRef);
        scaleChart.setOption({
          color: ["#2b6de8", "#f0d68a", "#5b8ff0", "#e9c46a", "#8fb0f5"],
          tooltip: { trigger: "item" },
          legend: { bottom: 0 },
          series: [{
            type: "pie",
            radius: ["40%", "65%"],
            center: ["50%", "45%"],
            itemStyle: { borderRadius: 6, borderColor: "#fff", borderWidth: 2 },
            label: { formatter: "{b}: {c}" },
            data: this.mapName(data.scale || [], this.scaleDict)
          }]
        });

        const indData = this.mapName(data.industry || [], this.industryDict);
        const industryChart = echarts.init(this.$refs.industryChartRef);
        industryChart.setOption({
          tooltip: { trigger: "axis" },
          grid: { left: 40, right: 20, top: 20, bottom: 60 },
          xAxis: {
            type: "category",
            data: indData.map(d => d.name),
            axisLabel: { rotate: 30, interval: 0 }
          },
          yAxis: { type: "value", minInterval: 1 },
          series: [{
            type: "bar",
            barWidth: 30,
            itemStyle: { color: "#2b6de8", borderRadius: [4, 4, 0, 0] },
            label: { show: true, position: "top" },
            data: indData.map(d => d.value)
          }]
        });

        const statusChart = echarts.init(this.$refs.statusChartRef);
        statusChart.setOption({
          color: ["#2b6de8", "#f0d68a"],
          tooltip: { trigger: "item" },
          legend: { bottom: 0 },
          series: [{
            type: "pie",
            radius: ["55%", "75%"],
            center: ["50%", "45%"],
            label: { formatter: "{b}: {c}" },
            data: this.mapName(data.status || [], this.statusDict)
          }]
        });
      });
    }
  }
};
</script>

<style scoped>
.stat-card {
  display: flex;
  align-items: center;
  gap: 14px;
}
.stat-icon {
  width: 52px;
  height: 52px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 26px;
  flex-shrink: 0;
}
.stat-num {
  font-size: 24px;
  font-weight: 700;
  line-height: 1.2;
}
.stat-label {
  color: #909399;
  font-size: 13px;
}
.chart-box {
  width: 100%;
  height: 320px;
}
</style>
