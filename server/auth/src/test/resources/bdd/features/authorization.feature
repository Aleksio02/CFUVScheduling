Feature: Scenarios for user authorization

  Scenario: CS-Auth-01 | Successful authorization
    Given user user with password pass is exist
    When server got authorization request with username user and password pass
    Then request should be processed successfully

  Scenario: CS-Auth-02 | Authorization failed due to empty password
    Given user user2 with password pass2 is exist
    When server got authorization request with username user2 and empty password
    Then request won't be processed due to IncorrectRequestDataException

  Scenario: CS-Auth-03 | Authorization failed due to empty username
    Given user user3 with password pass3 is exist
    When server got authorization request with empty username and password pass
    Then request won't be processed due to IncorrectRequestDataException

  Scenario: CS-Auth-03 | Authorization failed due to incorrect username or password
    When server got authorization request with username user and password pass2
    Then request won't be processed due to UnauthorizedException

