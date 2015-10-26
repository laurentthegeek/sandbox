package jpa.test;

import java.util.List;
import jpa.common.ISOCountryCode;
import jpa.common.Country;
import javax.persistence.Query;
import jpa.common.CountryRef;
import jpa.project.Address;
import jpa.project.Customer;
import org.junit.Test;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

public class QueryTest extends TestBase {

    @BeforeClass
    public static void setUpClass() {

        TestBase.setUpClass();

        QueryTest test = new QueryTest();
        test.setUp();
        test.setUpDatabase();
        test.tearDown();
    }

    private void setUpDatabase() {
        tx.begin();

        Country fra = new Country();
        fra.setName("France");
        fra.setCountryCode(ISOCountryCode.FRA);
        em.persist(fra);

        Country usa = new Country();
        usa.setName("United State of America");
        usa.setCountryCode(ISOCountryCode.USA);
        em.persist(usa);

        Address malakoff = new Address();
        malakoff.setStreet("156 avenue Malakoff");
        malakoff.setCity("Paris");
        malakoff.setCountryCode(fra);

        Address slade = new Address();
        malakoff.setStreet("105 slade street");
        malakoff.setCity("Belmont");
        malakoff.setCountryCode(usa);

        Customer jean = new Customer();
        jean.setFirstname("Jean");
        jean.setLastname("Dupont");
        jean.setAddress(malakoff);
        em.persist(jean);
        assertNotNull(jean.getId());

        Customer john = new Customer();
        john.setFirstname("John");
        john.setLastname("Doe");
        john.setAddress(slade);
        em.persist(john);
        assertNotNull(john.getId());

        CountryRef cr1 = new CountryRef();
        cr1.setCountry(fra);
        em.persist(cr1);

        CountryRef cr2 = new CountryRef();
        cr2.setCountry(usa);
        em.persist(cr2);

        tx.commit();
    }

    @Test
    public void testNamedQueries() {
        Query q = em.createNamedQuery(Customer.FIND_ALL);
        List<Customer> customers = q.getResultList();
        assertEquals(2, customers.size());

        q = em.createNamedQuery(Customer.FIND_BY_FIRSTNAME);
        q.setParameter(Customer.FIRSTNAME, "Jean");
        customers = q.getResultList();
        assertEquals(1, customers.size());
    }

    @Test
    public void testQuerySimpleResult() {
        Query query = em.createQuery("select c.countryCode from Country c");
        List<ISOCountryCode> codes = query.getResultList();
        assertEquals(2, codes.size());

        for (ISOCountryCode code : codes) {
            System.out.printf("code=%s\n", code);
        }
    }

    @Test
    public void testQueryCompoundResult() {
        Query query = em.createQuery("select c.countryCode, c.name from Country c");
        List<Object[]> arrays = query.getResultList();
        assertEquals(2, arrays.size());

        for (Object[] array : arrays) {
            System.out.printf("code=%s, name=%s\n", array[0], array[1]);
        }
    }

    private static class CodeNameDTO {

        public final ISOCountryCode code;
        public final String name;

        public CodeNameDTO(ISOCountryCode code, String name) {
            this.code = code;
            this.name = name;
        }
    }

    @Test
    public void testQueryDTOResult() {
        Query query = em.createQuery(
                "select new jpa.test.QueryTest.CodeNameDTO(c.countryCode, c.name)" // EclipseLink
                //"select new jpa.common.CodeNameDTO(c.countryCode, c.name)" // Hibernate
                + " from Country c");
        List<CodeNameDTO> codenames = query.getResultList();
        assertEquals(2, codenames.size());

        for (CodeNameDTO codename : codenames) {
            System.out.printf("code=%s, name=%s\n", codename.code, codename.name);
        }
    }

    @Test
    public void testJoinQuery() {
        Query query = em.createQuery(
                "select cu.firstname, cu.lastname, co.countryCode"
                + " from Customer cu, Country co");

        List list = query.getResultList();
        assertEquals(4, list.size());

        query = em.createQuery(
                "select cr.country.countryCode from CountryRef cr");
        list = query.getResultList();
        assertEquals(2, list.size());

        query = em.createQuery(
                "select cr, co from CountryRef cr, Country co where cr.country = co");
        list = query.getResultList();
        assertEquals(2, list.size());

        query = em.createQuery(
                "select cr, co from CountryRef cr inner join cr.country co");
        list = query.getResultList();
        assertEquals(2, list.size());
    }

    @Test
    public void testInnerJoinQuery() {
        Query query = em.createQuery(
                "select cu.firstname, cu.lastname, co.countryCode"
                + " from Customer cu inner join cu.address.country co");

        List<Object[]> list = query.getResultList();
        assertEquals(1, list.size());

        for (Object[] a : list) {
            System.out.printf("name=%s %s, country=%s\n", a[0], a[1], a[2]);
        }
    }
}
