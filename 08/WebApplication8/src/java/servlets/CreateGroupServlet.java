package servlets;

import beans.DAOBeanRemote;
import shared.exceptions.EmptyStringException;
import shared.exceptions.PreexistingEntityException;
import shared.exceptions.StringFormatException;
import java.io.IOException;
import java.sql.SQLException;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import static shared.util.Utils.*;

@WebServlet(name = "CreateGroupServlet", urlPatterns = {"/CreateGroupServlet"})
public class CreateGroupServlet extends HttpServlet {

    @EJB
    private DAOBeanRemote bean;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        try {
            int groupId = validateGroupId((String) session.getAttribute("groupId"));
            String groupName = validateGroupName((String) session.getAttribute("groupName"));
            bean.createGroup(groupId, groupName);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/creategroupresult.jsp");
            if (dispatcher != null) {
                dispatcher.forward(request, response);
            }
        } catch (EmptyStringException | PreexistingEntityException |
                SQLException | StringFormatException ex) {
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
