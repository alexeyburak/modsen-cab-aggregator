Feature: Driver Service

  Scenario: Find all drivers
    Given the driver repository contains the following drivers:
      | id                                   | name   |
      | 00000000-0000-0000-0000-000000000001 | Alex   |
      | 00000000-0000-0000-0000-000000000002 | Alexey |
    When I request all drivers with page 0, size 10, and ascending order by name
    Then the response should contain 2 drivers

  Scenario: Saving a new driver
    Given I have a new driver request with name "Alex", surname "Burak", phone "80299998877"
    When I save the driver
    Then the driver should be saved successfully
    And the driver details should match the request details

  Scenario: Deleting a driver
    Given there is a driver with ID "00000000-0000-0000-0000-000000000001"
    When I delete the driver
    Then the driver with ID "00000000-0000-0000-0000-000000000001" should be deleted

  Scenario: Finding a driver by ID
    Given there is a driver with ID "00000000-0000-0000-0000-000000000001"
    When I search for the driver with ID "00000000-0000-0000-0000-000000000001"
    Then I should receive the driver details

  Scenario: Updating a driver's details
    Given there is a driver with ID "00000000-0000-0000-0000-000000000001"
    And I have an update request with name "Alex", surname "Burak", phone "80299998877"
    When I update the driver with ID "00000000-0000-0000-0000-000000000001"
    Then the driver details should be updated successfully
    And the updated driver details should match the update request