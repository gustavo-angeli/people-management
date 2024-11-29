import Table from './components/Table/Table'
import NavBar from './components/NavBar/NavBar'
import Modal from './components/Modal/Modal'
import { createPerson, findAll, findById, updateData, deleteData } from './apiService'
import { useState, useEffect } from 'react'
import TableHeader from './components/Table/TableHeader/TableHeader'
import TableBody from './components/Table/TableBody/TableBody'
import TableFooter from './components/Table/TableFooter/TableFooter'

function App() {
  const [pageData, setPageData] = useState()
  const [errorMessage, setErrorMessage] = useState()
  const [isLoading, setIsLoading] = useState()
  const [showModal, setShowModal] = useState(false)
  const [modalPerson, setModalPerson] = useState()
  const [modalAction, setModalAction] = useState()

  function getAllPerson(page = 0, size = 10) {
    setIsLoading(true)
    if (errorMessage) {
      setErrorMessage(null)
    }
    const fetchData = async () => {
        try {
            const result = await findAll(page, size);
            setPageData(prevPageData => {
                setIsLoading(false)
                if (result.length == 0) {
                  return prevPageData
                }
                return result
            });
        } catch (error) {
            console.error('Erro ao carregar dados:', error);
        }
    };

    return fetchData();
  }

  function getPersonById(id) {
    setIsLoading(true)

    const fetchData = async () => {
      const result = await findById(id);
      if (result === 'Nonexistent person') {
        setErrorMessage(result)     
      } else {
        setErrorMessage(null)
        setPageData(result);
      }
      setIsLoading(false)
    };

    return fetchData();
  }

  function addPerson(person) {
    console.log('add')
    createPerson(person)
    setPageData(prevPageData => {
      const newPageData = [...prevPageData]
      newPageData.push(person)
      return newPageData
    })
    closeModal()
  }

  function updatePerson(updatedPerson) {
    updateData(updatedPerson)
    if (Array.isArray(pageData)) {
      const personIndex = pageData.findIndex(person => person.id == updatedPerson.id)
      setPageData(prevPageData => {
        const newData = [...prevPageData];
        newData[personIndex] = updatedPerson
        return newData
      })
    } else {
      setPageData(updatedPerson)
    }
    closeModal()
  }

  function deletePerson(id) {
    if (Array.isArray(pageData)) {
      deleteData(id)
      setPageData(prevPageData => {
        return prevPageData.filter(p => p.id != id)
      })
    } else {
      deleteData(id)
      getAllPerson()
    }
  }

  useEffect(() => {
    getAllPerson();
  }, [])

  function openModal(person = {name: '', email: '', password: ''}, modalAction) {
    setShowModal(true)
    setModalPerson(person)
    setModalAction(modalAction)
  }
  function closeModal() {
    setShowModal(false)
  }

  return (
    <>
      <NavBar 
        findPerson={getPersonById} 
        toggleModal={openModal}
      />
      <Table>
        <TableHeader 
          columns={['Id', 'Name', 'Email', 'Password', 'Actions']}
        />
        <TableBody 
          content={pageData} 
          errorMessage={errorMessage} 
          isLoading={isLoading} 
          toggleModal={openModal} 
          handleDelete={deletePerson} 
          handleRefresh={getAllPerson}
        />
        <TableFooter 
          handleUpdatePage={getAllPerson} 
          pageDataSize={pageData && pageData.length}
        />
      </Table>
      {showModal && 
        <Modal 
          closeModal={closeModal} 
          modalAction={modalAction} 
          handleAdd={addPerson} 
          handleUpdate={updatePerson} 
          person={modalPerson}
        />
      }
    </>
  )
}

export default App
