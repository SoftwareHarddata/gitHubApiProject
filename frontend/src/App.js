import { useEffect, useState } from 'react'
import AddNewUser from './components/AddNewUser'
import UserList from './components/UserList'
import { Switch, Route } from 'react-router-dom'
import { getUsers, postUser } from './services/bingoApiService'
import UserDetails from './pages/UserDetails'

function App() {
  const [users, setUsers] = useState([])

  useEffect(() => {
    getUsers()
      .then(setUsers)
      .catch((error) => console.error(error))
  }, [])

  const addNewUser = (name) =>
    postUser(name)
      .then((newUser) => {
        const updatedUsers = [...users, newUser]
        setUsers(updatedUsers)
      })
      .catch((error) => console.error(error))

  return (
    <div>
      <Switch>
        <Route exact path="/">
          <AddNewUser onAdd={addNewUser} />
          <UserList users={users} />
        </Route>
        <Route path="/user/:username">
          <UserDetails />
        </Route>
      </Switch>
    </div>
  )
}

export default App
