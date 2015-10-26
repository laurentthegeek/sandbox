package hornetq;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientSession;
import org.hornetq.api.core.client.ClientSessionFactory;
import org.hornetq.api.core.client.HornetQClient;
import org.hornetq.core.remoting.impl.invm.InVMConnectorFactory;
import org.hornetq.core.server.embedded.EmbeddedHornetQ;

public class Main {

    public static void main(String[] args) throws Exception {


        EmbeddedHornetQ embedded = new EmbeddedHornetQ();
        embedded.start();

        ClientSessionFactory factory = HornetQClient.createClientSessionFactory(
                new TransportConfiguration(
                InVMConnectorFactory.class.getName()));

        ClientSession session = factory.createSession();

        session.createQueue("example", "example", true);

        ClientProducer producer = session.createProducer("example");

        ClientMessage message = session.createMessage(true);

        message.getBody().writeString("Hello");

        producer.send(message);

        session.start();

        ClientConsumer consumer = session.createConsumer("example");

        ClientMessage msgReceived = consumer.receive();

        System.out.println("message = " + msgReceived.getBody().readString());

        session.close();
    }
}
