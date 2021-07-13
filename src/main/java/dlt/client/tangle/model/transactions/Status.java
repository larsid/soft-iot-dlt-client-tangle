package dlt.client.tangle.model.transactions;

import dlt.client.tangle.enums.TransactionType;

/**
 *
 * @author Uellington Damasceno
 */
public class Status extends Transaction {

    private final double avgLoad;
    private final double lastLoad;

    public Status(String source, String group, boolean lbEntry, double avgLoad, double lastLoad) {
        super(source, group, lbEntry ? TransactionType.LB_ENTRY : TransactionType.LB_STATUS);
        this.avgLoad = avgLoad;
        this.lastLoad = lastLoad;
    }

    public double getAvgLoad() {
        return avgLoad;
    }

    public double getLastLoad() {
        return lastLoad;
    }
    
}
