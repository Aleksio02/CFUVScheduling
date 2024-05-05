Feature: Scenarios for user authorization

  Scenario: Test-01 | Test scenario
    Given group named PI-231
    When delete group request for current group incoming from admin
    Then group with name PI-231 won't exist

  @Bug
  Scenario: Test-02 | Bug scenario
    Given group named PI-232
    When delete group request for current group incoming from admin
    Then group with name PI-232(1) won't exist