import axios from 'axios'

const baseUrl = '/api/user'

export const postUser = (name, token) =>
  axios
    .post(
      baseUrl,
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
    .get(baseUrl, {
      headers: {
        Authorization: 'Bearer ' + token,
      },
    })
    .then((response) => response.data)

export const getUser = (username, token) =>
  axios
    .get(`${baseUrl}/${username}`, {
      headers: {
        Authorization: 'Bearer ' + token,
      },
    })
    .then((response) => response.data)

export const getUserRepositories = (username, token) =>
  axios
    .get(`${baseUrl}/${username}/repos`, {
      headers: {
        Authorization: 'Bearer ' + token,
      },
    })
    .then((response) => response.data)

export const getWatchlist = () =>
    axios.get(`${baseUrl}/watchlist`).then((response)=>response.data)

export const addRepositoryToWatchlist = (userRepository, username) =>
    axios.post(`${baseUrl}/${username}/repos`, userRepository).then((response)=>response.data)

export const deleteRepositoryFromWatchlist = (userRepository) =>
    axios.delete(`${baseUrl}/repos/${userRepository.id}`)

