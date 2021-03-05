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

export default function RepositoryDetails() {

    return <></>
}


