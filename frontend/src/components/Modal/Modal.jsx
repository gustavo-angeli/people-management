import "./Modal.css";
import closeBtn from '../../assets/closeBtn.svg'
import { useState } from "react";

function Modal({ closeModal, person, handleAdd, handleUpdate, modalAction }) {

    const [personState, setPersonState] = useState(person)

    function handleChange(value, field) { 
        setPersonState(prevPerson => {
            return {
                ...prevPerson,
                [field]: value
            }
        })
    }

    function save() {
        if (modalAction === 'add') {
            handleAdd(personState)
        } else {
            handleUpdate(personState)
        }
        setPersonState()
    }

    return <div className="modal">
        <div onClick={() => closeModal()} className="overlay"></div>
        <div className="modal-content">
            <div>
                <img src={closeBtn} alt="" width='30rem' onClick={() => closeModal()} className="close-modal"/>
            </div>
            <div className="inputs">
                <label htmlFor="usernameInput">Name</label>
                <input type="text" name="usernameInput" id="usernameInput" value={personState.name} onChange={(e) => handleChange(e.target.value, 'name')}/>
            </div>
            <div className="inputs">
                <label htmlFor="emailInput">Email</label>
                <input type="text" name="emailInput" id="emailInput" value={personState.email} onChange={(e) => handleChange(e.target.value, 'email')}/>
            </div>
            <div className="inputs">
                <label htmlFor="passwordInput">Password</label>
                <input type="text" name="passwordInput" id="passwordInput" value={personState.password} onChange={(e) => handleChange(e.target.value, 'password')}/>
            </div>
            <button className="save-btn" onClick={() => save()}>Save</button>
        </div>
    </div>
}

export default Modal