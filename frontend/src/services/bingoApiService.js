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
