package cecs277.logging;

import cecs277.Simulation;

public abstract class Logger {
	private static Logger mInstance;
	private Simulation mSimulation;
	public Logger(Simulation simulation) {
		mSimulation = simulation;
	}
	
	public static void setInstance(Logger instance) {
		mInstance = instance;
	}
	
	public static Logger getInstance() {
		return mInstance;
	}
	
	public Simulation getSimulation() {
		return mSimulation;
	}

	public void logEvent(Object b) {
		logString(mSimulation.currentTime() + "s: " + b.toString());
	}
	
	public abstract void logString(String message);
}
