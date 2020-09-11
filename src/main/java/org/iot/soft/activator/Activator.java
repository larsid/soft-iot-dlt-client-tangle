package org.iot.soft.activator;

/**
 * 
 * @author Uellington Damasceno
 */
public class Activator implements IActivator {

    @Override
    public void start() {
        System.out.println("The Bundle is Running!");
    }

    @Override
    public void stop() {
        System.out.println("The Bundle has been Finalized!");
    }
}
