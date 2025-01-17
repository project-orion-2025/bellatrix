import requests as rq
import random
import project_orion.automation_pytest.Configs.config as config
import project_orion.automation_pytest.Utils.question_utils as question_utils
#import faker


# Data is fetched from the endpoint.
endpoint =  config.ENDPOINT

# Function to create a test
def test_create_question():
    payload = question_utils.generate_payload()  # Generate a fresh payload for each call
    create_question_response = rq.post(config.CREATE_QUESTION_URL, json=payload)
    assert create_question_response.status_code == 201
    question_data = create_question_response.json()
    question_id = question_data["questionId"]
    # Testing by getting the craeted question and matching it with the title
    get_question_response = rq.get(config.GET_QUESTION_URL + f"/{question_id}")
    assert get_question_response.status_code == 200
    
    get_question_data = get_question_response.json()
    assert get_question_data["title"] == payload["title"] 
        
