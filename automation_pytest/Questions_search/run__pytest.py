import pytest

def run_selected_pytest_files():
    # List the specific test files you want to run
    test_files = [
        "test_getallquestions.py",
        "test_question_search.py",
    ]
    
    # Run pytest programmatically
    exit_code = pytest.main(test_files)
    
    # Handle the exit code if needed
    if exit_code == 0:
        print("All tests passed successfully!")
    else:
        print(f"Some tests failed. Exit code: {exit_code}")

if __name__ == "__main__":
    run_selected_pytest_files()
