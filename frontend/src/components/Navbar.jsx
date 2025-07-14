import { useContext } from 'react'
import { AuthContext } from '../AuthContext'
import { Link, useNavigate } from 'react-router-dom'
import '../assests/Navbar.css'

function Navbar() {
  const { user, logout } = useContext(AuthContext)
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <nav className="navbar">
      <div className="nav-left">
        <Link to="/" className="nav-brand">Task Manager</Link>
        <Link to="/" className="nav-link">Dashboard</Link>
        <Link to="/projects" className="nav-link">Projects</Link>
      </div>
      <div className="nav-right">
        {user ? (
          <>
            <button onClick={handleLogout} className="nav-logout-btn">Logout</button>
          </>
        ) : (
          <>
            <Link to="/login" className="nav-link">Login</Link>
            <Link to="/register" className="nav-link">Register</Link>
          </>
        )}
      </div>
    </nav>
  )
}

export default Navbar
