import { Switch, Route } from 'react-router-dom'
import UserDetails from './pages/UserDetails'
import Overview from './pages/Overview'

function App() {
  return (
    <div>
      <Switch>
        <Route exact path="/">
          <Overview />
        </Route>
        <Route path="/user/:username">
          <UserDetails />
        </Route>
      </Switch>
    </div>
  )
}

export default App
