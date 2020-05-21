package cecs277.passengers;

import cecs277.Simulation;

/**
 * An abstract factory for representing a type of passenger. A PassengerFactory implementation chooses boarding,
 * embarking, debarking, and and travel strategies to return from its factory methods. Three utility methods help with
 * output and with random selection of passengers to spawn.
 */
public interface PassengerFactory {
	/**
	 * Gets the name of this factory, that is, a brief description of a passenger that it creates.
	 */
	String factoryName();
	
	/**
	 * Gets a short (1 or 2 letter) abbreviation for the name of this factory.
	 */
	String shortName();
	
	/**
	 * Gets the weight with which this factory should be selected in a uniform random selection of known factories.
	 */
	int factoryWeight();
	
	/**
	 * Creates a BoardingStrategy used by passengers represented by this factory.
	 * @param simulation a reference to the simulation, in case the strategy needs a source of random numbers
	 *                   or other information about the simulation.
	 */
	BoardingStrategy createBoardingStrategy(Simulation simulation);
	
	/**
	 * Creates a TravelStrategy used by passengers represented by this factory.
	 * @param simulation a reference to the simulation, in case the strategy needs a source of random numbers
	 *                   or other information about the simulation.
	 */
	TravelStrategy createTravelStrategy(Simulation simulation);
	
	/**
	 * Creates a EmbarkingStrategy used by passengers represented by this factory.
	 * @param simulation a reference to the simulation, in case the strategy needs a source of random numbers
	 *                   or other information about the simulation.
	 */
	EmbarkingStrategy createEmbarkingStrategy(Simulation simulation);
	
	/**
	 * Creates a DebarkingStrategy used by passengers represented by this factory.
	 * @param simulation a reference to the simulation, in case the strategy needs a source of random numbers
	 *                   or other information about the simulation.
	 */
	DebarkingStrategy createDebarkingStrategy(Simulation simulation);
}
