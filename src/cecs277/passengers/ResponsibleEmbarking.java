package cecs277.passengers;

import cecs277.elevators.Elevator;

public class ResponsibleEmbarking implements EmbarkingStrategy{
    @Override
    public void enteredElevator(Passenger passenger, Elevator elevator) {
        elevator.mWaitingRequest[passenger.getTravelStrategy().getDestination()-1] = true;
    }

    public String toString() {
        return " responsibly";
    }
}
