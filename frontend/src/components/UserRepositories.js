import styled from 'styled-components/macro'

export default function UserRepositories({userRepositories}) {

    return (
        <List>
            {userRepositories.map((repository) => (
                <li><a target="_blank" href={repository.html_url}>{repository.name}</a></li>
            ))}
        </List>
    )

}

const List = styled.ul`
  list-style: none;
  li + li {
    margin-top: 8px;
  }
`