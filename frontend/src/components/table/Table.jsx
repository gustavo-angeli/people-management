import { useState, useEffect } from 'react'
import './Table.css'
import arrowLeft from '../../assets/arrowLeft.svg'
import arrowRight from '../../assets/arrowRight.svg'
import trashLogo from '../../assets/trash.svg'
import updateLogo from '../../assets/update.svg'
import okLogo from '../../assets/ok.svg'

function Table() {
    const [page, setPage] = useState(0)
    const [pageSize, setPageSize] = useState(10)
    const [isLoading, setIsLoading] = useState(true)
    const [pageData, setPageData] = useState([])

    const [editingRow, setEditingRow] = useState(null)
    const [personData, setPersonData] = useState({
        id: '',
        name: '',
        email: '',
        password: ''
    })


    useEffect(() => {
        fetch('http://localhost:8080/api/person?page=0')
        .then(response => response.json())
        .then(response => {
            setPageData(() => {
                setIsLoading(false)
                return response;
            })
        })
    }, [])
    function getData(page = 0, size = 10) {
        setIsLoading(true)
        fetch(`http://localhost:8080/api/person?page=${page}&size=${size}`)
        .then(response => response.json())
        .then(response => {
            setPageData(() => {
                setIsLoading(false)
                return response;
            })
        })
    }
    function updateData(personId, person) {
        fetch(`http://localhost:8080/api/person/${personId}`, {
            method: 'PUT',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(person)
        })
    }
    function deleteData(personId) {
        fetch(`http://localhost:8080/api/person/${personId}`, {
            method: 'DELETE'
        })
    }

    function changePage(page, size) {
        if (page < 0) {
            return;
        }
        setPage((prevPage) => {
            if (pageData.length < pageSize && prevPage < page) {
                return prevPage;
            }
            getData(page, size);
            return page
        })
        
    }
    function changePageSize(value) {
        setPageSize(() => {
            getData(page, value)
            return value
        })
    }

    function changeDataFields(idx) {
        setEditingRow(editingRow === idx ? null : idx)
        setPersonData(pageData[idx])
    }
    function updatePersonAttribute(event, field) {
        setPersonData(prevPerson => {
            return {
                ...prevPerson,
                [field]: event.target.value
            }
        })
    }
    function saveUpdatedPerson(idx, personId, person) {
        setPageData((prevPageData) => {
            const updatedPageData = [...prevPageData]
            updatedPageData[idx] = person;
            return updatedPageData;
        })

        updateData(personId, person)
    
        setPersonData({
            id: '',
            name: '',
            email: '',
            password: ''
        });
    }

    function deletePerson(personId) {
        deleteData(personId)
        setPageData(pageData.filter(person => person.id != personId))
    }



    return <div id='table'>
        <div id='head'>
            <div className='tableData'>Id</div>
            <div className='tableData'>Name</div>
            <div className='tableData'>Email</div>
            <div className='tableData'>Password</div>
            <div className='tableData'>Actions</div>
        </div>
        <div id='body'>
            {isLoading ? 
                <p>Carregando...</p> : 
                pageData.map((person, idx) =>
                    <div className='row' key={person.id + idx}>
                        <div className='tableData'>{person.id}</div>
                        <div className='tableData'>{editingRow === idx ? <input value={personData.name} onChange={(e) => updatePersonAttribute(e, 'name')}/> : person.name}</div>
                        <div className='tableData'>{editingRow === idx ? <input value={personData.email} onChange={(e) => updatePersonAttribute(e, 'email')}/> : person.email}</div>
                        <div className='tableData'>{editingRow === idx ? <input value={personData.password} onChange={(e) => updatePersonAttribute(e, 'password')}/> : person.password}</div>
                        <div className='tableData'>
                            <button onClick={() => {
                                deletePerson(person.id)
                            }}>
                                <img src={trashLogo} alt="" width='20rem'/>
                            </button>
                
                            <button onClick={() => {
                                changeDataFields(idx)
                                if (editingRow === idx) {
                                    saveUpdatedPerson(idx, person.id, personData)
                                }
                            }
                            }>
                                {editingRow === idx ? <img src={okLogo} alt="" width='20rem' /> : <img src={updateLogo} alt="" width='20rem'/>}
                            </button>
                        </div>
                    </div>
                )
            }
        </div>
        <div id='footer'>
            <div className='seilaman'>
                <div id='rowsPerPage'>Rows per page:
                    <select name="rowOptions" className='rowOptions' onChange={(e) => changePageSize(e.target.value)}>
                        <option value="10">10</option>
                        <option value="25">25</option>
                        <option value="50">50</option>
                        <option value="100">100</option>
                    </select>
                </div>
                <div>{page + 1}</div>
            </div>
            <div id='footer_buttons'>
                <img src={arrowLeft} alt="" onClick={() => changePage(page - 1, pageSize)} width='40rem'/>
                <img src={arrowRight} alt="" onClick={() => changePage(page + 1, pageSize)} width='40rem'/>
            </div>
        </div>
    </div>
}

export default Table

