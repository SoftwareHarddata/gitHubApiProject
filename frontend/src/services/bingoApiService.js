import axios from 'axios'

const userUrl = '/api/user'
const watchlistUrl = '/api/watchlist'

export const postUser = (name, token) =>
  axios
    .post(
      userUrl,
      { name },
      {
        headers: {
          Authorization: 'Bearer ' + token,
        },
      }
    )
    .then((response) => response.data)

export const getUsers = (token) =>
  axios
    .get(userUrl, {
      headers: {
        Authorization: 'Bearer ' + token,
      },
    })
    .then((response) => response.data)

export const getUser = (username, token) =>
  axios
    .get(`${userUrl}/${username}`, {
      headers: {
        Authorization: 'Bearer ' + token,
      },
    })
    .then((response) => response.data)

export const getUserRepositories = (username, token) =>
  axios
    .get(`${userUrl}/${username}/repos`, {
      headers: {
        Authorization: 'Bearer ' + token,
      },
    })
    .then((response) => response.data)

export const getWatchlist = (token) =>
  axios
    .get(watchlistUrl, {
      headers: {
        Authorization: 'Bearer ' + token,
      },
    })
    .then((response) => response.data)

export const addRepositoryToWatchlist = (username, repositoryName, token) =>
  axios
    .post(
      watchlistUrl,
      { username, repositoryName },
      {
        headers: {
          Authorization: 'Bearer ' + token,
        },
      }
    )
    .then((response) => response.data)

export const deleteRepositoryFromWatchlist = (repoId, token) =>
  axios.delete(`${watchlistUrl}/${repoId}`, {
    headers: {
      Authorization: 'Bearer ' + token,
    },
  })
