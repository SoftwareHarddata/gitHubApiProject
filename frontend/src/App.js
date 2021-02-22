import AddNewUser from './components/AddNewUser'

function App() {
  return (
    <div>
      <AddNewUser onAdd={(name) => console.log(name)} />
    </div>
  )
}

export default App
