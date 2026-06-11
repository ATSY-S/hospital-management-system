import { createRouter, createWebHistory } from 'vue-router'
import HospitalManage from '../views/HospitalManage.vue'
import Dashboard from '../views/Dashboard.vue'
import PatientManage from '../views/PatientManage.vue'
import RegisterManage from '../views/RegisterManage.vue'
import MedicineManage from '../views/MedicineManage.vue'
import FinanceManage from '../views/FinanceManage.vue'

const routes = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: Dashboard
  },
  {
    path: '/hospital',
    name: 'HospitalManage',
    component: HospitalManage
  },
  {
    path: '/patient',
    name: 'PatientManage',
    component: PatientManage
  },
  {
    path: '/register',
    name: 'RegisterManage',
    component: RegisterManage
  },
  {
    path: '/medicine',
    name: 'MedicineManage',
    component: MedicineManage
  },
  {
    path: '/finance',
    name: 'FinanceManage',
    component: FinanceManage
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router