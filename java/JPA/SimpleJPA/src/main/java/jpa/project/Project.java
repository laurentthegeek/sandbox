package jpa.project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
@Access(AccessType.FIELD)
public class Project {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    @ManyToOne(fetch=FetchType.EAGER) @JoinColumn
    private Customer customer;
    //@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL) @JoinColumn // EclipseLink
    @OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL) @JoinColumn // Hibernate
    private List<Task> tasks;

    @Override
    public String toString() {
        return String.format("Project[%d]: %s, %d, %s", id, title, tasks.size(), customer);
    }
    
    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Collection<Task> getTasks() {
        return tasks;
    }

    public String getTitle() {
        return title;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addTask(Task task) {
        if (tasks == null)
            tasks = new ArrayList(); // TODO check what happens if lazy loaded

        tasks.add(task); // check task doesn't already exists before adding
    }
}
