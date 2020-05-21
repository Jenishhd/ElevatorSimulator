package cecs277.buildings;

import cecs277.Simulation;
import cecs277.elevators.Elevator;
import cecs277.elevators.ElevatorObserver;
import java.util.*;

public class Building implements ElevatorObserver, FloorObserver {
    public class FloorRequest {
        private Floor mDestination;
        private Elevator.Direction mDirection;

        private FloorRequest(Floor destination, Elevator.Direction direction) {
            mDestination = destination;
            mDirection = direction;
        }

        public Floor getmDestination() {
            return mDestination;
        }

        public Elevator.Direction getmDirection() {
            return mDirection;
        }

        public String toString() {
            return mDestination + "  ";
        }
        public boolean equals(FloorRequest f) {
            return this.getmDestination().equals(f.mDestination);
        }

    }

    private List<Elevator> mElevators = new ArrayList<>();
    private List<Floor> mFloors = new ArrayList<>();
    private Simulation mSimulation;
    private Queue<FloorRequest> mWaitingFloors = new ArrayDeque<>();

    public Building(int floors, int elevatorCount, Simulation sim) {
        mSimulation = sim;

        for (int i = 0; i < floors; i++) {
            Floor f = new Floor(i + 1, this);
            f.addObserver(this);
            mFloors.add(f);
        }

        for (int i = 0; i < elevatorCount; i++) {
            Elevator elevator = new Elevator(i + 1, this);
            elevator.addObserver(this);
            for (Floor f : mFloors) {
                elevator.addObserver(f);
            }
            mElevators.add(elevator);
        }
    }

    public int getFloorCount() {
        return mFloors.size();
    }

    public Floor getFloor(int floor) {
        return mFloors.get(floor - 1);
    }

    public Simulation getSimulation() {
        return mSimulation;
    }

    public List<Elevator> getmElevators() {
        return mElevators;
    }


    public String toString(){
        ArrayList<String> rows=new ArrayList<>();
        ArrayList<String> columns=new ArrayList<>();
        for(int j=0;j<mElevators.size();j++){
            columns.add("|  |");
        }
        for(int i=mFloors.size()-1;i>=0;i--){
            for(int k=0;k<mElevators.size();k++){
                if(mElevators.get(k).getCurrentFloor().getNumber()-1==i){
                    columns.set(k, "| X |");
                }
                else{columns.set(k,"|   |");}
            }

            if(i+1>=10){
                rows.add((i+1)+ ": "+String.join("",columns)+mFloors.get(i).getDirectionSymbol()+
                        mFloors.get(i).getWaitingPassengers()+"\n");

            }
            else{
                rows.add(" "+(i+1)+": "+String.join("",columns)+mFloors.get(i).getDirectionSymbol()+
                        mFloors.get(i).getWaitingPassengers()+"\n");
            }
        }

        for(Elevator x:mElevators){
            rows.add(x.toString()+"\n");
        }
        return String.join("",rows);

    }

    public Queue<FloorRequest> getmWaitingFloors(){
        return mWaitingFloors;
    }


    @Override
    public void elevatorDecelerating(Elevator elevator) {}

    @Override
    public void elevatorDoorsOpened(Elevator elevator) {}

    @Override
    public void elevatorWentIdle(Elevator elevator) {
        if(mWaitingFloors.isEmpty()!=true){
            FloorRequest first = mWaitingFloors.poll();

            Queue<FloorRequest> temp = new ArrayDeque<>();
            if(mElevators.size() > 1) {
                for (FloorRequest f : mWaitingFloors) {
                    if (first.equals(f)) {
                        mWaitingFloors.remove(f);

                    }
                }
            }
            else{//i get error for 1 elevator when my output is fine for multiple elevator so i made an if else clause..
                for (FloorRequest f : temp) {// i would get weird Concurrent error for some reason ? so I made a temp and everything works
                    if (first.equals(f)) {
                        mWaitingFloors.remove(f);

                    }
                }
            }

            elevator.mWaitingRequest[first.getmDestination().getNumber()-1]=true;
            elevator.getoMode().dispatchToFloor(elevator, first.getmDestination(),first.getmDirection());
        }
    }

    @Override
    public void elevatorArriving(Floor sender, Elevator elevator) {
        mWaitingFloors.removeIf(f -> f.mDestination.getNumber() == sender.getNumber() &&

                (elevator.getCurrentDirection() == Elevator.Direction.NOT_MOVING ||

                        elevator.getCurrentDirection() == f.mDirection));
    }

    @Override
    public void directionRequested(Floor floor, Elevator.Direction direction) {
        for (Elevator e : mElevators) {
            if (e.getoMode().canBeDispatchedToFloor(e, floor)) {
                e.mWaitingRequest[floor.getNumber() - 1] = true;
                e.getoMode().dispatchToFloor(e, floor, direction);
                break;
            }
            else{
                FloorRequest request = new FloorRequest(floor,direction);
                mWaitingFloors.add(request);
            }
        }
    }




}