<template>
  <header class="header">
    <div class="header-left">
      <button class="sidebar-toggle" @click="$emit('toggle-sidebar')">
        <el-icon :size="20"><Menu /></el-icon>
      </button>
      <div class="breadcrumbs">
        <el-breadcrumb separator="/" class="breadcrumb">
          <el-breadcrumb-item :to="{ path: '/dashboard' }">
            <span class="breadcrumb-icon"><el-icon :size="16"><HomeFilled /></el-icon></span>
            <span>首页</span>
          </el-breadcrumb-item>
          <el-breadcrumb-item v-for="item in breadcrumbItems" :key="item.path" :to="{ path: item.path }">
            {{ item.label }}
          </el-breadcrumb-item>
        </el-breadcrumb>
      </div>
    </div>
    
    <div class="header-center">
      <div class="search-wrapper">
        <el-input 
          v-model="searchQuery" 
          placeholder="搜索医院、患者、挂号..." 
          class="header-search"
          clearable
          @keyup.enter="handleSearch"
        >
          <template #prefix>
            <el-icon :size="16" class="search-icon"><Search /></el-icon>
          </template>
        </el-input>
      </div>
    </div>
    
    <div class="header-right">
      <button class="header-btn notification-btn" @click="openNotification">
        <el-icon :size="20" class="btn-icon"><Bell /></el-icon>
        <span v-if="notificationCount > 0" class="notification-badge">{{ notificationCount > 99 ? '99+' : notificationCount }}</span>
      </button>
      <button class="header-btn settings-btn" @click="openSettings">
        <el-icon :size="20" class="btn-icon"><Setting /></el-icon>
      </button>
      <button class="header-btn help-btn" @click="openHelp">
        <el-icon :size="20" class="btn-icon"><HelpFilled /></el-icon>
      </button>
      <div class="user-menu">
        <div class="user-trigger" @click="toggleUserMenu">
          <div class="user-avatar">
            <el-icon :size="18"><User /></el-icon>
          </div>
          <span class="user-name">{{ userInfo.nickname || '管理员' }}</span>
          <el-icon :size="16" class="user-arrow" :class="{ rotated: userMenuOpen }">
            <CaretBottom />
          </el-icon>
        </div>
        <div v-if="userMenuOpen" class="user-dropdown">
          <div class="dropdown-item" @click="goToProfile">
            <el-icon :size="16"><User /></el-icon>
            <span>个人中心</span>
          </div>
          <div class="dropdown-item" @click="goToSetting">
            <el-icon :size="16"><Setting /></el-icon>
            <span>账号设置</span>
          </div>
          <div class="dropdown-item" @click="goToLogs">
            <el-icon :size="16"><Document /></el-icon>
            <span>操作日志</span>
          </div>
          <div class="dropdown-divider"></div>
          <div class="dropdown-item logout" @click="handleLogout">
            <el-icon :size="16"><TurnOff /></el-icon>
            <span>退出登录</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 通知弹窗 -->
    <el-dialog title="消息通知" :visible.sync="notificationVisible" width="420px" :close-on-click-modal="true">
      <div class="notification-list">
        <div v-if="notificationList.length === 0" class="empty-notification">
          <el-icon :size="48" class="empty-icon"><Bell /></el-icon>
          <p>暂无新通知</p>
        </div>
        <div v-for="item in notificationList" :key="item.id" 
             class="notification-item" 
             :class="{ unread: item.status === 0 }"
             @click="markNotificationRead(item.id)">
          <div class="notification-dot" v-if="item.status === 0"></div>
          <div class="notification-content">
            <p class="notification-title">{{ item.title }}</p>
            <p class="notification-desc">{{ item.content }}</p>
            <p class="notification-time">{{ formatTime(item.createTime) }}</p>
          </div>
        </div>
      </div>
      <div slot="footer" class="notification-footer">
        <el-button size="small" @click="markAllRead">全部标记已读</el-button>
        <el-button size="small" type="primary" @click="notificationVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 设置弹窗 -->
    <el-dialog title="系统设置" :visible.sync="settingsVisible" width="500px">
      <el-form :model="settingsForm" label-width="120px">
        <el-form-item label="系统名称">
          <el-input v-model="settingsForm.siteName" />
        </el-form-item>
        <el-form-item label="系统Logo">
          <el-input v-model="settingsForm.siteLogo" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="settingsForm.contactPhone" />
        </el-form-item>
        <el-form-item label="邮箱地址">
          <el-input v-model="settingsForm.contactEmail" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button size="small" @click="saveSettings">保存设置</el-button>
        <el-button size="small" type="primary" @click="settingsVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 帮助文档弹窗 -->
    <el-dialog title="帮助文档" :visible.sync="helpVisible" width="600px">
      <div class="help-container">
        <div class="help-sidebar">
          <div v-for="category in helpCategories" :key="category" 
               class="help-category" 
               :class="{ active: activeHelpCategory === category }"
               @click="activeHelpCategory = category">
            {{ category }}
          </div>
        </div>
        <div class="help-content">
          <div v-if="currentHelpDoc" class="help-doc">
            <h3>{{ currentHelpDoc.title }}</h3>
            <div v-html="currentHelpDoc.content"></div>
          </div>
          <div v-else class="help-list">
            <div v-for="doc in filteredHelpDocs" :key="doc.id" 
                 class="help-item" 
                 @click="selectHelpDoc(doc)">
              <h4>{{ doc.title }}</h4>
              <p>{{ doc.summary }}</p>
            </div>
          </div>
        </div>
      </div>
      <div slot="footer">
        <el-button size="small" type="primary" @click="helpVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 个人中心弹窗 -->
    <el-dialog title="个人中心" :visible.sync="profileVisible" width="450px">
      <div class="profile-container">
        <div class="profile-avatar">
          <div class="avatar-circle">
            <el-icon :size="32"><User /></el-icon>
          </div>
          <p class="profile-name">{{ userInfo.nickname }}</p>
          <p class="profile-role">系统管理员</p>
        </div>
        <div class="profile-info">
          <div class="info-row">
            <span class="info-label">用户名</span>
            <span class="info-value">{{ userInfo.username }}</span>
          </div>
          <div class="info-row">
            <span class="info-label">手机号</span>
            <span class="info-value">{{ userInfo.phone || '未设置' }}</span>
          </div>
          <div class="info-row">
            <span class="info-label">邮箱</span>
            <span class="info-value">{{ userInfo.email || '未设置' }}</span>
          </div>
          <div class="info-row">
            <span class="info-label">创建时间</span>
            <span class="info-value">{{ formatTime(userInfo.createTime) }}</span>
          </div>
        </div>
      </div>
      <div slot="footer">
        <el-button size="small" type="primary" @click="profileVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 账号设置弹窗 -->
    <el-dialog title="账号设置" :visible.sync="accountVisible" width="450px">
      <el-form :model="accountForm" label-width="100px">
        <el-form-item label="昵称">
          <el-input v-model="accountForm.nickname" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="accountForm.phone" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="accountForm.email" />
        </el-form-item>
        <el-divider content-position="left">修改密码</el-divider>
        <el-form-item label="旧密码">
          <el-input type="password" v-model="accountForm.oldPassword" />
        </el-form-item>
        <el-form-item label="新密码">
          <el-input type="password" v-model="accountForm.newPassword" />
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input type="password" v-model="accountForm.confirmPassword" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button size="small" @click="saveAccount">保存设置</el-button>
        <el-button size="small" type="primary" @click="accountVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 操作日志弹窗 -->
    <el-dialog title="操作日志" :visible.sync="logsVisible" width="700px">
      <el-table :data="operationLogs" border stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="module" label="操作模块" width="120" />
        <el-table-column prop="operation" label="操作类型" width="120" />
        <el-table-column prop="content" label="操作内容" />
        <el-table-column prop="ip" label="IP地址" width="120" />
        <el-table-column prop="createTime" label="操作时间" width="180" />
      </el-table>
      <div slot="footer" class="logs-footer">
        <el-pagination 
          :total="logsTotal" 
          :page-size="10" 
          @current-change="loadLogs"
          layout="prev, pager, next">
        </el-pagination>
      </div>
    </el-dialog>
  </header>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { Menu, Bell, Setting, HelpFilled, CaretBottom, User, TurnOff, Document, HomeFilled, Search } from '@element-plus/icons-vue'

defineEmits(['toggle-sidebar'])

const route = useRoute()

const request = axios.create({
  baseURL: '/hos',
  timeout: 10000
})

const searchQuery = ref('')
const userMenuOpen = ref(false)

// 用户信息
const userInfo = reactive({
  username: '',
  nickname: '',
  phone: '',
  email: '',
  createTime: ''
})

// 通知相关
const notificationCount = ref(0)
const notificationVisible = ref(false)
const notificationList = ref([])

// 设置相关
const settingsVisible = ref(false)
const settingsForm = reactive({
  siteName: '',
  siteLogo: '',
  contactPhone: '',
  contactEmail: ''
})

// 帮助文档相关
const helpVisible = ref(false)
const activeHelpCategory = ref('系统介绍')
const helpCategories = ref(['系统介绍', '功能指南', '常见问题'])
const helpList = ref([])
const filteredHelpDocs = computed(() => {
  return helpList.value.filter(doc => doc.category === activeHelpCategory.value)
})
const currentHelpDoc = ref(null)

// 个人中心相关
const profileVisible = ref(false)

// 账号设置相关
const accountVisible = ref(false)
const accountForm = reactive({
  nickname: '',
  phone: '',
  email: '',
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 操作日志相关
const logsVisible = ref(false)
const operationLogs = ref([])
const logsTotal = ref(0)
const currentLogPage = ref(1)

const breadcrumbItems = computed(() => {
  const pathMap = {
    '/dashboard': { label: '数据仪表盘', path: '/dashboard' },
    '/hospital': { label: '医院管理', path: '/hospital' },
    '/patient': { label: '患者管理', path: '/patient' },
    '/register': { label: '挂号管理', path: '/register' },
    '/medicine': { label: '药品管理', path: '/medicine' },
    '/finance': { label: '收费管理', path: '/finance' }
  }
  
  const currentPath = route.path
  if (pathMap[currentPath]) {
    return [pathMap[currentPath]]
  }
  return []
})

const formatTime = (time) => {
  if (!time) return ''
  return time.replace('T', ' ').substring(0, 19)
}

const loadUserInfo = async () => {
  try {
    const res = await request.get('/api/user/info')
    if (res.data.code === '200' && res.data.data) {
      Object.assign(userInfo, res.data.data)
    }
  } catch (error) {
    console.error('加载用户信息失败:', error)
  }
}

const loadNotificationCount = async () => {
  try {
    const res = await request.get('/api/notification/unreadCount')
    if (res.data.code === '200') {
      notificationCount.value = res.data.data || 0
    }
  } catch (error) {
    console.error('加载未读通知数失败:', error)
  }
}

const loadNotifications = async () => {
  try {
    const res = await request.get('/api/notification/list', { params: { page: 1, size: 10 } })
    if (res.data.code === '200' && res.data.data) {
      notificationList.value = res.data.data.list || []
    }
  } catch (error) {
    console.error('加载通知列表失败:', error)
  }
}

const loadSettings = async () => {
  try {
    const res = await request.get('/api/setting/list')
    if (res.data.code === '200') {
      const settings = res.data.data || []
      settings.forEach(item => {
        if (item.keyName === 'site_name') settingsForm.siteName = item.keyValue || ''
        if (item.keyName === 'site_logo') settingsForm.siteLogo = item.keyValue || ''
        if (item.keyName === 'contact_phone') settingsForm.contactPhone = item.keyValue || ''
        if (item.keyName === 'contact_email') settingsForm.contactEmail = item.keyValue || ''
      })
    }
  } catch (error) {
    console.error('加载系统设置失败:', error)
  }
}

const loadHelpDocs = async () => {
  try {
    const res = await request.get('/api/help/list', { params: { page: 1, size: 20 } })
    if (res.data.code === '200' && res.data.data) {
      helpList.value = res.data.data.list || []
    }
  } catch (error) {
    console.error('加载帮助文档失败:', error)
  }
}

const loadLogs = async (page = 1) => {
  currentLogPage.value = page
  try {
    const res = await request.get('/api/log/list', { params: { page, size: 10 } })
    if (res.data.code === '200' && res.data.data) {
      operationLogs.value = res.data.data.list || []
      logsTotal.value = res.data.data.total || 0
    }
  } catch (error) {
    console.error('加载操作日志失败:', error)
  }
}

const openNotification = async () => {
  notificationVisible.value = true
  await loadNotifications()
}

const markNotificationRead = async (id) => {
  try {
    await request.put(`/api/notification/read/${id}`)
    const item = notificationList.value.find(n => n.id === id)
    if (item) item.status = 1
    notificationCount.value = Math.max(0, notificationCount.value - 1)
  } catch (error) {
    console.error('标记已读失败:', error)
  }
}

const markAllRead = async () => {
  try {
    await request.put('/api/notification/readAll')
    notificationList.value.forEach(item => item.status = 1)
    notificationCount.value = 0
    ElMessage.success('已全部标记为已读')
  } catch (error) {
    console.error('全部标记已读失败:', error)
  }
}

const openSettings = async () => {
  settingsVisible.value = true
  await loadSettings()
}

const saveSettings = async () => {
  try {
    await request.put('/api/setting', { key: 'site_name', value: settingsForm.siteName })
    await request.put('/api/setting', { key: 'contact_phone', value: settingsForm.contactPhone })
    await request.put('/api/setting', { key: 'contact_email', value: settingsForm.contactEmail })
    ElMessage.success('设置保存成功')
    settingsVisible.value = false
  } catch (error) {
    ElMessage.error('保存失败')
    console.error('保存设置失败:', error)
  }
}

const openHelp = async () => {
  helpVisible.value = true
  if (helpList.value.length === 0) {
    await loadHelpDocs()
  }
}

const selectHelpDoc = (doc) => {
  currentHelpDoc.value = doc
}

const goToProfile = () => {
  userMenuOpen.value = false
  profileVisible.value = true
}

const goToSetting = () => {
  userMenuOpen.value = false
  accountVisible.value = true
  accountForm.nickname = userInfo.nickname
  accountForm.phone = userInfo.phone || ''
  accountForm.email = userInfo.email || ''
}

const goToLogs = () => {
  userMenuOpen.value = false
  logsVisible.value = true
  loadLogs(1)
}

const saveAccount = async () => {
  try {
    // 更新基本信息
    await request.put('/api/user/update', {
      id: 1,
      nickname: accountForm.nickname,
      phone: accountForm.phone,
      email: accountForm.email
    })
    
    // 更新密码（如果填写了）
    if (accountForm.oldPassword && accountForm.newPassword) {
      if (accountForm.newPassword !== accountForm.confirmPassword) {
        ElMessage.error('两次输入的密码不一致')
        return
      }
      await request.put('/api/user/changePassword', {
        oldPassword: accountForm.oldPassword,
        newPassword: accountForm.newPassword
      })
      ElMessage.success('信息和密码更新成功')
    } else {
      ElMessage.success('信息更新成功')
    }
    
    // 刷新用户信息
    await loadUserInfo()
    accountVisible.value = false
  } catch (error) {
    ElMessage.error('更新失败')
    console.error('保存账号设置失败:', error)
  }
}

const handleLogout = async () => {
  userMenuOpen.value = false
  try {
    await request.post('/api/user/logout')
    ElMessage.success('退出成功')
    // 实际项目中应该跳转到登录页
  } catch (error) {
    ElMessage.success('退出成功')
    console.error('退出登录失败:', error)
  }
}

const toggleUserMenu = () => {
  userMenuOpen.value = !userMenuOpen.value
}

const handleSearch = () => {
  if (searchQuery.value.trim()) {
    ElMessage.info(`搜索: ${searchQuery.value}`)
    searchQuery.value = ''
  }
}

onMounted(() => {
  loadUserInfo()
  loadNotificationCount()
})
</script>

<style scoped>
.header {
  height: var(--header-height);
  background-color: var(--bg-card);
  border-bottom: 1px solid var(--border-light);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 var(--spacing-xl);
  position: sticky;
  top: 0;
  z-index: 50;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.header-left {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  flex: 1;
}

.sidebar-toggle {
  background: none;
  border: none;
  padding: var(--spacing-sm);
  border-radius: var(--radius-sm);
  color: var(--text-secondary);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.sidebar-toggle:hover {
  background-color: var(--bg-hover);
  color: var(--text-primary);
}

.breadcrumbs {
  font-size: var(--font-size-sm);
}

.breadcrumb {
  display: flex;
  align-items: center;
}

.breadcrumb :deep(.el-breadcrumb__item) {
  color: var(--text-secondary);
}

.breadcrumb :deep(.el-breadcrumb__item:last-child) {
  color: var(--text-primary);
  font-weight: 500;
}

.breadcrumb :deep(.el-breadcrumb__separator) {
  color: var(--text-light);
  margin: 0 4px;
}

.breadcrumb-icon {
  display: flex;
  align-items: center;
  margin-right: 4px;
}

.header-center {
  flex: 2;
  display: flex;
  justify-content: center;
}

.search-wrapper {
  width: 100%;
  max-width: 400px;
}

.header-search {
  width: 100%;
  border-radius: var(--radius-lg);
  background-color: var(--bg-page);
  border: 1px solid transparent;
  transition: all var(--transition-fast);
}

.header-search:focus {
  border-color: var(--primary-color);
  background-color: var(--bg-card);
}

.search-icon {
  color: var(--text-light);
}

.header-right {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  flex: 1;
  justify-content: flex-end;
}

.header-btn {
  background: none;
  border: none;
  padding: var(--spacing-sm);
  border-radius: var(--radius-sm);
  color: var(--text-secondary);
  cursor: pointer;
  transition: all var(--transition-fast);
  position: relative;
}

.header-btn:hover {
  background-color: var(--bg-hover);
  color: var(--text-primary);
}

.btn-icon {
  transition: color var(--transition-fast);
}

.notification-badge {
  position: absolute;
  top: 2px;
  right: 2px;
  min-width: 16px;
  height: 16px;
  background-color: var(--danger-color);
  color: white;
  font-size: 10px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 4px;
}

.user-menu {
  position: relative;
  margin-left: var(--spacing-md);
}

.user-trigger {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  padding: var(--spacing-sm) var(--spacing-md);
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: background-color var(--transition-fast);
}

.user-trigger:hover {
  background-color: var(--bg-hover);
}

.user-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--primary-light) 0%, #D0E4FF 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--primary-color);
}

.user-name {
  font-size: var(--font-size-sm);
  color: var(--text-primary);
  font-weight: 500;
}

.user-arrow {
  color: var(--text-light);
  transition: transform var(--transition-fast);
}

.user-arrow.rotated {
  transform: rotate(180deg);
}

.user-dropdown {
  position: absolute;
  right: 0;
  top: calc(100% + 4px);
  background-color: var(--bg-card);
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-md);
  border: 1px solid var(--border-light);
  min-width: 160px;
  padding: var(--spacing-xs);
  z-index: 100;
}

.dropdown-item {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  padding: var(--spacing-md);
  border-radius: var(--radius-sm);
  cursor: pointer;
  transition: background-color var(--transition-fast);
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
}

.dropdown-item:hover {
  background-color: var(--bg-hover);
  color: var(--text-primary);
}

.dropdown-item.logout {
  color: var(--danger-color);
}

.dropdown-item.logout:hover {
  background-color: rgba(245, 63, 63, 0.08);
}

.dropdown-divider {
  height: 1px;
  background-color: var(--border-light);
  margin: var(--spacing-xs) 0;
}

/* 通知弹窗样式 */
.notification-list {
  max-height: 300px;
  overflow-y: auto;
}

.empty-notification {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 0;
  color: var(--text-light);
}

.empty-icon {
  margin-bottom: 12px;
}

.notification-item {
  display: flex;
  gap: 12px;
  padding: 12px;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.2s;
  position: relative;
}

.notification-item:hover {
  background-color: var(--bg-hover);
}

.notification-item.unread {
  background-color: rgba(22, 93, 255, 0.04);
}

.notification-dot {
  position: absolute;
  top: 12px;
  left: 12px;
  width: 6px;
  height: 6px;
  background-color: var(--danger-color);
  border-radius: 50%;
}

.notification-content {
  flex: 1;
  margin-left: 8px;
}

.notification-title {
  font-weight: 500;
  color: var(--text-primary);
  margin-bottom: 4px;
}

.notification-desc {
  font-size: 12px;
  color: var(--text-secondary);
  margin-bottom: 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.notification-time {
  font-size: 11px;
  color: var(--text-light);
}

.notification-footer {
  display: flex;
  justify-content: space-between;
}

/* 设置弹窗样式 */
.settings-form {
  max-height: 300px;
  overflow-y: auto;
}

/* 帮助文档弹窗样式 */
.help-container {
  display: flex;
  height: 400px;
}

.help-sidebar {
  width: 150px;
  border-right: 1px solid var(--border-light);
  padding-right: 12px;
}

.help-category {
  padding: 10px 12px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 13px;
  color: var(--text-secondary);
  transition: all 0.2s;
}

.help-category:hover {
  background-color: var(--bg-hover);
}

.help-category.active {
  background-color: var(--bg-active);
  color: var(--primary-color);
}

.help-content {
  flex: 1;
  padding-left: 16px;
  overflow-y: auto;
}

.help-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.help-item {
  padding: 12px;
  border-radius: 8px;
  cursor: pointer;
  background-color: var(--bg-page);
  transition: background-color 0.2s;
}

.help-item:hover {
  background-color: var(--bg-hover);
}

.help-item h4 {
  margin-bottom: 4px;
  color: var(--text-primary);
}

.help-item p {
  font-size: 12px;
  color: var(--text-secondary);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.help-doc h3 {
  margin-bottom: 16px;
  color: var(--text-primary);
}

.help-doc div {
  color: var(--text-secondary);
  line-height: 1.8;
}

/* 个人中心弹窗样式 */
.profile-container {
  padding: 16px 0;
}

.profile-avatar {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 24px;
}

.avatar-circle {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--primary-light) 0%, #D0E4FF 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 12px;
  color: var(--primary-color);
}

.profile-name {
  font-size: 18px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 4px;
}

.profile-role {
  font-size: 13px;
  color: var(--text-light);
}

.profile-info {
  background-color: var(--bg-page);
  border-radius: 8px;
  padding: 16px;
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 10px 0;
  border-bottom: 1px solid var(--border-light);
}

.info-row:last-child {
  border-bottom: none;
}

.info-label {
  color: var(--text-light);
}

.info-value {
  color: var(--text-primary);
  font-weight: 500;
}

/* 操作日志弹窗样式 */
.logs-footer {
  display: flex;
  justify-content: center;
}

@media (max-width: 992px) {
  .header-center {
    display: none;
  }
  
  .header-left {
    justify-content: flex-start;
  }
  
  .header-right {
    justify-content: flex-end;
  }
}

@media (max-width: 768px) {
  .header {
    padding: 0 var(--spacing-md);
  }
  
  .breadcrumbs {
    display: none;
  }
}
</style>