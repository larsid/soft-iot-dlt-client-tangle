package dlt.client.tangle.model;

import dlt.client.tangle.services.ILedgerReader;

/**
 *
 * @author Uellington Damasceno
 * @version 0.0.1
 */
public class LedgerReader implements ILedgerReader {

    public void start() {
        System.out.println("LedgerReader started");
    }

    public void stop() {
        System.out.println("LedgerReader stopped");
    }
}
