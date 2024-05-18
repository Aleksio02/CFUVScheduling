Feature: Scenarios for user registration

  Scenario: CS-Auth-01 | Successful registration
    Given username user with password pass not exist
    When server got registration request with username user and password pass
    Then request registration should be processed successfully

  Scenario: CS-Auth-02 | Registration failed due exist user
    Given username user2 with password pass2 is exist
    When server got registration request with username user2 and password pass2
    Then request registration won't be processed due to AlreadyExistsException

  Scenario: CS-Auth-03 | Registration failed due to empty username
    Given username user3 with password pass3 is exist
    When server got registration request with empty username and password pass3
    Then request registration won't be processed due to IncorrectRequestDataException

  Scenario: CS-Auth-05 | Registration failed due to empty password
    Given username user4 with password pass4 is exist
    When server got registration request with username user4 and empty password
    Then request registration won't be processed due to IncorrectRequestDataException
