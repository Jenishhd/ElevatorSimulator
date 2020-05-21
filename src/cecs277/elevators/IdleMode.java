package cecs277.elevators;


import cecs277.buildings.Floor;

/**
 * An IdleMode elevator is not servicing any requests, and is available for dispatch.
 */
public class IdleMode implements OperationMode {
	/**
	 * An idle elevator can be dispatched to any floor that it is not on.
	 */
	@Override
	public boolean canBeDispatchedToFloor(Elevator elevator, Floor floor) {
		return elevator.getCurrentFloor().getNumber() != floor.getNumber();
	}
	
	/**
	 * Schedules an operation change to DispatchMode.
	 */
	@Override
	public void dispatchToFloor(Elevator elevator, Floor targetFloor, Elevator.Direction targetDirection) {
		elevator.getCurrentFloor().removeObserver(elevator);
		elevator.scheduleModeChange(Elevator.ElevatorState.IDLE_STATE,new DispatchMode(targetFloor, targetDirection), 0);
	}
	
	/**
	 * Called when an elevator is set to IdleMode. There are no physical state changes when idle, so this tick()
	 * will only be called once when the elevator first goes idle.
	 */
	@Override
	public void tick(Elevator elevator) {

		Floor currentFloor = elevator.getCurrentFloor();
		
		// Paranoia: I found a bug where an elevator observed its floor twice. This prevents it.
		currentFloor.removeObserver(elevator);
		currentFloor.addObserver(elevator);
		
		elevator.announceElevatorIdle();
	}
	
	@Override
	public void directionRequested(Elevator elevator, Floor floor, Elevator.Direction direction) {
		floor.removeObserver(elevator);
		elevator.setCurrentDirection(direction);
		elevator.announceElevatorDecelerating();
		
		elevator.scheduleModeChange(Elevator.ElevatorState.DOORS_OPENING,new ActiveMode(), 0);
	}
	
	// I like to print elevator operation modes when debugging.
	@Override
	public String toString() {
		return "Idle";
	}
}
