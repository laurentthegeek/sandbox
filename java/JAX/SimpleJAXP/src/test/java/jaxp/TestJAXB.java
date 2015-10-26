package jaxp;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import laurentthegeek.catalog.Book;
import laurentthegeek.catalog.Catalog;
import laurentthegeek.catalogue.Catalogue;
import laurentthegeek.catalogue.Genre;
import laurentthegeek.catalogue.Livre;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

public class TestJAXB extends Base {

    public static final String[] XSD_URLS = {"/catalog.xsd", "/catalogue.xsd"};
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

    public static Schema newSchema(SchemaFactory sf, String[] urls) throws SAXException {
        Source[] sources = new Source[urls.length];
        for (int i = 0; i < urls.length; ++i) {
            sources[i] = new StreamSource(Base.class.getResourceAsStream(urls[i]));
        }
        return sf.newSchema(sources);
    }

    private void run(Schema schema) throws Exception {
        JAXBContext jcontext = JAXBContext.newInstance(Catalog.class, Catalogue.class);
        Unmarshaller unmarshaller = jcontext.createUnmarshaller();
        unmarshaller.setSchema(schema);
        Marshaller marshaller = jcontext.createMarshaller();
        marshaller.setSchema(schema);

        while (computePerf()) {
            InputStream in = getClass().getResourceAsStream(XML_URL);
            Catalog catalog = (Catalog) unmarshaller.unmarshal(in);

            Catalogue catalogue = new Catalogue();
            for (Book book : catalog.getBook()) {
                Livre livre = new Livre();

                livre.setAuteur(book.getAuthor());
                livre.setTitre(book.getTitle());
                livre.setGenre(toGenre(book.getGenre()));
                livre.setPrix(book.getPrice());
                livre.setDatePublication(book.getPublishDate());
                livre.setDescription(book.getDescription());

                catalogue.getLivre().add(livre);
            }

            buffer.reset();
            marshaller.marshal(catalogue, buffer);
        }
    }

    @Test
    public void test_JaxbUnmarshallConvertMarshallNoValidation() throws Exception {
        System.out.println("=> test_JaxbUnmarshallConvertMarshallNoValidation");
        run(null);
    }

    @Test
    public void test_JaxbUnmarshallConvertMarshallWithValidation() throws Exception {
        System.out.println("=> test_JaxbUnmarshallConvertMarshallWithValidation");
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        run(newSchema(sf, XSD_URLS));
    }

    private Genre toGenre(laurentthegeek.catalog.Genre genre) {
        switch (genre) {
            case COMPUTER:
                return Genre.INFORMATIQUE;
            case FANTASY:
                return Genre.FANTASTIQUE;
            case HORROR:
                return Genre.HORREUR;
            case ROMANCE:
                return Genre.ROMANCE;
            case SCIENCE_FICTION:
                return Genre.SCIENCE_FICTION;
        }
        return null;
    }
}
