package cecs277.passengers;

import cecs277.Simulation;

import java.util.Random;

public class StonerPassengerFactory implements PassengerFactory {
    private static long meanDuration = 3600;
    private static long stDuration = 1200;
    private int weight;

    public StonerPassengerFactory(int weight) {
        this.weight = weight;
    }

    @Override
    public String factoryName() {
        return "Stoner";
    }

    @Override
    public String shortName() {
        return "S";
    }

    @Override
    public int factoryWeight() {
        return weight;
    }

    @Override
    public BoardingStrategy createBoardingStrategy(Simulation simulation) {
        return new CapacityBoarding();
    }

    @Override
    public TravelStrategy createTravelStrategy(Simulation simulation) {
        Random r = simulation.getRandom();
        return new SingleDestinationTravel((2+r.nextInt(simulation.getBuilding().getFloorCount()-1)),
                (long)(meanDuration+r.nextGaussian()*stDuration));
    }

    @Override
    public EmbarkingStrategy createEmbarkingStrategy(Simulation simulation) {
        return new ClumsyEmbarking();
    }

    @Override
    public DebarkingStrategy createDebarkingStrategy(Simulation simulation) {
        return new ConfusedDebarking();
    }
}
