package dlt.client.tangle.model;

import dlt.client.tangle.services.ILedgerWriter;

/**
 *
 * @author Uellington Damasceno
 * @version 0.0.1
 */
public class LedgerWriter implements ILedgerWriter {

    public void start() {
        System.out.println("LedgerWriter started");
    }

    public void stop() {
        System.out.println("LedgerWriter stopped");
    }
}
