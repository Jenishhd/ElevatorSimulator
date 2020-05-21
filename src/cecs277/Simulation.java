package cecs277;

import cecs277.buildings.Building;
import cecs277.events.SimulationEvent;
import cecs277.events.SpawnPassengerEvent;
import cecs277.logging.printLogger;
import cecs277.passengers.*;

import java.util.*;

public class Simulation {
	private Random mRandom;
	private PriorityQueue<SimulationEvent> mEvents = new PriorityQueue<>();
	private long mCurrentTime;
	private List<PassengerFactory> factories =new ArrayList<>();
	private Building mBuilding;
	public static int disableMode  = 0;


	/**
	 * Seeds the Simulation with a given random number generator.
	 * @param random
	 */
	public Simulation(Random random) {
		mRandom = random;
	}

	public long getTime(){
		return mCurrentTime;
	}

	public Building getBuilding(){
		return mBuilding;
	}
	public List<PassengerFactory> getFactories(){
		return factories;
	}


	public void addFactories(PassengerFactory pas){
		factories.add(pas);
	}
	/**
	 * Gets the current time of the simulation.
	 * @return
	 */
	public long currentTime() {
		return mCurrentTime;
	}

	/**
	 * Access the Random object for the simulation.
	 * @return
	 */
	public Random getRandom() {
		return mRandom;
	}

	/**
	 * Adds the given event to a priority queue sorted on the scheduled time of execution.
	 * @param ev
	 */
	public void scheduleEvent(SimulationEvent ev) {
		mEvents.add(ev);
	}

	public void startSimulation(Scanner input) {

		printLogger lgr=new printLogger(this);

		lgr.logString("Please enter your building floors number");
		int floorcount=input.nextInt();
		lgr.logString("Please enter your elevator number");
		int ele=input.nextInt();

		mBuilding = new Building(floorcount, ele, this);
		SpawnPassengerEvent ev = new SpawnPassengerEvent(0, mBuilding);
		scheduleEvent(ev);

		long nextSimLength = -1;

		boolean simulateRealTime = false;
		double realTimeScale = 1.0;

		lgr.logString("Do you want DisabledMode on or off during this simulation: 1 - yes, 0 - no");
		disableMode = input.nextInt();

		lgr.logString("Enter your simulation time:");
		nextSimLength = input.nextInt();
		while(nextSimLength!=-1){
			long nextStopTime = mCurrentTime + nextSimLength;
			if (mEvents.peek().getScheduledTime() >= nextStopTime) {
				mCurrentTime = nextStopTime;
			}

			while (!mEvents.isEmpty() && mEvents.peek().getScheduledTime() <= nextStopTime) {
				SimulationEvent nextEvent = mEvents.poll();

				long diffTime = nextEvent.getScheduledTime() - mCurrentTime;
				if (simulateRealTime && diffTime > 0) {
					try {
						Thread.sleep((long)(realTimeScale * diffTime * 1000));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				mCurrentTime += diffTime;
				nextEvent.execute(this);
				lgr.logEvent(nextEvent);

			}

			lgr.logString(mBuilding.toString());


			lgr.logString("Enter how many seconds you would like to simulate. Enter -1 to exit the simulation ");
			nextSimLength = input.nextInt();

		}

	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		// TODO: ask the user for a seed value and change the line below.
		System.out.println("Please enter your seed number.");
		int seed=s.nextInt();

		VisitorPassengerFactory pass1=new VisitorPassengerFactory(10);

		WorkerPassengerFactory pass2=new WorkerPassengerFactory(2);

		ChildPassengerFactory pass3=new ChildPassengerFactory(3);

		DeliveryPersonPassengerFactory pass4=new DeliveryPersonPassengerFactory(2);

		StonerPassengerFactory pass5=new StonerPassengerFactory(1);

		JerkPassengerFactory pass6=new JerkPassengerFactory(2);

		Simulation sim = new Simulation(new Random(seed));
		sim.addFactories(pass1);
		sim.addFactories(pass2);
		sim.addFactories(pass3);
		sim.addFactories(pass4);
		sim.addFactories(pass5);
		sim.addFactories(pass6);
		sim.startSimulation(s);

	}
}
