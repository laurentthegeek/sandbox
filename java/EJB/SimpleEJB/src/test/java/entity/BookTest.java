package entity;

import ejb.entity.Book;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class BookTest {

    private static EntityManagerFactory emf;
    private static EntityManager em;
    private EntityTransaction tx;

    public BookTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        emf = Persistence.createEntityManagerFactory("local-unit");
        em = emf.createEntityManager();

    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        em.close();
        emf.close();
    }

    @Before
    public void setUp() {
        tx = em.getTransaction();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void test1() {
        System.out.println("test1");

        Book book = new Book();
        book.setTitle("Mon titre 1");
        book.setPrice(22.2f);
        book.setDescription("Ma description 1");

        tx.begin();
        em.persist(book);
        tx.commit();

        assertNotNull("Book ID should have been generated", book.getId());

        List<Book> books = em.createNamedQuery("findAllBooks").getResultList();
        for (Book b : books)
            System.out.printf("Book/@id=%d\n", b.getId());
    }

}