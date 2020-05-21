package cecs277.elevators;

import cecs277.buildings.Floor;

import java.util.ArrayList;

import static cecs277.elevators.Elevator.Direction.MOVING_DOWN;
import static cecs277.elevators.Elevator.Direction.MOVING_UP;

/**
 * An ActiveMode elevator is handling at least one floor request.
 */
public class ActiveMode implements OperationMode {

	public boolean canBeDispatchedToFloor(Elevator elevator, Floor floor){
		return false;
	}

	public void dispatchToFloor(Elevator elevator, Floor targetFloor, Elevator.Direction targetDirection){

	}

	public void directionRequested(Elevator elevator, Floor floor, Elevator.Direction direction){

	}

	public void tick(Elevator elevator){
		ArrayList<ElevatorObserver> tObservers = new ArrayList<>(elevator.getObservers());
		Elevator.Direction direction = elevator.getCurrentDirection();

		switch(elevator.getmCurrentState()){
			case DOORS_OPENING:
				elevator.scheduleStateChange(Elevator.ElevatorState.DOORS_OPEN,2);
				break;


			case DOORS_OPEN:
				int pFPassengers = elevator.getCurrentFloor().getWaitingPassengers().size();
				int pEPassengers = elevator.getmPassengers().size();
				for(ElevatorObserver eo : tObservers){
					eo.elevatorDoorsOpened(elevator);
				}
				int cFPassenger = elevator.getCurrentFloor().getWaitingPassengers().size();
				int cEPassenger = elevator.getmPassengers().size();

				int count = Math.abs(pFPassengers-cFPassenger)+pEPassengers-cEPassenger+Math.abs(pFPassengers-cFPassenger);
				int time = count/2;

				elevator.scheduleStateChange(Elevator.ElevatorState.DOORS_CLOSING,1+time);
				break;

			case DOORS_CLOSING:
				if(null==elevator.getCurrentDirection()){

					elevator.setCurrentDirection(Elevator.Direction.NOT_MOVING);

					elevator.scheduleModeChange(Elevator.ElevatorState.IDLE_STATE,new IdleMode(),2);

				}
				else switch (elevator.getCurrentDirection()) {

					case MOVING_DOWN:
						if(elevator.nextRequestDown(elevator.getCurrentFloor().getNumber())!=-1){
							elevator.scheduleStateChange(Elevator.ElevatorState.ACCELERATING,2);
						}
						else if(elevator.nextRequestUp(elevator.getCurrentFloor().getNumber())!=-1){
							elevator.setCurrentDirection(Elevator.Direction.NOT_MOVING);
							elevator.scheduleStateChange(Elevator.ElevatorState.DOORS_OPENING,2);
						}
						else{
							elevator.setCurrentDirection(Elevator.Direction.NOT_MOVING);
							elevator.scheduleModeChange(Elevator.ElevatorState.IDLE_STATE,new IdleMode(),2);
						}
						break;
					case MOVING_UP:
						if(elevator.nextRequestUp(elevator.getCurrentFloor().getNumber())!=-1){
							elevator.scheduleStateChange(Elevator.ElevatorState.ACCELERATING,2);
						}
						else if(elevator.nextRequestDown(elevator.getCurrentFloor().getNumber())!=-1){
							elevator.setCurrentDirection(Elevator.Direction.MOVING_DOWN);
							elevator.scheduleStateChange(Elevator.ElevatorState.DOORS_OPENING,2);
						}
						else{
							elevator.setCurrentDirection(Elevator.Direction.NOT_MOVING);
							elevator.scheduleModeChange(Elevator.ElevatorState.IDLE_STATE,new IdleMode(),2);
						}
						break;
					default:
						elevator.setCurrentDirection(Elevator.Direction.NOT_MOVING);
						elevator.scheduleModeChange(Elevator.ElevatorState.IDLE_STATE,new IdleMode(),2);
						break;

				}
				break;



			case ACCELERATING:
				elevator.getCurrentFloor().removeObserver(elevator);
				elevator.scheduleStateChange(Elevator.ElevatorState.MOVING,3);
				break;

			case MOVING:
				if(elevator.getCurrentDirection() == MOVING_UP) {
					elevator.setCurrentFloor(elevator.getBuilding().getFloor(elevator.getCurrentFloor().getNumber() + 1));
					if (elevator.mWaitingRequest[elevator.getBuilding().getFloor(elevator.getCurrentFloor().getNumber()).getNumber() - 1]
							|| elevator.getCurrentFloor().directionIsPressed(elevator.getCurrentDirection())) {

						elevator.scheduleStateChange(Elevator.ElevatorState.DECELERATING, 2);
					} else {
						elevator.scheduleStateChange(Elevator.ElevatorState.MOVING, 2);
					}
				}

				else if(elevator.getCurrentDirection() == MOVING_DOWN) {
					elevator.setCurrentFloor(elevator.getBuilding().getFloor(elevator.getCurrentFloor().getNumber()-1));

					if(elevator.mWaitingRequest[elevator.getCurrentFloor().getNumber()- 1]
							|| elevator.getCurrentFloor().directionIsPressed(elevator.getCurrentDirection())) {
						elevator.scheduleStateChange(Elevator.ElevatorState.DECELERATING,2);
					}
					else {
						elevator.scheduleStateChange(Elevator.ElevatorState.MOVING,2);
					}
				}

				break;


			case DECELERATING:
				elevator.mWaitingRequest[elevator.getCurrentFloor().getNumber()-1]=false;
				if(!elevator.getCurrentFloor().directionIsPressed(direction)){

					if(direction==Elevator.Direction.MOVING_UP){

						if(elevator.getCurrentFloor().directionIsPressed(Elevator.Direction.MOVING_UP)||
								elevator.nextRequestUp(elevator.getCurrentFloor().getNumber())!=-1){

							elevator.setCurrentDirection(Elevator.Direction.MOVING_UP);
						}
						else if(elevator.getCurrentFloor().directionIsPressed(Elevator.Direction.MOVING_DOWN)&&
								elevator.nextRequestUp(elevator.getCurrentFloor().getNumber())==-1){

							elevator.setCurrentDirection(Elevator.Direction.MOVING_DOWN);
						}
						else{
							elevator.setCurrentDirection(Elevator.Direction.NOT_MOVING);
						}
					}
					else if(direction==Elevator.Direction.MOVING_DOWN){
						if(elevator.getCurrentFloor().directionIsPressed(Elevator.Direction.MOVING_DOWN)||
								elevator.nextRequestDown(elevator.getCurrentFloor().getNumber())!=-1){
							elevator.setCurrentDirection(Elevator.Direction.MOVING_DOWN);
						}
						else if(elevator.getCurrentFloor().directionIsPressed(Elevator.Direction.MOVING_UP)&&
								elevator.nextRequestDown(elevator.getCurrentFloor().getNumber())==-1){
							elevator.setCurrentDirection(Elevator.Direction.MOVING_UP);
						}

						else{

							elevator.setCurrentDirection(Elevator.Direction.NOT_MOVING);

						}

					}

				}

				for (int i = 0; i < elevator.getObservers().size(); i++) {
					elevator.getObservers().get(i).elevatorDecelerating(elevator);
				}
				elevator.scheduleStateChange(Elevator.ElevatorState.DOORS_OPENING,3);
				break;
		}

	}


	@Override
	public String toString() {
		return "Active";
	}
}
