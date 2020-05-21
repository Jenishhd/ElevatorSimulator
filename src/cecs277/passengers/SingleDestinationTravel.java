package cecs277.passengers;

import cecs277.Simulation;
import cecs277.buildings.Floor;
import cecs277.events.PassengerNextDestinationEvent;

public class SingleDestinationTravel implements TravelStrategy {
    int destination;
    long duration;

    public SingleDestinationTravel(int destination, long duration){
        this.destination = destination;
        this.duration = duration;
    }
    @Override
    public int getDestination() {
        return destination;
    }

    @Override
    public void scheduleNextDestination(Passenger passenger, Floor currentFloor) {
            destination = 1;
            Simulation sim = currentFloor.getmBuilding().getSimulation();
            PassengerNextDestinationEvent event = new PassengerNextDestinationEvent(sim.currentTime() + duration, passenger, currentFloor);
            sim.scheduleEvent(event);

    }
}
