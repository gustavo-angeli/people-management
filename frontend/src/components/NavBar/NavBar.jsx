import { useState } from 'react'
import './NavBar.css'


function NavBar({ findPerson, toggleModal }) {
    const [id, setId] = useState();

    function handleClick() {
        if (!id || id.trim().length === 0 || isNaN(id)) {
            return;
        }
        findPerson(id)
    }

    return <header>
        <nav id="navbar">
            <div className='searchBox'>
                <input className='navInput' type="text" name="" placeholder="find by id" onChange={(e) => setId(e.target.value)}/>
                <button className='btn' onClick={() => handleClick(id)}>Search</button>
            </div>
            <button className='btn' onClick={() => toggleModal(undefined, 'add')}>Add</button>
        </nav>
    </header>
}

export default NavBar