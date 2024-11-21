import { useState } from 'react'
import Table from './components/table/Table'

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
      <main>
        <Table />
      </main>
    </>
  )
}

export default App
