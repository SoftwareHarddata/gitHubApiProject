import { useParams } from 'react-router-dom'
import { useEffect, useState } from 'react'
import { getUser } from '../services/bingoApiService'
import styled from 'styled-components/macro'

export default function UserDetails() {
  const { username } = useParams()
  const [userData, setUserData] = useState()

  useEffect(() => {
    getUser(username).then(setUserData)
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
      <img src={userData.avatar} alt={userData.name} />
      <span className="user-name">{userData.name}</span>
    </UserDetailsContainer>
  )
}

const UserDetailsContainer = styled.section`
  img {
    width: 48px;
    height: 48px;
    border-radius: 50%;
  }

  .user-name {
    margin-left: 16px;
  }
`
