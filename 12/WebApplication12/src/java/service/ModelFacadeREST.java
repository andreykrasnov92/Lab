package service;

import beans.DAOFacadeBean;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import model.Model;
import shared.model.SharedModel;
import static server.util.DataMapper.*;

@Stateless
@Path("model")
public class ModelFacadeREST {

    @EJB
    private DAOFacadeBean ejbFacade;

    public ModelFacadeREST() {
    }

    protected DAOFacadeBean getFacade() {
        return ejbFacade;
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public SharedModel getModel() {
        return mapModel(new Model(getFacade().readGroups(), getFacade().readStudents()));
    }
}
