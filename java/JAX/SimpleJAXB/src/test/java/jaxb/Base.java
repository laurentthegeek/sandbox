package jaxb;

import java.io.InputStream;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;

public class Base {

    public static final String[] XSD_URLS = {
        // Order of XSD declaration is significant (import bottom-up)
        "/xsd/xmldsig-core-schema.xsd",
        "/xsd/PurchaseOrder.xsd"
        //        "/xsd/ISOCurrencyCode.xsd", // include must be resolved with a resolver
        //        "/xsd/ISOCountryCode.xsd", // include must be resolved with a resolver
    };
    public static final String XML_URL = "/xml/purchaseOrder.xml";
    private static final long ELAPSE_TIME = 10 * 1000; // 10s
    private long begin;
    private long count;

    public static synchronized SchemaFactory newSchemaFactory() {
        LSResourceResolver resolver = new LSResourceResolver() {

            @Override
            public LSInput resolveResource(String type, String namespaceURI,
                    String publicId, String systemId, String baseURI) {

                if (systemId.equals("ISOCurrencyCode.xsd") || systemId.equals("ISOCountryCode.xsd")) {
                    InputStream in = Base.class.getResourceAsStream(String.format("/xsd/%s", systemId));
                    return new LSInputStream(publicId, systemId, in);
                } else {
                    return null;
                }
            }
        };

        synchronized (SchemaFactory.class) {
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            sf.setResourceResolver(resolver);
            return sf;
        }
    }

    public static Schema newSchema(SchemaFactory sf) throws SAXException {
        Source[] sources = new Source[XSD_URLS.length];
        for (int i = 0; i < XSD_URLS.length; ++i) {
            sources[i] = new StreamSource(Base.class.getResourceAsStream(XSD_URLS[i]));
        }

        synchronized (sf) {
            return sf.newSchema(sources);
        }
    }

    public void initPerf() {
        begin = System.currentTimeMillis();
        count = 0;
    }

    public void termPerf() {
        long elapsed = System.currentTimeMillis() - begin;
        System.out.printf("elapsed=%dms, count=%d, call=%sms, tps=%s\n",
                elapsed, count, (float) elapsed / count, (float) count * 1000 / elapsed);
        begin = System.currentTimeMillis();
        count = 0;
    }

    public boolean computePerf() {
        ++count;
        return System.currentTimeMillis() - begin < ELAPSE_TIME;
    }
}
