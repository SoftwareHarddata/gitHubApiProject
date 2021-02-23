import { useEffect, useState } from 'react'
import AddNewUser from './components/AddNewUser'
import UserList from './components/UserList'
import { getUsers, postUser } from './services/bingoApiService'

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
      <AddNewUser onAdd={addNewUser} />
      <UserList users={users} />
    </div>
  )
}

export default App
