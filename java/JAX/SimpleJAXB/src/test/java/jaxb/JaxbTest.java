package jaxb;

import com.laurent.simple.v2009_09.ListOfOrder;
import java.io.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.SAXException;

public class JaxbTest extends Base {

    // JAXB thread safety is not standardize. In the case of Sun JAXB RI
    // (https://jaxb.dev.java.net/faq/index.html)
    // - JAXBContext is thread-safe
    // - Unmarshaller is not thread-safe
    // - SchemaFactory is not thread-safe
    // - Schema is thread-safe and should be shared amongs many threads
    private static JAXBContext jcontext;
    private static Schema schema;
    private Marshaller marshaller;
    private Unmarshaller unmarshaller;
    private ListOfOrder listOfOrder;

    @BeforeClass
    public static void setUpClass() throws JAXBException, SAXException {
        SchemaFactory sf = newSchemaFactory();
        schema = newSchema(sf);
        jcontext = JAXBContext.newInstance(ListOfOrder.class);
    }

    @Before
    public void setUp() throws JAXBException {
        unmarshaller = jcontext.createUnmarshaller();
        unmarshaller.setSchema(schema);

        marshaller = jcontext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false); // default
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8"); // default
        marshaller.setSchema(schema);
    }

    @Test
    public void testUnmarshallMarshall() throws JAXBException, IOException {
        InputStream isrc = getClass().getResourceAsStream(XML_URL);
        listOfOrder = (ListOfOrder) unmarshaller.unmarshal(isrc);

        ByteArrayOutputStream copy = new ByteArrayOutputStream();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(listOfOrder, copy);
        System.out.printf("--- BEGIN XML ---\n%s--- END XML ---\n", copy);
        IOUtils.write(copy.toByteArray(), new FileOutputStream("target/copy.xml"));

        isrc = getClass().getResourceAsStream(XML_URL);
        InputStream icopy = new ByteArrayInputStream(copy.toByteArray());
        assertTrue(IOUtils.contentEquals(isrc, icopy));
    }
}
