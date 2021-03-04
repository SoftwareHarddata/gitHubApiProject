import {useParams} from 'react-router-dom'
import {useEffect, useState} from 'react'
import {getUser, getUserRepositories} from '../services/bingoApiService'
import styled from 'styled-components/macro'
import UserRepositories from "../components/UserRepositories";

export default function UserDetails({token}) {
    const {username} = useParams()
    const [userData, setUserData] = useState()
    const [userRepositories, setUserRepositories] = useState()

    useEffect(() => {
        getUser(username, token).then(setUserData)
        getUserRepositories(username, token).then(setUserRepositories)
    }, [username])

    if (!userData) {
        return (
            <section>
                <p>Loading</p>
            </section>
        )
    }

    return (
        <UserDetailsContainer>
            <img src={userData.avatar} alt={userData.name}/>
            <span className="user-name">{userData.name}</span>
            {userRepositories && <UserRepositories userRepositories={userRepositories}/>}
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
