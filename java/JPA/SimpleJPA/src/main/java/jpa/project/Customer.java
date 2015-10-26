package jpa.project;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@Access(AccessType.FIELD)
@NamedQueries({
    @NamedQuery(name = Customer.FIND_ALL,
    query = "select c from Customer c"),
    @NamedQuery(name = Customer.FIND_BY_FIRSTNAME,
    query = "select c from Customer c where c.firstname = :" + Customer.FIRSTNAME)})
public class Customer {

    // Query and parameters names
    public static final String FIND_ALL = "jpa.project.Customer.FIND_ALL";
    public static final String FIND_BY_FIRSTNAME = "jpa.project.Customer.FIND_BY_FIRSTNAME";
    public static final String FIRSTNAME = "firstname";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstname;
    private String lastname;
    @Embedded
    private Address address;

    @Override
    public String toString() {
        return String.format("Customer[%d]: %s, %s, %s", id, firstname, lastname, address);
    }

    public Long getId() {
        return id;
    }

    public Address getAddress() {
        return address;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
