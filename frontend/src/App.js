import { Switch, Route } from 'react-router-dom'
import UserDetails from './pages/UserDetails'
import UserOverview from './pages/UserOverview'
import Login from './pages/Login'
import AuthProvider from './auth/AuthProvider'
import ProtectedRoute from './auth/ProtectedRoute'

function App() {
  return (
    <AuthProvider>
      <div>
        <Switch>
          <Route exact path="/login">
            <Login />
          </Route>

          <ProtectedRoute exact path="/">
            <UserOverview />
          </ProtectedRoute>
          <ProtectedRoute path="/user/:username">
            <UserDetails />
          </ProtectedRoute>
        </Switch>
      </div>
    </AuthProvider>
  )
}

export default App
