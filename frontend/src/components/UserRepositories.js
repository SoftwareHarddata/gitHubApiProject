import styled from 'styled-components/macro'
import {Link} from "react-router-dom";

export default function UserRepositories({userRepositories, toggleWatchlist}) {

    return (
        <List>
            {userRepositories.map((repository) => (
                <li key={repository.repositoryWebUrl}>
                    <Link target="_blank" to={repository.repositoryWebUrl}>
                        {repository.repositoryName}
                    </Link>
                    <button onClick={() => {toggleWatchlist(repository)}} type="button">Favorite</button>
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