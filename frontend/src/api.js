import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
})

// Attach token if present
api.interceptors.request.use((config) => {
  const token = sessionStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

export default api
