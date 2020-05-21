package cecs277.passengers;

import cecs277.buildings.Floor;

/**
 * A TravelStrategy codes rules for determining a Passenger's current destination.
 */
public interface TravelStrategy {
	/**
	 * Returns the floor number of the passenger's current destination, so that other strategies can base their
	 * decisions on where the passenger is trying to get to.
	 */
	int getDestination();
	
	/**
	 * Called when it is time to schedule a PassengerNextDestinationEvent according to the rules of this travel strategy.
	 * Typically this occurs when the passenger departs the elevator on the correct floor, but that is not guaranteed.
	 * @param currentFloor the floor that the passenger got off.
	 */
	void scheduleNextDestination(Passenger passenger, Floor currentFloor);
}
