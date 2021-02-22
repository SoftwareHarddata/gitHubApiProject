import AddNewUser from './components/AddNewUser'
import { postUser } from './services/bingoApiService'

function App() {
  const addNewUser = (name) =>
    postUser(name)
      .then((user) => console.log(user))
      .catch((error) => console.error(error))

  return (
    <div>
      <AddNewUser onAdd={addNewUser} />
    </div>
  )
}

export default App
