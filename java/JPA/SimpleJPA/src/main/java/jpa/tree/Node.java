package jpa.tree;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQueries({
    @NamedQuery(name=Node.FIND_ALL, query="select n from Node n"),
    @NamedQuery(name=Node.FIND_BY_NAME, query="select n from Node n where n.name = :" + Node.NAME)})
public class Node {

    public static final String FIND_ALL = "study.jpa.tree.Node.findAll";
    public static final String FIND_BY_NAME = "study.jpa.tree.Node.findByName";
    public static final String NAME = "name";

    @Id @GeneratedValue
    private Long id;
    private String name;
    @ManyToOne(fetch=FetchType.EAGER) @JoinColumn
    private Node parent;
    //@OneToMany(fetch=FetchType.EAGER, mappedBy = "parent"") @JoinColumn // EclipseLink
    @OneToMany(fetch=FetchType.LAZY, mappedBy = "parent") // Hibernate
    private List<Node> children;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }
}
