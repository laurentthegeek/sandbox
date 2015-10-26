package jpa.tree;

import java.util.List;
import javax.persistence.Entity;

@Entity
public class Leaf extends Node {

    @Override
    public void setChildren(List<Node> children) {
        throw new UnsupportedOperationException("cannot set children to a leaf node");
    }
}
