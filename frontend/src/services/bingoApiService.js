import axios from 'axios'

const baseUrl = '/api/user'

export const postUser = (username) =>
    axios.post(baseUrl, {username}).then((response) => response.data)

export const getUsers = () =>
    axios.get(baseUrl).then((response) => response.data)

export const getUser = (username) =>
    axios.get(`${baseUrl}/${username}`).then((response) => response.data)

export const getUserRepositories = (username) =>
    axios.get(`${baseUrl}/${username}/repos`).then((response) => response.data)

export const getWatchlist = () =>
    axios.get(`${baseUrl}/watchlist`).then((response)=>response.data)

export const postRepositoryToWatchlist = (userRepository, username) =>
    axios.post(`${baseUrl}/${username}/repos`, userRepository).then((response)=>response.data)

export const deleteRepositoryFromWatchlist = (userRepository) =>
    axios.delete(`${baseUrl}/${username}/repos`, userRepository).then((response)=>response.data)
