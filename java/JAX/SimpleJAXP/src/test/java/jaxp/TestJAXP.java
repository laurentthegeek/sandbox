package jaxp;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestJAXP extends Base {

    public static final String XSLT_URL = "/translate.xslt";
    public static final String XML_URL = "/catalog.xml";
    private static ByteArrayOutputStream buffer;

    @BeforeClass
    public static void setUpClass() {
        buffer = new ByteArrayOutputStream();
    }

    @Before
    public void setUp() {
        initPerf();
    }

    @After
    public void tearDown() {
        termPerf();
    }

    @Test
    public void testOutput() throws Exception {
        Source stylesheet = new StreamSource(getClass().getResourceAsStream(XSLT_URL));
        Source input = new StreamSource(getClass().getResourceAsStream(XML_URL));
        FileOutputStream file = new FileOutputStream("target/output.xml");
        Result output = new StreamResult(file);

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(stylesheet);
        transformer.transform(input, output);
        file.close();
    }

    @Test
    public void test_NoReuse() throws TransformerException {
        System.out.println("=> test_NoReuse");
        while (computePerf()) {
            Source stylesheet = new StreamSource(getClass().getResourceAsStream(XSLT_URL));
            Source input = new StreamSource(getClass().getResourceAsStream(XML_URL));
            Result output = new StreamResult(buffer);
            buffer.reset();

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(stylesheet);
            transformer.transform(input, output);
        }
    }

    @Test
    public void test_ReuseFactory() throws TransformerException {
        System.out.println("=> test_ReuseFactory");
        TransformerFactory factory = TransformerFactory.newInstance();
        while (computePerf()) {
            Source stylesheet = new StreamSource(getClass().getResourceAsStream(XSLT_URL));
            Source input = new StreamSource(getClass().getResourceAsStream(XML_URL));
            Result output = new StreamResult(buffer);
            buffer.reset();

            Transformer transformer = factory.newTransformer(stylesheet);
            transformer.transform(input, output);
        }
    }

    @Test
    public void test_ReuseTemplate() throws TransformerException {
        System.out.println("=> test_ReuseTemplate");
        TransformerFactory factory = TransformerFactory.newInstance();
        Source stylesheet = new StreamSource(getClass().getResourceAsStream(XSLT_URL));
        Templates template = factory.newTemplates(stylesheet); // Template are thread-safe

        while (computePerf()) {
            Source input = new StreamSource(getClass().getResourceAsStream(XML_URL));
            Result output = new StreamResult(buffer);
            buffer.reset();

            Transformer transformer = template.newTransformer();
            transformer.transform(input, output);
        }
    }

    @Test
    public void test_ReuseTransformer() throws TransformerException {
        System.out.println("=> test_ReuseTransformer");
        TransformerFactory factory = TransformerFactory.newInstance();
        Source stylesheet = new StreamSource(getClass().getResourceAsStream(XSLT_URL));
        Transformer transformer = factory.newTransformer(stylesheet);

        while (computePerf()) {
            Source input = new StreamSource(getClass().getResourceAsStream(XML_URL));
            Result output = new StreamResult(buffer);
            buffer.reset();

            transformer.transform(input, output);
        }
    }
}
