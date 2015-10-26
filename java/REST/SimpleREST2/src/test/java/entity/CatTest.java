package entity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CatTest {

    private static EntityManagerFactory emf;
    private static EntityManager em;
    private EntityTransaction tx;

    @BeforeClass
    public static void setUpClass() throws Exception {
        emf = Persistence.createEntityManagerFactory("mypu1");
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
        if (tx.isActive()) {
            tx.rollback();
        }
    }

    @Test
    public void testMyEntity() {
        System.out.println("testMyEntity");

        tx.begin();

        Cat c = new Cat();
        c.setName("zebulon");

        em.persist(c);

        tx.commit();
    }
}
