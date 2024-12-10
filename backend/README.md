# API Documentation

## Endpoints

### Create Person
* **Method:** `POST`
* **Path:** `/api/person`
* **Request Body:**
  + `PersonDTO` object with required fields:
  - `id`: not applicable (auto-generated)
  - `name`: non-empty string (`@NotBlank`)
  - `email`: valid email address (`@Email`) and non-empty string
  - `password`: non-empty string of at least 8 characters long (`@Size(min=8)`, `@NotBlank`)
* **Request Body Example:**
```json
{
  "id": null,
  "name": "John Doe", 
  "email": "john.doe@example.com",
  "password": "mysecretpassword"
}
```

<hr>

### Get Person by ID
* **Method:** `GET`
* **Path:** `/api/person/{id}`
* **Parameters:**
  + `{id}` - the unique identifier of the person (integer)
* **Response:**
  + The person's data in JSON format if found (`200 OK`)
  + Error message if not found or internal server error occurs (`400 Bad Request`)

<hr>

### Get All Persons
* **Method:** `GET`
* **Path:** `/api/person`
* **Query Parameters:**
  + `page`: page number (integer, default=0)
  + `size`: page size (integer, default=10)
* **Response:**
  + List of persons in JSON format (`200 OK`)
  + Error message if internal server error occurs or invalid parameters are provided (`400 Bad Request`)

<hr>

### Update Person by ID
* **Method:** `PUT`
* **Path:** `/api/person/{id}`
* **Parameters:**
  + `{id}` - the unique identifier of the person (integer)
* **Request Body:**
  + `PersonDTO` object with required fields:
  - `name`: non-empty string (`@NotBlank`)
  - `email`: valid email address (`@Email`) and non-empty string
  - `password`: non-empty string of at least 8 characters long (`@Size(min=8)`, `@NotBlank`)
* **Request Body Example:**
```json
{
  "id": null,
  "name": "John Doe", 
  "email": "john.doe@example.com",
  "password": "mysecretpassword"
}
```
* **Response:**
  + Updated person's data in JSON format if successful (`204 No Content`)
  + Error message if not found or internal server error occurs (`400 Bad Request`)

<hr>

### Delete Person by ID
* **Method:** `DELETE`
* **Path:** `/api/person/{id}`
* **Parameters:**
  + `{id}` - the unique identifier of the person (integer)
* **Response:**
  + No content if successful (`204 No Content`)
  + Error message if not found or internal server error occurs (`400 Bad Request`)