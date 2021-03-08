import styled from 'styled-components/macro'
import {Link, Redirect, Route, useParams} from 'react-router-dom'

export default function PullRequestItem({ pullRequest }) {

    const { username } = useParams()

  return (
      <>
          <h1>Hallo!</h1>
      </>
  )
}


