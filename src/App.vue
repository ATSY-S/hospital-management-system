<template>
  <div class="app-container">
    <Sidebar :collapsed="sidebarCollapsed" @toggle="sidebarCollapsed = !sidebarCollapsed" />
    <div class="main-content" :class="{ 'sidebar-collapsed': sidebarCollapsed }">
      <Header @toggle-sidebar="sidebarCollapsed = !sidebarCollapsed" />
      <div class="page-container">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </div>
    </div>
    <AIAssistant />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import Sidebar from './components/Sidebar.vue'
import Header from './components/Header.vue'
import AIAssistant from './components/AIAssistant.vue'

const sidebarCollapsed = ref(false)
</script>

<style>
:root {
  --primary-color: #165DFF;
  --primary-light: #E8F0FF;
  --primary-dark: #0D47A1;
  --success-color: #00B42A;
  --success-light: #E6F7E6;
  --warning-color: #FF7D00;
  --warning-light: #FFF7E6;
  --danger-color: #F53F3F;
  --danger-light: #FFF2F0;
  --info-color: #8F959E;
  --info-light: #F5F7FA;
  
  --text-primary: #1D2129;
  --text-secondary: #4E5969;
  --text-light: #8F959E;
  --text-placeholder: #BBBFC4;
  
  --bg-page: #F5F7FA;
  --bg-card: #FFFFFF;
  --bg-hover: #F2F3F5;
  --bg-active: #E8F0FF;
  
  --border-color: #E5E7EB;
  --border-light: #F0F1F2;
  
  --radius-sm: 4px;
  --radius-md: 8px;
  --radius-lg: 12px;
  --radius-xl: 16px;
  
  --shadow-sm: 0 2px 8px rgba(0, 0, 0, 0.04);
  --shadow-md: 0 4px 16px rgba(0, 0, 0, 0.08);
  --shadow-lg: 0 8px 24px rgba(0, 0, 0, 0.12);
  
  --spacing-xs: 4px;
  --spacing-sm: 8px;
  --spacing-md: 16px;
  --spacing-lg: 20px;
  --spacing-xl: 24px;
  --spacing-xxl: 32px;
  
  --font-size-xs: 12px;
  --font-size-sm: 14px;
  --font-size-md: 16px;
  --font-size-lg: 18px;
  --font-size-xl: 20px;
  --font-size-xxl: 24px;
  --font-size-title: 28px;
  
  --sidebar-width: 220px;
  --sidebar-collapsed-width: 64px;
  --header-height: 60px;
  
  --transition-fast: 0.15s ease;
  --transition-normal: 0.25s ease;
  --transition-slow: 0.35s ease;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html {
  font-family: 'PingFang SC', 'Microsoft YaHei', 'Segoe UI', -apple-system, BlinkMacSystemFont, sans-serif;
  font-size: var(--font-size-sm);
  color: var(--text-primary);
  background-color: var(--bg-page);
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

body {
  min-height: 100vh;
}

#app {
  min-height: 100vh;
}

.app-container {
  display: flex;
  min-height: 100vh;
  background-color: var(--bg-page);
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-width: 0;
  transition: all var(--transition-normal);
  margin-left: var(--sidebar-width);
}

.main-content.sidebar-collapsed {
  margin-left: var(--sidebar-collapsed-width);
}

.page-container {
  flex: 1;
  padding: var(--spacing-xl);
  overflow-y: auto;
  min-height: calc(100vh - var(--header-height));
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity var(--transition-normal), transform var(--transition-normal);
}

.fade-enter-from {
  opacity: 0;
  transform: translateY(8px);
}

.fade-leave-to {
  opacity: 0;
  transform: translateY(-8px);
}

::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-track {
  background: var(--bg-page);
}

::-webkit-scrollbar-thumb {
  background: var(--border-color);
  border-radius: var(--radius-sm);
}

::-webkit-scrollbar-thumb:hover {
  background: var(--text-light);
}

:deep(.el-select-dropdown__item.selected) {
  background-color: var(--bg-active);
  color: var(--primary-color);
}

:deep(.el-button--primary) {
  background-color: var(--primary-color);
  border-color: var(--primary-color);
  height: 36px;
  padding: 0 var(--spacing-xl);
  font-size: var(--font-size-sm);
  border-radius: var(--radius-md);
  transition: all var(--transition-fast);
}

:deep(.el-button--primary:hover) {
  background-color: var(--primary-dark);
  border-color: var(--primary-dark);
}

:deep(.el-button--danger) {
  background-color: var(--danger-color);
  border-color: var(--danger-color);
  height: 36px;
  padding: 0 var(--spacing-xl);
  font-size: var(--font-size-sm);
  border-radius: var(--radius-md);
}

:deep(.el-button--danger:hover) {
  background-color: #D93026;
  border-color: #D93026;
}

:deep(.el-button--default) {
  height: 36px;
  padding: 0 var(--spacing-xl);
  font-size: var(--font-size-sm);
  border-radius: var(--radius-md);
}

:deep(.el-tag--success) {
  background-color: rgba(0, 180, 42, 0.1);
  border-color: rgba(0, 180, 42, 0.2);
  color: var(--success-color);
  padding: 2px 8px;
  font-size: var(--font-size-xs);
  border-radius: var(--radius-sm);
}

:deep(.el-tag--warning) {
  background-color: rgba(255, 125, 0, 0.1);
  border-color: rgba(255, 125, 0, 0.2);
  color: var(--warning-color);
  padding: 2px 8px;
  font-size: var(--font-size-xs);
  border-radius: var(--radius-sm);
}

:deep(.el-tag--danger) {
  background-color: rgba(245, 63, 63, 0.1);
  border-color: rgba(245, 63, 63, 0.2);
  color: var(--danger-color);
  padding: 2px 8px;
  font-size: var(--font-size-xs);
  border-radius: var(--radius-sm);
}

:deep(.el-tag--info) {
  background-color: rgba(143, 149, 158, 0.1);
  border-color: rgba(143, 149, 158, 0.2);
  color: var(--text-secondary);
  padding: 2px 8px;
  font-size: var(--font-size-xs);
  border-radius: var(--radius-sm);
}

:deep(.el-dialog) {
  border-radius: var(--radius-lg);
  overflow: hidden;
  box-shadow: var(--shadow-lg);
}

:deep(.el-dialog__header) {
  background-color: var(--bg-page);
  border-bottom: 1px solid var(--border-light);
  padding: var(--spacing-lg) var(--spacing-xl);
}

:deep(.el-dialog__body) {
  padding: var(--spacing-xl);
}

:deep(.el-dialog__footer) {
  border-top: 1px solid var(--border-light);
  padding: var(--spacing-md) var(--spacing-xl);
  display: flex;
  justify-content: flex-end;
  gap: var(--spacing-md);
}

:deep(.el-form-item__label) {
  font-weight: 500;
  color: var(--text-secondary);
  text-align: right;
}

:deep(.el-input__wrapper) {
  box-shadow: none;
  height: 40px;
  border-radius: var(--radius-md);
}

:deep(.el-select__wrapper) {
  height: 40px;
  border-radius: var(--radius-md);
}

:deep(.el-date-picker__wrapper) {
  height: 40px;
  border-radius: var(--radius-md);
}

:deep(.el-table) {
  font-size: var(--font-size-sm);
  border-radius: var(--radius-md);
  overflow: hidden;
}

:deep(.el-table__header) {
  background-color: #F8FAFC;
}

:deep(.el-table__header th) {
  color: var(--text-secondary);
  font-weight: 500;
  padding: var(--spacing-md);
  border-bottom: 1px solid var(--border-light);
}

:deep(.el-table__row) {
  height: 48px;
}

:deep(.el-table__row:hover) {
  background-color: rgba(22, 93, 255, 0.04);
}

:deep(.el-table__body tr.current-row) {
  background-color: var(--bg-active);
}

:deep(.el-pagination) {
  padding: var(--spacing-lg) 0;
  display: flex;
  justify-content: center;
}

:deep(.el-pagination.is-background .el-pager li:not(.disabled).active) {
  background-color: var(--primary-color);
}

:deep(.el-loading-mask) {
  background-color: rgba(255, 255, 255, 0.8);
}

:deep(.el-loading-spinner .el-loading-text) {
  color: var(--text-secondary);
}
</style>
