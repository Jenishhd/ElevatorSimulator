package cecs277.passengers;

import cecs277.elevators.Elevator;
import cecs277.logging.printLogger;

public class AttentiveDebarking implements DebarkingStrategy {
    @Override
    public boolean willLeaveElevator(Passenger passenger, Elevator elevator) {
        return passenger.getTravelStrategy().getDestination() == elevator.getCurrentFloor().getNumber();

    }

    @Override
    public void departedElevator(Passenger passenger, Elevator elevator) {
        printLogger log = new printLogger(elevator.getBuilding().getSimulation());
        passenger.getTravelStrategy().scheduleNextDestination(passenger,elevator.getCurrentFloor());
        log.logString(elevator.getBuilding().getSimulation().getTime() + "s: " + passenger.getName()
                + " " + passenger.getId() + " debarked at their destination " + elevator.getCurrentFloor().getNumber());
    }
}
