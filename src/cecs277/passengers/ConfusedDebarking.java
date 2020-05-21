package cecs277.passengers;

import cecs277.elevators.Elevator;
import cecs277.logging.printLogger;

public class ConfusedDebarking implements DebarkingStrategy{
    @Override
    public boolean willLeaveElevator(Passenger passenger, Elevator elevator) {
        if(elevator.getCurrentFloor().getNumber() == 1){
            return true;
        }
        return false;
    }

    @Override
    public void departedElevator(Passenger passenger, Elevator elevator) {
        printLogger log = new printLogger(elevator.getBuilding().getSimulation());
        log.logString(elevator.getBuilding().getSimulation().getTime() + "s: " + passenger.getName()
                + " " + passenger.getId() + " debarked at their destination " + elevator.getCurrentFloor().getNumber());
    }
}
