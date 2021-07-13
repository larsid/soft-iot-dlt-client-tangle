package dlt.client.tangle.services;

import dlt.client.tangle.model.transactions.Transaction;

/**
 *
 * @author Uellington Damasceno
 * @version 0.0.1
 */
public interface ILedgerWriter {

    public void put(Transaction transaction) throws InterruptedException;

    public Transaction getTransactionByHash(String hashTransaction);
}
