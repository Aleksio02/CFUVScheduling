Feature: Scenarios for adding a group

  Scenario: CS-NewGroup-01 | Successful addition of a group
    Given group named PI-231
    When  server got a request to add a group named PI-231
    Then  a group named PI-231 has been added to the database

  Scenario: CS-NewGroup-02 | The group name has a null value
    Given group named null
    When server got a request to add a group named null
    Then The null group will not be added

  Scenario: CS-NewGroup-03 | The name is too short
    Given group named IT
    When server got a request to add a group named IT
    Then The IT group will not be added

  Scenario: CS-NewGroup-04 | A group with this name already exists
    Given group named PI-231
    When server got a request to add a group named PI-231 and group already exists
    Then The PI-231 group will not be added