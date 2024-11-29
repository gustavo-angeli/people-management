export async function createPerson(person) {
    const response = await fetch('http://localhost:8080/api/person',
        {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(person)
        }
    )
}
export async function findAll(page = 0, size = 10) {
    const response = await fetch(`http://localhost:8080/api/person?page=${page}&size=${size}`)
    return response.json();
}

export async function findById(id) {
    try {
        const response = await fetch(`http://localhost:8080/api/person/${id}`)
        if (!response.ok) {
            const errorBody = await response.text();
            return errorBody;
        }

        return response.json();
    } catch (error) {
        console.error('Fetch', error);
    }
}

export async function updateData(person) {
    const response = await fetch(
        `http://localhost:8080/api/person/${person.id}`,
        {
            method: 'PUT',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(person)
        }
    )

    if (response.status != '204') {
        throw new Error(`error: ${response.status}`);
    }
}

export async function deleteData(id) {
    const response = await fetch(
        `http://localhost:8080/api/person/${id}`,
        {
            method: 'DELETE',
            headers: {
                "Content-Type": "application/json",
            }
        }
    )

    if (response.status != '204') {
        throw new Error(`error: ${response.status}`);
    }
}