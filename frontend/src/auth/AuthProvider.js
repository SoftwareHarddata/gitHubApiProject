import { AuthContext } from './AuthContext'
import { useState } from 'react'

export default function AuthProvider({ children }) {
  const [token, setToken] = useState('')
  return (
    <AuthContext.Provider value={{ token, setToken }}>
      {children}
    </AuthContext.Provider>
  )
}
