<template>
  <div class="stats-cards">
    <div class="stat-card" v-for="stat in stats" :key="stat.title">
      <div class="stat-icon" :style="{ background: stat.bgColor }">
        <svg :width="24" :height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <path v-if="stat.icon === 'hospital'" d="M4 22h16"/>
          <path v-if="stat.icon === 'hospital'" d="M10 14V6a2 2 0 0 1 2-2v0a2 2 0 0 1 2 2v8"/>
          <path v-if="stat.icon === 'hospital'" d="M4 6h16"/>
          <path v-if="stat.icon === 'hospital'" d="M4 10h16"/>
          <path v-if="stat.icon === 'hospital'" d="M4 14h6"/>
          <path v-if="stat.icon === 'hospital'" d="M14 18h6"/>
          
          <path v-if="stat.icon === 'check'" d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/>
          <polyline v-if="stat.icon === 'check'" points="22 4 12 14.01 9 11.01"/>
          
          <path v-if="stat.icon === 'pause'" d="M12 2H2v20h20V2z"/>
          <line v-if="stat.icon === 'pause'" x1="6" x2="6" y1="4" y2="20"/>
          <line v-if="stat.icon === 'pause'" x1="18" x2="18" y1="4" y2="20"/>
          
          <path v-if="stat.icon === 'award'" d="M6 9H4.5a2.5 2.5 0 0 1 0-5H6"/>
          <path v-if="stat.icon === 'award'" d="M18 9h1.5a2.5 2.5 0 0 0 0-5H18"/>
          <path v-if="stat.icon === 'award'" d="M4 22h16"/>
          <path v-if="stat.icon === 'award'" d="M10 14.66V17c0 .55-.47.98-.97 1.21C7.85 18.75 7 20.24 7 22"/>
          <path v-if="stat.icon === 'award'" d="M14 14.66V17c0 .55.47.98.97 1.21C16.15 18.75 17 20.24 17 22"/>
          <path v-if="stat.icon === 'award'" d="M18 2H6v7a6 6 0 0 0 12 0V2Z"/>
        </svg>
      </div>
      <div class="stat-content">
        <p class="stat-value">{{ stat.value }}</p>
        <p class="stat-title">{{ stat.title }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  total: {
    type: Number,
    default: 0
  },
  normalCount: {
    type: Number,
    default: 0
  },
  pauseCount: {
    type: Number,
    default: 0
  },
  topLevelCount: {
    type: Number,
    default: 0
  }
})

const stats = computed(() => [
  {
    title: '医院总数',
    value: props.total,
    icon: 'hospital',
    bgColor: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
  },
  {
    title: '正常运营',
    value: props.normalCount,
    icon: 'check',
    bgColor: 'linear-gradient(135deg, #11998e 0%, #38ef7d 100%)'
  },
  {
    title: '暂停运营',
    value: props.pauseCount,
    icon: 'pause',
    bgColor: 'linear-gradient(135deg, #fc4a1a 0%, #f7b733 100%)'
  },
  {
    title: '三甲医院',
    value: props.topLevelCount,
    icon: 'award',
    bgColor: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)'
  }
])
</script>

<style scoped>
.stats-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 20px;
  margin-bottom: 24px;
}

.stat-card {
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.stat-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 4px;
  height: 100%;
  background: linear-gradient(180deg, #2C7DA0 0%, #1E3A5F 100%);
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.12);
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  flex-shrink: 0;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 32px;
  font-weight: 700;
  color: #1E3A5F;
  margin: 0 0 4px 0;
  line-height: 1.2;
}

.stat-title {
  font-size: 14px;
  color: #888;
  margin: 0;
}
</style>
