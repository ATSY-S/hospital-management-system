import axios from 'axios'

const request = axios.create({
  baseURL: '/hos',
  timeout: 10000
})

export const getHospitalCount = () => {
  return request.get('/spa/getHosCount')
}

export const getPatientCount = () => {
  return request.get('/spa/getPatCount')
}

export const getPatientByHospital = () => {
  return request.get('/spa/getHosPatCount')
}

export const getTopThreeCount = () => {
  return request.get('/spa/getSJHosCount')
}

export const getPatientAgeDistribution = () => {
  return request.get('/spa/getPatAge')
}

export const getMalePatientCount = () => {
  return request.get('/spa/getPatSex')
}