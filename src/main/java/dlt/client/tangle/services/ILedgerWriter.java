package dlt.client.tangle.services;

import dlt.client.tangle.model.transactions.Transaction;

/**
 *
 * @author Allan Capistrano, Uellington Damasceno
 * @version 0.0.2
 */
public interface ILedgerWriter {

    public void put(Transaction transaction) throws InterruptedException;

    public Transaction getTransactionByHash(String hashTransaction);

    public String getUrl();
}
