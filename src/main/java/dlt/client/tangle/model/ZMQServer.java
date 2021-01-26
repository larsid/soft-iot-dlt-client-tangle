package dlt.client.tangle.model;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author Uellington Damasceno
 * @version 0.0.2
 */
public class ZMQServer implements Runnable {

    private Thread server;
    private final BlockingQueue<String> DLTInboundBuffer;

    public ZMQServer(int bufferSize) {
        this.DLTInboundBuffer = new ArrayBlockingQueue(bufferSize);
    }

    public void start() {
        if (this.server == null) {
            this.server = new Thread(this);
            this.server.setName("CLIENT_TANGLE/ZMQ_SERVER");
            this.server.start();
        }
    }

    public void stop() {
        this.server.interrupt();
    }

    public void subscribe(String topic) {
    }

    public void unsubscribe(String topic) {
    }

    public String take() throws InterruptedException {
        return this.DLTInboundBuffer.take();
    }

    @Override
    public void run() {
        while (!this.server.isInterrupted()) {
        }
    }

}
