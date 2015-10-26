package jpa.test;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import jpa.common.Country;
import jpa.common.ISOCountryCode;
import jpa.project.Address;
import jpa.project.Project;
import jpa.project.Customer;
import jpa.project.Task;
import jpa.project.Worker;
import org.junit.Test;
import jpa.tree.Leaf;
import jpa.tree.Node;
import jpa.tree.Root;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

public class EntityManagerTest extends TestBase {

    @BeforeClass
    public static void setUpClass() {

        TestBase.setUpClass();

        EntityManagerTest test = new EntityManagerTest();
        test.setUp();
        test.addCountry(ISOCountryCode.FRA, "France");
        test.addCountry(ISOCountryCode.USA, "United State of America");
        test.tearDown();
    }

    protected Country addCountry(ISOCountryCode code, String name) {
        Country c = new Country();
        c.setCountryCode(code);
        c.setName(name);
        em.persist(c);

        return c;
    }

    protected Customer addCustomer() {
        Address a = new Address();
        a.setStreet("156 avenue Malakoff");
        a.setCity("Paris");
        a.setCountryCode(em.getReference(Country.class, ISOCountryCode.FRA));

        Customer c = new Customer();
        c.setFirstname("John");
        c.setLastname("Customer");
        c.setAddress(a);
        em.persist(c);
        assertNotNull(c.getId());

        return c;
    }

    protected Project addProject() {
        Project p = new Project();
        p.setTitle("Project 1");
        em.persist(p);
        assertNotNull(p.getId());
        assertNotNull(p.getTasks());

        return p;
    }

    protected Task addTask() {
        Task t = new Task();
        t.setName("Task 1");
        t.setWorkTime(10);
        em.persist(t);
        assertNotNull(t.getId());
        assertNull(t.getWorkers()); // TODO check whether the EM shouldn't have initialized this list?

        return t;
    }

    protected Worker addWorker() {
        Address a = new Address();
        a.setStreet("111 powndermill rd");
        a.setCity("Maynard");
        a.setCountryCode(em.getReference(Country.class, ISOCountryCode.USA));

        Worker w = new Worker();
        w.setFirstname("John");
        w.setLastname("Doe");
        w.setAddress(a);
        em.persist(w);

        return w;
    }

    @Test
    public void testEntities() {
        System.out.println("testEntities");

        tx.begin();

        addCustomer();
        addProject();
        addTask();
        addWorker();

        tx.commit();
    }

    @Test
    public void testRelationships() {
        System.out.println("testRelationships");

        tx.begin();

        final int MAX = 3;

        Customer[] customers = new Customer[MAX];
        for (int i = 0; i < MAX; ++i) {
            customers[i] = addCustomer();
        }

        Worker[] workers = new Worker[MAX];
        for (int i = 0; i < MAX; ++i) {
            workers[i] = addWorker();
        }

        for (int i = 0; i < MAX; ++i) {
            Project p = new Project();
            p.setTitle("Project " + i);
            p.setCustomer(customers[i]);
            em.persist(p); // TODO should it be done first or last ?

            for (int j = 0; j < MAX; ++j) {
                Task t = new Task();
                t.setName(String.format("Task %d/%d", i, j));
                t.setWorkTime(i * j); // some dumb value
                t.addWorker(workers[j]);
                p.addTask(t);
            }
        }

        tx.commit();
    }

    @Test
    public void testInheritance() {
        System.out.println("testInheritance");

        tx.begin();

        Root root = new Root();
        em.persist(root);
        root.setName("Root");

        final int NODE_MAX = 10;
        final int LEAF_MAX = 10;

        List<Node> branches = new ArrayList();
        for (int i = 1; i <= NODE_MAX; ++i) {

            Node b = new Node();
            em.persist(b);
            b.setName("Node " + i);
            b.setParent(root);

            List<Node> leafs = new ArrayList(); // Java compiler error if List<Leaf>
            for (int j = 1; j <= LEAF_MAX; ++j) {

                Leaf l = new Leaf();
                em.persist(l);
                l.setName(String.format("Leaf %d/%d", i, j));
                l.setParent(b);
                leafs.add(l);
            }

            b.setChildren(leafs);
            branches.add(b);
        }

        root.setChildren(branches);

        tx.commit();
    }

    private int countNodes(Node node) {
        int count = 1; // this node

        // plus any children
        if (node.getChildren() != null) {
            for (Node n : node.getChildren()) {
                count += countNodes(n);
            }
        }

        return count;
    }

    @Test
    public void testToDownNavigation() {
        System.out.println("testToDownNavigation");

        Query q = em.createNamedQuery(Node.FIND_BY_NAME);
        q.setParameter(Node.NAME, "Root");
        Root root = (Root) q.getSingleResult();
        assertEquals(111, countNodes(root));
    }
}
