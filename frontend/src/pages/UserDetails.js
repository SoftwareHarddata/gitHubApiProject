import { useParams } from 'react-router-dom'

export default function UserDetails() {
  const { username } = useParams()

  return <section>{username}</section>
}
