package cecs277.passengers;

import cecs277.buildings.Floor;
import cecs277.buildings.FloorObserver;
import cecs277.elevators.Elevator;
import cecs277.elevators.ElevatorObserver;

/**
 * A passenger that is either waiting on a floor or riding an elevator.
 */
public class Passenger implements FloorObserver, ElevatorObserver {
	String Name;
	String shortName;
	private TravelStrategy travelStrategy;
	private BoardingStrategy boardingStrategy;
	private EmbarkingStrategy embarkingStrategy;
	private DebarkingStrategy debarkingStrategy;
	// An enum for determining whether a Passenger is on a floor, an elevator, or busy (visiting a room in the building).
	public enum PassengerState {
		WAITING_ON_FLOOR,
		ON_ELEVATOR,
		BUSY
	}
	
	// A cute trick for assigning unique IDs to each object that is created. (See the constructor.)
	private static int mNextId;
	protected static int nextPassengerId() {
		return ++mNextId;
	}
	
	private int mIdentifier;
	private PassengerState mCurrentState;

	
	public Passenger(String Name, String shortName, TravelStrategy travelStrategy, BoardingStrategy boardingStrategy, EmbarkingStrategy embarkingStrategy, DebarkingStrategy debarkingStrategy) {
		this.Name = Name;
		this.shortName = shortName;
		this.travelStrategy = travelStrategy;
		this.boardingStrategy = boardingStrategy;
		this.embarkingStrategy = embarkingStrategy;
		this.debarkingStrategy = debarkingStrategy;
		mIdentifier = nextPassengerId();
		mCurrentState = PassengerState.WAITING_ON_FLOOR;

	}
	public TravelStrategy getTravelStrategy() {
		return travelStrategy;
	}

	public BoardingStrategy getBoardingStrategy() {
		return boardingStrategy;
	}

	public EmbarkingStrategy getEmbarkingStrategy() {
		return embarkingStrategy;
	}

	public DebarkingStrategy getDebarkingStrategy() {
		return debarkingStrategy;
	}

	public String getName() {
		return Name;
	}

	public String getShortName() {
		return shortName;
	}


	public void setState(PassengerState state) {
		mCurrentState = state;
	}
	
	/**
	 * Gets the passenger's unique identifier.
	 */
	public int getId() {
		return mIdentifier;
	}
	
	
	/**
	 * Handles an elevator arriving at the passenger's current floor.
	 */
	@Override
	public void elevatorArriving(Floor floor, Elevator elevator) {
		if (floor.getWaitingPassengers().contains(this) && mCurrentState == PassengerState.WAITING_ON_FLOOR) {
			Elevator.Direction direction = elevator.getCurrentDirection();
			floor.clearDirection(direction);
			if (direction != null) {
				switch (direction) {
					case NOT_MOVING:
						elevator.addObserver(this);
						break;

					case MOVING_UP:
						if (this.getTravelStrategy().getDestination() > elevator.getCurrentFloor().getNumber()) {
							elevator.addObserver(this);

						}
						break;

					case MOVING_DOWN:
						if (this.getTravelStrategy().getDestination() < elevator.getCurrentFloor().getNumber()) {
							elevator.addObserver(this);
						}

						break;

					default:
						break;
				}
			}
		}
	}




	//Helper methods for some Strategies
	public boolean checkPassengerDirectionGoingUp(Elevator elevator) {
		return travelStrategy.getDestination() > elevator.getCurrentFloor().getNumber() && elevator.getCurrentDirection() == Elevator.Direction.MOVING_UP;
	}

	public boolean checkPassengerDirectionGoingDown(Elevator elevator) {
		return travelStrategy.getDestination() < elevator.getCurrentFloor().getNumber() && elevator.getCurrentDirection() == Elevator.Direction.MOVING_DOWN;

	}
	
	/**
	 * Handles an observed elevator opening its doors. Depart the elevator if we are on it; otherwise, enter the elevator.
	 */
	@Override
	public void elevatorDoorsOpened(Elevator elevator) {
		if (mCurrentState == PassengerState.ON_ELEVATOR) {
			if(this.debarkingStrategy.willLeaveElevator(this, elevator)==true){
				elevator.removeObserver(this);
				elevator.removePassenger(this);
				this.debarkingStrategy.departedElevator(this, elevator);
			}

		}

		else if (mCurrentState == PassengerState.WAITING_ON_FLOOR) {
			if(this.boardingStrategy.willBoardElevator(this, elevator)==true){
				if(!elevator.getmPassengers().contains(this)){
					elevator.addPassenger(this);
					mCurrentState = PassengerState.ON_ELEVATOR;
				}
				this.embarkingStrategy.enteredElevator(this, elevator);
				elevator.getCurrentFloor().removeWaitingPassenger(this);
				elevator.getCurrentFloor().removeObserver(this);
				for(Elevator e : elevator.getBuilding().getmElevators()){
					if(!e.equals(elevator)){
						e.removeObserver(this);
					}
				}

			}
			else{
				elevator.removeObserver(this);
				if(elevator.getCurrentFloor().getNumber()<this.getTravelStrategy().getDestination()){
					elevator.getCurrentFloor().requestDirection(Elevator.Direction.MOVING_UP);
				}
				else if(elevator.getCurrentFloor().getNumber()>this.getTravelStrategy().getDestination()){
					elevator.getCurrentFloor().requestDirection(Elevator.Direction.MOVING_DOWN);

				}

			}
		}
	}

	
	@Override
	public String toString() {
		return shortName+ " " + this.getId() + "->" + travelStrategy.getDestination();
	}
	
	@Override
	public void directionRequested(Floor sender, Elevator.Direction direction) {}
	
	@Override
	public void elevatorWentIdle(Elevator elevator) {}
	
	// The next two methods allow Passengers to be used in data structures, using their id for equality. Don't change 'em.
	@Override
	public int hashCode() {
		return Integer.hashCode(mIdentifier);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Passenger passenger = (Passenger)o;
		return mIdentifier == passenger.mIdentifier;
	}

	@Override
	public void elevatorDecelerating(Elevator sender) {

	}
	
}