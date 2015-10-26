package jpa.tree;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class Root extends Node {

    @Override
    public void setParent(Node parent) {
        throw new UnsupportedOperationException("cannot set a parent to the root node");
    }
}
