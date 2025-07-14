import { useState, useContext } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import api from '../api'
import { AuthContext } from '../AuthContext'
import '../assests/Register.css'

function Register() {
  const navigate = useNavigate()
  const { login } = useContext(AuthContext)

  const [form, setForm] = useState({
    username: '',
    email: '',
    password: ''
  })

  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(false)

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value })
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)
    setError(null)

    try {
      // Register the user
      await api.post('/auth/register', form)

      // Automatically log them in
      const loginRes = await api.post('/auth/login', {
        username: form.username,
        password: form.password,
      })

      const token = loginRes.data.token
      login(token) // save token + decode user
      navigate('/') // go to dashboard
    } catch (err) {
      setError(err.response?.data || 'Registration or auto-login failed')
    }

    setLoading(false)
  }

  return (
    <div className="register-container">
      <form className="register-form" onSubmit={handleSubmit}>
        <h2 className="register-title">Register</h2>
        {error && <div className="error-message">{error}</div>}

        <label className="input-label">
          Username
          <input
            type="text"
            name="username"
            value={form.username}
            onChange={handleChange}
            required
            className="input-field"
          />
        </label>

        <label className="input-label">
          Email
          <input
            type="email"
            name="email"
            value={form.email}
            onChange={handleChange}
            required
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

        <button type="submit" className="register-button" disabled={loading}>
          {loading ? 'Registering...' : 'Register'}
        </button>

        <p className="login-text">
          Already have an account?{' '}
          <Link to="/login" className="login-link">Login here</Link>
        </p>
      </form>
    </div>
  )
}

export default Register
