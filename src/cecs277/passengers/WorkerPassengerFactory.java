package cecs277.passengers;

import cecs277.Simulation;

import java.util.ArrayList;
import java.util.Random;

public class WorkerPassengerFactory implements PassengerFactory {
    private int weight;

    public WorkerPassengerFactory(int weight) {
        this.weight = weight;
    }

    @Override
    public String factoryName() {
        return "Worker";
    }

    @Override
    public String shortName() {
        return "W";
    }

    @Override
    public int factoryWeight() {
        return weight;
    }

    @Override
    public BoardingStrategy createBoardingStrategy(Simulation simulation) {
        return new ThresholdBoarding(3);
    }

    @Override
    public TravelStrategy createTravelStrategy(Simulation simulation) {
        Random ran=simulation.getRandom();
        ArrayList<Integer> destination=new ArrayList<>();
        ArrayList<Long> durations=new ArrayList<>();
        int nFloor;
        int oFlo=0;
        int destinationFloors=ran.nextInt(4)+2;
        for(int i=0; i<destinationFloors;i++){
            nFloor=ran.nextInt(simulation.getBuilding().getFloorCount()-1)+2;
            while(nFloor==oFlo){
                nFloor=ran.nextInt(simulation.getBuilding().getFloorCount()-1)+2;
            }
            destination.add(nFloor);
            oFlo=nFloor;
        }
        for(int i=0; i<destinationFloors;i++){
            double tempschedule=180*ran.nextGaussian()+600;
            durations.add((long)tempschedule);
        }
        return new MultipleDestinationTravel(destination,durations);
    }

    @Override
    public EmbarkingStrategy createEmbarkingStrategy(Simulation simulation) {
        return new ResponsibleEmbarking();
    }

    @Override
    public DebarkingStrategy createDebarkingStrategy(Simulation simulation) {
        return new AttentiveDebarking();
    }
}
