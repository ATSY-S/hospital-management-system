<template>
  <div class="medicine-page">
    <div class="page-header">
      <div class="page-title-section">
        <h1 class="page-title">药品管理</h1>
        <p class="page-subtitle">管理药品信息，支持增删改查操作</p>
      </div>
    </div>

    <div class="stats-row">
      <div class="stat-card">
        <div class="stat-icon bg-blue">💊</div>
        <div class="stat-content">
          <div class="stat-value">{{ stats.total }}</div>
          <div class="stat-label">药品总数</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon bg-green">📦</div>
        <div class="stat-content">
          <div class="stat-value">{{ stats.inStock }}</div>
          <div class="stat-label">库存充足</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon bg-orange">⚠️</div>
        <div class="stat-content">
          <div class="stat-value">{{ stats.lowStock }}</div>
          <div class="stat-label">库存不足</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon bg-purple">⏳</div>
        <div class="stat-content">
          <div class="stat-value">{{ stats.expired }}</div>
          <div class="stat-label">即将过期</div>
        </div>
      </div>
    </div>

    <div class="card">
      <div class="card-header">
        <div class="search-bar">
          <el-input v-model="searchKeyword" placeholder="请输入药品名称或编码" class="search-input" />
          <el-select v-model="searchType" placeholder="药品类型" class="search-select">
            <el-option label="全部" value="" />
            <el-option label="西药" value="西药" />
            <el-option label="中药" value="中药" />
            <el-option label="中成药" value="中成药" />
            <el-option label="保健品" value="保健品" />
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
            添加药品
          </el-button>
        </div>

        <div class="table-wrapper">
          <el-table 
            :data="displayData" 
            border
            stripe
            :header-cell-style="{ background: '#F8FAFC', color: '#4E5969', fontWeight: '500' }"
          >
            <el-table-column prop="id" label="ID" width="80" align="center" />
            <el-table-column prop="medicineCode" label="药品编码" width="120" />
            <el-table-column prop="medicineName" label="药品名称" min-width="150" />
            <el-table-column prop="medicineType" label="药品类型" width="100">
              <template #default="{ row }">
                <el-tag :type="getTypeTagType(row.medicineType)">{{ row.medicineType }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="specification" label="规格" width="120" />
            <el-table-column prop="unit" label="单位" width="80" />
            <el-table-column prop="price" label="单价" width="100">
              <template #default="{ row }">
                <span>¥{{ row.price.toFixed(2) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="stock" label="库存" width="100">
              <template #default="{ row }">
                <span :class="row.stock < 100 ? 'low-stock' : ''">{{ row.stock }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="expireDate" label="有效期" width="120" />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === '正常' ? 'success' : 'danger'">{{ row.status }}</el-tag>
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
        <el-form-item label="药品编码" required>
          <el-input v-model="formData.medicineCode" placeholder="请输入药品编码" />
        </el-form-item>
        <el-form-item label="药品名称" required>
          <el-input v-model="formData.medicineName" placeholder="请输入药品名称" />
        </el-form-item>
        <el-form-item label="药品类型" required>
          <el-select v-model="formData.medicineType" placeholder="请选择药品类型">
            <el-option label="西药" value="西药" />
            <el-option label="中药" value="中药" />
            <el-option label="中成药" value="中成药" />
            <el-option label="保健品" value="保健品" />
          </el-select>
        </el-form-item>
        <el-form-item label="规格" required>
          <el-input v-model="formData.specification" placeholder="请输入规格" />
        </el-form-item>
        <el-form-item label="单位" required>
          <el-select v-model="formData.unit" placeholder="请选择单位">
            <el-option label="盒" value="盒" />
            <el-option label="瓶" value="瓶" />
            <el-option label="片" value="片" />
            <el-option label="支" value="支" />
            <el-option label="袋" value="袋" />
          </el-select>
        </el-form-item>
        <el-form-item label="单价" required>
          <el-input v-model="formData.price" type="number" placeholder="请输入单价" />
        </el-form-item>
        <el-form-item label="库存" required>
          <el-input v-model="formData.stock" type="number" placeholder="请输入库存" />
        </el-form-item>
        <el-form-item label="有效期" required>
          <el-date-picker v-model="formData.expireDate" type="date" placeholder="选择有效期" />
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
const searchType = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const stats = computed(() => ({
  total: tableData.value.length,
  inStock: tableData.value.filter(item => item.stock >= 100).length,
  lowStock: tableData.value.filter(item => item.stock < 100 && item.stock > 0).length,
  expired: tableData.value.filter(item => new Date(item.expireDate) < new Date()).length
}))

const displayData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return tableData.value.slice(start, end)
})

const dialogVisible = ref(false)
const dialogTitle = ref('添加药品')
const formData = reactive({
  id: null,
  medicineCode: '',
  medicineName: '',
  medicineType: '西药',
  specification: '',
  unit: '盒',
  price: 0,
  stock: 0,
  expireDate: ''
})

const getTypeTagType = (type) => {
  const typeMap = {
    '西药': 'primary',
    '中药': 'success',
    '中成药': 'warning',
    '保健品': 'info'
  }
  return typeMap[type] || 'info'
}

const fetchData = () => {
  tableData.value = [
    { id: 1, medicineCode: 'MED001', medicineName: '感冒灵颗粒', medicineType: '中成药', specification: '10g*10袋', unit: '盒', price: 12.50, stock: 500, expireDate: '2028-12-31', status: '正常' },
    { id: 2, medicineCode: 'MED002', medicineName: '阿莫西林胶囊', medicineType: '西药', specification: '0.5g*24粒', unit: '盒', price: 28.00, stock: 300, expireDate: '2027-06-30', status: '正常' },
    { id: 3, medicineCode: 'MED003', medicineName: '板蓝根颗粒', medicineType: '中成药', specification: '10g*20袋', unit: '盒', price: 15.80, stock: 80, expireDate: '2028-09-30', status: '正常' },
    { id: 4, medicineCode: 'MED004', medicineName: '布洛芬缓释胶囊', medicineType: '西药', specification: '0.3g*20粒', unit: '盒', price: 22.00, stock: 250, expireDate: '2027-12-31', status: '正常' },
    { id: 5, medicineCode: 'MED005', medicineName: '复方丹参片', medicineType: '中成药', specification: '60片/瓶', unit: '瓶', price: 35.00, stock: 180, expireDate: '2029-03-31', status: '正常' },
    { id: 6, medicineCode: 'MED006', medicineName: '维生素C片', medicineType: '西药', specification: '100mg*100片', unit: '瓶', price: 8.50, stock: 600, expireDate: '2028-06-30', status: '正常' },
    { id: 7, medicineCode: 'MED007', medicineName: '阿胶糕', medicineType: '保健品', specification: '200g/盒', unit: '盒', price: 198.00, stock: 50, expireDate: '2027-02-28', status: '正常' },
    { id: 8, medicineCode: 'MED008', medicineName: '双黄连口服液', medicineType: '中成药', specification: '10ml*10支', unit: '盒', price: 26.00, stock: 200, expireDate: '2027-11-30', status: '正常' },
    { id: 9, medicineCode: 'MED009', medicineName: '头孢克肟分散片', medicineType: '西药', specification: '100mg*12片', unit: '盒', price: 45.00, stock: 120, expireDate: '2028-04-30', status: '正常' },
    { id: 10, medicineCode: 'MED010', medicineName: '枸杞', medicineType: '中药', specification: '250g/袋', unit: '袋', price: 38.00, stock: 95, expireDate: '2026-12-31', status: '正常' }
  ]
  total.value = tableData.value.length
  currentPage.value = 1
}

const handleSearch = () => {
  let filtered = [...tableData.value]
  
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    filtered = filtered.filter(item => 
      item.medicineName.toLowerCase().includes(keyword) || 
      item.medicineCode.toLowerCase().includes(keyword)
    )
  }
  
  if (searchType.value) {
    filtered = filtered.filter(item => item.medicineType === searchType.value)
  }
  
  tableData.value = filtered
  total.value = tableData.value.length
  currentPage.value = 1
}

const resetSearch = () => {
  searchKeyword.value = ''
  searchType.value = ''
  fetchData()
}

const handleCurrentChange = (page) => {
  currentPage.value = page
}

const openAddDialog = () => {
  dialogTitle.value = '添加药品'
  formData.id = null
  formData.medicineCode = ''
  formData.medicineName = ''
  formData.medicineType = '西药'
  formData.specification = ''
  formData.unit = '盒'
  formData.price = 0
  formData.stock = 0
  formData.expireDate = ''
  dialogVisible.value = true
}

const openEditDialog = (row) => {
  dialogTitle.value = '修改药品'
  formData.id = row.id
  formData.medicineCode = row.medicineCode
  formData.medicineName = row.medicineName
  formData.medicineType = row.medicineType
  formData.specification = row.specification
  formData.unit = row.unit
  formData.price = row.price
  formData.stock = row.stock
  formData.expireDate = row.expireDate
  dialogVisible.value = true
}

const handleSubmit = () => {
  if (!formData.medicineName || !formData.specification || formData.price <= 0) {
    ElMessage.warning('请填写完整信息')
    return
  }
  
  const today = new Date().toISOString().split('T')[0]
  const status = formData.expireDate < today ? '过期' : '正常'
  
  if (formData.id) {
    const index = tableData.value.findIndex(item => item.id === formData.id)
    if (index > -1) {
      tableData.value[index] = { ...formData, status }
    }
    ElMessage.success('修改成功')
  } else {
    const newId = tableData.value.length + 1
    tableData.value.unshift({
      ...formData,
      id: newId,
      status
    })
    total.value = tableData.value.length
    ElMessage.success('添加成功')
  }
  
  dialogVisible.value = false
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定删除该药品吗？', '提示', { type: 'warning' })
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
.medicine-page {
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
.search-select {
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

.low-stock {
  color: #F53F3F;
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