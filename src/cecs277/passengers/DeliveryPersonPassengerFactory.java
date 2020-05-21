package cecs277.passengers;

import cecs277.Simulation;

import java.util.ArrayList;
import java.util.Random;

public class DeliveryPersonPassengerFactory implements PassengerFactory {
    private int weight;

    public DeliveryPersonPassengerFactory(int weight) {
        this.weight = weight;
    }

    @Override
    public String factoryName() {
        return "Delivery Person";
    }

    @Override
    public String shortName() {
        return "DP";
    }

    @Override
    public int factoryWeight() {
        return weight;
    }

    @Override
    public BoardingStrategy createBoardingStrategy(Simulation simulation) {
        return  new ThresholdBoarding(5);
    }

    @Override
    public TravelStrategy createTravelStrategy(Simulation simulation) {
        Random ran=simulation.getRandom();
        ArrayList<Integer> destination=new ArrayList<>();
        ArrayList<Long> durations=new ArrayList<>();
        int FloorNeedVisit=ran.nextInt(simulation.getBuilding().getFloorCount()*2/3)+1;
        int newFloor;
        for(int i=0; i<FloorNeedVisit;i++){
            newFloor=ran.nextInt(simulation.getBuilding().getFloorCount()-1)+2;
            while(destination.contains(newFloor)){
                newFloor=ran.nextInt(simulation.getBuilding().getFloorCount()-1)+2;
            }
            destination.add(newFloor);

        }
        for(int i=0; i<FloorNeedVisit;i++){
            double tDurations=10*ran.nextGaussian()+60;
            durations.add((long)tDurations);
        }

        return new MultipleDestinationTravel(destination,durations);
    }

    @Override
    public EmbarkingStrategy createEmbarkingStrategy(Simulation simulation) {
        return new ResponsibleEmbarking();
    }

    @Override
    public DebarkingStrategy createDebarkingStrategy(Simulation simulation) {
        return new DistractedDebarking();
    }
}
