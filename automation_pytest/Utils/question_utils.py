import requests as rq
import random
import project_orion.automation_pytest.Configs.config as config
# import faker

def fetch_all_questions(params=None):
    """
    Fetch all questions from the API with optional query parameters.
    """
    response = rq.get(config.GET_ALL_QUESTION_URL, params=params)
    if response.status_code != 200:
        raise Exception(f"Failed to fetch questions. Status code: {response.status_code}")
    return response.json()

def search_questions(payload=None):
    """
    Search for questions based on filters.
    """
    response = rq.post(config.SEARCH_QUESTION_URL, json=payload)
    if response.status_code != 200:
        raise Exception(f"Search API failed. Status code: {response.status_code}")
    return response.json()

def generate_payload():
    """
    Generate a payload for creating a new question.
    """
    random_int = random.random()
    payload = {
        "title": f"This is a text {random_int}",
        "description": "Choose the correct answer.",
        "subject": "CHEMISTRY",
        "difficulty": "MEDIUM",
        "status": "ACTIVE",
        "author": "Kushidhar",
        "options": [
            {"text": "1"},
            {"text": "2"},
            {"text": "3"},
            {"text": "4"}
        ],
        "tagList": [
            {"text": "human"},
            {"text": "jio"}
        ],
        "correctOptionId": 1
    }
    return payload

def create_question_id():
    """
    Create a new question and return its ID.
    """
    payload = generate_payload()  # Generate a fresh payload for each call
    create_question_response = rq.post(config.CREATE_QUESTION_URL, json=payload)
    if create_question_response.status_code != 201:
        raise Exception(f"Failed to create question. Status code: {create_question_response.status_code}")
    question_data = create_question_response.json()
    question_id = question_data["questionId"]
    return question_id

def get_mock_search_payload():
    """
    Returns a mock payload for the search API.
    """
    return {
        "subject": "CHEMISTRY",
        "difficulty": "MEDIUM",
        "title": "Hydrogen",
        "tagList": [1, 2]
    }

def get_mock_all_questions_response():
    """
    Returns a mock response for the 'Get All Questions' API.
    """
    return {
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
                    {"optionId": 1, "text": "1"},
                    {"optionId": 2, "text": "2"},
                    {"optionId": 3, "text": "3"},
                    {"optionId": 4, "text": "4"}
                ],
                "tagList": [
                    {"tagId": 1, "text": "periodic_table"},
                    {"tagId": 2, "text": "hydrogen"}
                ],
                "correctOptionId": None
            }
        ],
        "pageNumber": 0,
        "pageSize": 2,
        "totalElements": 1,
        "totalPages": 1,
        "lastPage": True
    }
    
    
    
    