import styled from 'styled-components/macro'
import {Link, Redirect, Route, useParams} from 'react-router-dom'

export default function WatchlistItem({ repository, onDeleteWatchlistItem, onClick }) {

    const { username } = useParams()

  return (
      <WatchListItemContainer>
          <a target="_blank" rel="noreferrer" href={repository.repositoryWebUrl}>{repository.repositoryName}</a>
          <button onClick={() => onClick(username,repository)} type="button">
              Details
          </button>
          <button onClick={() => onDeleteWatchlistItem(repository)} type="button">
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
