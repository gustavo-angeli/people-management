import './tableHeader.css' 

function TableHeader({ columns }) {
    return <div id='head'>
        {columns.map(column => 
            <div className='tableData' key={column}>{column}</div>
        )}
    </div>
}

export default TableHeader