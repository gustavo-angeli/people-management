import trashLogo from '../../../assets/trash.svg'
import updateLogo from '../../../assets/update.svg'
import './TableBody.css'

function TableBody({ content, errorMessage, isLoading, toggleModal, handleDelete, handleRefresh }) {
    function mapData(data) {
        if (data == undefined) {
            return
        }
        if (errorMessage) {
            return <div>
                <p>{errorMessage}</p>
                <button className='btn' onClick={() => handleRefresh()}>Refresh</button>
            </div>
        }

        return Array.isArray(data) ? 
            data.map((person) =>
                <div className='row' key={person.email}>
                    <div className='tableData'>{person.id}</div>
                    <div className='tableData'>{person.name}</div>
                    <div className='tableData'>{person.email}</div>
                    <div className='tableData'>{person.password}</div>
                    <div className='tableData'>
                        <button className='rowBtn' onClick={() => {handleDelete(person.id)}}>
                            <img src={trashLogo} alt="" width='20rem'/>
                        </button>
                        <button className='rowBtn' onClick={() => toggleModal(person, 'update')}>
                            {<img src={updateLogo} alt="" width='20rem'/>}
                        </button>
                    </div>
                </div>
            ) :
            <div>
                <div className='row' key={data.email}>
                    <div className='tableData'>{data.id}</div>
                    <div className='tableData'>{data.name}</div>
                    <div className='tableData'>{data.email}</div>
                    <div className='tableData'>{data.password}</div>
                    <div className='tableData'>
                        <button className='rowBtn' onClick={() => {
                            handleDelete(data.id)
                        }}>
                            <img src={trashLogo} alt="" width='20rem'/>
                        </button>
                        <button className='rowBtn' onClick={() => toggleModal(data, 'update')}>
                            {<img src={updateLogo} alt="" width='20rem'/>}
                        </button>
                    </div>
                </div>
                <div>
                    <button className='btn' onClick={() => handleRefresh()}>Refresh</button>
                </div>
            </div>
                
    }

    return <div id='body'>
        {isLoading ? 
            <p>Loading...</p> : 
            mapData(content)
        }
    </div>
}

export default TableBody