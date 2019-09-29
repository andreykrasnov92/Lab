package service;

import beans.DAOFacadeBean;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import shared.entities.SharedGroup;
import shared.model.SharedGroups;
import server.util.DataMapper;

@Stateless
@Path("entities.gruppa")
public class GruppaFacadeREST {

    @EJB
    private DAOFacadeBean ejbFacade;

    public GruppaFacadeREST() {
    }

    protected DAOFacadeBean getFacade() {
        return ejbFacade;
    }

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public void create(SharedGroup sharedGroup) {
        getFacade().createGroup(DataMapper.unmapSharedGroup(sharedGroup));
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_XML)
    public void edit(@PathParam("id") Integer id, SharedGroup sharedGroup) {
        getFacade().updateGroup(DataMapper.unmapSharedGroup(sharedGroup));
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        getFacade().deleteGroup(getFacade().readGroup(id));
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_XML)
    public SharedGroup find(@PathParam("id") Integer id) {
        return DataMapper.mapGroup(getFacade().readGroup(id));
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public SharedGroups findAll() {
        return DataMapper.mapGroups(getFacade().readGroups());
    }

    @GET
    @Path("{from}/{to}")
    @Produces(MediaType.APPLICATION_XML)
    public SharedGroups findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return DataMapper.mapGroups(getFacade().readGroupsByRange(new int[]{from, to}));
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(getFacade().readGroupsCount());
    }
}
