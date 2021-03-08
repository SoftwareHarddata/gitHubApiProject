import styled from 'styled-components/macro'
import {Link} from "react-router-dom";

export default function UserRepositories({userRepositories, toggleWatchlist}) {

    return (
        <List>
            {userRepositories.map((repository, i) => (
                <li key={i}>
                    <a target="_blank" rel="noreferrer" href={repository.repositoryWebUrl}>
                        {repository.repositoryName}</a>
                    <button onClick={() => {toggleWatchlist(repository)}} type="button">
                        {repository.onWatchlist ? "Delete" : "Favorite"}
                    </button>
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
