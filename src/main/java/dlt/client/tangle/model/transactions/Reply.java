package dlt.client.tangle.model.transactions;

import dlt.client.tangle.enums.TransactionType;

/**
 *
 * @author Uellington Damasceno
 */
public class Reply extends TargetedTransaction {

    public Reply(String source, String group, String target) {
        super(source, group, TransactionType.LB_REPLY, target);
    }

}
