package dlt.client.tangle.model.transactions;

import dlt.client.tangle.enums.TransactionType;

/**
 *
 * @author Uellington Damasceno
 */
public class LBReply extends Transaction {

    public LBReply(String source, String group) {
        super(source, group, TransactionType.LB_ENTRY_REPLY);
    }
}
