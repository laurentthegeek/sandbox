/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package converter;

import entity.Cat;
import java.net.URI;
import java.util.Collection;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAttribute;
import java.util.ArrayList;

/**
 *
 * @author laurentthegeek
 */

@XmlRootElement(name = "cats")
public class CatsConverter {
    private Collection<Cat> entities;
    private Collection<CatConverter> items;
    private URI uri;
    private int expandLevel;
  
    /** Creates a new instance of CatsConverter */
    public CatsConverter() {
    }

    /**
     * Creates a new instance of CatsConverter.
     *
     * @param entities associated entities
     * @param uri associated uri
     * @param expandLevel indicates the number of levels the entity graph should be expanded
     */
    public CatsConverter(Collection<Cat> entities, URI uri, int expandLevel) {
        this.entities = entities;
        this.uri = uri;
        this.expandLevel = expandLevel;
        getCat();
    }

    /**
     * Returns a collection of CatConverter.
     *
     * @return a collection of CatConverter
     */
    @XmlElement
    public Collection<CatConverter> getCat() {
        if (items == null) {
            items = new ArrayList<CatConverter>();
        }
        if (entities != null) {
            items.clear();
            for (Cat entity : entities) {
                items.add(new CatConverter(entity, uri, expandLevel, true));
            }
        }
        return items;
    }

    /**
     * Sets a collection of CatConverter.
     *
     * @param a collection of CatConverter to set
     */
    public void setCat(Collection<CatConverter> items) {
        this.items = items;
    }

    /**
     * Returns the URI associated with this converter.
     *
     * @return the uri
     */
    @XmlAttribute
    public URI getUri() {
        return uri;
    }

    /**
     * Returns a collection Cat entities.
     *
     * @return a collection of Cat entities
     */
    @XmlTransient
    public Collection<Cat> getEntities() {
        entities = new ArrayList<Cat>();
        if (items != null) {
            for (CatConverter item : items) {
                entities.add(item.getEntity());
            }
        }
        return entities;
    }
}
