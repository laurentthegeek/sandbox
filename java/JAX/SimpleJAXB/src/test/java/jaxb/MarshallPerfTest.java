package jaxb;

import com.laurent.simple.v2009_09.ListOfOrder;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

public class MarshallPerfTest extends Base {

    private static final int EXPECTED_SIZE = 1429;
    private static ListOfOrder listOfOrder;
    private static ByteArrayOutputStream buffer;

    @Before
    public void setUp() {
        initPerf();
    }

    @After
    public void tearDown() {
        termPerf();
    }

    @BeforeClass
    public static void setUpClass() throws JAXBException, SAXException {
        InputStream in = MarshallPerfTest.class.getResourceAsStream(XML_URL);
        listOfOrder = JAXB.unmarshal(in, ListOfOrder.class);
        buffer = new ByteArrayOutputStream();
    }

    // elapsed=10000ms, count=110, call=90.90909ms, tps=11.0
    @Test
    public void test_NoReuseWithValidation() throws Exception {
        System.out.println("=> test_NoReuseWithValidation");
        while (computePerf()) {
            SchemaFactory sf = newSchemaFactory();
            Schema schema = newSchema(sf);
            JAXBContext jcontext = JAXBContext.newInstance(ListOfOrder.class);
            Marshaller marshaller = jcontext.createMarshaller();
            marshaller.setSchema(schema);

            buffer.reset();
            marshaller.marshal(listOfOrder, buffer);
            assertEquals(EXPECTED_SIZE, buffer.size());
        }
    }

    // elapsed=10016ms, count=176, call=56.909092ms, tps=17.571884
    @Test
    public void test_ReuseFactoryWithValidation() throws Exception {
        System.out.println("=> test_ReuseFactoryWithValidation");
        SchemaFactory sf = newSchemaFactory();
        while (computePerf()) {
            Schema schema = newSchema(sf);
            JAXBContext jcontext = JAXBContext.newInstance(ListOfOrder.class);
            Marshaller marshaller = jcontext.createMarshaller();
            marshaller.setSchema(schema);

            buffer.reset();
            marshaller.marshal(listOfOrder, buffer);
            assertEquals(EXPECTED_SIZE, buffer.size());
        }
    }

    // elapsed=10000ms, count=517, call=19.34236ms, tps=51.7
    @Test
    public void test_ReuseSchemaWithValidation() throws Exception {
        System.out.println("=> test_ReuseSchemaWithValidation");
        SchemaFactory sf = newSchemaFactory();
        Schema schema = newSchema(sf);
        while (computePerf()) {
            JAXBContext jcontext = JAXBContext.newInstance(ListOfOrder.class);
            Marshaller marshaller = jcontext.createMarshaller();
            marshaller.setSchema(schema);

            buffer.reset();
            marshaller.marshal(listOfOrder, buffer);
            assertEquals(EXPECTED_SIZE, buffer.size());
        }
    }

    // elapsed=10000ms, count=36295, call=0.27552006ms, tps=3629.5
    @Test
    public void test_ReuseContextWithValidation() throws Exception {
        System.out.println("=> test_ReuseContextWithValidation");
        SchemaFactory sf = newSchemaFactory();
        Schema schema = newSchema(sf);
        JAXBContext jcontext = JAXBContext.newInstance(ListOfOrder.class);

        while (computePerf()) {
            Marshaller marshaller = jcontext.createMarshaller();
            marshaller.setSchema(schema);

            buffer.reset();
            marshaller.marshal(listOfOrder, buffer);
            assertEquals(EXPECTED_SIZE, buffer.size());
        }
    }

    // elapsed=10000ms, count=44599, call=0.22422028ms, tps=4459.9
    @Test
    public void test_ReuseMarshallerWithValidation() throws Exception {
        System.out.println("=> test_ReuseMarshallerWithValidation");
        SchemaFactory sf = newSchemaFactory();
        Schema schema = newSchema(sf);
        JAXBContext jcontext = JAXBContext.newInstance(ListOfOrder.class);
        Marshaller marshaller = jcontext.createMarshaller();
        marshaller.setSchema(schema);

        while (computePerf()) {
            buffer.reset();
            marshaller.marshal(listOfOrder, buffer);
            assertEquals(EXPECTED_SIZE, buffer.size());
        }
    }

    // elapsed=10015ms, count=666258, call=0.015031714ms, tps=66526.01
    @Test
    public void test_ReuseMarshallerNoValidation() throws Exception {
        System.out.println("=> test_ReuseMarshallerNoValidation");
        JAXBContext jcontext = JAXBContext.newInstance(ListOfOrder.class);
        Marshaller marshaller = jcontext.createMarshaller();
        marshaller.setSchema(null); // no validation (default)

        while (computePerf()) {
            buffer.reset();
            marshaller.marshal(listOfOrder, buffer);
            assertEquals(EXPECTED_SIZE, buffer.size());
        }
    }
}
