# Tests/test_get_all_questions.py
import pytest
import requests as rq
import project_orion.automation_pytest.Configs.config as config
import project_orion.automation_pytest.Utils.question_utils as question_utils

def test_get_all_questions_valid():
    """
    Test fetching all questions with valid parameters.
    """
    response = rq.get(config.GET_ALL_QUESTION_URL, params={"pageNumber": 0, "pageSize": 2})
    assert response.status_code == 200, "Expected status code 200 for valid request"
    response_json = response.json()
    assert "content" in response_json, "Response should contain 'content'"
    assert isinstance(response_json["content"], list), "'content' should be a list"

def test_get_all_questions_missing_params():
    """
    Test fetching all questions with missing parameters (should use defaults).
    """
    response = rq.get(config.GET_ALL_QUESTION_URL)
    assert response.status_code == 200, "Expected status code 200 for valid request"

def test_get_all_questions_invalid_page_number():
    """
    Test fetching all questions with an invalid page number.
    """
    response = rq.get(config.GET_ALL_QUESTION_URL, params={"pageNumber": -1, "pageSize": 2})
    assert response.status_code in [200, 400], "Unexpected status code for invalid page number"

def test_get_all_questions_large_page_size():
    """
    Test fetching all questions with a large page size.
    """
    response = rq.get(config.GET_ALL_QUESTION_URL, params={"pageNumber": 0, "pageSize": 1000})
    assert response.status_code == 200, "Expected status code 200 for large page size"
