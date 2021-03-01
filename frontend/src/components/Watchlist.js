import WatchlistItem from "./WatchlistItem";

export default function Watchlist({watchlist, onDeleteWatchlistItem}) {
    return (
        <ul>
            {watchlist.reverse().map((repository) =>
                <WatchlistItem key={repository.id} repository={repository} onDeleteWatchlistItem={onDeleteWatchlistItem}/>)}
        </ul>
    )
}