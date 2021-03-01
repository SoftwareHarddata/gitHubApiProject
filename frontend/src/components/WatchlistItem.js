import styled from 'styled-components/macro'
import {Link} from "react-router-dom";

export default function WatchlistItem({repository, onDeleteWatchlistItem}) {

    return (
        <WatchListItemContainer>
            <img src={repository.avatarUrl} alt="a Picture"/>
            <Link to={repository.repositoryWebUrl}>
                {repository.repositoryName}
            </Link>
            <button onClick={() => onDeleteWatchlistItem(repository)} type="button" >
                Delete
            </button>
        </WatchListItemContainer>
    )

}

const WatchListItemContainer = styled.li`
  margin: 0;
  padding: 20px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  
  button {
    width: 96px;
  }


  img {
    width: 96px;
    height: 96px;
    border-radius: 50%;
  }
`