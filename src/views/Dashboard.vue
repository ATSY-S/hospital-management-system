﻿﻿﻿<template>
  <div class="dashboard-page">
    <div class="page-header">
      <div class="page-title-section">
        <h1 class="page-title">数据仪表盘</h1>
        <p class="page-subtitle">实时监控医院运营数据</p>
      </div>
      <div class="page-actions">
        <el-button @click="refreshData" :loading="loading" class="btn-refresh">
          <el-icon :size="16" class="mr-2"><Refresh /></el-icon>
          刷新
        </el-button>
      </div>
    </div>

    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon bg-blue">
          <el-icon :size="24"><Calendar /></el-icon>
        </div>
        <div class="stat-content">
          <p class="stat-value">{{ stats.todayRegister }}</p>
          <p class="stat-label">今日挂号</p>
          <div class="stat-trend" :class="trends.todayRegister > 0 ? 'up' : 'down'">
            <el-icon :size="12"><ArrowUpBold v-if="trends.todayRegister > 0" /><ArrowDownBold v-else /></el-icon>
            <span>{{ Math.abs(trends.todayRegister) }}%</span>
          </div>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon bg-green">
          <el-icon :size="24"><User /></el-icon>
        </div>
        <div class="stat-content">
          <p class="stat-value">{{ stats.inHospital }}</p>
          <p class="stat-label">在院患者</p>
          <div class="stat-trend" :class="trends.inHospital > 0 ? 'up' : 'down'">
            <el-icon :size="12"><ArrowUpBold v-if="trends.inHospital > 0" /><ArrowDownBold v-else /></el-icon>
            <span>{{ Math.abs(trends.inHospital) }}%</span>
          </div>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon bg-orange">
          <el-icon :size="24"><Document /></el-icon>
        </div>
        <div class="stat-content">
          <p class="stat-value">{{ stats.prescription }}</p>
          <p class="stat-label">处方数</p>
          <div class="stat-trend" :class="trends.prescription > 0 ? 'up' : 'down'">
            <el-icon :size="12"><ArrowUpBold v-if="trends.prescription > 0" /><ArrowDownBold v-else /></el-icon>
            <span>{{ Math.abs(trends.prescription) }}%</span>
          </div>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon bg-purple">
          <el-icon :size="24"><Wallet /></el-icon>
        </div>
        <div class="stat-content">
          <p class="stat-value">¥{{ stats.revenue.toLocaleString() }}</p>
          <p class="stat-label">营收</p>
          <div class="stat-trend" :class="trends.revenue > 0 ? 'up' : 'down'">
            <el-icon :size="12"><ArrowUpBold v-if="trends.revenue > 0" /><ArrowDownBold v-else /></el-icon>
            <span>{{ Math.abs(trends.revenue) }}%</span>
          </div>
        </div>
      </div>
    </div>

    <div class="charts-grid">
      <div class="chart-card">
        <div class="chart-header">
          <h3 class="chart-title">近7日挂号量趋势</h3>
        </div>
        <div ref="lineChartRef" class="chart-container"></div>
      </div>
      
      <div class="chart-card">
        <div class="chart-header">
          <h3 class="chart-title">各科室就诊人数对比</h3>
        </div>
        <div ref="barChartRef" class="chart-container"></div>
      </div>
    </div>

    <div class="charts-row">
      <div class="chart-card">
        <div class="chart-header">
          <h3 class="chart-title">患者年龄分布</h3>
        </div>
        <div ref="pieChartRef" class="chart-container"></div>
      </div>
      
      <div class="chart-card">
        <div class="chart-header">
          <h3 class="chart-title">医院运营状态</h3>
        </div>
        <div class="status-grid">
          <div class="status-item">
            <div class="status-circle normal"></div>
            <div class="status-info">
              <span class="status-value">{{ stats.normalHospitals }}</span>
              <span class="status-label">正常运营</span>
            </div>
          </div>
          <div class="status-item">
            <div class="status-circle pause"></div>
            <div class="status-info">
              <span class="status-value">{{ stats.pauseHospitals }}</span>
              <span class="status-label">暂停运营</span>
            </div>
          </div>
          <div class="status-item">
            <div class="status-circle top"></div>
            <div class="status-info">
              <span class="status-value">{{ stats.topHospitals }}</span>
              <span class="status-label">三甲医院</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="card">
      <div class="card-header">
        <h3 class="card-title">最近挂号记录</h3>
      </div>
      <div class="card-body">
        <el-table 
          :data="recentRegisterData" 
          border
          stripe
          :header-cell-style="{ background: '#F8FAFC', color: '#4E5969', fontWeight: '500' }"
          :row-class-name="getRowClassName"
        >
          <el-table-column prop="id" label="ID" width="80" align="center" />
          <el-table-column prop="patientName" label="患者姓名" width="120" />
          <el-table-column prop="departmentName" label="科室" width="120" />
          <el-table-column prop="doctorName" label="医生" width="120" />
          <el-table-column prop="registerDate" label="挂号日期" width="140" />
          <el-table-column prop="registerTime" label="挂号时间" width="120" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="row.status === '已就诊' ? 'success' : 'warning'">{{ row.status }}</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { Refresh, Calendar, User, Document, Wallet, ArrowUpBold, ArrowDownBold } from '@element-plus/icons-vue'

const lineChartRef = ref(null)
const barChartRef = ref(null)
const pieChartRef = ref(null)
let lineChartInstance = null
let barChartInstance = null
let pieChartInstance = null

const loading = ref(false)

const stats = reactive({
  todayRegister: 128,
  inHospital: 356,
  prescription: 892,
  revenue: 125800,
  normalHospitals: 5,
  pauseHospitals: 1,
  topHospitals: 2
})

const trends = reactive({
  todayRegister: 12.5,
  inHospital: -2.3,
  prescription: 8.7,
  revenue: 15.3
})

const registerTrendData = ref([
  { date: '1月9日', count: 85 },
  { date: '1月10日', count: 78 },
  { date: '1月11日', count: 92 },
  { date: '1月12日', count: 105 },
  { date: '1月13日', count: 110 },
  { date: '1月14日', count: 95 },
  { date: '1月15日', count: 130 }
])

const departmentCountData = ref([
  { name: '内科', value: 150 },
  { name: '外科', value: 98 },
  { name: '儿科', value: 120 },
  { name: '骨科', value: 75 },
  { name: '眼科', value: 45 },
  { name: '皮肤科', value: 82 }
])

const ageDistributionData = ref([
  { name: '0-18岁', value: 25 },
  { name: '18-35岁', value: 35 },
  { name: '35-55岁', value: 28 },
  { name: '55岁以上', value: 12 }
])

const recentRegisterData = ref([
  { id: 1, patientName: '张三', departmentName: '内科', doctorName: '王医生', registerDate: '2026-06-08', registerTime: '09:30:00', status: '已就诊' },
  { id: 2, patientName: '李四', departmentName: '外科', doctorName: '李医生', registerDate: '2026-06-08', registerTime: '10:15:00', status: '待就诊' },
  { id: 3, patientName: '王五', departmentName: '儿科', doctorName: '张医生', registerDate: '2026-06-07', registerTime: '14:20:00', status: '已就诊' },
  { id: 4, patientName: '赵六', departmentName: '妇产科', doctorName: '刘医生', registerDate: '2026-06-07', registerTime: '16:45:00', status: '已就诊' },
  { id: 5, patientName: '孙七', departmentName: '骨科', doctorName: '陈医生', registerDate: '2026-06-06', registerTime: '08:50:00', status: '已就诊' }
])

const getRowClassName = ({ rowIndex }) => {
  if (rowIndex % 2 === 1) {
    return 'bg-light'
  }
  return ''
}

const initLineChart = () => {
  if (!lineChartRef.value) return
  if (lineChartInstance) lineChartInstance.dispose()
  
  lineChartInstance = echarts.init(lineChartRef.value)
  
  const days = registerTrendData.value.map(item => item.date) || []
  const counts = registerTrendData.value.map(item => item.count) || []
  
  const option = {
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(255, 255, 255, 0.95)',
      borderColor: '#E5E7EB',
      borderWidth: 1,
      textStyle: { color: '#1D2129' }
    },
    grid: { left: '3%', right: '4%', bottom: '3%', top: '10%', containLabel: true },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: days,
      axisLine: { lineStyle: { color: '#E5E7EB' } },
      axisLabel: { color: '#4E5969', fontSize: 12 }
    },
    yAxis: {
      type: 'value',
      axisLine: { show: false },
      axisTick: { show: false },
      splitLine: { lineStyle: { color: '#F3F4F6' } },
      axisLabel: { color: '#4E5969', fontSize: 12 }
    },
    series: [{
      type: 'line',
      smooth: true,
      data: counts,
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: 'rgba(22, 93, 255, 0.2)' },
          { offset: 1, color: 'rgba(22, 93, 255, 0.02)' }
        ])
      },
      lineStyle: { color: '#165DFF', width: 3 },
      itemStyle: { color: '#165DFF' },
      symbol: 'circle',
      symbolSize: 8
    }]
  }
  
  lineChartInstance.setOption(option)
}

const initBarChart = () => {
  if (!barChartRef.value) return
  if (barChartInstance) barChartInstance.dispose()
  
  barChartInstance = echarts.init(barChartRef.value)
  
  const departments = departmentCountData.value.map(item => item.name) || []
  const counts = departmentCountData.value.map(item => item.value) || []
  
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' },
      backgroundColor: 'rgba(255, 255, 255, 0.95)',
      borderColor: '#E5E7EB',
      borderWidth: 1,
      textStyle: { color: '#1D2129' }
    },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: {
      type: 'category',
      data: departments,
      axisLine: { lineStyle: { color: '#E5E7EB' } },
      axisLabel: { color: '#4E5969', fontSize: 12 }
    },
    yAxis: {
      type: 'value',
      axisLine: { show: false },
      axisTick: { show: false },
      splitLine: { lineStyle: { color: '#F3F4F6' } },
      axisLabel: { color: '#4E5969', fontSize: 12 }
    },
    series: [{
      type: 'bar',
      data: counts,
      barWidth: '55%',
      itemStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: '#165DFF' },
          { offset: 1, color: '#4080FF' }
        ]),
        borderRadius: [6, 6, 0, 0]
      }
    }]
  }
  
  barChartInstance.setOption(option)
}

const initPieChart = () => {
  if (!pieChartRef.value) return
  if (pieChartInstance) pieChartInstance.dispose()
  
  pieChartInstance = echarts.init(pieChartRef.value)
  
  const colors = ['#165DFF', '#00B42A', '#FF7D00', '#F53F3F', '#7232DD']
  
  const pieData = ageDistributionData.value.map((item, index) => ({
    value: item.value,
    name: item.name,
    itemStyle: { color: colors[index % colors.length] }
  }))
  
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c}',
      backgroundColor: 'rgba(255, 255, 255, 0.95)',
      borderColor: '#E5E7EB',
      borderWidth: 1,
      textStyle: { color: '#1D2129' }
    },
    legend: {
      orient: 'vertical',
      left: 'left',
      top: 'center',
      textStyle: { color: '#4E5969', fontSize: 12 }
    },
    series: [{
      name: '年龄分布',
      type: 'pie',
      radius: ['45%', '75%'],
      avoidLabelOverlap: false,
      itemStyle: { borderRadius: 8, borderColor: '#fff', borderWidth: 2 },
      label: { show: true, formatter: '{b}\n{c}', color: '#1D2129', fontSize: 11 },
      emphasis: { label: { show: true, fontSize: 14, fontWeight: 'bold' } },
      labelLine: { show: true },
      data: pieData
    }]
  }
  
  pieChartInstance.setOption(option)
}

const refreshData = () => {
  loading.value = true
  setTimeout(() => {
    loading.value = false
  }, 500)
}

onMounted(() => {
  nextTick(() => {
    initLineChart()
    initBarChart()
    initPieChart()
  })
  
  window.addEventListener('resize', () => {
    lineChartInstance?.resize()
    barChartInstance?.resize()
    pieChartInstance?.resize()
  })
})
</script>

<style scoped>
.dashboard-page {
  padding: 24px;
  min-height: 100vh;
  background: #F5F7FA;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.page-title-section {
  flex: 1;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: #1D2129;
  margin: 0 0 8px 0;
}

.page-subtitle {
  font-size: 14px;
  color: #8F959E;
  margin: 0;
}

.page-actions {
  display: flex;
  gap: 12px;
}

.btn-refresh {
  background: #fff;
  border-color: #E5E7EB;
  color: #4E5969;
}

.btn-refresh:hover {
  background: #F5F7FA;
  border-color: #165DFF;
  color: #165DFF;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 24px;
}

.stat-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-icon.bg-blue {
  background: #E8F0FF;
  color: #165DFF;
}

.stat-icon.bg-green {
  background: #E6F7E6;
  color: #00B42A;
}

.stat-icon.bg-orange {
  background: #FFF7E6;
  color: #FF7D00;
}

.stat-icon.bg-purple {
  background: #F5EEFF;
  color: #7232DD;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 24px;
  font-weight: 600;
  color: #1D2129;
  margin: 0 0 4px 0;
}

.stat-label {
  font-size: 14px;
  color: #8F959E;
  margin: 0 0 4px 0;
}

.stat-trend {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
}

.stat-trend.up {
  color: #00B42A;
}

.stat-trend.down {
  color: #F53F3F;
}

.charts-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
  margin-bottom: 24px;
}

.charts-row {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
  margin-bottom: 24px;
}

.chart-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.chart-title {
  font-size: 16px;
  font-weight: 500;
  color: #1D2129;
  margin: 0;
}

.chart-container {
  height: 240px;
}

.status-grid {
  display: flex;
  justify-content: space-around;
  align-items: center;
  height: 200px;
}

.status-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.status-circle {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.status-circle.normal {
  background: #00B42A;
}

.status-circle.pause {
  background: #FF7D00;
}

.status-circle.top {
  background: #165DFF;
}

.status-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}

.status-value {
  font-size: 20px;
  font-weight: 600;
  color: #1D2129;
}

.status-label {
  font-size: 12px;
  color: #8F959E;
}

.card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  overflow: hidden;
}

.card-header {
  padding: 16px 20px;
  border-bottom: 1px solid #F0F1F2;
}

.card-title {
  font-size: 16px;
  font-weight: 500;
  color: #1D2129;
  margin: 0;
}

.card-body {
  padding: 16px 20px;
}

.bg-light {
  background: #F8FAFC;
}

.mr-2 {
  margin-right: 8px;
}

@media (max-width: 1200px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .charts-grid,
  .charts-row {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
}
</style>