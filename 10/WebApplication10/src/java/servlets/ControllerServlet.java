package servlets;

import beans.DAOFacadeBean;
import entities.Gruppa;
import entities.Student;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import static server.util.XmlUtils.*;
import shared.exceptions.DataUpdatingException;
import shared.exceptions.EmptyStringException;
import shared.exceptions.IllegalOrphanException;
import shared.exceptions.NonexistentEntityException;
import shared.exceptions.PreexistingEntityException;
import shared.exceptions.StringFormatException;
import shared.exceptions.WrongMessageException;
import static shared.util.Utils.*;

@WebServlet(name = "ControllerServlet", urlPatterns = {"/controller.jsp"})
public class ControllerServlet extends HttpServlet {

    @EJB
    private DAOFacadeBean ejbFacade;

    protected DAOFacadeBean getFacade() {
        return ejbFacade;
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        String message = request.getParameter("message");
        RequestDispatcher dispatcher = null;
        try {
            switch (message) {
                case "createGroup": {
                    /* !!! */
                    int groupId = validateGroupId(session.getAttribute("groupId").toString());
                    String groupName = validateGroupName(session.getAttribute("groupName").toString());
                    getFacade().createGroup(groupId, groupName);
                    dispatcher = request.getRequestDispatcher("/creategroupresult.jsp");
                    break;
                }
                case "updateGroup": {
                    /* !!! */
                    int groupId = validateGroupId(session.getAttribute("groupId").toString());
                    String groupName = validateGroupName(session.getAttribute("groupName").toString());
                    getFacade().updateGroup(groupId, groupName);
                    dispatcher = request.getRequestDispatcher("/updategroupresult.jsp");
                    break;
                }
                case "performGroupUpdating": {
                    int groupId = Integer.parseInt(session.getAttribute("groupId").toString());
                    Gruppa group = getFacade().startGroupUpdating(groupId);
                    session.setAttribute("groupId", group.getGroupid());
                    session.setAttribute("groupName", group.getGroupname());
                    /*String group = convertXmlElementToUtf(convertGroupToXmlElement(getFacade().startGroupUpdating(groupId)));
                     session.setAttribute("group", group);*/
                    dispatcher = request.getRequestDispatcher("/performgroupupdating.jsp");
                    break;
                }
                case "deleteGroup": {
                    /* !!! */
                    int groupId = Integer.parseInt(session.getAttribute("groupId").toString());
                    getFacade().deleteGroup(groupId);
                    dispatcher = request.getRequestDispatcher("/deletegroupresult.jsp");
                    break;
                }
                case "readGroups": {
                    dispatcher = showGroupsRequestDispatcher(request, "/readgroupsresult.jsp");
                    break;
                }
                case "startGroupUpdating": {
                    dispatcher = showGroupsRequestDispatcher(request, "/startgroupupdating.jsp");
                    break;
                }
                case "startGroupDeleting": {
                    dispatcher = showGroupsRequestDispatcher(request, "/startgroupdeleting.jsp");
                    break;
                }
                case "startGroupsFinding": {
                    dispatcher = showGroupsRequestDispatcher(request, "/startgroupsfinding.jsp");
                    break;
                }
                case "performGroupsFinding": {
                    /* !!! */
                    String groupName = session.getAttribute("groupName").toString();
                    String groups = convertXmlElementToUtf(convertGroupsToXmlElement(getFacade().readGroupsByName(groupName)));
                    session.setAttribute("groups", groups);
                    dispatcher = request.getRequestDispatcher("/findgroupsresult.jsp");
                    break;
                }
                case "cancelGroupUpdating": {
                    /* !!! */
                    int groupId = Integer.parseInt(session.getAttribute("groupId").toString());
                    getFacade().cancelGroupUpdating(groupId);
                    dispatcher = request.getRequestDispatcher("/index.jsp");
                    break;
                }
                case "createStudent": {
                    /* !!! */
                    int studentId = validateStudentId(session.getAttribute("studentId").toString());
                    String studentName = validateStudentName(session.getAttribute("studentName").toString());
                    int groupId = validateGroupId(session.getAttribute("groupId").toString());
                    getFacade().createStudent(studentId, studentName, groupId);
                    dispatcher = request.getRequestDispatcher("/createstudentresult.jsp");
                    break;
                }
                case "updateStudent": {
                    /* !!! */
                    int studentId = validateStudentId(session.getAttribute("studentId").toString());
                    String studentName = validateStudentName(session.getAttribute("studentName").toString());
                    int groupId = validateGroupId(session.getAttribute("groupId").toString());
                    getFacade().updateStudent(studentId, studentName, groupId);
                    dispatcher = request.getRequestDispatcher("/updatestudentresult.jsp");
                    break;
                }
                case "performStudentUpdating": {
                    /* !!! */
                    int studentId = Integer.parseInt(session.getAttribute("studentId").toString());
                    Student student = getFacade().startStudentUpdating(studentId);
                    session.setAttribute("studentId", student.getStudentid());
                    session.setAttribute("studentName", student.getStudentname());
                    session.setAttribute("groupId", student.getGroupid().getGroupid());
                    /*String student = convertXmlElementToUtf(convertStudentToXmlElement(getFacade().startStudentUpdating(studentId)));
                     session.setAttribute("student", student);*/
                    dispatcher = request.getRequestDispatcher("/performstudentupdating.jsp");
                    break;
                }
                case "deleteStudent": {
                    /* !!! */
                    int studentId = Integer.parseInt(session.getAttribute("studentId").toString());
                    getFacade().deleteStudent(studentId);
                    dispatcher = request.getRequestDispatcher("/deletestudentresult.jsp");
                    break;
                }
                case "readStudents": {
                    dispatcher = showStudentsRequestDispatcher(request, "/readstudentsresult.jsp");
                    break;
                }
                case "startStudentUpdating": {
                    dispatcher = showStudentsRequestDispatcher(request, "/startstudentupdating.jsp");
                    break;
                }
                case "startStudentDeleting": {
                    dispatcher = showStudentsRequestDispatcher(request, "/startstudentdeleting.jsp");
                    break;
                }
                case "startStudentsFinding": {
                    dispatcher = showStudentsRequestDispatcher(request, "/startstudentsfinding.jsp");
                    break;
                }
                case "performStudentsFinding": {
                    /* !!! */
                    String studentName = session.getAttribute("studentName").toString();
                    String students = convertXmlElementToUtf(convertStudentsToXmlElement(getFacade().readStudentsByName(studentName)));
                    session.setAttribute("students", students);
                    dispatcher = request.getRequestDispatcher("/findstudentsresult.jsp");
                    break;
                }
                case "cancelStudentUpdating": {
                    /* !!! */
                    int studentId = Integer.parseInt(session.getAttribute("studentId").toString());
                    getFacade().cancelStudentUpdating(studentId);
                    dispatcher = request.getRequestDispatcher("/index.jsp");
                    break;
                }
                default: {
                    throw new WrongMessageException("Wrong message!");
                }
            }
        } catch (DataUpdatingException | EmptyStringException |
                IllegalOrphanException | NonexistentEntityException |
                NumberFormatException | PreexistingEntityException |
                StringFormatException | WrongMessageException ex) {
            printException(ex);
            session.setAttribute("errorMessage", ex.getMessage());
            dispatcher = request.getRequestDispatcher("/errorMessage.jsp");
        }
        if (dispatcher != null) {
            dispatcher.forward(request, response);
        }
    }

    protected RequestDispatcher showGroupsRequestDispatcher(HttpServletRequest request, String path) {
        String groups = convertXmlElementToUtf(convertGroupsToXmlElement(getFacade().readGroups()));
        HttpSession session = request.getSession();
        session.setAttribute("groups", groups);
        return request.getRequestDispatcher(path);
    }

    protected RequestDispatcher showStudentsRequestDispatcher(HttpServletRequest request, String path) {
        String students = convertXmlElementToUtf(convertStudentsToXmlElement(getFacade().readStudents()));
        HttpSession session = request.getSession();
        session.setAttribute("students", students);
        return request.getRequestDispatcher(path);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
