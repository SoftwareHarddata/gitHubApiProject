import { Switch, Route } from 'react-router-dom'
import UserDetails from './pages/UserDetails'
import UserOverview from './pages/UserOverview'
import Login from "./pages/Login";
import {useState} from 'react'

function App() {
  const [token, setToken] = useState(undefined);

  return (
    <div>
      <Switch>
        <Route exact path="/login">
          <Login setToken={setToken} token={token}/>
        </Route>

        <Route exact path="/">
          <UserOverview token={token}/>
        </Route>
        <Route path="/user/:username">
          <UserDetails token={token} />
        </Route>
      </Switch>
    </div>
  )
}

export default App
