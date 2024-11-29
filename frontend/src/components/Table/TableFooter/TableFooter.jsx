import './TableFooter.css'
import { useState } from 'react'
import arrowLeft from '../../../assets/arrowLeft.svg'
import arrowRight from '../../../assets/arrowRight.svg'

function TableFooter({ handleUpdatePage, pageDataSize }) {
    const [page, setPage] = useState(0)
    const [pageSize, setPageSize] = useState(10)

    function updatePageSize(value) {
        handleUpdatePage(0, value)
        setPage(0)
        setPageSize(value)
    }

    function changePage(pageNumber) {
        if (pageDataSize < pageSize && pageNumber > page || pageNumber < 0) {
            return;
        }
        handleUpdatePage(pageNumber, pageSize)
        setPage(pageNumber)
    }

    return <div id='footer'>
        <div className='pageOptions'>
            <div className='rowsPerPage'>
                <p>Rows per page: </p>
                <select name="rowOptions" className='rowOptions' onChange={(e) => updatePageSize(e.target.value)}>
                        <option value="10">10</option>
                        <option value="25">25</option>
                        <option value="50">50</option>
                        <option value="100">100</option>
                </select>
            </div>
        </div>
        <div id='footer_buttons'>
            <button onClick={() => changePage(page - 1)}>
                <img src={arrowLeft} alt="Icon"/>
            </button>
            <button onClick={() => changePage(page + 1)}>
                <img src={arrowRight} alt="Icon"/>
            </button>
        </div>
    </div>
}

export default TableFooter