
# API ENDPOINTS

---

## 1. Get All Questions

``` GET /api/public/questions ```

### Description

Fetches a list of questions with options for pagination and sorting.


### Request Parameters

| Parameter    | Type    | Default   | Required | Description                                       |
|--------------|---------|-----------|----------|---------------------------------------------------|
| `pageNumber` | Integer | `0`       | No       | The page number to retrieve (0-based indexing).   |
| `pageSize`   | Integer | `20`      | No       | The number of questions per page.                |
| `sortBy`     | String  | `questionId` | No    | The field used to sort the questions.            |
| `sortOrder`  | String  | `asc`     | No       | The sort direction: `asc` for ascending or `desc` for descending order. |

### Response

#### Status Code
- **200 OK**

#### Response Body
A JSON object containing:
- The list of questions.
- Pagination metadata such as the current page, total pages, and total number of questions.

<details>
  <summary>Click to view JSON response</summary>

<pre>
    <code>
{
    "content": [
        {
            "questionId": 1,
            "title": "What is the atomic number of Hydrogen?",
            "description": "Choose the correct answer.",
            "author": "Kushidhar",
            "status": "ACTIVE",
            "subject": "CHEMISTRY",
            "difficulty": "EASY",
            "options": [
                {
                    "optionId": 1,
                    "text": "1"
                },
                {
                    "optionId": 2,
                    "text": "2"
                },
                {
                    "optionId": 3,
                    "text": "3"
                },
                {
                    "optionId": 4,
                    "text": "4"
                }
            ],
            "tagList": [
                {
                    "tagId": 3,
                    "text": "atomic_number"
                },
                {
                    "tagId": 2,
                    "text": "hydrogen"
                },
                {
                    "tagId": 1,
                    "text": "periodic_table"
                }
            ],
            "correctOptionId": null
        }
    ],
    "pageNumber": 0,
    "pageSize": 2,
    "totalElements": 1,
    "totalPages": 1,
    "lastPage": true
}
 </code>
  </pre>
</details>

---

## 2. Get Question by ID

```GET /api/public/question/{questionId}```

### Description

This API retrieves a question by its `questionId`. 
If the question exists, it returns the details of the question.
If not, a 404 error with a message is returned.


### Path Parameter
- `questionId` (required): The unique identifier for the question.

### Response JSONs
<details>
  <summary>Success JSON Response (HTTP 200) </summary>
<pre>
    <code>
  {
    "questionId": 1,
    "title": "What is the atomic number of Hydrogen?",
    "description": "Choose the correct answer.",
    "author": "Kushidhar",
    "status": "ACTIVE",
    "subject": "CHEMISTRY",
    "difficulty": "EASY",
    "options": [
      { "optionId": 1, "text": "1" },
      { "optionId": 2, "text": "2" },
      { "optionId": 3, "text": "3" },
      { "optionId": 4, "text": "4" }
    ],
    "tagList": [
      { "tagId": 1, "text": "periodic_table" },
      { "tagId": 3, "text": "atomic_number" },
      { "tagId": 2, "text": "hydrogen" }
    ],
    "correctOptionId": null
  }
 </code>
  </pre>
</details>

<details>
  <summary>Error Response (HTTP 404) JSON</summary>
<pre>
    <code>
{
  "message": "Question not found with questionId: 11",
  "status": false
}
 </code>
  </pre>
</details>

---

## 3. Create Question API

`POST /api/admin/questions`

### Description
Creates a new question with the provided details, including options and tags.

<details>
<summary>Request JSON Payload </summary>
<pre>
    <code>
{
    "title": "What is the atomic number of Hydrogen?",
    "description": "Choose the correct answer.",
    "subject": "CHEMISTRY",
    "difficulty": "EASY",
    "status": "ACTIVE",
    "author": "Kushidhar",
    "options": [
        {
            "text": "1"
        },
        {
            "text": "2"
        },
        {
            "text": "3"
        },
        {
            "text": "4"
        }
    ],
    "tagList": [
        {
            "text": "atomic_number"
        },
        {
            "text": "hydrogen"
        },
        {
            "text": "periodic_table"
        }
    ],
    "correctOptionId": 1
}
 </code>
  </pre>
</details>

<details>
  <summary>Success JSON Response (HTTP 201 Created) </summary>
<pre>
    <code>
{
    "questionId": 1,
    "title": "What is the atomic number of Hydrogen?",
    "description": "Choose the correct answer.",
    "author": "Kushidhar",
    "status": "ACTIVE",
    "subject": "CHEMISTRY",
    "difficulty": "EASY",
    "options": [
        {
            "optionId": 1,
            "text": "1"
        },
        {
            "optionId": 2,
            "text": "2"
        },
        {
            "optionId": 3,
            "text": "3"
        },
        {
            "optionId": 4,
            "text": "4"
        }
    ],
    "tagList": [
        {
            "tagId": 1,
            "text": "atomic_number"
        },
        {
            "tagId": 2,
            "text": "hydrogen"
        },
        {
            "tagId": 3,
            "text": "periodic_table"
        }
    ],
    "correctOptionId": 1
}
 </code>
  </pre>
</details>

### Validation and Error Handling
The following validation rules must be adhered to when creating a question:

<div style="padding-left: 20px;">

<details>
<summary>Title</summary>

- **Required**: Yes
  - **Constraints**:
      - Minimum 3 characters
      - Maximum 50 characters
  - **Validation Message**: `"must contain at-least 3 characters & at-max 50 characters"`
  - Must be unique for every question.

</details>

<details>
<summary>Description</summary>

- **Required**: Yes
  - **Constraints**:
      - Minimum 6 characters
      - Maximum 1000 characters
  - **Validation Message**: `"must contain at-least 6 characters & at-max 1000 characters"`

</details>

<details>
<summary>Author</summary>

- **Required**: No

</details>

<details>
<summary>Status</summary>

- **Required**: Yes
  - **Allowed Values**: Enum `["ACTIVE", "INACTIVE"]`
  - **Validation Message**: `"Status must not be null"`

</details>

<details>
<summary>Subject</summary>

- **Required**: Yes
  - **Allowed Values**: Enum `["CHEMISTRY", "PHYSICS", "MATH", "BIOLOGY"]`
  - **Validation Message**: `"Subject must not be null"`

</details>

<details>
<summary>Difficulty</summary>

- **Required**: Yes
  - **Allowed Values**: Enum `["EASY", "MEDIUM", "HARD"]`
  - **Validation Message**: `"Difficulty must not be null"`

</details>

<details>
<summary>Options</summary>

- **Required**: Yes
  - **Constraints**:
      - Exactly 4 options
      - Each option must have a `text` field

</details>

<details>
<summary>Tags (tagList)</summary>

- **Required**: Yes
  - **Details**:
      - A set of tags describing the question
      - Creates a new tag if it doesn't exist.
      - At least 1 tag is required for a question

</details>

<details>
<summary>CorrectOptionID</summary>

- **Required**: Yes
  - **Constraints**:
      - Must be between 1 and 4
  - **Validation Message**: `"CorrectOptionId must be between 1 and 4"`

</details>
</div>

### Error Responses
All possible error responses for this API are listed below:

1. **Validation Errors**:
    - **Status Code**: `400 Bad Request`
    - **Response Body**:
        ```json
        {
          "errors": {
                "description": "must contain at-least 6 characters & at-max 1000 characters",
                "title": "must contain at-least 3 characters & at-max 25 characters"
            },
          "status": false
        }
       ```

2. **Duplicate Question Title**:
    - **Status Code**: `400 Bad Request`
    - **Response Body**:
      ```json
      {
          "message": "Question with title 'What is the atomic number of Hydrogen?' already exists!!!",
          "status": false
      }
      ```

3. **Invalid Enum Value**:
    - **Status Code**: `400 Bad Request`
    - **Response Body**:
      ```json
      {
          "message": "Invalid value for subject 'MATH'. Allowed values are [OTHERS, CHEMISTRY, BIOLOGY, MATHEMATICS, PHYSICS].",
          "status": false
      }
      ```

4. **Missing Required Fields**:
    - **Status Code**: `400 Bad Request`
    - **Response Body**:
      ```json
      {
          "errors": {
              "subject": "Subject must not be null"
          },
          "status": false
      }
      ```

### Note
- The validation rules are enforced on both the backend model (`Question`) and the Data Transfer Object (`QuestionDTO`).
- Any unexpected validation or persistence errors will return a generic `500 Internal Server Error` with details in the logs for debugging.

---

## 4. Update Question

This API is used to update the details of a specific question in the system. It allows partial updates, meaning only the fields provided in the request body will be updated, and the rest of the question details will remain unchanged.



### Endpoint
`PUT /api/admin/question/{questionId}`

This endpoint allows updating a specific question based on the provided `questionId`.

### Request Parameters
- `questionId` (path variable): The unique ID of the question to be updated.


<details>
  <summary>Request Body</summary>
<pre>
    <code>
{
    "title": "What is the atomic number of Hydrogen1?",
    "description": "Choose the correct answer.",
    // "subject": "CHEMISTRY",
    // "difficulty": "EASY",
    // "status": "ACTIVE",
    // "author": "Kushidhar",
    "options": [
        {
            "text": "1"
        },
        {
            "text": "2"
        },
        {
            "text": "3"
        },
        {
            "text": "4"
        }
    ],
    // "tagList": [
    //     {
    //         "text": "atomic_number"
    //     },
    //     {
    //         "text": "hydrogen"
    //     },
    //     {
    //         "text": "periodic_table"
    //     }
    // ]
    "correctOptionId": 3
}
 </code>
  </pre>
</details>

<details>
  <summary>Response Body</summary>
<pre>
    <code>
{
    "questionId": 1,
    "title": "What is the atomic number of Hydrogen1?",
    "description": "Choose the correct answer.",
    "author": "Kushidhar",
    "status": "ACTIVE",
    "subject": "CHEMISTRY",
    "difficulty": "EASY",
    "options": [
        {
            "optionId": 1,
            "text": "1"
        },
        {
            "optionId": 2,
            "text": "2"
        },
        {
            "optionId": 3,
            "text": "3"
        },
        {
            "optionId": 4,
            "text": "4"
        }
    ],
    "tagList": [
        {
            "tagId": 1,
            "text": "hydrogen"
        },
        {
            "tagId": 2,
            "text": "periodic_table"
        },
        {
            "tagId": 3,
            "text": "atomic_number"
        }
    ],
    "correctOptionId": null
}
 </code>
  </pre>
</details>

### NOTE
1. Caution when updating options. <br> The options must be provided in the same order which is sent by the get api.
2. Caution when updating tags. <br> If the tags field is provided, the question will be updated to include only the tags specified in the payload. Any tags not included will be removed from the question but will remain persisted in the database.

---

## 5. DELETE QUESTION

This API is used to delete a specific question from the system.
The question, along with its linked options and answer, will be deleted from the database.
The tags linked to the question will remain in the tagsDB and will not be deleted.

### Endpoint

`DELETE /api/admin/question/{questionId}`

### Request Parameters
- `questionId` (path variable): The unique ID of the question to be deleted.

### Response
- **200 OK**: The deleted `QuestionDTO` is returned, containing the details of the deleted question.

### Error Responses
- `404 Not Found`: If the question with the given questionId does not exist, a 404 Not Found error is returned with the message:
    ```json
      {
          "message": "Question with id 1 not found!",
          "status": false
      }
    ```

---

## 6. GET ANSWER

### Endpoint
`GET /api/public/answer/{questionId}`

### Description
Fetches the correct option ID for a specific question.

### **Request**

- **Path Parameter**:
    - `questionId` (Long): The ID of the question.

### **Response**

#### **SUCCESS (200 OK)**

```json
{
  "questionId": 1,
  "correctOptionId": 4
}
```

#### QUESTION NOT FOUND (400 BAD REQUEST)
```json
{
    "message": "Question with id 3 not found!",
    "status": false
}
```

---

## 7. VALIDATE ANSWER

### Endpoint

`POST /api/admin/validate/answer`

### Description
Validates the submitted option for a question and returns the status of the submission.

### Request

```json
    {
        "questionId": 1,
        "optionId": 4
    }
```
### Response

```json
    {
      "status": "CORRECT"
    }
```

### Fields:
- `CORRECT`: If the submitted option matches the correct answer.
- `INCORRECT`: If the submitted option does not match the correct answer.
- `ANSWER_DOES_NOT_EXIST`: If no correct answer is defined for the question.

### QUESTION NOT FOUND (400 BAD REQUEST)
```json
{
    "message": "Question with id 3 not found!",
    "status": false
}
```


---

## 7. SEARCH QUESTIONS BY FILTERS

This api allows clients to search for questions based on `tags`, `subject`, `title`, `difficulty` 
and retrieve paginated results, with optional sorting.

### Endpoint
`POST /public/question/search/`

### Request Parameters
| Parameter    | Type    | Default   | Required | Description                                                                                  |
|--------------|---------|-----------|----------|----------------------------------------------------------------------------------------------|
| `pageNumber` | Integer | `0`       | No       | The page number to retrieve (0-based indexing).                                              |
| `pageSize`   | Integer | `20`      | No       | The number of questions per page.                                                            |
| `sortBy`     | String  | `questionId` | No    | Allowed sorting options: `questionId`, `title`, `difficulty`, `subject`. |
| `sortOrder`  | String  | `asc`     | No       | The sort direction: `asc` for ascending or `desc` for descending order.                      |

### Request Body
Filter Object 
1. `subject` _optional_ 
    - The subject of the question (Enum type: Subject).
2. `difficulty` `optional`
   - The difficulty level of the question (Enum type: Difficulty). 
3. `title` _optional_ 
   - The title of the question. This field supports partial matching (e.g., "title": "biology" will match questions with titles containing the word "biology"). 
4. `tagList` _optional_: 
   - A list of tag IDs associated with the question. If provided, only questions with these tags will be returned.

### Request Payload
```json
    {
      "subject": "CHEMISTRY",
      "difficulty": "MEDIUM",
      "title": "addition",
      "tagList": [1, 2, 3]
    } 
```
<details>
  <summary>Click to view JSON response</summary>

<pre>
    <code>
{
    "content": [
        {
            "questionId": 1,
            "title": "What is the atomic number of Hydrogen?",
            "description": "Choose the correct answer.",
            "author": "Kushidhar",
            "status": "ACTIVE",
            "subject": "CHEMISTRY",
            "difficulty": "EASY",
            "options": [
                {
                    "optionId": 1,
                    "text": "1"
                },
                {
                    "optionId": 2,
                    "text": "2"
                },
                {
                    "optionId": 3,
                    "text": "3"
                },
                {
                    "optionId": 4,
                    "text": "4"
                }
            ],
            "tagList": [
                {
                    "tagId": 3,
                    "text": "atomic_number"
                },
                {
                    "tagId": 2,
                    "text": "hydrogen"
                },
                {
                    "tagId": 1,
                    "text": "periodic_table"
                }
            ],
            "correctOptionId": null
        }
    ],
    "pageNumber": 0,
    "pageSize": 2,
    "totalElements": 1,
    "totalPages": 1,
    "lastPage": true
}
 </code>
  </pre>
</details>

---

## **License and Copyright**
© 2025 ProjectOrion Group. All rights reserved.


This project and its content, including but not limited to code, documentation, and design, are the intellectual property of ProjectOrion Group. 
Unauthorized copying, modification, distribution, or use of any part of this project without prior written permission is strictly prohibited.

---
