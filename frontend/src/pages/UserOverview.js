import { useEffect, useState, useContext } from 'react'
import { getUsers, postUser } from '../services/bingoApiService'
import AddNewUser from '../components/AddNewUser'
import UserList from '../components/UserList'
import { useAuth } from '../auth/AuthContext'

export default function UserOverview() {
  const { token } = useAuth()
  const [users, setUsers] = useState([])

  useEffect(() => {
    getUsers(token)
      .then(setUsers)
      .catch((error) => console.error(error))
  }, [])

  const addNewUser = (name, token) =>
    postUser(name, token)
      .then((newUser) => {
        const updatedUsers = [...users, newUser]
        setUsers(updatedUsers)
      })
      .catch((error) => console.error(error))

  return (
    <>
      <AddNewUser onAdd={addNewUser} token={token} />
      <UserList users={users} />
    </>
  )
}
