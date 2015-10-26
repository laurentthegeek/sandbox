package jpa.project;

import java.util.Collection;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
@Access(AccessType.FIELD)
public class Worker {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstname;
    private String lastname;
    @Embedded
    private Address address;
    @ManyToMany(mappedBy = "workers")
    private Collection<Task> tasks;

    @Override
    public String toString() {
        return String.format("Customer[%d]: %s %s, %s", id, firstname, lastname, address);
    }

    public Long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public Address getAddress() {
        return address;
    }

    public Collection<Task> getTasks() {
        return tasks;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setTasks(Collection<Task> tasks) {
        this.tasks = tasks;
    }
}
