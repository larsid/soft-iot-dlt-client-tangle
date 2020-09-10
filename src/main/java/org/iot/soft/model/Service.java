package org.iot.soft.model;

/**
 *
 * @author Uellington Damasceno
 * @version 0.0.1
 */
public abstract class Service {
    
    public void start(){
        System.out.println("This service is running!");
    }
    
    public void stop(){
        System.out.println("Service stopped!");
    }
}
