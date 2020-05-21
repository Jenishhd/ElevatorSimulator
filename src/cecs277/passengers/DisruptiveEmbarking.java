package cecs277.passengers;

import cecs277.elevators.Elevator;

public class DisruptiveEmbarking implements EmbarkingStrategy {
    @Override
    public void enteredElevator(Passenger passenger, Elevator elevator) {
        elevator.mWaitingRequest[passenger.getTravelStrategy().getDestination()-1] = true;
        if(passenger.checkPassengerDirectionGoingUp(elevator)){
            for(int i = passenger.getTravelStrategy().getDestination(); i < elevator.getBuilding().getFloorCount(); i++) {
                elevator.mWaitingRequest[i] = true;
            }
        }
        else if(passenger.checkPassengerDirectionGoingDown(elevator)){
            for(int i = 0; i < passenger.getTravelStrategy().getDestination()-1; i++) {
                elevator.mWaitingRequest[i] = true;
            }
        }
    }

    public String toString(){
        return " disruptively";
    }
}
