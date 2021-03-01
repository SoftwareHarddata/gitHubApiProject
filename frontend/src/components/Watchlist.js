import WatchlistItem from "./WatchlistItem";

export default function Watchlist({watchlist}) {
    return (
        <ul>
            {watchlist.reverse().map((repository) => <WatchlistItem repository={repository}/>)}
        </ul>
    )
}