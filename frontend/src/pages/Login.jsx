import { useState, useContext } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { AuthContext } from '../AuthContext'
import api from '../api'
import '../assests/Login.css'

function Login() {
  const navigate = useNavigate()
  const { login } = useContext(AuthContext)

  const [form, setForm] = useState({ username: '', password: '' })
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(false)

  const handleChange = e => {
    setForm({ ...form, [e.target.name]: e.target.value })
  }

  const handleSubmit = async e => {
    e.preventDefault()
    setError(null)
    setLoading(true)
    try {
      const res = await api.post('/auth/login', form)
      login(res.data.token)
      navigate('/')
    } catch (err) {
      setError(err.response?.data || 'Login failed')
    }
    setLoading(false)
  }

  return (
    <div className="login-container">
      <form className="login-form" onSubmit={handleSubmit}>
        <h2 className="login-title">Login</h2>
        {error && <div className="error-message">{error}</div>}

        <label className="input-label">
          Username
          <input
            type="text"
            name="username"
            value={form.username}
            onChange={handleChange}
            required
            autoFocus
            className="input-field"
          />
        </label>

        <label className="input-label">
          Password
          <input
            type="password"
            name="password"
            value={form.password}
            onChange={handleChange}
            required
            className="input-field"
          />
        </label>

        <button type="submit" disabled={loading} className="login-button">
          {loading ? 'Logging in...' : 'Login'}
        </button>

        <p className="register-text">
          New user?{' '}
          <Link to="/register" className="register-link">
            Register here
          </Link>
        </p>
      </form>
    </div>
  )
}

export default Login
