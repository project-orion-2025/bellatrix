# Tests/test_search_questions.py
import pytest
import requests as rq
import project_orion.automation_pytest.Configs.config as config
import project_orion.automation_pytest.Utils.question_utils as question_utils

@pytest.fixture
def search_payload():
    """
    Provides a mock payload for the search API.
    """
    return question_utils.get_mock_search_payload()

def test_search_questions_valid(search_payload):
    """
    Test searching questions with valid payload.
    """
    response = rq.post(config.SEARCH_QUESTION_URL, json=search_payload)
    assert response.status_code == 200, "Expected status code 200 for valid request"
    response_json = response.json()
    assert "content" in response_json, "Response should contain 'content'"
    assert isinstance(response_json["content"], list), "'content' should be a list"

def test_search_questions_invalid_payload():
    """
    Test searching questions with an invalid payload.
    """
    invalid_payload = {"invalid_field": "invalid_value"}
    response = rq.post(config.SEARCH_QUESTION_URL, json=invalid_payload)
    assert response.status_code in [200, 400], "Unexpected status code for invalid payload"

def test_search_questions_empty_payload():
    """
    Test searching questions with an empty payload.
    """
    response = rq.post(config.SEARCH_QUESTION_URL, json={})
    assert response.status_code == 200, "Expected status code 200 for empty payload"
