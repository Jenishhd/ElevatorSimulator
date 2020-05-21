package cecs277.elevators;

import cecs277.buildings.Floor;

import java.util.ArrayList;

/**
 * A DispatchMode elevator is in the midst of a dispatch to a target floor in order to handle a request in a target
 * direction. The elevator will not stop on any floor that is not its destination, and will not respond to any other
 * request until it arrives at the destination.
 */
public class DispatchMode implements OperationMode {
	boolean flag = false;
	private Floor mDestination;
	private Elevator.Direction mDesiredDirection;
	
	public DispatchMode(Floor destination, Elevator.Direction desiredDirection) {
		mDestination = destination;
		mDesiredDirection = desiredDirection;
	}
	
	@Override
	public String toString() {
		return "Dispatching to " + mDestination.getNumber() + " " + mDesiredDirection;
	}

	@Override
	public boolean canBeDispatchedToFloor(Elevator elevator, Floor floor) {
		if(elevator.isIdle() == true && elevator.getCurrentFloor() != floor){
			return true;
		}
		else
			return false;
	}

	@Override
	public void dispatchToFloor(Elevator elevator, Floor targetFloor, Elevator.Direction targetDirection) {
		elevator.setCurrentDirection(targetDirection);
		elevator.mWaitingRequest[targetFloor.getNumber()-1] = true;

	}

	@Override
	public void directionRequested(Elevator elevator, Floor floor, Elevator.Direction direction) {
	}

	@Override
	public void tick(Elevator elevator) {

		ArrayList<ElevatorObserver> tObservers = new ArrayList<>(elevator.getObservers());

		switch(elevator.getmCurrentState()){
			case IDLE_STATE:
				if (!elevator.dFlag && elevator.getBuilding().getSimulation().disableMode==1 && mDestination.getNumber()!=1){
					elevator.dFlag = true;
					elevator.disable(elevator);
				}
				else {
					if (elevator.getCurrentFloor().getNumber() > this.mDestination.getNumber()) {
						elevator.setCurrentDirection(Elevator.Direction.MOVING_DOWN);
					} else if (elevator.getCurrentFloor().getNumber() < this.mDestination.getNumber()) {
						elevator.setCurrentDirection(Elevator.Direction.MOVING_UP);
					}

					elevator.scheduleStateChange(Elevator.ElevatorState.ACCELERATING, 0);
				}

				break;

			case ACCELERATING:
				elevator.getCurrentFloor().removeObserver(elevator);
				elevator.scheduleStateChange(Elevator.ElevatorState.MOVING,3);
				break;

			case MOVING:
				if(elevator.getCurrentDirection() == Elevator.Direction.MOVING_UP) {
					elevator.setCurrentFloor(elevator.getBuilding().getFloor(elevator.getCurrentFloor().getNumber() + 1));
					if (elevator.mWaitingRequest[elevator.getCurrentFloor().getNumber()-1]) {
						elevator.scheduleStateChange(Elevator.ElevatorState.DECELERATING, 2);
						elevator.setCurrentDirection(mDesiredDirection);
					} else {
						elevator.scheduleStateChange(Elevator.ElevatorState.MOVING, 2);
					}
				}

				else if(elevator.getCurrentDirection() == Elevator.Direction.MOVING_DOWN) {
					elevator.setCurrentFloor(elevator.getBuilding().getFloor(elevator.getCurrentFloor().getNumber()-1));

					if(elevator.mWaitingRequest[elevator.getCurrentFloor().getNumber()- 1]) {
						elevator.scheduleStateChange(Elevator.ElevatorState.DECELERATING,2);
						elevator.setCurrentDirection(mDesiredDirection);
					}
					else {
						elevator.scheduleStateChange(Elevator.ElevatorState.MOVING,2);
					}
				}
				break;

			case DECELERATING:
				elevator.mWaitingRequest[elevator.getCurrentFloor().getNumber()-1] = false;
				elevator.announceElevatorDecelerating();
				elevator.scheduleModeChange(Elevator.ElevatorState.DOORS_OPENING,new ActiveMode(),3);
				break;
		}


	}
}
