package cecs277.passengers;

import cecs277.elevators.Elevator;

/**
 * An EmbarkingStrategy specifies what to do when a passenger entered an open elevator.
 */
public interface EmbarkingStrategy {
	/**
	 * Called when the passenger entered the given elevator, giving a chance to request floors, etc.
	 */
	void enteredElevator(Passenger passenger, Elevator elevator);
}
