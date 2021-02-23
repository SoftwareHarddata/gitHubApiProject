import { useEffect, useState } from 'react'
import { getUsers, postUser } from '../services/bingoApiService'
import AddNewUser from '../components/AddNewUser'
import UserList from '../components/UserList'

export default function UserOverview() {
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
    <>
      <AddNewUser onAdd={addNewUser} />
      <UserList users={users} />
    </>
  )
}
