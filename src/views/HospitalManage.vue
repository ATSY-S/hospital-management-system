<template>
  <div class="hospital-page">
    <div class="page-header">
      <div class="page-title-section">
        <h1 class="page-title">医院管理</h1>
        <p class="page-subtitle">管理医院信息，支持增删改查操作</p>
      </div>
    </div>

    <div class="stats-row">
      <div class="stat-card">
        <div class="stat-icon bg-blue">🏥</div>
        <div class="stat-content">
          <div class="stat-value">{{ stats.total }}</div>
          <div class="stat-label">医院总数</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon bg-green">✅</div>
        <div class="stat-content">
          <div class="stat-value">{{ stats.normal }}</div>
          <div class="stat-label">正常运营</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon bg-orange">⏸️</div>
        <div class="stat-content">
          <div class="stat-value">{{ stats.pause }}</div>
          <div class="stat-label">暂停运营</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon bg-purple">🏆</div>
        <div class="stat-content">
          <div class="stat-value">{{ stats.topLevel }}</div>
          <div class="stat-label">三甲医院</div>
        </div>
      </div>
    </div>

    <div class="card">
      <div class="card-header">
        <div class="search-bar">
          <el-input v-model="searchName" placeholder="请输入医院名称" class="search-input" />
          <el-select v-model="searchLevel" placeholder="医院级别" class="search-select">
            <el-option label="全部" value="" />
            <el-option label="三甲" value="三甲" />
            <el-option label="三乙" value="三乙" />
            <el-option label="二甲" value="二甲" />
            <el-option label="二乙" value="二乙" />
          </el-select>
          <el-button type="primary" @click="handleSearch" class="btn-search">
            <el-icon :size="16"><Search /></el-icon>
            查询
          </el-button>
          <el-button @click="resetSearch" class="btn-reset">重置</el-button>
          <el-button type="success" @click="handleExport" class="btn-export">
            <el-icon :size="16"><Download /></el-icon>
            导出Excel
          </el-button>
        </div>
      </div>

      <div class="card-body">
        <div class="toolbar">
          <el-button type="primary" @click="openAddDialog" class="btn-add">
            <el-icon :size="16"><Plus /></el-icon>
            添加医院
          </el-button>
          <el-button 
            type="danger" 
            :disabled="selectedRows.length === 0" 
            @click="handleBatchDelete" 
            class="btn-batch-delete"
          >
            <el-icon :size="16"><Delete /></el-icon>
            批量删除 ({{ selectedRows.length }})
          </el-button>
        </div>

        <div class="table-wrapper">
          <el-table 
            :data="displayData" 
            border
            stripe
            :header-cell-style="{ background: '#F8FAFC', color: '#4E5969', fontWeight: '500' }"
            @selection-change="handleSelectionChange"
          >
            <el-table-column type="selection" width="55" />
            <el-table-column prop="id" label="ID" width="80" align="center" />
            <el-table-column prop="hospitalName" label="医院名称" min-width="150" />
            <el-table-column prop="hospitalType" label="医院类型" width="120" />
            <el-table-column prop="hospitalLevel" label="医院级别" width="100">
              <template #default="{ row }">
                <el-tag :type="getLevelTagType(row.hospitalLevel)">{{ row.hospitalLevel }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="hospitalCode" label="医院编码" width="120" />
            <el-table-column prop="hospitalAddress" label="医院地址" min-width="200" />
            <el-table-column prop="hospitalStatus" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.hospitalStatus === '正常' ? 'success' : 'warning'">
                  {{ row.hospitalStatus }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="160">
              <template #default="{ row }">
                <el-button type="text" @click="openEditDialog(row)" class="btn-edit">
                  <el-icon :size="14"><Edit /></el-icon>
                  编辑
                </el-button>
                <el-button type="text" @click="handleDelete(row.id)" class="btn-delete">
                  <el-icon :size="14"><Delete /></el-icon>
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div class="pagination-wrapper">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :total="total"
            layout="total, prev, pager, next, jumper"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </div>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="520px">
      <el-form :model="formData" label-width="110px" class="form-container">
        <el-form-item label="医院名称" required>
          <el-input v-model="formData.hospitalName" placeholder="请输入医院名称" />
        </el-form-item>
        <el-form-item label="医院类型" required>
          <el-select v-model="formData.hospitalType" placeholder="请选择医院类型">
            <el-option label="综合" value="综合" />
            <el-option label="专科" value="专科" />
            <el-option label="康复" value="康复" />
            <el-option label="护理" value="护理" />
            <el-option label="传染" value="传染" />
          </el-select>
        </el-form-item>
        <el-form-item label="医院级别" required>
          <el-select v-model="formData.hospitalLevel" placeholder="请选择医院级别">
            <el-option label="三甲" value="三甲" />
            <el-option label="三乙" value="三乙" />
            <el-option label="二甲" value="二甲" />
            <el-option label="二乙" value="二乙" />
          </el-select>
        </el-form-item>
        <el-form-item label="医院编码" required>
          <el-input v-model="formData.hospitalCode" placeholder="请输入医院编码" />
        </el-form-item>
        <el-form-item label="医院地址" required>
          <el-input v-model="formData.hospitalAddress" placeholder="请输入医院地址" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="formData.hospitalStatus">
            <el-radio label="正常" value="正常" />
            <el-radio label="暂停" value="暂停" />
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <div class="float-button" @click="openChatbot">
      <span class="float-icon">💬</span>
    </div>

    <div class="chatbot-modal" v-if="chatbotVisible" @click.self="closeChatbot">
      <div class="chatbot-container">
        <div class="chatbot-header">
          <span class="chatbot-icon">💬</span>
          <span class="chatbot-title">智能医疗助手</span>
          <button class="chatbot-close" @click="closeChatbot">✕</button>
        </div>
        <div class="chatbot-content">
          <div class="chatbot-welcome">
            <div class="welcome-icon">🤖</div>
            <div class="welcome-title">医疗问答助手</div>
            <div class="welcome-desc">专业、可靠的医疗健康咨询服务</div>
          </div>
          <div class="chatbot-messages">
            <div class="message bot-message">
              <div class="avatar">🤖</div>
              <div class="bubble">
                <div class="bubble-title">您好！我是您的智能医疗助手</div>
                <div class="bubble-text">请问有什么可以帮助您的？</div>
              </div>
            </div>
            <div class="message user-message">
              <div class="bubble user-bubble">黑龙江中医药大学附属医院在哪里？</div>
              <div class="avatar user-avatar">👤</div>
            </div>
            <div class="message bot-message">
              <div class="avatar">🤖</div>
              <div class="bubble">
                <div class="bubble-title">已为您找到附近的医院，请查看下方的医院信息。</div>
                <div class="bubble-text">健康提醒：前往医院就诊前，建议提前通过医院官方渠道预约挂号，避免耽误就诊时间。</div>
                <div class="hospital-card">
                  <div class="hospital-card-header">
                    <span class="hospital-icon">🏥</span>
                    <span class="hospital-name">黑龙江中医药大学附属第一医院</span>
                  </div>
                  <div class="hospital-item">
                    <span class="item-icon">📍</span>
                    <span class="item-text">和平路24号</span>
                  </div>
                  <div class="hospital-item">
                    <span class="item-icon">📞</span>
                    <span class="item-text">0451-82111401</span>
                  </div>
                  <div class="hospital-item">
                    <span class="item-icon">🏠</span>
                    <span class="item-text">哈尔滨市·香坊区</span>
                  </div>
                  <div class="hospital-actions">
                    <el-button type="primary" size="small" @click="openMap">导航</el-button>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="chatbot-input-area">
            <el-input v-model="chatInput" placeholder="请输入您的问题..." class="chat-input" @keyup.enter="sendMessage" />
            <el-button type="primary" @click="sendMessage">发送</el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { utils, writeFile } from 'xlsx'
import { Search, Download, Plus, Delete, Edit } from '@element-plus/icons-vue'
import { getHospitalList, addHospital, updateHospital, deleteHospital, searchHospital } from '../api/hospital'

const tableData = ref([])
const searchName = ref('')
const searchLevel = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const selectedRows = ref([])

const stats = computed(() => ({
  total: tableData.value.length,
  normal: tableData.value.filter(item => item.hospitalStatus === '正常').length,
  pause: tableData.value.filter(item => item.hospitalStatus === '暂停').length,
  topLevel: tableData.value.filter(item => item.hospitalLevel === '三甲').length
}))

const displayData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return tableData.value.slice(start, end)
})

const dialogVisible = ref(false)
const dialogTitle = ref('添加医院')
const formData = reactive({
  id: null,
  hospitalName: '',
  hospitalType: '',
  hospitalLevel: '',
  hospitalCode: '',
  hospitalAddress: '',
  hospitalStatus: '正常'
})

const chatbotVisible = ref(false)
const chatInput = ref('')

const getLevelTagType = (level) => {
  const typeMap = {
    '三甲': 'danger',
    '三乙': 'warning',
    '二甲': 'success',
    '二乙': 'info'
  }
  return typeMap[level] || 'info'
}

const fetchData = async () => {
  tableData.value = [
    { id: 1, hospitalName: '黑龙江中医药大学附属第一医院', hospitalType: '综合', hospitalLevel: '三甲', hospitalCode: 'HLJ001', hospitalAddress: '哈尔滨市香坊区和平路24号', hospitalStatus: '正常' },
    { id: 2, hospitalName: '哈尔滨市第一医院', hospitalType: '综合', hospitalLevel: '三甲', hospitalCode: 'HLJ002', hospitalAddress: '哈尔滨市道里区地段街151号', hospitalStatus: '正常' },
    { id: 3, hospitalName: '黑龙江省人民医院', hospitalType: '综合', hospitalLevel: '三甲', hospitalCode: 'HLJ003', hospitalAddress: '哈尔滨市香坊区中山路82号', hospitalStatus: '正常' },
    { id: 4, hospitalName: '哈尔滨市儿童医院', hospitalType: '专科', hospitalLevel: '三甲', hospitalCode: 'HLJ004', hospitalAddress: '哈尔滨市道里区友谊路57号', hospitalStatus: '正常' },
    { id: 5, hospitalName: '黑龙江省肿瘤医院', hospitalType: '专科', hospitalLevel: '三甲', hospitalCode: 'HLJ005', hospitalAddress: '哈尔滨市南岗区哈平路150号', hospitalStatus: '暂停' },
    { id: 6, hospitalName: '哈尔滨市中医院', hospitalType: '专科', hospitalLevel: '二甲', hospitalCode: 'HLJ006', hospitalAddress: '哈尔滨市道外区北棵头道街36号', hospitalStatus: '正常' },
    { id: 7, hospitalName: '黑龙江省康复医院', hospitalType: '康复', hospitalLevel: '二甲', hospitalCode: 'HLJ007', hospitalAddress: '哈尔滨市香坊区南直路365号', hospitalStatus: '正常' },
    { id: 8, hospitalName: '哈尔滨市传染病医院', hospitalType: '传染', hospitalLevel: '三乙', hospitalCode: 'HLJ008', hospitalAddress: '哈尔滨市道外区北棵头道街36号', hospitalStatus: '正常' },
    { id: 9, hospitalName: '黑龙江省眼科医院', hospitalType: '专科', hospitalLevel: '三乙', hospitalCode: 'HLJ009', hospitalAddress: '哈尔滨市南岗区东大直街151号', hospitalStatus: '暂停' },
    { id: 10, hospitalName: '哈尔滨市口腔医院', hospitalType: '专科', hospitalLevel: '二甲', hospitalCode: 'HLJ010', hospitalAddress: '哈尔滨市道里区经纬头道街23号', hospitalStatus: '正常' }
  ]
  total.value = tableData.value.length
  currentPage.value = 1
}

const handleSearch = async () => {
  try {
    const res = await searchHospital(searchName.value, searchLevel.value)
    tableData.value = res.data || []
    total.value = tableData.value.length
    currentPage.value = 1
  } catch (error) {
    ElMessage.error('查询失败')
  }
}

const resetSearch = () => {
  searchName.value = ''
  searchLevel.value = ''
  fetchData()
}

const handleCurrentChange = (page) => {
  currentPage.value = page
}

const handleExport = () => {
  if (tableData.value.length === 0) {
    ElMessage.warning('没有数据可导出')
    return
  }
  const exportData = tableData.value.map(item => ({
    'ID': item.id,
    '医院名称': item.hospitalName,
    '医院类型': item.hospitalType,
    '医院级别': item.hospitalLevel,
    '医院编码': item.hospitalCode,
    '医院地址': item.hospitalAddress,
    '状态': item.hospitalStatus
  }))
  const worksheet = utils.json_to_sheet(exportData)
  const workbook = utils.book_new()
  utils.book_append_sheet(workbook, worksheet, '医院数据')
  writeFile(workbook, '医院数据.xlsx')
  ElMessage.success('导出成功')
}

const handleSelectionChange = (val) => {
  selectedRows.value = val
}

const openAddDialog = () => {
  dialogTitle.value = '添加医院'
  formData.id = null
  formData.hospitalName = ''
  formData.hospitalType = ''
  formData.hospitalLevel = ''
  formData.hospitalCode = ''
  formData.hospitalAddress = ''
  formData.hospitalStatus = '正常'
  dialogVisible.value = true
}

const openEditDialog = (row) => {
  dialogTitle.value = '修改医院'
  formData.id = row.id
  formData.hospitalName = row.hospitalName
  formData.hospitalType = row.hospitalType
  formData.hospitalLevel = row.hospitalLevel
  formData.hospitalCode = row.hospitalCode
  formData.hospitalAddress = row.hospitalAddress
  formData.hospitalStatus = row.hospitalStatus
  dialogVisible.value = true
}

const handleSubmit = async () => {
  try {
    if (formData.id) {
      await updateHospital(formData)
      ElMessage.success('修改成功')
    } else {
      await addHospital(formData)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    fetchData()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定删除该医院吗？', '提示', { type: 'warning' })
    .then(async () => {
      await deleteHospital(id)
      ElMessage.success('删除成功')
      fetchData()
    }).catch(() => {})
}

const handleBatchDelete = () => {
  ElMessageBox.confirm(`确定删除选中的 ${selectedRows.value.length} 家医院吗？`, '提示', { type: 'warning' })
    .then(async () => {
      const ids = selectedRows.value.map(row => row.id)
      for (const id of ids) {
        await deleteHospital(id)
      }
      ElMessage.success('批量删除成功')
      fetchData()
    }).catch(() => {})
}

const openChatbot = () => {
  chatbotVisible.value = true
}

const closeChatbot = () => {
  chatbotVisible.value = false
}

const sendMessage = () => {
  if (chatInput.value.trim()) {
    ElMessage.info('消息已发送')
    chatInput.value = ''
  }
}

const openMap = () => {
  const address = '黑龙江中医药大学附属第一医院'
  const key = '7cd9f10782ac1d363895d92096927931'
  const mapUrl = `https://uri.amap.com/marker?position=126.6424,45.7487&name=${encodeURIComponent(address)}&coordinate=wgs84&callnative=1&key=${key}`
  window.open(mapUrl, '_blank')
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.hospital-page {
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(8px); }
  to { opacity: 1; transform: translateY(0); }
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-xxl);
}

.page-title-section {
  flex: 1;
}

.page-title {
  font-size: var(--font-size-xl);
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 var(--spacing-xs) 0;
}

.page-subtitle {
  font-size: var(--font-size-sm);
  color: var(--text-light);
  margin: 0;
}

.stats-row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: var(--spacing-xl);
  margin-bottom: var(--spacing-xxl);
}

.stat-card {
  background: var(--bg-card);
  border-radius: var(--radius-md);
  padding: var(--spacing-xl);
  display: flex;
  align-items: center;
  gap: var(--spacing-lg);
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--border-light);
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
  background: var(--primary-color);
}

.stat-icon {
  width: 50px;
  height: 50px;
  border-radius: var(--radius-lg);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}

.stat-icon.bg-blue {
  background: linear-gradient(135deg, #E8F0FF 0%, #D0E4FF 100%);
}

.stat-icon.bg-green {
  background: linear-gradient(135deg, #E6F7E6 0%, #C8E6C9 100%);
}

.stat-icon.bg-orange {
  background: linear-gradient(135deg, #FFF7E6 0%, #FFE0B2 100%);
}

.stat-icon.bg-purple {
  background: linear-gradient(135deg, #F3E5F5 0%, #E1BEE7 100%);
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: var(--text-primary);
}

.stat-label {
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
}

.card {
  background: var(--bg-card);
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--border-light);
  overflow: hidden;
}

.card-header {
  padding: var(--spacing-lg);
  border-bottom: 1px solid var(--border-light);
  background: #FAFBFC;
}

.search-bar {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  flex-wrap: wrap;
}

.search-input {
  width: 260px;
}

.search-select {
  width: 130px;
}

.card-body {
  padding: var(--spacing-lg);
}

.toolbar {
  display: flex;
  gap: var(--spacing-md);
  margin-bottom: var(--spacing-lg);
}

.btn-add, .btn-batch-delete, .btn-search, .btn-reset, .btn-export {
  display: flex;
  align-items: center;
  gap: var(--spacing-xs);
}

.table-wrapper {
  overflow-x: auto;
}

.el-table {
  font-size: var(--font-size-sm);
  border-radius: var(--radius-md);
  overflow: hidden;
}

.el-table th {
  padding: var(--spacing-md);
  background: #F8FAFC;
}

.el-table td {
  padding: var(--spacing-md);
}

.el-table__row {
  height: 48px;
}

.el-table__body tr:hover {
  background: rgba(22, 93, 255, 0.04);
}

.btn-edit {
  color: var(--primary-color);
}

.btn-delete {
  color: var(--danger-color);
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: var(--spacing-xl);
  padding-top: var(--spacing-lg);
  border-top: 1px solid var(--border-light);
}

.form-container {
  padding: var(--spacing-sm);
}

.form-container :deep(.el-form-item) {
  margin-bottom: var(--spacing-md);
}

.float-button {
  position: fixed;
  right: 30px;
  bottom: 30px;
  background: linear-gradient(135deg, #165DFF 0%, #0D47A1 100%);
  color: white;
  width: 56px;
  height: 56px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 4px 15px rgba(22, 93, 255, 0.4);
  z-index: 100;
  animation: float 3s ease-in-out infinite;
}

.float-icon {
  font-size: 24px;
}

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}

.chatbot-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.chatbot-container {
  width: 520px;
  height: 620px;
  background: white;
  border-radius: var(--radius-lg);
  overflow: hidden;
  box-shadow: var(--shadow-lg);
  display: flex;
  flex-direction: column;
}

.chatbot-header {
  background: linear-gradient(135deg, #165DFF 0%, #0D47A1 100%);
  color: white;
  padding: var(--spacing-lg);
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
}

.chatbot-icon {
  font-size: 20px;
}

.chatbot-title {
  flex: 1;
  font-size: var(--font-size-md);
  font-weight: 600;
}

.chatbot-close {
  background: rgba(255,255,255,0.2);
  border: none;
  color: white;
  width: 28px;
  height: 28px;
  border-radius: 50%;
  cursor: pointer;
  font-size: 14px;
}

.chatbot-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.chatbot-welcome {
  padding: var(--spacing-xl);
  text-align: center;
  background: linear-gradient(135deg, #F5F7FA 0%, #E8F0FF 100%);
}

.welcome-icon {
  font-size: 48px;
  margin-bottom: var(--spacing-sm);
}

.welcome-title {
  font-size: var(--font-size-lg);
  font-weight: 600;
  color: var(--primary-color);
  margin-bottom: var(--spacing-xs);
}

.welcome-desc {
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
}

.chatbot-messages {
  flex: 1;
  padding: var(--spacing-lg);
  overflow-y: auto;
}

.message {
  display: flex;
  gap: var(--spacing-md);
  margin-bottom: var(--spacing-lg);
}

.message.bot-message {
  justify-content: flex-start;
}

.message.user-message {
  justify-content: flex-end;
}

.avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: #E8F0FF;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  flex-shrink: 0;
}

.user-avatar {
  background: #D0E4FF;
}

.bubble {
  max-width: 70%;
  background: #F5F7FA;
  border-radius: var(--radius-lg);
  padding: var(--spacing-md);
}

.user-bubble {
  background: var(--primary-color);
  color: white;
}

.bubble-title {
  font-weight: 500;
  font-size: var(--font-size-sm);
  margin-bottom: var(--spacing-xs);
}

.user-bubble .bubble-title {
  color: white;
}

.bubble-text {
  font-size: var(--font-size-xs);
  color: var(--text-secondary);
  line-height: 1.5;
}

.user-bubble .bubble-text {
  color: rgba(255,255,255,0.85);
}

.hospital-card {
  background: white;
  border: 1px solid var(--primary-light);
  border-radius: var(--radius-md);
  padding: var(--spacing-md);
  margin-top: var(--spacing-md);
}

.hospital-card-header {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  margin-bottom: var(--spacing-md);
}

.hospital-icon {
  font-size: 18px;
}

.hospital-name {
  font-weight: 500;
  font-size: var(--font-size-sm);
  color: var(--primary-color);
}

.hospital-item {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  margin-bottom: var(--spacing-sm);
  font-size: var(--font-size-xs);
  color: var(--text-secondary);
}

.item-icon {
  font-size: 12px;
}

.hospital-actions {
  margin-top: var(--spacing-md);
  text-align: right;
}

.chatbot-input-area {
  padding: var(--spacing-md) var(--spacing-lg);
  border-top: 1px solid var(--border-light);
  display: flex;
  gap: var(--spacing-md);
}

.chat-input {
  flex: 1;
}

@media (max-width: 1200px) {
  .stats-row {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .stats-row {
    grid-template-columns: 1fr;
  }
  
  .search-bar {
    flex-direction: column;
    align-items: stretch;
  }
  
  .search-input, .search-select {
    width: 100%;
  }
  
  .toolbar {
    flex-wrap: wrap;
  }
  
  .chatbot-container {
    width: calc(100% - 32px);
    height: 80vh;
  }
}
</style>