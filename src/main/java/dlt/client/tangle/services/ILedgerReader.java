package dlt.client.tangle.services;

/**
 *
 * @author Uellington Damasceno
 * @version 0.0.1
 */
public interface ILedgerReader {

    public void subscribe(String topic, ILedgerSubscriber subscriber);

    public void unsubscribe(String topic, ILedgerSubscriber subscriber);
}
