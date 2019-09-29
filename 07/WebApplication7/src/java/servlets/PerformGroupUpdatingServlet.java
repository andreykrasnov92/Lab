package servlets;

import controller.JDBCController;
import shared.exceptions.DataUpdatingException;
import shared.exceptions.NonexistentEntityException;
import entities.Group;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import static shared.util.Utils.*;

@WebServlet(name = "PerformGroupUpdatingServlet", urlPatterns = {"/startperformgroupupdating.jsp"})
public class PerformGroupUpdatingServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        int groupId = Integer.parseInt((String) session.getAttribute("groupId"));
        JDBCController controller = JDBCController.getInstance();
        try {
            Group group = controller.startGroupUpdating(groupId);
            session.setAttribute("groupId", group.getGroupId());
            session.setAttribute("groupName", group.getGroupName());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/performgroupupdating.jsp");
            if (dispatcher != null) {
                dispatcher.forward(request, response);
            }
        } catch (DataUpdatingException | NonexistentEntityException | SQLException ex) {
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
