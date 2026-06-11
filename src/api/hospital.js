import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({
  baseURL: '/hos',
  timeout: 10000
})

request.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== '200' && res.code !== '201' && res.code !== '202' && res.code !== '203' && res.code !== '204' && res.code !== '205') {
      ElMessage.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    return res
  },
  error => {
    ElMessage.error(error.message || '网络请求失败')
    return Promise.reject(error)
  }
)

export const getHospitalList = () => {
  return request.get('/findHosList')
}

export const addHospital = (data) => {
  return request.post('/insertHos', data)
}

export const updateHospital = (data) => {
  return request.put('/updateHos', data)
}

export const deleteHospital = (id) => {
  return request.delete('/deleteById', { params: { id } })
}

export const batchDeleteHospital = (ids) => {
  return request.delete('/deleteByIds', { data: ids })
}

export const searchHospital = (hosName, hosLevel) => {
  return request.get('/findHosByLikeName', { params: { hosName, hosLevel } })
}
