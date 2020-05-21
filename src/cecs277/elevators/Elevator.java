package cecs277.elevators;

import cecs277.Simulation;
import cecs277.buildings.Building;
import cecs277.buildings.Floor;
import cecs277.buildings.FloorObserver;
import cecs277.events.ElevatorModeEvent;
import cecs277.events.ElevatorStateEvent;
import cecs277.logging.printLogger;
import cecs277.passengers.Passenger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Elevator implements FloorObserver{
	
	public enum ElevatorState {
		IDLE_STATE,
		DOORS_OPENING,
		DOORS_CLOSING,
		DOORS_OPEN,
		ACCELERATING,
		DECELERATING,
		MOVING
	}
	
	public enum Direction {
		NOT_MOVING,
		MOVING_UP,
		MOVING_DOWN
	}

	public static boolean dFlag = false;
	private int mNumber;
	private Building mBuilding;

	private ElevatorState mCurrentState = ElevatorState.IDLE_STATE;
	private Direction mCurrentDirection = Direction.NOT_MOVING;
	private Floor mCurrentFloor;
	private List<Passenger> mPassengers = new ArrayList<>();
	private List<ElevatorObserver> mObservers = new ArrayList<>();
	private OperationMode oMode;
	private OperationMode savedModeState;
	public boolean[] mWaitingRequest;


	// TODO: declare a field to keep track of which floors have been requested by passengers.
	
	
	public Elevator(int number, Building bld) {
		mNumber = number;
		mBuilding = bld;
		mCurrentFloor = bld.getFloor(1);
		mWaitingRequest = new boolean[mBuilding.getFloorCount()];
		scheduleStateChange(ElevatorState.IDLE_STATE, 0);
		for(int i = 0; i < mWaitingRequest.length; i++){
			mWaitingRequest[i] = false;
		}
		oMode = new IdleMode();
	}

	public void enable(Elevator elevator) {
		scheduleModeChange(ElevatorState.IDLE_STATE,savedModeState,0);
	}

	public void disable(Elevator elevator) {
		savedModeState = elevator.oMode;
		scheduleModeChange(ElevatorState.IDLE_STATE,new DisabledMode(),0);
	}


	public void setoMode(OperationMode oMode) {
		this.oMode = oMode;
	}

	public OperationMode getoMode(){return  oMode;}

	public void scheduleModeChange(ElevatorState state, OperationMode oMode, long timeFromNow){
		Simulation sim = mBuilding.getSimulation();
		sim.scheduleEvent(new ElevatorModeEvent(sim.currentTime()+timeFromNow,oMode,this,state));
	}

	public void announceElevatorDecelerating(){
		List<ElevatorObserver> temp = new ArrayList<>(mObservers);
		for(ElevatorObserver eo : temp){
			eo.elevatorDecelerating(this);
		}
	}

	public void announceElevatorIdle(){
		List<ElevatorObserver> temp = new ArrayList<>(mObservers);
		for(ElevatorObserver eo : temp){
			eo.elevatorWentIdle(this);
		}
	}

	public void requestFloor(Floor floor){
		if(floor!=mCurrentFloor){
			mWaitingRequest[floor.getNumber()-1]=true;
		}
	}

	/**
	 * Helper method to schedule a state change in a given number of seconds from now.
	 */
	public void scheduleStateChange(ElevatorState state, long timeFromNow) {
		Simulation sim = mBuilding.getSimulation();
		sim.scheduleEvent(new ElevatorStateEvent(sim.currentTime() + timeFromNow, state, this));
	}
	
	/**
	 * Adds the given passenger to the elevator's list of passengers, and requests the passenger's destination floor.
	 */
	public void addPassenger(Passenger passenger) {
		printLogger log = new printLogger(this.getBuilding().getSimulation());
		log.logString(this.getBuilding().getSimulation().getTime()+"s: "+passenger.toString()+passenger.getEmbarkingStrategy().toString()+" request floor "+
				passenger.getTravelStrategy().getDestination()+" on elevator "+this.mNumber);

		mPassengers.add(passenger);
		mWaitingRequest[passenger.getTravelStrategy().getDestination()-1] = true;

	}
	
	public void removePassenger(Passenger passenger) {
		mPassengers.remove(passenger);
	}


	public int nextRequestUp(int FromFloor){
		int smallestmax=-1;
		for (int i = 0; i<mWaitingRequest.length;i++){
			if(mWaitingRequest[i]==true && (i+1) > FromFloor){
				smallestmax=i+1;
			}

		}
		return smallestmax;
	}

	public int nextRequestDown(int FromFloor){
		int biggestmin=-1;
		for (int i = 0; i<mWaitingRequest.length;i++){
			if(mWaitingRequest[i]==true && (i+1) < FromFloor){
				biggestmin = i+1;
			}
		}
		return biggestmin;
	}
	
	
	/**
	 * Schedules the elevator's next state change based on its current state.
	 */
	public void tick() {
		System.out.println();
		oMode.tick(this);
	}
	
	
	/**
	 * Sends an idle elevator to the given floor.
	 */
	
	// Simple accessors
	public Floor getCurrentFloor() {
		return mCurrentFloor;
	}
	
	public Direction getCurrentDirection() {
		return mCurrentDirection;
	}
	
	public Building getBuilding() {
		return mBuilding;
	}
	
	/**
	 * Returns true if this elevator is in the idle state.
	 * @return
	 */
	public boolean isIdle() {
		if (mCurrentState == ElevatorState.IDLE_STATE){return true;}
		return false;
		// TODO: complete this method.
	}
	
	// All elevators have a capacity of 10, for now.
	public int getCapacity() {
		return 10;
	}
	
	public int getPassengerCount() {
		return mPassengers.size();
	}
	
	// Simple mutators
	public void setState(ElevatorState newState) {
		this.mCurrentState = newState;
	}

	public void setOperationMode(OperationMode o){
		this.oMode = o;
	}
	
	public void setCurrentDirection(Direction direction) {
		this.mCurrentDirection = direction;
	}
	
	public void setCurrentFloor(Floor floor) {
		this.mCurrentFloor = floor;
	}
	
	// Observers
	public void addObserver(ElevatorObserver observer) {
		mObservers.add(observer);
	}
	
	public void removeObserver(ElevatorObserver observer) {
		mObservers.remove(observer);
	}
	
	
	// FloorObserver methods
	@Override
	public void elevatorArriving(Floor floor, Elevator elevator) { }

	@Override
	public void directionRequested(Floor sender, Direction direction) {
		// TODO: if we are currently idle, change direction to match the request. Then alert all our observers that we are decelerating,
		// TODO: then schedule an immediate state change to DOORS_OPENING.
		this.oMode.directionRequested(this,sender,direction);
	}

	public ElevatorState getmCurrentState(){
		return mCurrentState;
	}

	public List<Passenger> getmPassengers(){return mPassengers;}
	public List<ElevatorObserver> getObservers(){return mObservers;}
	public boolean[] getmWaitingRequest(){return mWaitingRequest;}




	// Voodoo magic.
	@Override
	public String toString() {
		ArrayList<Integer> list = new ArrayList<>();
		for(int i = 1; i <= mWaitingRequest.length; i++){
			if(mWaitingRequest[i-1] == true){
				list.add(i);
			}
		}
		return "Elevator " + mNumber + "["+this.oMode +"]" + " - " + mCurrentFloor + " - " + mCurrentState + " - " + mCurrentDirection + " - "
		 + "[" + mPassengers.stream().map(p -> p.getShortName()+p.getId()).collect(Collectors.joining(", "))
		 + "]" + "{" + list.toString()  + "}";
	}
	
}
