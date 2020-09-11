package dlt.client.tangle.activator;

/**
 * 
 * @author Uellington Damasceno
 * @version 0.0.1
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
