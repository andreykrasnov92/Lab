package servlets;

import beans.DAOBeanRemote;
import shared.exceptions.DataUpdatingException;
import shared.exceptions.NonexistentEntityException;
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
import static shared.util.Utils.*;
import static shared.util.XmlUtils.*;

@WebServlet(name = "PerformStudentUpdatingServlet", urlPatterns = {"/startperformstudentupdating.jsp"})
public class PerformStudentUpdatingServlet extends HttpServlet {

    @EJB
    private DAOBeanRemote bean;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        int studentId = Integer.parseInt((String) session.getAttribute("studentId"));
        try {
            Student student = bean.startStudentUpdating(studentId);
            session.setAttribute("studentId", student.getStudentid());
            session.setAttribute("studentName", student.getStudentname());
            session.setAttribute("groupId", student.getGroupid().getGroupid());
            //String student = convertXMLToString(getStudentXmlElement(bean.startStudentUpdating(studentId)));
            RequestDispatcher dispatcher = request.getRequestDispatcher("/performstudentupdating.jsp");
            if (dispatcher != null) {
                dispatcher.forward(request, response);
            }
        } catch (DataUpdatingException | NonexistentEntityException ex) {
            printException(ex);
            session.setAttribute("errorMessage", ex.getLocalizedMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            if (dispatcher != null) {
                dispatcher.forward(request, response);
            }
        } catch (Exception ex) {
            printException(ex);
            session.setAttribute("errorMessage", ex.getLocalizedMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            if (dispatcher != null) {
                dispatcher.forward(request, response);
            }
        }
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
