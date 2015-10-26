package ejb;

import ejb.entity.Book;
import java.util.List;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class BookEJBTest {

    private static EJBContainer ec;
    private static Context ctx;

    @BeforeClass
    public static void initClass() {
        ec = EJBContainer.createEJBContainer();
        ctx = ec.getContext();
    }

    @AfterClass
    public static void termClass() throws Exception {
        ctx.close();
        ec.close();
    }

    @Test
    public void testBookEJB() throws Exception {

        BookEJB ejb = (BookEJB)ctx.lookup("java:global/BookEJB");

        Book book = new Book();
        book.setTitle("Mon titre 1");
        book.setPrice(22.2f);
        book.setDescription("Ma description 1");

        ejb.createBook(book);
        assertNotNull("Book ID should have been generated", book.getId());

        List<Book> books = ejb.findBooks();
        assertNotNull(books);
        assertTrue(books.size() == 1);
    }
}
