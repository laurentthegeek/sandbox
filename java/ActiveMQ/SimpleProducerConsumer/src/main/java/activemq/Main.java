package activemq;

import java.net.URI;
import java.util.Random;

import javax.jms.Connection;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;

public class Main {

    // VM Transport
    public static final String VM_BROKER_URL = "vm://mybroker?create=false"; // ?option=value&...
    public static final int PRODUCER_MAX = 20;
    public static final int CONSUMER_MAX = 10;

    public static void main(String[] args) throws Exception {
        // BrokerService broker = new BrokerService();
        BrokerService broker = BrokerFactory.createBroker(new URI("xbean:activemq.xml"));
        broker.setBrokerName("mybroker");
        broker.start();

        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(VM_BROKER_URL);
        //cf.setUseAsyncSend(false); // jms.useAsyncSend=true
        //cf.setUseCompression(true); // jms.useCompression=true

        // Setting up producers
        final Connection producerConn = cf.createConnection();
        producerConn.start();
        for (int i = 0; i < PRODUCER_MAX; ++i) {
            System.out.printf("Starting producer %d...\n", i);
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Session session = producerConn.createSession(false, Session.AUTO_ACKNOWLEDGE);
                        Queue queue = session.createQueue("TEST_QUEUE");
                        MessageProducer producer = session.createProducer(queue);
                        Random random = new Random();

                        long begin, end;

                        for (int i = 0;; ++i) {
                            byte[] messageBytes = new byte[random.nextInt(10 * 1024)];
                            random.nextBytes(messageBytes);
                            Message message = session.createObjectMessage(messageBytes);
                            begin = System.currentTimeMillis();
                            producer.send(message);
                            end = System.currentTimeMillis();
                            if (end - begin > 100) {
                                System.out.printf("Producer '%s' blocked for %dms\n",
                                        Thread.currentThread().getName(), end - begin);
                            }
//                            if (i % 1000 == 0) {
//                                session.commit();
//                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, String.format("Producer-%d", i)).start();
        }

        // Setting up consumers
        final Connection consumerConn = cf.createConnection();
        consumerConn.start();
        for (int i = 0; i < CONSUMER_MAX; ++i) {
            System.out.printf("Starting consumer %d...\n", i);

            Session session = consumerConn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue("TEST_QUEUE");
            MessageConsumer consumer = session.createConsumer(queue);
            consumer.setMessageListener(new MessageListener() {

                private long begin = 0, end;

                @Override
                public void onMessage(Message message) {
                    try {
                        end = System.currentTimeMillis();
                        if (begin != 0 && end - begin > 100) {
                            System.out.printf("Consumer '%s' blocked for %dms\n",
                                    Thread.currentThread().getName(), end - begin);
                        }
                        begin = System.currentTimeMillis();
                        //message.acknowledge();
                        //producerConn.acknowledge();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}