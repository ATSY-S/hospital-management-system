<template>
  <aside 
    class="sidebar" 
    :class="{ collapsed: collapsed }"
    :style="{ width: collapsed ? '64px' : '220px' }"
  >
    <div class="sidebar-header">
      <div class="logo" @click="$router.push('/dashboard')">
        <div class="logo-icon">
          <el-icon :size="24"><OfficeBuilding /></el-icon>
        </div>
        <span v-if="!collapsed" class="logo-text">医院管理系统</span>
      </div>
      <button class="collapse-btn" @click="$emit('toggle')">
        <el-icon :size="18"><CaretLeft v-if="!collapsed" /><CaretRight v-else /></el-icon>
      </button>
    </div>
    
    <nav class="sidebar-nav">
      <ul class="nav-list">
        <li 
          v-for="item in menuItems" 
          :key="item.path"
          class="nav-item"
          :class="{ active: $route.path === item.path, 'has-children': item.children }"
        >
          <router-link 
            v-if="!item.children" 
            :to="item.path"
            class="nav-link"
          >
            <div class="nav-icon-wrapper">
              <el-icon :size="20" class="nav-icon"><component :is="item.icon" /></el-icon>
            </div>
            <span v-if="!collapsed" class="nav-text">{{ item.label }}</span>
            <span v-if="!collapsed && item.badge" class="nav-badge">{{ item.badge }}</span>
          </router-link>
          
          <div v-else class="nav-link has-dropdown" @click="toggleDropdown(item.path)">
            <div class="nav-icon-wrapper">
              <el-icon :size="20" class="nav-icon"><component :is="item.icon" /></el-icon>
            </div>
            <span v-if="!collapsed" class="nav-text">{{ item.label }}</span>
            <el-icon v-if="!collapsed" :size="16" class="nav-arrow" :class="{ rotated: expandedMenus.includes(item.path) }">
                <CaretBottom />
              </el-icon>
          </div>
          
          <ul v-if="item.children && expandedMenus.includes(item.path)" class="sub-nav">
            <li v-for="child in item.children" :key="child.path">
              <router-link :to="child.path" class="sub-nav-link">
                <span v-if="!collapsed" class="sub-nav-text">{{ child.label }}</span>
              </router-link>
            </li>
          </ul>
        </li>
      </ul>
    </nav>
    
    <div class="sidebar-footer">
      <div v-if="!collapsed" class="user-info">
        <div class="user-avatar">
          <el-icon :size="18"><User /></el-icon>
        </div>
        <div class="user-detail">
          <div class="user-name">管理员</div>
          <div class="user-role">系统管理员</div>
        </div>
      </div>
      <div v-else class="user-avatar-mini">
        <el-icon :size="18"><User /></el-icon>
      </div>
    </div>
  </aside>
</template>

<script setup>
import { ref } from 'vue'
import { 
  Grid, 
  OfficeBuilding, 
  User, 
  Calendar, 
  Files, 
  Wallet,
  CaretLeft, 
  CaretRight, 
  CaretBottom
} from '@element-plus/icons-vue'

defineProps({
  collapsed: Boolean
})

defineEmits(['toggle'])

const expandedMenus = ref(['/hospital'])

const menuItems = [
  { path: '/dashboard', label: '数据仪表盘', icon: Grid, badge: 'NEW' },
  { 
    path: '/hospital', 
    label: '医院管理', 
    icon: OfficeBuilding,
    children: [
      { path: '/hospital', label: '医院列表' }
    ]
  },
  { 
    path: '/patient', 
    label: '患者管理', 
    icon: User,
    children: [
      { path: '/patient', label: '患者列表' }
    ]
  },
  { 
    path: '/register', 
    label: '挂号管理', 
    icon: Calendar,
    children: [
      { path: '/register', label: '挂号记录' }
    ]
  },
  { path: '/medicine', label: '药品管理', icon: Files },
  { path: '/finance', label: '收费管理', icon: Wallet }
]

const toggleDropdown = (path) => {
  const index = expandedMenus.value.indexOf(path)
  if (index > -1) {
    expandedMenus.value.splice(index, 1)
  } else {
    expandedMenus.value.push(path)
  }
}
</script>

<style scoped>
.sidebar {
  background-color: var(--bg-card);
  border-right: 1px solid var(--border-light);
  display: flex;
  flex-direction: column;
  height: 100vh;
  position: fixed;
  left: 0;
  top: 0;
  z-index: 100;
  transition: width var(--transition-normal);
  overflow: hidden;
  box-shadow: var(--shadow-sm);
}

.sidebar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--spacing-md);
  border-bottom: 1px solid var(--border-light);
}

.logo {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  cursor: pointer;
  padding: var(--spacing-sm) var(--spacing-md);
  border-radius: var(--radius-md);
  transition: background-color var(--transition-fast);
  flex: 1;
}

.logo:hover {
  background-color: var(--bg-hover);
}

.logo-icon {
  width: 36px;
  height: 36px;
  border-radius: var(--radius-md);
  background: linear-gradient(135deg, var(--primary-color) 0%, var(--primary-dark) 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.logo-text {
  font-size: var(--font-size-md);
  font-weight: 600;
  color: var(--text-primary);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.collapse-btn {
  background: none;
  border: none;
  padding: var(--spacing-sm);
  border-radius: var(--radius-sm);
  color: var(--text-secondary);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.collapse-btn:hover {
  background-color: var(--bg-hover);
  color: var(--text-primary);
}

.sidebar-nav {
  flex: 1;
  padding: var(--spacing-md);
}

.nav-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.nav-item {
  margin-bottom: var(--spacing-xs);
}

.nav-item.active > .nav-link {
  background-color: var(--bg-active);
  color: var(--primary-color);
}

.nav-item.active > .nav-link .nav-icon-wrapper {
  background-color: var(--primary-color);
}

.nav-item.active > .nav-link .nav-icon {
  color: white;
}

.nav-link {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  padding: var(--spacing-md);
  border-radius: var(--radius-md);
  color: var(--text-secondary);
  text-decoration: none;
  transition: all var(--transition-fast);
  cursor: pointer;
}

.nav-link:hover {
  background-color: var(--bg-hover);
  color: var(--text-primary);
}

.nav-icon-wrapper {
  width: 36px;
  height: 36px;
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  background-color: var(--bg-hover);
  transition: all var(--transition-fast);
}

.nav-link:hover .nav-icon-wrapper {
  background-color: var(--primary-light);
}

.nav-icon {
  color: var(--text-secondary);
  transition: color var(--transition-fast);
}

.nav-link:hover .nav-icon {
  color: var(--primary-color);
}

.nav-text {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-size: var(--font-size-sm);
}

.nav-badge {
  font-size: 10px;
  padding: 2px 6px;
  background-color: var(--danger-color);
  color: white;
  border-radius: 10px;
  font-weight: 500;
}

.nav-arrow {
  transition: transform var(--transition-fast);
  color: var(--text-light);
}

.nav-arrow.rotated {
  transform: rotate(180deg);
}

.sub-nav {
  list-style: none;
  padding: var(--spacing-xs) 0;
  margin: 0;
}

.sub-nav-link {
  display: flex;
  align-items: center;
  padding: var(--spacing-sm) var(--spacing-md) var(--spacing-sm) calc(var(--spacing-md) * 3);
  color: var(--text-secondary);
  text-decoration: none;
  font-size: var(--font-size-xs);
  border-radius: var(--radius-sm);
  transition: all var(--transition-fast);
}

.sub-nav-link:hover {
  background-color: var(--bg-hover);
  color: var(--text-primary);
}

.sub-nav-text {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.sidebar-footer {
  padding: var(--spacing-md);
  border-top: 1px solid var(--border-light);
}

.user-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  padding: var(--spacing-sm);
  border-radius: var(--radius-md);
  transition: background-color var(--transition-fast);
}

.user-info:hover {
  background-color: var(--bg-hover);
}

.user-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--primary-light) 0%, #D0E4FF 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--primary-color);
}

.user-avatar-mini {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--primary-light) 0%, #D0E4FF 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--primary-color);
  margin: 0 auto;
}

.user-detail {
  flex: 1;
  overflow: hidden;
}

.user-name {
  font-size: var(--font-size-sm);
  font-weight: 500;
  color: var(--text-primary);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.user-role {
  font-size: var(--font-size-xs);
  color: var(--text-light);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

@media (max-width: 768px) {
  .sidebar {
    transform: translateX(-100%);
    transition: transform var(--transition-normal);
  }
  
  .sidebar.collapsed {
    transform: translateX(0);
    width: 64px;
  }
}
</style>
