<template>
  <div class="register-page">
    <div class="page-header">
      <div class="page-title-section">
        <h1 class="page-title">挂号管理</h1>
        <p class="page-subtitle">管理挂号信息，支持预约、取消等操作</p>
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
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon bg-green">
          <el-icon :size="24"><Clock /></el-icon>
        </div>
        <div class="stat-content">
          <p class="stat-value">{{ stats.waiting }}</p>
          <p class="stat-label">待就诊</p>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon bg-orange">
          <el-icon :size="24"><CircleCheck /></el-icon>
        </div>
        <div class="stat-content">
          <p class="stat-value">{{ stats.completed }}</p>
          <p class="stat-label">已就诊</p>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon bg-purple">
          <el-icon :size="24"><CircleClose /></el-icon>
        </div>
        <div class="stat-content">
          <p class="stat-value">{{ stats.cancelled }}</p>
          <p class="stat-label">已取消</p>
        </div>
      </div>
    </div>

    <div class="card">
      <div class="card-header">
        <div class="search-bar">
          <el-input
            v-model="searchPatient"
            placeholder="请输入患者姓名"
            class="search-input"
            clearable
            prefix-icon="Search"
          />
          <el-select v-model="searchDepartment" placeholder="就诊科室" clearable class="search-select">
            <el-option label="全部" value="" />
            <el-option label="内科" value="内科" />
            <el-option label="外科" value="外科" />
            <el-option label="儿科" value="儿科" />
            <el-option label="骨科" value="骨科" />
            <el-option label="眼科" value="眼科" />
            <el-option label="皮肤科" value="皮肤科" />
            <el-option label="耳鼻喉" value="耳鼻喉" />
            <el-option label="口腔科" value="口腔科" />
            <el-option label="急诊科" value="急诊科" />
          </el-select>
          <el-select v-model="searchStatus" placeholder="挂号状态" clearable class="search-select">
            <el-option label="全部" value="" />
            <el-option label="待就诊" value="待就诊" />
            <el-option label="已就诊" value="已就诊" />
            <el-option label="已取消" value="已取消" />
          </el-select>
        </div>
        <div class="action-group">
          <el-button type="primary" @click="handleSearch" class="btn-search">
            <el-icon :size="16" class="mr-1"><Search /></el-icon>
            查询
          </el-button>
          <el-button @click="resetSearch">重置</el-button>
        </div>
      </div>
    </div>

    <div class="card">
      <div class="card-header">
        <div class="button-bar">
          <el-button type="primary" @click="openAddDialog" class="btn-add">
            <el-icon :size="16" class="mr-1"><Plus /></el-icon>
            预约挂号
          </el-button>
          <el-button type="danger" :disabled="selectedRows.length === 0" @click="handleBatchCancel">
            <el-icon :size="16" class="mr-1"><CircleClose /></el-icon>
            批量取消 ({{ selectedRows.length }})
          </el-button>
        </div>
      </div>
      <div class="table-wrapper">
        <el-table
          v-loading="loading"
          :data="paginatedData"
          border
          stripe
          class="register-table"
          @selection-change="handleSelectionChange"
          :header-cell-style="{ background: '#f8fafc', color: '#4E5969', fontWeight: '500' }"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="id" label="ID" width="80" align="center" />
          <el-table-column prop="patientName" label="患者姓名" min-width="120" />
          <el-table-column prop="hospitalName" label="就诊医院" min-width="150" />
          <el-table-column prop="department" label="就诊科室" width="120" />
          <el-table-column prop="doctorName" label="接诊医生" width="120" />
          <el-table-column prop="registerDate" label="挂号日期" width="120" />
          <el-table-column prop="registerTime" label="挂号时段" width="120" />
          <el-table-column prop="fee" label="挂号费用" width="100" align="center">
            <template #default="{ row }">
              <span class="fee-text">¥{{ row.fee }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100" align="center">
            <template #default="{ row }">
              <el-tag :type="getStatusTagType(row.status)" class="status-tag">{{ row.status }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180" align="center">
            <template #default="{ row }">
              <el-button type="text" @click="openEditDialog(row)" class="btn-action btn-edit" v-if="row.status === '待就诊'">
                <el-icon :size="14"><Edit /></el-icon>
                编辑
              </el-button>
              <el-button type="text" @click="handleCancel(row.id)" class="btn-action btn-cancel" v-if="row.status === '待就诊'">
                <el-icon :size="14"><CircleClose /></el-icon>
                取消
              </el-button>
              <el-button type="text" @click="handleComplete(row.id)" class="btn-action btn-complete" v-if="row.status === '待就诊'">
                <el-icon :size="14"><CircleCheck /></el-icon>
                完成
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="pagination-container">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 50, 100]"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            background
          />
        </div>
      </div>
    </div>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      :close-on-click-modal="false"
      class="custom-dialog"
      @close="handleDialogClose"
    >
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="患者姓名" prop="patientName">
          <el-input v-model="formData.patientName" placeholder="请输入患者姓名" />
        </el-form-item>
        <el-form-item label="就诊医院" prop="hospitalName">
          <el-select v-model="formData.hospitalName" placeholder="请选择就诊医院">
            <el-option label="哈尔滨第一医院" value="哈尔滨第一医院" />
            <el-option label="哈尔滨第二医院" value="哈尔滨第二医院" />
            <el-option label="杭州第三医院" value="杭州第三医院" />
            <el-option label="哈尔滨第四医院" value="哈尔滨第四医院" />
            <el-option label="哈尔滨第五医院" value="哈尔滨第五医院" />
          </el-select>
        </el-form-item>
        <el-form-item label="就诊科室" prop="department">
          <el-select v-model="formData.department" placeholder="请选择就诊科室">
            <el-option label="内科" value="内科" />
            <el-option label="外科" value="外科" />
            <el-option label="儿科" value="儿科" />
            <el-option label="骨科" value="骨科" />
            <el-option label="眼科" value="眼科" />
            <el-option label="皮肤科" value="皮肤科" />
            <el-option label="耳鼻喉" value="耳鼻喉" />
            <el-option label="口腔科" value="口腔科" />
            <el-option label="急诊科" value="急诊科" />
          </el-select>
        </el-form-item>
        <el-form-item label="接诊医生" prop="doctorName">
          <el-input v-model="formData.doctorName" placeholder="请输入接诊医生" />
        </el-form-item>
        <el-form-item label="挂号日期" prop="registerDate">
          <el-date-picker v-model="formData.registerDate" type="date" placeholder="请选择挂号日期" />
        </el-form-item>
        <el-form-item label="挂号时段" prop="registerTime">
          <el-select v-model="formData.registerTime" placeholder="请选择挂号时段">
            <el-option label="上午 08:00-12:00" value="上午 08:00-12:00" />
            <el-option label="下午 14:00-18:00" value="下午 14:00-18:00" />
            <el-option label="晚上 18:00-21:00" value="晚上 18:00-21:00" />
          </el-select>
        </el-form-item>
        <el-form-item label="挂号费用" prop="fee">
          <el-input type="number" v-model="formData.fee" placeholder="请输入挂号费用" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Calendar, Clock, CircleCheck, CircleClose, Search, Plus, Edit } from '@element-plus/icons-vue'

const loading = ref(false)
const tableData = ref([])
const selectedRows = ref([])
const searchPatient = ref('')
const searchDepartment = ref('')
const searchStatus = ref('')

const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const paginatedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return tableData.value.slice(start, end)
})

const stats = computed(() => ({
  todayRegister: tableData.value.filter(item => item.registerDate === new Date().toISOString().split('T')[0]).length,
  waiting: tableData.value.filter(item => item.status === '待就诊').length,
  completed: tableData.value.filter(item => item.status === '已就诊').length,
  cancelled: tableData.value.filter(item => item.status === '已取消').length
}))

const dialogVisible = ref(false)
const dialogTitle = ref('预约挂号')
const formRef = ref(null)
const formData = reactive({
  id: null,
  patientName: '',
  hospitalName: '',
  department: '',
  doctorName: '',
  registerDate: '',
  registerTime: '',
  fee: '',
  status: '待就诊'
})

const formRules = {
  patientName: [{ required: true, message: '请输入患者姓名', trigger: 'blur' }],
  hospitalName: [{ required: true, message: '请选择就诊医院', trigger: 'change' }],
  department: [{ required: true, message: '请选择就诊科室', trigger: 'change' }],
  doctorName: [{ required: true, message: '请输入接诊医生', trigger: 'blur' }],
  registerDate: [{ required: true, message: '请选择挂号日期', trigger: 'change' }],
  registerTime: [{ required: true, message: '请选择挂号时段', trigger: 'change' }],
  fee: [{ required: true, message: '请输入挂号费用', trigger: 'blur' }, { type: 'number', message: '费用必须为数字', trigger: 'blur' }]
}

const getStatusTagType = (status) => {
  const typeMap = {
    '待就诊': 'warning',
    '已就诊': 'success',
    '已取消': 'danger'
  }
  return typeMap[status] || 'info'
}

const fetchData = async () => {
  loading.value = true
  try {
    const today = new Date().toISOString().split('T')[0]
    tableData.value = [
      { id: 1, patientName: '张三', hospitalName: '哈尔滨第一医院', department: '内科', doctorName: '李医生', registerDate: today, registerTime: '上午 08:00-12:00', fee: 50, status: '待就诊' },
      { id: 2, patientName: '李四', hospitalName: '哈尔滨第二医院', department: '外科', doctorName: '王医生', registerDate: today, registerTime: '上午 08:00-12:00', fee: 60, status: '待就诊' },
      { id: 3, patientName: '王五', hospitalName: '杭州第三医院', department: '儿科', doctorName: '张医生', registerDate: today, registerTime: '下午 14:00-18:00', fee: 40, status: '已就诊' },
      { id: 4, patientName: '赵六', hospitalName: '哈尔滨第一医院', department: '骨科', doctorName: '刘医生', registerDate: today, registerTime: '下午 14:00-18:00', fee: 70, status: '待就诊' },
      { id: 5, patientName: '孙七', hospitalName: '哈尔滨第四医院', department: '眼科', doctorName: '陈医生', registerDate: today, registerTime: '晚上 18:00-21:00', fee: 50, status: '已取消' },
      { id: 6, patientName: '周八', hospitalName: '哈尔滨第五医院', department: '内科', doctorName: '李医生', registerDate: today, registerTime: '上午 08:00-12:00', fee: 50, status: '已就诊' },
      { id: 7, patientName: '吴九', hospitalName: '哈尔滨第六医院', department: '皮肤科', doctorName: '郑医生', registerDate: today, registerTime: '下午 14:00-18:00', fee: 45, status: '待就诊' },
      { id: 8, patientName: '郑十', hospitalName: '哈尔滨第七医院', department: '耳鼻喉', doctorName: '黄医生', registerDate: today, registerTime: '上午 08:00-12:00', fee: 55, status: '已就诊' },
      { id: 9, patientName: '钱十一', hospitalName: '哈尔滨第八医院', department: '口腔科', doctorName: '周医生', registerDate: today, registerTime: '下午 14:00-18:00', fee: 50, status: '待就诊' },
      { id: 10, patientName: '孙十二', hospitalName: '哈尔滨第九医院', department: '急诊科', doctorName: '吴医生', registerDate: today, registerTime: '晚上 18:00-21:00', fee: 80, status: '已就诊' }
    ]
    total.value = tableData.value.length
    currentPage.value = 1
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  let result = [...tableData.value]
  if (searchPatient.value) {
    result = result.filter(item => item.patientName.includes(searchPatient.value))
  }
  if (searchDepartment.value) {
    result = result.filter(item => item.department === searchDepartment.value)
  }
  if (searchStatus.value) {
    result = result.filter(item => item.status === searchStatus.value)
  }
  tableData.value = result
  total.value = tableData.value.length
  currentPage.value = 1
  ElMessage.success('查询成功')
}

const resetSearch = () => {
  searchPatient.value = ''
  searchDepartment.value = ''
  searchStatus.value = ''
  fetchData()
}

const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
}

const handleCurrentChange = (val) => {
  currentPage.value = val
}

const handleSelectionChange = (selection) => {
  selectedRows.value = selection
}

const openAddDialog = () => {
  dialogTitle.value = '预约挂号'
  resetFormData()
  dialogVisible.value = true
}

const openEditDialog = (row) => {
  dialogTitle.value = '编辑挂号信息'
  Object.assign(formData, {
    id: row.id,
    patientName: row.patientName,
    hospitalName: row.hospitalName,
    department: row.department,
    doctorName: row.doctorName,
    registerDate: row.registerDate,
    registerTime: row.registerTime,
    fee: row.fee
  })
  dialogVisible.value = true
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
  resetFormData()
}

const resetFormData = () => {
  Object.assign(formData, {
    id: null,
    patientName: '',
    hospitalName: '',
    department: '',
    doctorName: '',
    registerDate: '',
    registerTime: '',
    fee: '',
    status: '待就诊'
  })
}

const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()

    if (formData.id) {
      const index = tableData.value.findIndex(item => item.id === formData.id)
      if (index !== -1) {
        tableData.value[index] = { ...formData, status: '待就诊' }
      }
      ElMessage.success('更新成功')
    } else {
      formData.id = Date.now()
      tableData.value.unshift({ ...formData })
      total.value = tableData.value.length
      ElMessage.success('挂号成功')
    }

    dialogVisible.value = false
  } catch (error) {
    if (error !== false) {
      console.error('操作失败:', error)
      ElMessage.error('操作失败')
    }
  }
}

const handleCancel = (id) => {
  ElMessageBox.confirm(
    '确定要取消该挂号吗？',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    const index = tableData.value.findIndex(item => item.id === id)
    if (index !== -1) {
      tableData.value[index].status = '已取消'
      ElMessage.success('取消成功')
    }
  }).catch(() => {})
}

const handleComplete = (id) => {
  const index = tableData.value.findIndex(item => item.id === id)
  if (index !== -1) {
    tableData.value[index].status = '已就诊'
    ElMessage.success('就诊完成')
  }
}

const handleBatchCancel = async () => {
  if (selectedRows.value.length === 0) return

  try {
    await ElMessageBox.confirm(
      `确定要取消选中的 ${selectedRows.value.length} 条挂号吗？`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const ids = selectedRows.value.map(row => row.id)
    tableData.value.forEach(item => {
      if (ids.includes(item.id)) {
        item.status = '已取消'
      }
    })
    selectedRows.value = []
    ElMessage.success('批量取消成功')
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量取消失败:', error)
      ElMessage.error('批量取消失败')
    }
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.register-page {
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

.stats-grid {
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
  transition: all 0.2s ease;
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

.stat-card:hover {
  box-shadow: var(--shadow-md);
  transform: translateY(-2px);
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: var(--radius-lg);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.stat-icon.bg-blue {
  background: linear-gradient(135deg, #E8F0FF 0%, #D0E4FF 100%);
  color: var(--primary-color);
}

.stat-icon.bg-green {
  background: linear-gradient(135deg, #E6F7E6 0%, #C8E6C9 100%);
  color: var(--success-color);
}

.stat-icon.bg-orange {
  background: linear-gradient(135deg, #FFF7E6 0%, #FFE0B2 100%);
  color: var(--warning-color);
}

.stat-icon.bg-purple {
  background: linear-gradient(135deg, #F3E5F5 0%, #E1BEE7 100%);
  color: #7232DD;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0 0 var(--spacing-xs) 0;
}

.stat-label {
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
  margin: 0;
}

.card {
  background: var(--bg-card);
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-sm);
  margin-bottom: var(--spacing-xl);
  overflow: hidden;
  border: 1px solid var(--border-light);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--spacing-lg) var(--spacing-xl);
  border-bottom: 1px solid var(--border-light);
  background: #FAFBFC;
  flex-wrap: wrap;
  gap: var(--spacing-md);
}

.search-bar {
  display: flex;
  gap: var(--spacing-md);
  flex-wrap: wrap;
}

.search-input {
  width: 280px;
}

.search-select {
  width: 160px;
}

.action-group {
  display: flex;
  gap: var(--spacing-sm);
}

.btn-search {
  display: flex;
  align-items: center;
}

.button-bar {
  display: flex;
  gap: var(--spacing-md);
}

.btn-add {
  display: flex;
  align-items: center;
}

.table-wrapper {
  padding: var(--spacing-lg) var(--spacing-xl);
}

.register-table {
  width: 100%;
  border-radius: var(--radius-md);
  overflow: hidden;
}

.register-table :deep(.el-table__header) {
  background: #F8FAFC;
}

.register-table :deep(.el-table__body tr:hover) {
  background: rgba(22, 93, 255, 0.04);
}

.register-table :deep(.el-table__row) {
  height: 48px;
}

.fee-text {
  font-weight: 600;
  color: var(--primary-color);
}

.status-tag {
  padding: 2px 8px;
  border-radius: var(--radius-sm);
  font-size: var(--font-size-xs);
}

.btn-action {
  margin: 0 var(--spacing-xs);
  padding: var(--spacing-xs) var(--spacing-sm);
  font-size: var(--font-size-xs);
}

.btn-edit {
  color: var(--primary-color);
}

.btn-edit:hover {
  color: #0d47a1;
}

.btn-cancel {
  color: var(--danger-color);
}

.btn-cancel:hover {
  color: #c41e3a;
}

.btn-complete {
  color: var(--success-color);
}

.btn-complete:hover {
  color: #008929;
}

.pagination-container {
  padding: var(--spacing-lg) 0;
  display: flex;
  justify-content: center;
  border-top: 1px solid var(--border-light);
  margin-top: var(--spacing-lg);
}

.custom-dialog :deep(.el-dialog) {
  width: 600px;
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-lg);
}

.custom-dialog :deep(.el-dialog__header) {
  background: var(--bg-page);
  border-bottom: 1px solid var(--border-light);
  padding: var(--spacing-lg) var(--spacing-xl);
}

.custom-dialog :deep(.el-dialog__title) {
  color: var(--text-primary);
  font-weight: 600;
  font-size: var(--font-size-md);
}

.custom-dialog :deep(.el-dialog__headerbtn) {
  color: var(--text-light);
}

.custom-dialog :deep(.el-dialog__body) {
  padding: var(--spacing-xl);
}

.custom-dialog :deep(.el-dialog__footer) {
  border-top: 1px solid var(--border-light);
  padding: var(--spacing-md) var(--spacing-xl);
  display: flex;
  justify-content: flex-end;
  gap: var(--spacing-md);
}

.custom-dialog :deep(.el-form-item) {
  margin-bottom: var(--spacing-lg);
}

.custom-dialog :deep(.el-form-item__label) {
  text-align: right;
  color: var(--text-secondary);
  font-weight: 500;
  width: 100px;
}

.custom-dialog :deep(.el-form-item__content) {
  margin-left: 110px;
}

.custom-dialog :deep(.el-input__wrapper),
.custom-dialog :deep(.el-select__wrapper),
.custom-dialog :deep(.el-date-picker__wrapper) {
  height: 40px;
  border-radius: var(--radius-md);
}

.custom-dialog :deep(.el-button--primary) {
  height: 36px;
  padding: 0 var(--spacing-xl);
  border-radius: var(--radius-md);
}

.custom-dialog :deep(.el-button--default) {
  height: 36px;
  padding: 0 var(--spacing-xl);
  border-radius: var(--radius-md);
}

@media (max-width: 1200px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .card-header {
    flex-direction: column;
    align-items: stretch;
  }
  
  .search-bar {
    justify-content: center;
  }
  
  .search-input, .search-select {
    width: 100%;
  }
  
  .action-group {
    justify-content: center;
  }
  
  .custom-dialog :deep(.el-dialog) {
    width: calc(100% - 32px);
  }
}
</style>