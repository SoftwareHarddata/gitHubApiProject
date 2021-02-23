import { Switch, Route } from 'react-router-dom'
import UserDetails from './pages/UserDetails'
import UserOverview from './pages/UserOverview'

function App() {
  return (
    <div>
      <Switch>
        <Route exact path="/">
          <UserOverview />
        </Route>
        <Route path="/user/:username">
          <UserDetails />
        </Route>
      </Switch>
    </div>
  )
}

export default App
