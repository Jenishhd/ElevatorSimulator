package cecs277.elevators;

import cecs277.buildings.Floor;

public class DisabledMode implements OperationMode{

    @Override
    public String toString() {
        return "Disabled";
    }

    @Override
    public boolean canBeDispatchedToFloor(Elevator elevator, Floor floor) {
        return false;
    }

    @Override
    public void dispatchToFloor(Elevator elevator, Floor targetFloor, Elevator.Direction targetDirection) {

    }

    @Override
    public void directionRequested(Elevator elevator, Floor floor, Elevator.Direction direction) {

    }

    @Override
    public void tick(Elevator elevator) {
        elevator.scheduleModeChange(Elevator.ElevatorState.IDLE_STATE,new DisabledMode(),300);
    }
}
