import { createContext, useEffect, useState } from 'react'
import { jwtDecode } from "jwt-decode";

export const AuthContext = createContext()

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null)
  const [loading, setLoading] = useState(true)

 useEffect(() => {
    const token = sessionStorage.getItem('token')
    if (token) {
      try {
        const decoded = jwtDecode(token)
        if (decoded.exp * 1000 > Date.now()) {
          setUser(decoded)
        } else {
          sessionStorage.removeItem('token')
        }
      } catch (err) {
        console.error('Invalid token:', err)
        sessionStorage.removeItem('token')
      }
    }
    setLoading(false)
  }, [])

  const login = (token) => {
    sessionStorage.setItem('token', token)
    try {
      const decoded = jwtDecode(token)
      setUser(decoded)
    } catch (err) {
      console.error('Invalid token on login:', err)
      setUser(null)
    }

  }

  const logout = () => {
    sessionStorage.removeItem('token')
    setUser(null)
  }

  if (loading) {
    return <div>Loading...</div>
  }

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  )
}
