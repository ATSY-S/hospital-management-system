<template>
  <div class="patient-page">
    <div class="page-header">
      <div class="page-title-section">
        <h1 class="page-title">患者管理</h1>
        <p class="page-subtitle">管理患者信息，支持增删改查操作</p>
      </div>
    </div>

    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon bg-blue">
          <el-icon :size="24"><User /></el-icon>
        </div>
        <div class="stat-content">
          <p class="stat-value">{{ stats.total }}</p>
          <p class="stat-label">患者总数</p>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon bg-green">
          <el-icon :size="24"><UserFilled /></el-icon>
        </div>
        <div class="stat-content">
          <p class="stat-value">{{ stats.male }}</p>
          <p class="stat-label">男性患者</p>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon bg-orange">
          <el-icon :size="24"><User /></el-icon>
        </div>
        <div class="stat-content">
          <p class="stat-value">{{ stats.female }}</p>
          <p class="stat-label">女性患者</p>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon bg-purple">
          <el-icon :size="24"><FirstAidKit /></el-icon>
        </div>
        <div class="stat-content">
          <p class="stat-value">{{ stats.inHospital }}</p>
          <p class="stat-label">在院患者</p>
        </div>
      </div>
    </div>

    <div class="card">
      <div class="card-header">
        <div class="search-bar">
          <el-input
            v-model="searchName"
            placeholder="请输入患者姓名"
            class="search-input"
            clearable
            prefix-icon="Search"
          />
          <el-select v-model="searchGender" placeholder="性别" clearable class="search-select">
            <el-option label="全部" value="" />
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
          <el-select v-model="searchStatus" placeholder="就诊状态" clearable class="search-select">
            <el-option label="全部" value="" />
            <el-option label="就诊中" value="就诊中" />
            <el-option label="已出院" value="已出院" />
            <el-option label="预约中" value="预约中" />
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
            添加患者
          </el-button>
          <el-button type="danger" :disabled="selectedRows.length === 0" @click="handleBatchDelete">
            <el-icon :size="16" class="mr-1"><Delete /></el-icon>
            批量删除 ({{ selectedRows.length }})
          </el-button>
        </div>
      </div>
      <div class="table-wrapper">
        <el-table
          v-loading="loading"
          :data="paginatedData"
          border
          stripe
          class="patient-table"
          @selection-change="handleSelectionChange"
          :header-cell-style="{ background: '#f8fafc', color: '#4E5969', fontWeight: '500' }"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="id" label="ID" width="80" align="center" />
          <el-table-column prop="patientName" label="患者姓名" min-width="120" />
          <el-table-column prop="gender" label="性别" width="80" align="center" />
          <el-table-column prop="age" label="年龄" width="80" align="center" />
          <el-table-column prop="phone" label="联系电话" width="150" align="center" />
          <el-table-column prop="idCard" label="身份证号" width="180" />
          <el-table-column prop="hospitalName" label="就诊医院" min-width="150" />
          <el-table-column prop="department" label="就诊科室" width="120" />
          <el-table-column prop="status" label="状态" width="100" align="center">
            <template #default="{ row }">
              <el-tag :type="getStatusTagType(row.status)" class="status-tag">{{ row.status }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="160" align="center">
            <template #default="{ row }">
              <el-button type="text" @click="openEditDialog(row)" class="btn-action btn-edit">
                <el-icon :size="14"><Edit /></el-icon>
                编辑
              </el-button>
              <el-button type="text" @click="handleDelete(row.id)" class="btn-action btn-delete">
                <el-icon :size="14"><Delete /></el-icon>
                删除
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
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="formData.gender">
            <el-radio label="男" class="radio-item">男</el-radio>
            <el-radio label="女" class="radio-item">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input type="number" v-model="formData.age" placeholder="请输入年龄" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="formData.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="formData.idCard" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="就诊医院" prop="hospitalName">
          <el-input v-model="formData.hospitalName" placeholder="请输入就诊医院" />
        </el-form-item>
        <el-form-item label="就诊科室" prop="department">
          <el-input v-model="formData.department" placeholder="请输入就诊科室" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio label="就诊中" class="radio-item">就诊中</el-radio>
            <el-radio label="已出院" class="radio-item">已出院</el-radio>
            <el-radio label="预约中" class="radio-item">预约中</el-radio>
          </el-radio-group>
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
import { User, UserFilled, FirstAidKit, Search, Plus, Delete, Edit } from '@element-plus/icons-vue'

const loading = ref(false)
const tableData = ref([])
const selectedRows = ref([])
const searchName = ref('')
const searchGender = ref('')
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
  total: tableData.value.length,
  male: tableData.value.filter(item => item.gender === '男').length,
  female: tableData.value.filter(item => item.gender === '女').length,
  inHospital: tableData.value.filter(item => item.status === '就诊中').length
}))

const dialogVisible = ref(false)
const dialogTitle = ref('添加患者')
const formRef = ref(null)
const formData = reactive({
  id: null,
  patientName: '',
  gender: '男',
  age: '',
  phone: '',
  idCard: '',
  hospitalName: '',
  department: '',
  status: '就诊中'
})

const formRules = {
  patientName: [{ required: true, message: '请输入患者姓名', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  age: [{ required: true, message: '请输入年龄', trigger: 'blur' }, { type: 'number', message: '年龄必须为数字', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
  idCard: [{ required: true, message: '请输入身份证号', trigger: 'blur' }],
  hospitalName: [{ required: true, message: '请输入就诊医院', trigger: 'blur' }],
  department: [{ required: true, message: '请输入就诊科室', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

const getStatusTagType = (status) => {
  const typeMap = {
    '就诊中': 'success',
    '已出院': 'info',
    '预约中': 'warning'
  }
  return typeMap[status] || 'info'
}

const fetchData = async () => {
  loading.value = true
  try {
    tableData.value = [
      { id: 1, patientName: '张三', gender: '男', age: 35, phone: '13800138001', idCard: '110101198901011234', hospitalName: '哈尔滨第一医院', department: '内科', status: '就诊中' },
      { id: 2, patientName: '李四', gender: '女', age: 28, phone: '13800138002', idCard: '110101199602022345', hospitalName: '哈尔滨第二医院', department: '外科', status: '就诊中' },
      { id: 3, patientName: '王五', gender: '男', age: 45, phone: '13800138003', idCard: '110101197903033456', hospitalName: '杭州第三医院', department: '儿科', status: '已出院' },
      { id: 4, patientName: '赵六', gender: '女', age: 52, phone: '13800138004', idCard: '110101197204044567', hospitalName: '哈尔滨第一医院', department: '骨科', status: '预约中' },
      { id: 5, patientName: '孙七', gender: '男', age: 22, phone: '13800138005', idCard: '110101200205055678', hospitalName: '哈尔滨第四医院', department: '眼科', status: '就诊中' },
      { id: 6, patientName: '周八', gender: '女', age: 38, phone: '13800138006', idCard: '110101198606066789', hospitalName: '哈尔滨第五医院', department: '内科', status: '已出院' },
      { id: 7, patientName: '吴九', gender: '男', age: 49, phone: '13800138007', idCard: '110101197507077890', hospitalName: '哈尔滨第六医院', department: '皮肤科', status: '就诊中' },
      { id: 8, patientName: '郑十', gender: '女', age: 31, phone: '13800138008', idCard: '110101199308088901', hospitalName: '哈尔滨第七医院', department: '耳鼻喉', status: '预约中' },
      { id: 9, patientName: '钱十一', gender: '男', age: 56, phone: '13800138009', idCard: '110101196809099012', hospitalName: '哈尔滨第八医院', department: '口腔科', status: '已出院' },
      { id: 10, patientName: '孙十二', gender: '女', age: 25, phone: '13800138010', idCard: '110101199910100123', hospitalName: '哈尔滨第九医院', department: '急诊科', status: '就诊中' }
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
  if (searchName.value) {
    result = result.filter(item => item.patientName.includes(searchName.value))
  }
  if (searchGender.value) {
    result = result.filter(item => item.gender === searchGender.value)
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
  searchName.value = ''
  searchGender.value = ''
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
  dialogTitle.value = '添加患者'
  resetFormData()
  dialogVisible.value = true
}

const openEditDialog = (row) => {
  dialogTitle.value = '编辑患者'
  Object.assign(formData, {
    id: row.id,
    patientName: row.patientName,
    gender: row.gender,
    age: row.age,
    phone: row.phone,
    idCard: row.idCard,
    hospitalName: row.hospitalName,
    department: row.department,
    status: row.status
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
    gender: '男',
    age: '',
    phone: '',
    idCard: '',
    hospitalName: '',
    department: '',
    status: '就诊中'
  })
}

const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()

    if (formData.id) {
      const index = tableData.value.findIndex(item => item.id === formData.id)
      if (index !== -1) {
        tableData.value[index] = { ...formData }
      }
      ElMessage.success('更新成功')
    } else {
      formData.id = Date.now()
      tableData.value.unshift({ ...formData })
      total.value = tableData.value.length
      ElMessage.success('添加成功')
    }

    dialogVisible.value = false
  } catch (error) {
    if (error !== false) {
      console.error('操作失败:', error)
      ElMessage.error('操作失败')
    }
  }
}

const handleDelete = (id) => {
  ElMessageBox.confirm(
    '确定要删除该患者吗？',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    const index = tableData.value.findIndex(item => item.id === id)
    if (index !== -1) {
      tableData.value.splice(index, 1)
      total.value = tableData.value.length
      ElMessage.success('删除成功')
    }
  }).catch(() => {})
}

const handleBatchDelete = async () => {
  if (selectedRows.value.length === 0) return

  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedRows.value.length} 位患者吗？`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const ids = selectedRows.value.map(row => row.id)
    tableData.value = tableData.value.filter(item => !ids.includes(item.id))
    total.value = tableData.value.length
    selectedRows.value = []
    ElMessage.success('批量删除成功')
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除失败:', error)
      ElMessage.error('批量删除失败')
    }
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.patient-page {
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
  width: 140px;
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

.patient-table {
  width: 100%;
  border-radius: var(--radius-md);
  overflow: hidden;
}

.patient-table :deep(.el-table__header) {
  background: #F8FAFC;
}

.patient-table :deep(.el-table__body tr:hover) {
  background: rgba(22, 93, 255, 0.04);
}

.patient-table :deep(.el-table__row) {
  height: 48px;
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

.btn-delete {
  color: var(--danger-color);
}

.btn-delete:hover {
  color: #c41e3a;
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
.custom-dialog :deep(.el-select__wrapper) {
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

.radio-item {
  margin-right: var(--spacing-xl);
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