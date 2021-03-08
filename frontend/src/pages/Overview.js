import { useEffect, useState } from 'react'
import {
    deleteRepositoryFromWatchlist, getPullRequestList,
    getUsers,
    getWatchlist,
    postUser,
} from '../services/bingoApiService'
import AddNewUser from '../components/AddNewUser'
import UserList from '../components/UserList'
import Watchlist from '../components/Watchlist'
import { useAuth } from '../auth/AuthContext'
import {Redirect} from "react-router-dom";

export default function Overview() {

    const [users, setUsers] = useState([])
    const [watchlist, setWatchlist] = useState([])
    const {token} = useAuth()
    useEffect(() => {
        getUsers(token)
            .then(setUsers)
            .catch((error) => console.error(error))
        getWatchlist(token)
            .then(setWatchlist)
            .catch((error) => console.error(error))
    }, [])

    const deleteWatchListItem = (deleteRepository) => {
        deleteRepositoryFromWatchlist(deleteRepository)
        setWatchlist([
            ...watchlist.filter(
                (repository) => repository.id !== deleteRepository.id
            ),
        ])
    }

    const addNewUser = (name) =>
        postUser(name, token)
            .then((newUser) => {
                const updatedUsers = [...users, newUser]
                setUsers(updatedUsers)
            })
            .catch((error) => console.error(error))

    const repoDetails = (username, repository) => {
        console.log("test")
        getPullRequestList(username, repository, token)
            .then((response) => response.data)



    }

  return (
    <>
      <Watchlist
        watchlist={watchlist}
        onDeleteWatchlistItem={deleteWatchListItem}
        onClick={repoDetails}
      />
      <div>
        <AddNewUser onAdd={addNewUser} />
        <UserList users={users} />
      </div>
    </>
  )
}
