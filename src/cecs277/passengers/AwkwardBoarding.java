package cecs277.passengers;

import cecs277.elevators.Elevator;

public class AwkwardBoarding implements BoardingStrategy{
    int threshold;

    public AwkwardBoarding(int threshold){
        this.threshold = threshold;
    }

    @Override
    public boolean willBoardElevator(Passenger passenger, Elevator elevator) {
        if(threshold >= elevator.getPassengerCount()){
            return true;
        }
        else{
            threshold = threshold + 2;
            return false;
        }
    }

}
