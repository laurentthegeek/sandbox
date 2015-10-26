/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package service;

import entity.Cat;
import java.util.Collection;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.persistence.EntityManager;
import converter.CatsConverter;
import converter.CatConverter;
import javax.persistence.PersistenceContext;
import javax.ejb.Stateless;

/**
 *
 * @author laurentthegeek
 */

@Path("/cats/")
@Stateless
public class CatsResource {
    @javax.ejb.EJB
    private CatResource catResource;
    @Context
    protected UriInfo uriInfo;
    @PersistenceContext(unitName = "mypu1")
    protected EntityManager em;
  
    /** Creates a new instance of CatsResource */
    public CatsResource() {
    }

    /**
     * Get method for retrieving a collection of Cat instance in XML format.
     *
     * @return an instance of CatsConverter
     */
    @GET
    @Produces({"application/xml", "application/json"})
    public CatsConverter get(@QueryParam("start")
                             @DefaultValue("0")
    int start, @QueryParam("max")
               @DefaultValue("10")
    int max, @QueryParam("expandLevel")
             @DefaultValue("1")
    int expandLevel, @QueryParam("query")
                     @DefaultValue("SELECT e FROM Cat e")
    String query) {
        return new CatsConverter(getEntities(start, max, query), uriInfo.getAbsolutePath(), expandLevel);
    }

    /**
     * Post method for creating an instance of Cat using XML as the input format.
     *
     * @param data an CatConverter entity that is deserialized from an XML stream
     * @return an instance of CatConverter
     */
    @POST
    @Consumes({"application/xml", "application/json"})
    public Response post(CatConverter data) {
        Cat entity = data.resolveEntity(em);
        createEntity(data.resolveEntity(em));
        return Response.created(uriInfo.getAbsolutePath().resolve(entity.getId() + "/")).build();
    }

    /**
     * Returns a dynamic instance of CatResource used for entity navigation.
     *
     * @return an instance of CatResource
     */
    @Path("{id}/")
    public CatResource getCatResource(@PathParam("id")
    Long id) {
        catResource.setId(id);
        catResource.setEm(em);
        return catResource;
    }

    /**
     * Returns all the entities associated with this resource.
     *
     * @return a collection of Cat instances
     */
    protected Collection<Cat> getEntities(int start, int max, String query) {
        return em.createQuery(query).setFirstResult(start).setMaxResults(max).getResultList();
    }

    /**
     * Persist the given entity.
     *
     * @param entity the entity to persist
     */
    protected void createEntity(Cat entity) {
        entity.setId(null);
        em.persist(entity);
    }
}
