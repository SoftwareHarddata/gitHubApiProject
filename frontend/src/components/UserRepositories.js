import styled from 'styled-components/macro'

export default function UserRepositories({userRepositories, toggleAddToWatchlist}) {

    return (
        <List>
            {userRepositories.map((repository) => (
                <li>
                    <Link target="_blank" to={repository.repositoryWebUrl}>
                        {repository.repositoryName}
                    </Link>
                {/*  ADD A BUTTON !!!!!  */}
                </li>
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