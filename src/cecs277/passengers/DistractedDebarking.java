package cecs277.passengers;

import cecs277.Simulation;
import cecs277.elevators.Elevator;
import cecs277.events.PassengerNextDestinationEvent;
import cecs277.logging.printLogger;

public class DistractedDebarking implements DebarkingStrategy {
    private int mistake = 0;
    @Override
    public boolean willLeaveElevator(Passenger passenger, Elevator elevator) {
        if(passenger.getTravelStrategy().getDestination() == elevator.getCurrentFloor().getNumber() && mistake == 0){
            mistake++;
            return false;
        }
        else if(mistake == 1){
            mistake++;
            return true;
        }
        else if(passenger.getTravelStrategy().getDestination() == elevator.getCurrentFloor().getNumber() && mistake == 2){
            return true;
        }
        return false;
    }

    @Override
    public void departedElevator(Passenger passenger, Elevator elevator) {
        printLogger log = new printLogger(elevator.getBuilding().getSimulation());

        if(passenger.getTravelStrategy().getDestination() != elevator.getCurrentFloor().getNumber()) {
            Simulation sim = elevator.getCurrentFloor().getmBuilding().getSimulation();
            PassengerNextDestinationEvent e  =
                    new PassengerNextDestinationEvent(sim.currentTime()+5,passenger,elevator.getCurrentFloor());
            sim.scheduleEvent(e);
            log.logString(elevator.getBuilding().getSimulation().getTime() + "s: " + passenger.getName()
                    + " " + passenger.getId() + " got off floor " + elevator.getCurrentFloor().getNumber() + " on the wrong floor.");
        }


        if(passenger.getTravelStrategy().getDestination() == elevator.getCurrentFloor().getNumber()) {

            passenger.getTravelStrategy().scheduleNextDestination(passenger,elevator.getCurrentFloor());
            log.logString(elevator.getBuilding().getSimulation().getTime() + "s: " + passenger.getName()
                    + " " + passenger.getId() + " debarked at their destination " + elevator.getCurrentFloor().getNumber());

        }
    }
}
