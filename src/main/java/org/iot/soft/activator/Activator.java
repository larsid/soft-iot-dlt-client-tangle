package org.iot.soft.activator;

public class Activator implements IActivator {

  
    @Override
    public void start() {
        System.out.println("Bundle iniciado.");
    }


    @Override
    public void stop() {
        System.out.println("Bundle finalizado.");
    }

}
