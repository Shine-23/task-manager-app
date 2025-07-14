import { Route, Routes, Navigate } from 'react-router-dom'
import Login from './pages/Login'
import Register from './pages/Register'
import Dashboard from './pages/Dashboard'
import Projects from './pages/Projects' 
import Users from './pages/Users';
import { useContext } from 'react'
import { AuthContext } from './AuthContext'
import Layout from './Layout'
import UserProjects from './pages/UserProjects'; 

function PrivateRoute({ children }) {
  const { user } = useContext(AuthContext)
  console.log("PrivateRoute user:", user)
  return user ? children : <Navigate to="/login" />
}

function App() {
  return (
    
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />

      <Route
        path="/"
        element={
          <PrivateRoute>
            <Layout>
              <Dashboard />
            </Layout>
          </PrivateRoute>
        }
      />

      <Route
        path="/projects"
        element={
          <PrivateRoute>
            <Layout>
              <Projects />
            </Layout>
          </PrivateRoute>
        }
      />
      <Route
        path="/users"
        element={
          <PrivateRoute>
            <Layout>
              <Users />
            </Layout>
          </PrivateRoute>
        }
      />

      {/* Optional: redirect unknown routes */}
      <Route path="*" element={<Navigate to="/" />} />
      <Route path="/users/:userId/projects" element={<UserProjects />} />
    </Routes>
  )
}

export default App
