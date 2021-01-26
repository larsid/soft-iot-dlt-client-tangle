package dlt.client.tangle.model;

import dlt.client.tangle.services.ILedgerWriter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.iota.jota.IotaAPI;

/**
 *
 * @author Uellington Damasceno
 * @version 0.0.1
 */
public class LedgerWriter implements ILedgerWriter, Runnable {

    private IotaAPI api;
    private Thread DLTOutboundMonitor;
    private final BlockingQueue<Transaction> DLTOutboundBuffer;

    public LedgerWriter(String protocol, String url, int port, int bufferSize) {
        this.api = new IotaAPI.Builder()
                .protocol(protocol)
                .host(url)
                .port(port)
                .build();
        this.DLTOutboundBuffer = new ArrayBlockingQueue(bufferSize);
    }

    @Override
    public void put(Transaction transaction) throws InterruptedException {
        this.DLTOutboundBuffer.put(transaction);
    }

    public void start() {
        if (this.DLTOutboundMonitor == null) {
            this.DLTOutboundMonitor = new Thread(this);
            this.DLTOutboundMonitor.setName("CLIENT_TANGLE/DLT_OUTBOUND_MONITOR");
            this.DLTOutboundMonitor.start();
        }
    }

    public void stop() {
        this.DLTOutboundMonitor.interrupt();
    }

    @Override
    public void run() {
        while (!this.DLTOutboundMonitor.isInterrupted()) {
            try {
                Transaction transaction = this.DLTOutboundBuffer.take();
            } catch (InterruptedException ex) {
                this.DLTOutboundMonitor.interrupt();
            }
        }
    }
}
