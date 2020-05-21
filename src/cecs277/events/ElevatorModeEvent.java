package cecs277.events;

        import cecs277.Simulation;
        import cecs277.elevators.DisabledMode;
        import cecs277.elevators.Elevator;
        import cecs277.elevators.OperationMode;

public class ElevatorModeEvent extends SimulationEvent {
    public OperationMode oMode;
    public Elevator elevator;
    public Elevator.ElevatorState state;
    public static boolean DisabledFlag = false;
    long sTime;

    public ElevatorModeEvent(long scheduledTime, OperationMode oMode, Elevator elevator, Elevator.ElevatorState state){
        super(scheduledTime);
        this.elevator = elevator;
        this.state = state;
        this.setPriority(2);
        if(oMode instanceof DisabledMode && !DisabledFlag) {
            DisabledFlag = true;
            sTime = scheduledTime;
        }
        this.oMode = oMode;
    }

    public void execute(Simulation sim){
        if(oMode instanceof DisabledMode) {
            if(getScheduledTime() >= sTime+300){
                elevator.enable(elevator);
            }
            else{
                elevator.setoMode(oMode);
                elevator.setState(state);
                elevator.tick();
            }
        }
        else {
            elevator.setOperationMode(oMode);
            elevator.setState(state);
            elevator.tick();
        }
    }
    public String toString() {
        return super.toString() + elevator;
    }
}
