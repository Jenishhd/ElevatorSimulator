package cecs277.passengers;

import cecs277.Simulation;
import cecs277.buildings.Floor;
import cecs277.events.PassengerNextDestinationEvent;

import java.util.List;

public class MultipleDestinationTravel implements TravelStrategy {
    private List<Integer> destinations;
    private List<Long> durations;

    public MultipleDestinationTravel(List<Integer> destinations, List<Long> durations) {
        this.destinations = destinations;
        this.durations = durations;
    }

    @Override
    public int getDestination() {
        if(destinations.isEmpty()){
            return 1;
        }
        return destinations.get(0);
    }

    @Override
    public void scheduleNextDestination(Passenger passenger, Floor currentFloor) {

        if(destinations.isEmpty() != true){
        this.destinations.remove(0);
            Simulation sim = currentFloor.getmBuilding().getSimulation();
            PassengerNextDestinationEvent e  =
                    new PassengerNextDestinationEvent(sim.currentTime()+this.durations.get(0),passenger,currentFloor);
            this.durations.remove(0);
            sim.scheduleEvent(e);
        }

    }
}
