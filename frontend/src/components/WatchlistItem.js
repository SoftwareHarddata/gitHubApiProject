import styled from 'styled-components/macro'
import {Link} from "react-router-dom";

export default function WatchlistItem({repository, onDeleteWatchlistItem}) {

    return (
        <li>
            <img src={repository.avatarUrl} alt="a Picture"/>
            <Link to={repository.repositoryWebUrl}>
                {repository.repositoryName}
            </Link>
            <button onClick={() => onDeleteWatchlistItem(repository)} type="button" >
                Delete
            </button>
        </li>
    )

}