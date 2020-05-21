package cecs277.events;

import cecs277.Simulation;
import cecs277.buildings.Building;
import cecs277.passengers.Passenger;
import cecs277.passengers.PassengerFactory;

import java.util.stream.StreamSupport;

/**
 * A simulation event that adds a new random passenger on floor 1, and then schedules the next spawn event.
 */
public class SpawnPassengerEvent extends SimulationEvent {
	private static long SPAWN_MEAN_DURATION = 10_800;
	private static long SPAWN_STDEV_DURATION = 3_600;


	private Passenger mPassenger;
	private Building mBuilding;
	
	public SpawnPassengerEvent(long scheduledTime, Building building) {
		super(scheduledTime);
		mBuilding = building;
		this.setPriority(5);
	}

	@Override
	public String toString() {
		return super.toString() + "Adding " + mPassenger + " to floor 1.";
	}
	
	@Override
	public void execute(Simulation sim) {
		Iterable<PassengerFactory> factories = sim.getFactories();
		int totalWeight = StreamSupport.stream(factories.spliterator(), false).mapToInt(f -> f.factoryWeight()).sum();
		int cutOff = 0;
		int random = sim.getRandom().nextInt(totalWeight);
		for(PassengerFactory factory : factories){
			cutOff += factory.factoryWeight();
			if(random < cutOff) {
				mPassenger = new Passenger(factory.factoryName(),
				factory.shortName(),
				factory.createTravelStrategy(sim),
				factory.createBoardingStrategy(sim),
				factory.createEmbarkingStrategy(sim),
				factory.createDebarkingStrategy(sim));
				break;
			}
		}

		mBuilding.getFloor(1).addWaitingPassenger(mPassenger);

		long nextTime = 1 + sim.getRandom().nextInt(30) + sim.currentTime();
		SpawnPassengerEvent spe = new SpawnPassengerEvent(nextTime,mBuilding);
		sim.scheduleEvent(spe);

	}

}
