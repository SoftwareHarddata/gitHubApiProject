import { useParams } from 'react-router-dom'
import { useEffect, useState } from 'react'
import {
    addRepositoryToWatchlist,
    deleteRepositoryFromWatchlist,
    getUser,
    getUserRepositories,
} from '../services/bingoApiService'
import styled from 'styled-components/macro'
import UserRepositories from '../components/UserRepositories'
import { useAuth } from '../auth/AuthContext'
import WatchlistItem from "../components/WatchlistItem";
import PullRequestItem from "../components/PullRequestItem";

export default function RepositoryDetails({pullRequestList} ) {

    return (
        <ul>

            {pullRequestList.map((pullRequest) =>
                <li key={pullRequest.id}>
                <PullRequestItem key={pullRequest.id}
                                 pullRequest={pullRequest} />
                </li>)}

        </ul>
    )
}


