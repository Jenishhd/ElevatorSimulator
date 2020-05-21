package cecs277.logging;

import cecs277.Simulation;

public class printLogger extends Logger {
    public printLogger(Simulation sim){
        super(sim);
    }

    public void logString(String string){
        System.out.println(string);
    }
}
