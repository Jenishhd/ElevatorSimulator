package cecs277.buildings;

import cecs277.elevators.ElevatorObserver;
import cecs277.passengers.Passenger;
import cecs277.elevators.Elevator;

import java.util.*;

public class Floor implements ElevatorObserver {
	private Building mBuilding;
	private List<Passenger> mPassengers = new ArrayList<>();
	private ArrayList<FloorObserver> mObservers = new ArrayList<>();
	private int mNumber;
	EnumMap<Elevator.Direction,Boolean> flagDirection = new EnumMap<Elevator.Direction,Boolean>(Elevator.Direction.class);

	
	public Floor(int number, Building building) {
		mNumber = number;
		mBuilding = building;
		flagDirection.put(Elevator.Direction.MOVING_DOWN,Boolean.FALSE);
		flagDirection.put(Elevator.Direction.MOVING_UP,Boolean.FALSE);

	}
	
	/**
	 * Sets a flag that the given direction has been requested by a passenger on this floor. If the direction
	 * had NOT already been requested, then all observers of the floor are notified that directionRequested.
	 * @param direction
	 */
	public void requestDirection(Elevator.Direction direction) {
		// TODO: implement this method as described in the comment.
		if(!directionIsPressed(direction)){
			flagDirection.put(direction,Boolean.TRUE);
			ArrayList<FloorObserver> temp = new ArrayList<>(mObservers);
			for(FloorObserver fo : temp){
				fo.directionRequested(this,direction);
			}
		}
	}

	
	/**
	 * Returns true if the given direction button has been pressed.
	 */
	public boolean directionIsPressed(Elevator.Direction direction) {
		// TODO: complete this method.
		if(flagDirection.get(direction)){return true;}
		return false;
	}
	
	/**
	 * Clears the given direction button so it is no longer pressed.
	 */
	public void clearDirection(Elevator.Direction direction) {
		// TODO: complete this method.
		flagDirection.put(direction,Boolean.FALSE);
	}
	
	/**
	 * Adds a given Passenger as a waiting passenger on this floor, and presses the passenger's direction button.
	 */
	public void addWaitingPassenger(Passenger p) {
		mPassengers.add(p);
		addObserver(p);
		p.setState(Passenger.PassengerState.WAITING_ON_FLOOR);

		int d = p.getTravelStrategy().getDestination();
		if(this.getNumber() < d){
			requestDirection(Elevator.Direction.MOVING_UP);
		}
		else if(this.getNumber() > d){
			requestDirection(Elevator.Direction.MOVING_DOWN);
		}
	}
	
	/**
	 * Removes the given Passenger from the floor's waiting passengers.
	 */
	public void removeWaitingPassenger(Passenger p) {
		mPassengers.remove(p);
	}

	public ArrayList<Integer> getPassengerDestinations() {
		ArrayList<Integer> destinations = new ArrayList<>();
		for(Passenger p : mPassengers){
			if(!destinations.contains(p.getTravelStrategy().getDestination())){
				destinations.add(p.getTravelStrategy().getDestination());
			}
		}
		return destinations;
	}

	
	// Simple accessors.
	public Elevator.Direction getFloorDirection(){
		Elevator.Direction floorDirection=Elevator.Direction.NOT_MOVING;
		for(EnumMap.Entry<Elevator.Direction,Boolean> entry:flagDirection.entrySet()){
			if(entry.getValue()){
				floorDirection= entry.getKey();

			}
		}
		return floorDirection;
	}

	public String getDirectionSymbol() {
		String s = "";
		if(flagDirection.get(Elevator.Direction.MOVING_DOWN)==true&&flagDirection.get(Elevator.Direction.MOVING_UP)==false){
			s="\u2B07\uFE0F";
		}
		else if(flagDirection.get(Elevator.Direction.MOVING_DOWN)==false&&flagDirection.get(Elevator.Direction.MOVING_UP)==true){
			s="\u2B06\uFE0F";
		}
		else if(flagDirection.get(Elevator.Direction.MOVING_DOWN)==true&&flagDirection.get(Elevator.Direction.MOVING_UP)==true){
			s= "\u2195\uFE0F";
		}
		return s;
	}

	public Building getmBuilding() {
		return mBuilding;
	}

	public int getNumber() {
		return mNumber;
	}
	
	public List<Passenger> getWaitingPassengers() {
		return mPassengers;
	}
	
	@Override
	public String toString() {
		return "Floor " + mNumber;
	}
	
	// Observer methods.
	public void removeObserver(FloorObserver observer) {
		mObservers.remove(observer);
	}
	
	public void addObserver(FloorObserver observer) {
		mObservers.add(observer);
	}
	
	// Observer methods.
	@Override
	public void elevatorDecelerating(Elevator elevator) {
		if(elevator.getCurrentFloor() == this){
			for(FloorObserver fo : mObservers){
				fo.elevatorArriving(this,elevator);
			}
			clearDirection(elevator.getCurrentDirection());
		}

	}
	
	@Override
	public void elevatorDoorsOpened(Elevator elevator) {
		// Not needed.
	}
	
	@Override
	public void elevatorWentIdle(Elevator elevator) {
		// Not needed.
 	}

}
