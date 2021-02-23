import axios from 'axios'

const baseUrl = '/api/user'

export const postUser = (name) =>
  axios.post(baseUrl, { name }).then((response) => response.data)
