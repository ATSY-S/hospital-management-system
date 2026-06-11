<template>
  <div class="finance-page">
    <div class="page-header">
      <div class="page-title-section">
        <h1 class="page-title">收费管理</h1>
        <p class="page-subtitle">管理收费记录，支持查询和统计</p>
      </div>
    </div>

    <div class="stats-row">
      <div class="stat-card">
        <div class="stat-icon bg-blue">💰</div>
        <div class="stat-content">
          <div class="stat-value">¥{{ stats.totalRevenue.toLocaleString() }}</div>
          <div class="stat-label">今日营收</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon bg-green">📝</div>
        <div class="stat-content">
          <div class="stat-value">{{ stats.totalOrders }}</div>
          <div class="stat-label">收费订单</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon bg-orange">💊</div>
        <div class="stat-content">
          <div class="stat-value">{{ stats.medicineOrders }}</div>
          <div class="stat-label">药品收费</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon bg-purple">🏥</div>
        <div class="stat-content">
          <div class="stat-value">{{ stats.serviceOrders }}</div>
          <div class="stat-label">服务收费</div>
        </div>
      </div>
    </div>

    <div class="card">
      <div class="card-header">
        <div class="search-bar">
          <el-input v-model="searchKeyword" placeholder="请输入患者姓名或订单号" class="search-input" />
          <el-date-picker v-model="searchDate" type="date" placeholder="选择日期" class="search-date" />
          <el-select v-model="searchType" placeholder="收费类型" class="search-select">
            <el-option label="全部" value="" />
            <el-option label="药品收费" value="药品" />
            <el-option label="服务收费" value="服务" />
          </el-select>
          <el-button type="primary" @click="handleSearch" class="btn-search">
            <el-icon :size="16"><Search /></el-icon>
            查询
          </el-button>
          <el-button @click="resetSearch" class="btn-reset">重置</el-button>
        </div>
      </div>

      <div class="card-body">
        <div class="toolbar">
          <el-button type="primary" @click="openAddDialog" class="btn-add">
            <el-icon :size="16"><Plus /></el-icon>
            添加收费
          </el-button>
        </div>

        <div class="table-wrapper">
          <el-table 
            :data="displayData" 
            border
            stripe
            :header-cell-style="{ background: '#F8FAFC', color: '#4E5969', fontWeight: '500' }"
          >
            <el-table-column prop="id" label="订单号" width="120" />
            <el-table-column prop="patientName" label="患者姓名" width="120" />
            <el-table-column prop="patientId" label="患者ID" width="100" />
            <el-table-column prop="hospitalName" label="医院名称" width="180" />
            <el-table-column prop="departmentName" label="科室" width="100" />
            <el-table-column prop="chargeType" label="收费类型" width="100">
              <template #default="{ row }">
                <el-tag :type="row.chargeType === '药品' ? 'warning' : 'success'">{{ row.chargeType }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="itemName" label="收费项目" width="150" />
            <el-table-column prop="amount" label="金额" width="100">
              <template #default="{ row }">
                <span class="fee-text">¥{{ row.amount.toLocaleString() }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="payMethod" label="支付方式" width="100">
              <template #default="{ row }">
                <el-tag type="info">{{ row.payMethod }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="收费时间" width="160" />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === '已支付' ? 'success' : 'warning'">{{ row.status }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="140">
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
        <el-form-item label="患者姓名" required>
          <el-input v-model="formData.patientName" placeholder="请输入患者姓名" />
        </el-form-item>
        <el-form-item label="患者ID" required>
          <el-input v-model="formData.patientId" placeholder="请输入患者ID" />
        </el-form-item>
        <el-form-item label="医院名称" required>
          <el-input v-model="formData.hospitalName" placeholder="请输入医院名称" />
        </el-form-item>
        <el-form-item label="科室" required>
          <el-input v-model="formData.departmentName" placeholder="请输入科室名称" />
        </el-form-item>
        <el-form-item label="收费类型" required>
          <el-select v-model="formData.chargeType" placeholder="请选择收费类型">
            <el-option label="药品" value="药品" />
            <el-option label="服务" value="服务" />
          </el-select>
        </el-form-item>
        <el-form-item label="收费项目" required>
          <el-input v-model="formData.itemName" placeholder="请输入收费项目" />
        </el-form-item>
        <el-form-item label="金额" required>
          <el-input v-model="formData.amount" type="number" placeholder="请输入金额" />
        </el-form-item>
        <el-form-item label="支付方式">
          <el-radio-group v-model="formData.payMethod">
            <el-radio label="现金" value="现金" />
            <el-radio label="微信" value="微信" />
            <el-radio label="支付宝" value="支付宝" />
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
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus, Delete, Edit } from '@element-plus/icons-vue'

const tableData = ref([])
const searchKeyword = ref('')
const searchDate = ref('')
const searchType = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const stats = computed(() => ({
  totalRevenue: tableData.value.reduce((sum, item) => sum + item.amount, 0),
  totalOrders: tableData.value.length,
  medicineOrders: tableData.value.filter(item => item.chargeType === '药品').length,
  serviceOrders: tableData.value.filter(item => item.chargeType === '服务').length
}))

const displayData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return tableData.value.slice(start, end)
})

const dialogVisible = ref(false)
const dialogTitle = ref('添加收费')
const formData = reactive({
  id: null,
  patientName: '',
  patientId: '',
  hospitalName: '',
  departmentName: '',
  chargeType: '药品',
  itemName: '',
  amount: 0,
  payMethod: '微信'
})

const fetchData = () => {
  tableData.value = [
    { id: 'SF20260608001', patientName: '张三', patientId: 'P001', hospitalName: '黑龙江中医药大学附属第一医院', departmentName: '内科', chargeType: '药品', itemName: '感冒药', amount: 128.50, payMethod: '微信', createTime: '2026-06-08 09:30:00', status: '已支付' },
    { id: 'SF20260608002', patientName: '李四', patientId: 'P002', hospitalName: '哈尔滨市第一医院', departmentName: '外科', chargeType: '服务', itemName: '手术费', amount: 2500.00, payMethod: '支付宝', createTime: '2026-06-08 10:15:00', status: '已支付' },
    { id: 'SF20260608003', patientName: '王五', patientId: 'P003', hospitalName: '黑龙江省人民医院', departmentName: '儿科', chargeType: '药品', itemName: '退烧药', amount: 45.80, payMethod: '现金', createTime: '2026-06-08 11:00:00', status: '已支付' },
    { id: 'SF20260608004', patientName: '赵六', patientId: 'P004', hospitalName: '哈尔滨市儿童医院', departmentName: '儿科', chargeType: '服务', itemName: '检查费', amount: 380.00, payMethod: '微信', createTime: '2026-06-08 14:20:00', status: '已支付' },
    { id: 'SF20260608005', patientName: '孙七', patientId: 'P005', hospitalName: '黑龙江中医药大学附属第一医院', departmentName: '骨科', chargeType: '药品', itemName: '止痛药', amount: 89.00, payMethod: '微信', createTime: '2026-06-08 15:30:00', status: '已支付' },
    { id: 'SF20260607001', patientName: '周八', patientId: 'P006', hospitalName: '哈尔滨市中医院', departmentName: '中医科', chargeType: '药品', itemName: '中药调理', amount: 268.00, payMethod: '支付宝', createTime: '2026-06-07 09:00:00', status: '已支付' },
    { id: 'SF20260607002', patientName: '吴九', patientId: 'P007', hospitalName: '黑龙江省肿瘤医院', departmentName: '肿瘤科', chargeType: '服务', itemName: '化疗费', amount: 5800.00, payMethod: '微信', createTime: '2026-06-07 10:30:00', status: '已支付' },
    { id: 'SF20260607003', patientName: '郑十', patientId: 'P008', hospitalName: '哈尔滨市口腔医院', departmentName: '口腔科', chargeType: '服务', itemName: '洗牙', amount: 150.00, payMethod: '现金', createTime: '2026-06-07 14:00:00', status: '已支付' },
    { id: 'SF20260606001', patientName: '钱十一', patientId: 'P009', hospitalName: '黑龙江省康复医院', departmentName: '康复科', chargeType: '服务', itemName: '理疗费', amount: 200.00, payMethod: '微信', createTime: '2026-06-06 11:00:00', status: '已支付' },
    { id: 'SF20260606002', patientName: '陈十二', patientId: 'P010', hospitalName: '哈尔滨市传染病医院', departmentName: '传染科', chargeType: '药品', itemName: '抗病毒药', amount: 356.00, payMethod: '支付宝', createTime: '2026-06-06 16:00:00', status: '已支付' }
  ]
  total.value = tableData.value.length
  currentPage.value = 1
}

const handleSearch = () => {
  let filtered = [...tableData.value]
  
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    filtered = filtered.filter(item => 
      item.patientName.toLowerCase().includes(keyword) || 
      item.id.toLowerCase().includes(keyword)
    )
  }
  
  if (searchDate.value) {
    filtered = filtered.filter(item => item.createTime.startsWith(searchDate.value))
  }
  
  if (searchType.value) {
    filtered = filtered.filter(item => item.chargeType === searchType.value)
  }
  
  tableData.value = filtered
  total.value = tableData.value.length
  currentPage.value = 1
}

const resetSearch = () => {
  searchKeyword.value = ''
  searchDate.value = ''
  searchType.value = ''
  fetchData()
}

const handleCurrentChange = (page) => {
  currentPage.value = page
}

const openAddDialog = () => {
  dialogTitle.value = '添加收费'
  formData.id = null
  formData.patientName = ''
  formData.patientId = ''
  formData.hospitalName = ''
  formData.departmentName = ''
  formData.chargeType = '药品'
  formData.itemName = ''
  formData.amount = 0
  formData.payMethod = '微信'
  dialogVisible.value = true
}

const openEditDialog = (row) => {
  dialogTitle.value = '修改收费'
  formData.id = row.id
  formData.patientName = row.patientName
  formData.patientId = row.patientId
  formData.hospitalName = row.hospitalName
  formData.departmentName = row.departmentName
  formData.chargeType = row.chargeType
  formData.itemName = row.itemName
  formData.amount = row.amount
  formData.payMethod = row.payMethod
  dialogVisible.value = true
}

const handleSubmit = () => {
  if (!formData.patientName || !formData.itemName || formData.amount <= 0) {
    ElMessage.warning('请填写完整信息')
    return
  }
  
  if (formData.id) {
    const index = tableData.value.findIndex(item => item.id === formData.id)
    if (index > -1) {
      tableData.value[index] = { ...formData }
    }
    ElMessage.success('修改成功')
  } else {
    const newId = `SF${new Date().getFullYear()}${String(new Date().getMonth() + 1).padStart(2, '0')}${String(new Date().getDate()).padStart(2, '0')}${String(tableData.value.length + 1).padStart(3, '0')}`
    tableData.value.unshift({
      ...formData,
      id: newId,
      createTime: new Date().toLocaleString('zh-CN'),
      status: '已支付'
    })
    total.value = tableData.value.length
    ElMessage.success('添加成功')
  }
  
  dialogVisible.value = false
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定删除该收费记录吗？', '提示', { type: 'warning' })
    .then(() => {
      tableData.value = tableData.value.filter(item => item.id !== id)
      total.value = tableData.value.length
      ElMessage.success('删除成功')
    }).catch(() => {})
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.finance-page {
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

.stats-row {
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
  font-size: 24px;
}

.stat-icon.bg-blue {
  background: #E8F0FF;
}

.stat-icon.bg-green {
  background: #E6F7E6;
}

.stat-icon.bg-orange {
  background: #FFF7E6;
}

.stat-icon.bg-purple {
  background: #F5EEFF;
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
  margin: 0;
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

.search-bar {
  display: flex;
  gap: 12px;
  align-items: center;
}

.search-input,
.search-select,
.search-date {
  width: 180px;
}

.card-body {
  padding: 16px 20px;
}

.toolbar {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
}

.btn-add {
  background: #165DFF;
  border-color: #165DFF;
}

.btn-search {
  background: #165DFF;
  border-color: #165DFF;
}

.table-wrapper {
  overflow-x: auto;
}

.fee-text {
  color: #165DFF;
  font-weight: 500;
}

.pagination-wrapper {
  padding: 16px 0;
  display: flex;
  justify-content: center;
}

.btn-edit {
  color: #165DFF;
}

.btn-delete {
  color: #F53F3F;
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
    flex-wrap: wrap;
  }
}
</style>