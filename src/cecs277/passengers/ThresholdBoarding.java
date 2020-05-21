package cecs277.passengers;

import cecs277.elevators.Elevator;

public class ThresholdBoarding implements BoardingStrategy {
    int threshold;
    public ThresholdBoarding(int threshold){
        this.threshold = threshold;
    }
    @Override
    public boolean willBoardElevator(Passenger passenger, Elevator elevator) {
        if(threshold >= elevator.getPassengerCount()){
            return true;
        }
        else
            return false;
    }
}
