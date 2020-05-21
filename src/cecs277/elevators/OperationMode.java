package cecs277.elevators;

import cecs277.buildings.Floor;

/**
 * Represents the operational state of an elevator, which defines how it responds to particular requests and how it
 * updates it schedules physical state changes. An Elevator object composes an OperationMode instance and uses its
 * methods when programming the Elevator's variant behaviors.
 */
public interface OperationMode {
	/**
	 * Returns true if the given elevator is currently able to accept a dispatch request to the given floor.
	 */
	boolean canBeDispatchedToFloor(Elevator elevator, Floor floor);
	
	/**
	 * Asks the given elevator to dispatch to the given floor and direction. If this is a legal operation in the given mode,
	 * this schedules a transition to DispatchOperation.
	 */
	void dispatchToFloor(Elevator elevator, Floor targetFloor, Elevator.Direction targetDirection);
	
	/**
	 * Informs the given elevator that the floor it is currently on (also given) has requested an elevator going in
	 * the given direction.
	 */
	void directionRequested(Elevator elevator, Floor floor, Elevator.Direction direction);
	
	/**
	 * Ticks the elevator to progress its physical state in the simulation.
	 */
	void tick(Elevator elevator);
}
