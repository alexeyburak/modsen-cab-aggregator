Feature: Passenger Service

  Scenario: Find all passengers
    Given the passenger repository contains the following passengers:
      | id                                   | name   |
      | 00000000-0000-0000-0000-000000000001 | Alex   |
      | 00000000-0000-0000-0000-000000000002 | Alexey |
    When I request all passengers with page 0, size 10, and ascending order by email
    Then the response should contain 2 passengers

  Scenario: Saving a new passenger
    Given I have a new passenger request with name "Alex", surname "Burak", email "e@example.com", phone "80299998877"
    When I save the passenger
    Then the passenger should be saved successfully
    And the passenger details should match the request details

  Scenario: Deleting a passenger
    Given there is a passenger with ID "00000000-0000-0000-0000-000000000001"
    When I delete the passenger
    Then the passenger with ID "00000000-0000-0000-0000-000000000001" should be deleted

  Scenario: Finding a passenger by ID
    Given there is a passenger with ID "00000000-0000-0000-0000-000000000001"
    When I search for the passenger with ID "00000000-0000-0000-0000-000000000001"
    Then I should receive the passenger details

  Scenario: Updating a passenger's details
    Given there is a passenger with ID "00000000-0000-0000-0000-000000000001"
    And I have an update request with name "Alex", surname "Burak", email "e@example.com", phone "80299998877"
    When I update the passenger with ID "00000000-0000-0000-0000-000000000001"
    Then the passenger details should be updated successfully
    And the updated passenger details should match the update request
