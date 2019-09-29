package service;

import beans.DAOFacadeBean;
import entities.Student;
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
import shared.entities.SharedStudent;
import shared.model.SharedStudents;
import server.util.DataMapper;

@Stateless
@Path("entities.student")
public class StudentFacadeREST {

    @EJB
    private DAOFacadeBean ejbFacade;

    @EJB
    private GruppaFacadeREST groupFacade;

    public StudentFacadeREST() {
    }

    protected DAOFacadeBean getFacade() {
        return ejbFacade;
    }

    protected GruppaFacadeREST getGroupFacade() {
        return groupFacade;
    }

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public void create(SharedStudent sharedStudent) {
        Student student = DataMapper.unmapSharedStudent(sharedStudent);
        student.setGroupid(DataMapper.unmapSharedGroup(getGroupFacade().find(sharedStudent.getGroupId())));
        getFacade().createStudent(student);
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_XML)
    public void edit(@PathParam("id") Integer id, SharedStudent sharedStudent) {
        Student student = DataMapper.unmapSharedStudent(sharedStudent);
        student.setGroupid(DataMapper.unmapSharedGroup(getGroupFacade().find(sharedStudent.getGroupId())));
        getFacade().updateStudent(student);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        getFacade().deleteStudent(getFacade().readStudent(id));
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_XML)
    public SharedStudent find(@PathParam("id") Integer id) {
        return DataMapper.mapStudent(getFacade().readStudent(id));
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public SharedStudents findAll() {
        return DataMapper.mapStudents(getFacade().readStudents());
    }

    @GET
    @Path("{from}/{to}")
    @Produces(MediaType.APPLICATION_XML)
    public SharedStudents findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return DataMapper.mapStudents(getFacade().readStudentsByRange(new int[]{from, to}));
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(getFacade().readStudentsCount());
    }
}
