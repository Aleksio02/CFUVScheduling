Feature: Scenarios for adding a group

  Scenario: CS-NewGroup-01 | Successful addition of a group
    Given group not exists and named PI-999
    When  server got a request to add a group
    Then  a group has been added to the database

  Scenario: CS-NewGroup-02 | The group name has a null value
    Given group name is null and group not exists
    When server got a request to add a group
    Then the group will not be added due to an exception IncorrectRequestDataException

  Scenario: CS-NewGroup-03 | The name is too short
    Given group not exists and named IT
    When server got a request to add a group
    Then the group will not be added due to an exception IncorrectRequestDataException

  Scenario: CS-NewGroup-04 | A group with this name already exists
    Given group already exists and named PI-989
    When server got a request to add a group
    Then the group will not be added due to an exception AlreadyExistsException