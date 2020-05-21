package cecs277.passengers;

import cecs277.elevators.Elevator;

/**
 * A DebarkingStrategy specifies rules for deciding when a Passenger will leave an Elevator that has opened
 * its doors, and what to do when the Passenger does leave.
 */

public interface DebarkingStrategy {
	/**
	 * Returns true if the given passenger should depart the given elevator, which has opened its doors on some floor --
	 * not necessarily the passenger's destination.
	 */
	boolean willLeaveElevator(Passenger passenger, Elevator elevator);
	
	/**
	 * Called when the passenger departed the elevator, giving a chance to schedule the next trip etc.
	 */
	void departedElevator(Passenger passenger, Elevator elevator);
}
