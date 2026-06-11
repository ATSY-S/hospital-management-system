<template>
  <div class="ai-assistant-container">
    <div 
      class="ai-bubble" 
      :class="{ active: isHovered }"
      @mouseenter="isHovered = true"
      @mouseleave="isHovered = false"
      @click="openAssistant"
    >
      <div class="bubble-icon">
        <el-icon :size="24"><ChatRound /></el-icon>
      </div>
      <div class="bubble-pulse"></div>
      <div class="bubble-pulse pulse-delay"></div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ChatRound } from '@element-plus/icons-vue'

const isHovered = ref(false)
const assistantUrl = ref('https://fbzb84s6qg.coze.site')

const openAssistant = () => {
  const width = 600
  const height = 600
  const left = (window.screen.width - width) / 2
  const top = (window.screen.height - height) / 2
  
  window.open(
    assistantUrl.value,
    'medical-assistant',
    `width=${width},height=${height},left=${left},top=${top},toolbar=no,scrollbars=yes,resizable=yes`
  )
}
</script>

<style scoped>
.ai-assistant-container {
  position: fixed;
  right: 30px;
  bottom: 30px;
  z-index: 1000;
}

.ai-bubble {
  position: relative;
  width: 64px;
  height: 80px;
  background: linear-gradient(135deg, #165DFF 0%, #0D47A1 100%);
  border-radius: 50% 50% 50% 10%;
  box-shadow: 0 8px 24px rgba(22, 93, 255, 0.4);
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.ai-bubble:hover {
  transform: translateY(-4px) scale(1.05);
  box-shadow: 0 12px 32px rgba(22, 93, 255, 0.5);
}

.ai-bubble.active {
  transform: scale(1.05);
}

.bubble-icon {
  color: white;
  position: relative;
  z-index: 2;
}

.bubble-pulse {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 100%;
  height: 100%;
  border-radius: 50% 50% 50% 10%;
  background: rgba(22, 93, 255, 0.4);
  animation: pulse 2s ease-in-out infinite;
}

.bubble-pulse.pulse-delay {
  animation-delay: 1s;
}

@keyframes pulse {
  0% {
    transform: translate(-50%, -50%) scale(1);
    opacity: 0.6;
  }
  100% {
    transform: translate(-50%, -50%) scale(1.4);
    opacity: 0;
  }
}

@media (max-width: 768px) {
  .ai-bubble {
    right: 15px;
    bottom: 15px;
    width: 56px;
    height: 70px;
  }
}
</style>
