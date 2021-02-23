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
