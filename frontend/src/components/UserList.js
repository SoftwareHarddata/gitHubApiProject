import styled from 'styled-components/macro'

export default function UserList({ users }) {
  return (
    <List>
      {users.map((user) => (
        <li key={user.name}>
          <img src={user.avatar} alt={user.name} />
          <span className="user-name">{user.name}</span>
        </li>
      ))}
    </List>
  )
}

const List = styled.ul`
  list-style: none;
  padding: 0;
  margin: 0;

  li {
    display: flex;
    align-items: center;
  }

  li + li {
    margin-top: 16px;
  }

  img {
    width: 48px;
    height: 48px;
    border-radius: 50%;
  }

  .user-name {
    margin-left: 16px;
  }
`
