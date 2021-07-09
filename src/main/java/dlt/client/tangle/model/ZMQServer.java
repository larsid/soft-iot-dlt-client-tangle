package dlt.client.tangle.model;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;

/**
 *
 * @author Uellington Damasceno
 * @version 0.0.2
 */
public class ZMQServer implements Runnable {

    private Thread serverThread;
    private final BlockingQueue<String> DLTInboundBuffer;
    private ZMQ.Socket serverListener;

    public ZMQServer(int bufferSize) {
        this.DLTInboundBuffer = new ArrayBlockingQueue(bufferSize);
        this.serverListener = ZMQ.context(1).socket(SocketType.SUB);
    }

    public void start() {
        if (this.serverThread == null) {
            this.serverThread = new Thread(this);
            this.serverThread.setName("CLIENT_TANGLE/ZMQ_SERVER");
            this.serverThread.start();
        }
    }

    public void stop() {
        this.serverThread.interrupt();
        this.serverListener.close();
    }

    public void subscribe(String topic) {
        this.serverListener.subscribe(topic);
    }

    public void unsubscribe(String topic) {
        this.serverListener.unsubscribe(topic);
    }

    public String take() throws InterruptedException {
        return this.DLTInboundBuffer.take();
    }

    @Override
    public void run() {
        //usando configurações de teste até a definição dos tópicos
        serverListener.connect("tcp://zmq.devnet.iota.org:5556");
        serverListener.subscribe("tx");
        serverListener.subscribe("sn");
        //Remover após incluir a tangle local
        String address = "ZLGVEQ9JUZZWCZXLWVNTHBDX9G9KZTJP9VEERIIFHY9SIQKYBVAHIMLHXPQVE9IXFDDXNHQINXJDRPFDXNYVAPLZAW";
        while (!this.serverThread.isInterrupted()) {
            byte[] reply = serverListener.recv(0);
            String[] data = (new String(reply).split(" "));
            if (data[0].equals("tx") && data[2].equals(address)) {
                this.putReceivedMessageBuffer("tx/" + data[1]);
            } else if (data[0].equals("sn") && (data[3].equals(address))) {
                this.putReceivedMessageBuffer("sn/" + data[2]);
            }
        }
    }

    private void putReceivedMessageBuffer(String receivedMessage) {
        try {
            this.DLTInboundBuffer.put(receivedMessage);
        } catch (InterruptedException ex) {
            Logger.getLogger(ZMQServer.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
}
