package dlt.client.tangle.model;

import dlt.client.tangle.enums.TransactionType;

/**
 *
 * @author Uellington Damasceno
 */
public class Transaction {
    private TransactionType type;
    private String group;
    private String target;
    private long timestamp;
}
