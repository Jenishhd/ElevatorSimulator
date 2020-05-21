package cecs277.passengers;

import cecs277.elevators.Elevator;

public class ClumsyEmbarking implements EmbarkingStrategy{
    @Override
    public void enteredElevator(Passenger passenger, Elevator elevator) {
        elevator.requestFloor(elevator.getBuilding().getFloor(passenger.getTravelStrategy().getDestination()));
        if(passenger.getTravelStrategy().getDestination()>elevator.getCurrentFloor().getNumber()){
            elevator.requestFloor(elevator.getBuilding().getFloor(passenger.getTravelStrategy().getDestination()-1));
        }
        else if(passenger.getTravelStrategy().getDestination()<elevator.getCurrentFloor().getNumber()){
            elevator.requestFloor(elevator.getBuilding().getFloor(passenger.getTravelStrategy().getDestination()+1));

        }

    }

    public String toString(){
        return " clumsily";
    }
}
