import {useParams} from 'react-router-dom'
import {useEffect, useState} from 'react'
import {
    addRepositoryToWatchlist,
    deleteRepositoryFromWatchlist,
    getUser,
    getUserRepositories
} from '../services/bingoApiService'
import styled from 'styled-components/macro'
import UserRepositories from "../components/UserRepositories";

export default function UserDetails() {
    const {username} = useParams()
    const [userData, setUserData] = useState()
    const [userRepositories, setUserRepositories] = useState()

    useEffect(() => {
        getUser(username).then(setUserData)
        getUserRepositories(username).then(setUserRepositories)
    }, [])

    if (!userData) {
        return (
            <section>
                <p>Loading</p>
            </section>
        )
    }

    const toggleWatchlist = (updateRepo) => {
        if (updateRepo.onWatchlist) {
            deleteRepositoryFromWatchlist(updateRepo)
            updateRepo.onWatchlist = false;
            setUserRepositories([updateRepo, ...userRepositories.filter((repository) =>
                repository.id !== updateRepo.id)])
        } else {
            addRepositoryToWatchlist(updateRepo, username)
                .then((response) => setUserRepositories([response, ...userRepositories.filter((repository) =>
                    repository.id !== response.id)]
                ))
        }
    }

    return (
        <UserDetailsContainer>
            <img src={userData.avatar} alt={userData.name}/>
            <span className="user-name">{userData.name}</span>
            {userRepositories && <UserRepositories userRepositories={userRepositories}
                                                   toggleWatchlist={toggleWatchlist}/>}
            {!userRepositories && <span>Loading repositories</span>}
        </UserDetailsContainer>
    )
}

const UserDetailsContainer = styled.section`
  display: flex;
  flex-direction: column;
  align-items: center;

  img {
    width: 96px;
    height: 96px;
    border-radius: 50%;
  }

  .user-name {
    margin-left: 16px;
  }
`
