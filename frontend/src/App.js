import { Switch, Route } from 'react-router-dom'
import UserDetails from './pages/UserDetails'
import Overview from './pages/Overview'
import Login from './pages/Login'
import AuthProvider from './auth/AuthProvider'
import ProtectedRoute from './auth/ProtectedRoute'
import RepositoryDetails from "./pages/RepositoryDetails";

function App() {
  return (
    <AuthProvider>
      <div>
        <Switch>
          <Route exact path="/login">
            <Login />
          </Route>

          <ProtectedRoute exact path="/">
            <Overview />
          </ProtectedRoute>
          <ProtectedRoute path="/user/:username">
            <UserDetails />
          </ProtectedRoute>
          <ProtectedRoute path="/repo/details">
            <RepositoryDetails />
          </ProtectedRoute>
        </Switch>
      </div>
    </AuthProvider>
  )
}

export default App
