package dlt.client.tangle.model;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import dlt.client.tangle.services.ILedgerWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.iota.jota.IotaAPI;
import org.iota.jota.dto.response.GetBundleResponse;
import org.iota.jota.dto.response.SendTransferResponse;
import org.iota.jota.error.ArgumentException;
import org.iota.jota.model.Transfer;
import org.iota.jota.utils.SeedRandomGenerator;
import org.iota.jota.utils.TrytesConverter;

/**
 *
 * @author Uellington Damasceno
 * @version 0.0.1
 */
public class LedgerWriter implements ILedgerWriter, Runnable {

    private IotaAPI api;
    private Thread DLTOutboundMonitor;
    private final BlockingQueue<Transaction> DLTOutboundBuffer;

    private final String address;
    private final int depth;
    private final int minimumWeightMagnitude;
    private final int securityLevel;

    public LedgerWriter(String protocol, String url, int port, int bufferSize, String address, int depth, int mwm, int securityLevel) {
        this.api = new IotaAPI.Builder()
                .protocol(protocol)
                .host(url)
                .port(port)
                .build();
        this.address = address;
        this.depth = depth;
        this.minimumWeightMagnitude = mwm;
        this.securityLevel = securityLevel;
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
        Gson gson = new Gson();
        while (!this.DLTOutboundMonitor.isInterrupted()) {
            try {
                Transaction transaction = this.DLTOutboundBuffer.take();
                String transactionJson = gson.toJson(transaction);
                this.writeToTangle(transaction.getGroup(), transactionJson); //Group = 2.0.0 ? || group = cloud/c1 ?
            } catch (InterruptedException ex) {
                this.DLTOutboundMonitor.interrupt();
            }
        }
    }

    /*
    Método temporário.
     */
    public Transaction getTransactionByHash(String hashTransaction) {
        GetBundleResponse response = api.getBundle(hashTransaction);

        String trytes = response.getTransactions()
                .get(0)
                .getSignatureFragments()
                .substring(0, 2186);

        String transactionJSON = TrytesConverter.trytesToAscii(trytes);

        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new StringReader(transactionJSON));
        reader.setLenient(true);
        return gson.fromJson(reader, Transaction.class);
    }

    private void writeToTangle(String tagGroup, String message) {
        String myRandomSeed = SeedRandomGenerator.generateNewSeed();
        String messageTrytes = TrytesConverter.asciiToTrytes(message);
        String tagTrytes = TrytesConverter.asciiToTrytes(tagGroup);
        Transfer zeroValueTransaction = new Transfer(address, 0, messageTrytes, tagTrytes);
        List<Transfer> transfers = new ArrayList(1);
        transfers.add(zeroValueTransaction);
        try {
            SendTransferResponse response = api.sendTransfer(myRandomSeed,
                    securityLevel,
                    depth,
                    minimumWeightMagnitude,
                    transfers,
                    null, null, false, false, null);
        } catch (ArgumentException e) {
        }
    }
}
