import styled from 'styled-components/macro'
import {Link} from "react-router-dom";

export default function WatchlistItem({repository}) {

    return (
        <li>
            <img src={repository.avatarUrl} alt="a Picture"/>
            <Link to={repository.repositoryWebUrl}>
                {repository.repositoryName}
            </Link>
        </li>
    )

}