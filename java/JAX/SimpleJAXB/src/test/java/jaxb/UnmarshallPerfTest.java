package jaxb;

import com.laurent.simple.v2009_09.ListOfOrder;
import java.io.InputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.junit.After;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

public class UnmarshallPerfTest extends Base {

    @Before
    public void setUp() {
        initPerf();
    }

    @After
    public void tearDown() {
        termPerf();
    }

    // elapsed=10031ms, count=220, call=45.595455ms, tps=21.93201
    @Test
    public void test_ReuseNoneWithValidation() throws Exception {
        System.out.println("=> test_ReuseNoneWithValidation");
        while (computePerf()) {
            SchemaFactory sf = newSchemaFactory();
            Schema schema = newSchema(sf);
            JAXBContext jcontext = JAXBContext.newInstance(ListOfOrder.class);
            Unmarshaller unmarshaller = jcontext.createUnmarshaller();
            unmarshaller.setSchema(schema);

            InputStream in = getClass().getResourceAsStream(XML_URL);
            ListOfOrder listOfOrder = (ListOfOrder) unmarshaller.unmarshal(in);
            assertNotNull(listOfOrder);
        }
    }

    // elapsed=10031ms, count=256, call=39.183594ms, tps=25.520885
    @Test
    public void test_ReuseFactoryWithValidation() throws Exception {
        System.out.println("=> test_ReuseFactoryWithValidation");
        SchemaFactory sf = newSchemaFactory();
        while (computePerf()) {
            Schema schema = newSchema(sf);
            JAXBContext jcontext = JAXBContext.newInstance(ListOfOrder.class);
            Unmarshaller unmarshaller = jcontext.createUnmarshaller();
            unmarshaller.setSchema(schema);

            InputStream in = getClass().getResourceAsStream(XML_URL);
            ListOfOrder listOfOrder = (ListOfOrder) unmarshaller.unmarshal(in);
            assertNotNull(listOfOrder);
        }
    }

    // elapsed=10000ms, count=512, call=19.53125ms, tps=51.2
    @Test
    public void test_ReuseSchemaWithValidation() throws Exception {
        System.out.println("=> test_ReuseSchemaWithValidation");
        SchemaFactory sf = newSchemaFactory();
        Schema schema = newSchema(sf);

        while (computePerf()) {
            JAXBContext jcontext = JAXBContext.newInstance(ListOfOrder.class);
            Unmarshaller unmarshaller = jcontext.createUnmarshaller();
            unmarshaller.setSchema(schema);

            InputStream in = getClass().getResourceAsStream(XML_URL);
            ListOfOrder listOfOrder = (ListOfOrder) unmarshaller.unmarshal(in);
            assertNotNull(listOfOrder);
        }
    }

    // elapsed=10000ms, count=14441, call=0.6924728ms, tps=1444.1
    @Test
    public void test_ReuseContextWithValidation() throws Exception {
        System.out.println("=> test_ReuseContextWithValidation");
        SchemaFactory sf = newSchemaFactory();
        Schema schema = newSchema(sf);
        JAXBContext jcontext = JAXBContext.newInstance(ListOfOrder.class);

        while (computePerf()) {
            Unmarshaller unmarshaller = jcontext.createUnmarshaller();
            unmarshaller.setSchema(schema);

            InputStream in = getClass().getResourceAsStream(XML_URL);
            ListOfOrder listOfOrder = (ListOfOrder) unmarshaller.unmarshal(in);
            assertNotNull(listOfOrder);
        }
    }

    // elapsed=10015ms, count=23826, call=0.42033914ms, tps=2379.0315
    @Test
    public void test_ReuseUnmarshallerWithValidation() throws Exception {
        System.out.println("=> test_ReuseUnmarshallerWithValidation");
        SchemaFactory sf = newSchemaFactory();
        Schema schema = newSchema(sf);
        JAXBContext jcontext = JAXBContext.newInstance(ListOfOrder.class);
        Unmarshaller unmarshaller = jcontext.createUnmarshaller();
        unmarshaller.setSchema(schema);

        while (computePerf()) {
            InputStream in = getClass().getResourceAsStream(XML_URL);
            ListOfOrder listOfOrder = (ListOfOrder) unmarshaller.unmarshal(in);
            assertNotNull(listOfOrder);
        }
    }

    // elapsed=10000ms, count=50264, call=0.19894955ms, tps=5026.4
    @Test
    public void test_ReuseUnmarshallerNoValidation() throws Exception {
        System.out.println("=> test_ReuseUnmarshallerNoValidation");
        JAXBContext jcontext = JAXBContext.newInstance(ListOfOrder.class);
        Unmarshaller unmarshaller = jcontext.createUnmarshaller();
        unmarshaller.setSchema(null); // no validation (default)

        while (computePerf()) {
            InputStream in = getClass().getResourceAsStream(XML_URL);
            ListOfOrder listOfOrder = (ListOfOrder) unmarshaller.unmarshal(in);
            assertNotNull(listOfOrder);
        }
    }
}
