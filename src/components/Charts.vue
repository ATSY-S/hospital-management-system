<template>
  <div class="charts-container">
    <div class="chart-card">
      <h3 class="chart-title">医院类型分布</h3>
      <v-chart class="chart" :option="pieOption" autoresize />
    </div>
    <div class="chart-card">
      <h3 class="chart-title">医院等级分布</h3>
      <v-chart class="chart" :option="barOption" autoresize />
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import VChart from 'echarts-for-vue'

const props = defineProps({
  typeData: {
    type: Object,
    default: () => ({})
  },
  levelData: {
    type: Object,
    default: () => ({})
  }
})

const pieOption = computed(() => ({
  tooltip: {
    trigger: 'item',
    formatter: '{b}: {c}家 ({d}%)'
  },
  legend: {
    orient: 'vertical',
    right: '5%',
    top: 'center',
    textStyle: {
      color: '#666'
    }
  },
  series: [
    {
      name: '医院类型',
      type: 'pie',
      radius: ['45%', '70%'],
      center: ['40%', '50%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 8,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: {
        show: false,
        position: 'center'
      },
      emphasis: {
        label: {
          show: true,
          fontSize: 18,
          fontWeight: 'bold'
        }
      },
      labelLine: {
        show: false
      },
      data: Object.entries(props.typeData).map(([key, value], index) => ({
        name: key,
        value: value,
        itemStyle: {
          color: [
            '#667eea',
            '#11998e',
            '#fc4a1a',
            '#4facfe',
            '#f7b733',
            '#a855f7',
            '#ec4899',
            '#06b6d4'
          ][index % 8]
        }
      }))
    }
  ]
}))

const barOption = computed(() => ({
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'shadow'
    }
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    data: Object.keys(props.levelData),
    axisLine: {
      lineStyle: {
        color: '#ddd'
      }
    },
    axisLabel: {
      color: '#666',
      fontSize: 12
    }
  },
  yAxis: {
    type: 'value',
    axisLine: {
      show: false
    },
    axisTick: {
      show: false
    },
    splitLine: {
      lineStyle: {
        color: '#f0f0f0'
      }
    },
    axisLabel: {
      color: '#666'
    }
  },
  series: [
    {
      name: '医院数量',
      type: 'bar',
      barWidth: '50%',
      data: Object.values(props.levelData),
      itemStyle: {
        borderRadius: [6, 6, 0, 0],
        color: {
          type: 'linear',
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [
            { offset: 0, color: '#2C7DA0' },
            { offset: 1, color: '#1E3A5F' }
          ]
        }
      },
      emphasis: {
        itemStyle: {
          color: {
            type: 'linear',
            x: 0,
            y: 0,
            x2: 0,
            y2: 1,
            colorStops: [
              { offset: 0, color: '#4CA1AF' },
              { offset: 1, color: '#2C7DA0' }
            ]
          }
        }
      },
      label: {
        show: true,
        position: 'top',
        color: '#666',
        fontSize: 12
      }
    }
  ]
}))
</script>

<style scoped>
.charts-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
  gap: 20px;
  margin-bottom: 24px;
}

.chart-card {
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.chart-title {
  font-size: 16px;
  font-weight: 600;
  color: #1E3A5F;
  margin: 0 0 20px 0;
  padding-bottom: 12px;
  border-bottom: 1px solid #f0f0f0;
}

.chart {
  height: 300px;
}
</style>
