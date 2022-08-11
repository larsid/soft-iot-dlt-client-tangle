package dlt.client.tangle.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import dlt.client.tangle.enums.TransactionType;
import dlt.client.tangle.model.transactions.LBReply;
import dlt.client.tangle.model.transactions.Reply;
import dlt.client.tangle.model.transactions.Request;
import dlt.client.tangle.model.transactions.Status;
import dlt.client.tangle.model.transactions.Transaction;
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
 * @author  Allan Capistrano, Antonio Crispim, Uellington Damasceno
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

  public LedgerWriter(
    String protocol,
    String url,
    int port,
    int bufferSize,
    String address,
    int depth,
    int mwm,
    int securityLevel
  ) {
    this.api =
      new IotaAPI.Builder().protocol(protocol).host(url).port(port).build();

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
        transaction.setPublishedAt(System.currentTimeMillis());
        String transactionJson = gson.toJson(transaction);
        this.writeToTangle(transaction.getGroup(), transactionJson);
      } catch (InterruptedException ex) {
        this.DLTOutboundMonitor.interrupt();
      }
    }
  }

  @Override
  public Transaction getTransactionByHash(String hashTransaction) {
    GetBundleResponse response = api.getBundle(hashTransaction);

    String trytes = response
      .getTransactions()
      .get(0)
      .getSignatureFragments()
      .substring(0, 2186);

    String transactionJSON = TrytesConverter.trytesToAscii(trytes);

    return getTypeTransaction(transactionJSON);
  }

  private Transaction getTypeTransaction(String transactionJSON) {
    System.out.println("JSON Message:");
    System.out.println(transactionJSON);

    JsonParser jsonParser = new JsonParser();
    JsonReader reader = new JsonReader(new StringReader(transactionJSON));
    reader.setLenient(true);

    JsonObject jsonObject = jsonParser.parse(reader).getAsJsonObject();

    String type = jsonObject.get("type").getAsString();
    Gson gson = new Gson();
    
    reader = new JsonReader(new StringReader(transactionJSON));
    reader.setLenient(true);

    if (type.equals(TransactionType.LB_ENTRY.name())) {
      return gson.fromJson(reader, Status.class);
    } else if (type.equals(TransactionType.LB_ENTRY_REPLY.name())) {
      return gson.fromJson(reader, LBReply.class);
    } else if (type.equals(TransactionType.LB_REPLY.name())) {
      return gson.fromJson(reader, Reply.class);
    } else if (type.equals(TransactionType.LB_REQUEST.name())) {
      return gson.fromJson(reader, Request.class);
    } else if (type.equals(TransactionType.LB_STATUS.name())) {
      return gson.fromJson(reader, Status.class);
    }

    return null;
  }

  private void writeToTangle(String tagGroup, String message) {
    String myRandomSeed = SeedRandomGenerator.generateNewSeed();
    String messageTrytes = TrytesConverter.asciiToTrytes(message);
    String tagTrytes = TrytesConverter.asciiToTrytes(tagGroup);

    Transfer zeroValueTransaction = new Transfer(
      address,
      0,
      messageTrytes,
      tagTrytes
    );
    List<Transfer> transfers = new ArrayList(1);
    transfers.add(zeroValueTransaction);
    try {
      SendTransferResponse response = api.sendTransfer(
        myRandomSeed,
        securityLevel,
        depth,
        minimumWeightMagnitude,
        transfers,
        null,
        null,
        false,
        false,
        null
      );
    } catch (ArgumentException e) {
      System.out.println("Error in arguments!");
      e.printStackTrace();
    }
  }
}
