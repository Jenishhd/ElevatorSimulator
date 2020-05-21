package cecs277.passengers;

import cecs277.Simulation;

import java.util.Random;

public class ChildPassengerFactory implements PassengerFactory {
    private static long meanDuration = 7200;
    private static long stDuration = 1800;
    private int weight;

    public ChildPassengerFactory(int weight){ this.weight = weight;}

    @Override
    public String factoryName() {
        return "Child";
    }

    @Override
    public String shortName() {
        return "C";
    }

    @Override
    public int factoryWeight() {
        return weight;
    }

    @Override
    public BoardingStrategy createBoardingStrategy(Simulation simulation) {
        return new AwkwardBoarding(4);
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
        return new DistractedDebarking();
    }
}
