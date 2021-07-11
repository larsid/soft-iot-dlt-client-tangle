package dlt.client.tangle.model;

import dlt.client.tangle.services.ILedgerReader;
import dlt.client.tangle.services.ILedgerSubscriber;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Uellington Damasceno
 * @version 0.1.0
 */
public class LedgerReader implements ILedgerReader, Runnable {

    private Thread DLTInboundMonitor;
    private final Map<String, Set<ILedgerSubscriber>> topics;
    private ZMQServer server;

    public LedgerReader() {
        this.topics = new HashMap();
    }

    public void start() {
        if (this.DLTInboundMonitor == null) {
            this.DLTInboundMonitor = new Thread(this);
            this.DLTInboundMonitor.setName("CLIENT_TANGLE/DLT_INBOUND_MONITOR");
            this.DLTInboundMonitor.start();
        }
    }

    public void stop() {
        this.server.stop();
        this.DLTInboundMonitor.interrupt();
    }

    public void setZMQServer(ZMQServer server) {
        this.server = server;
    }

    @Override
    public void subscribe(String topic, ILedgerSubscriber subscriber) {
        if (topic != null) {
            Set<ILedgerSubscriber> subscribers = this.topics.get(topic);
            if (subscribers != null) {
                subscribers.add(subscriber);
            } else {
                subscribers = new HashSet();
                subscribers.add(subscriber);
                this.topics.put(topic, subscribers);
                this.server.subscribe(topic);
            }
        }
    }

    @Override
    public void unsubscribe(String topic, ILedgerSubscriber subscriber) {
        if (topic != null) {
            Set<ILedgerSubscriber> subscribers = this.topics.get(topic);
            if (subscribers != null && !subscribers.isEmpty()) {
                subscribers.remove(subscriber);
                if (subscribers.isEmpty()) {
                    this.server.unsubscribe(topic);
                }
            } 
        }
    }

    @Override
    public void run() {
        while (!this.DLTInboundMonitor.isInterrupted()) {
            try {
                this.server.take();
                String receivedMessage = this.server.take();
                if (receivedMessage != null && receivedMessage.contains("/")) {
                    String[] data = receivedMessage.split("/");
                    String topic = data[0];
                    String message = data[1];
                    notifyAll(topic, message);
                }
            } catch (InterruptedException ex) {
                this.DLTInboundMonitor.interrupt();
            }
        }
    }

    private void notifyAll(String topic, Object object) {
        if (topic != null && !topic.isEmpty()) {
            Set<ILedgerSubscriber> subscribers = this.topics.get(topic);
            if (subscribers != null && !subscribers.isEmpty()) {
                subscribers.forEach(sub -> sub.update(object));
            }
        }
    }

}
