package dlt.client.tangle.model.transactions;

import dlt.client.tangle.enums.TransactionType;

/**
 *
 * @author Uellington Damasceno
 */
public class LBMultiRequest extends TargetedTransaction {

    public LBMultiRequest(String source, String group, String target) {
        super(source, group, TransactionType.LB_MULTI_REQUEST, target);
    }

}
