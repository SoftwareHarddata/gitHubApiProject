import { useAuth } from './AuthContext'
import { Redirect, Route } from 'react-router-dom'

export default function ProtectedRoute(props) {
  const { token } = useAuth()
  return token ? <Route {...props} /> : <Redirect to="/login" />
}
