import WatchlistItem from "./WatchlistItem";
import styled from "styled-components/macro";

export default function Watchlist({watchlist, onDeleteWatchlistItem}) {
    return (
        <WatchListContainer>
            {watchlist.map((repository) =>
                <WatchlistItem key={repository.id} repository={repository} onDeleteWatchlistItem={onDeleteWatchlistItem}/>)}
        </WatchListContainer>
    )
}


const WatchListContainer = styled.ul`
  margin: 0;
  padding: 0;
`