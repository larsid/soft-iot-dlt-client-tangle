package dlt.client.tangle.model.transactions;

import dlt.client.tangle.enums.TransactionType;

/**
 *
 * @author Uellington Damasceno
 */
public class LBMultiResponse extends TargetedTransaction {

    public LBMultiResponse(String source, String group, String target) {
        super(source, group, TransactionType.LB_MULTI_RESPONSE, target);
    }

}
