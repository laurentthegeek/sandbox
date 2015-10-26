package jpa.project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
@Access(AccessType.FIELD)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(length = 80, nullable = false)
    private String name;
    private int worktime;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    private List<Worker> workers;

    public String toString() {
        return String.format("Task[%d]: %s, %d, %d", id, name, worktime, workers.size());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Collection<Worker> getWorkers() {
        return workers;
    }

    public int getWorkTime() {
        return worktime;
    }

    public void setName(String description) {
        this.name = description;
    }

    public void setWorkTime(int worktime) {
        this.worktime = worktime;
    }

    public void setWorkers(List<Worker> workers) {
        this.workers = workers;
    }

    public void addWorker(Worker worker) {
        if (workers == null) {
            workers = new ArrayList(); // TODO check what happens if lazy loaded
        }

        workers.add(worker); // TODO check worker doesn't already exists before adding
    }
}
