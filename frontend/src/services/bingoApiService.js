import axios from 'axios'

const baseUrl = '/api/user'

export const postUser = (name) =>
  axios.post(baseUrl, { name }).then((response) => response.data)

export const getUsers = () =>
  axios.get(baseUrl).then((response) => response.data)

export const getUser = (username) =>
  axios.get(`${baseUrl}/${username}`).then((response) => response.data)
