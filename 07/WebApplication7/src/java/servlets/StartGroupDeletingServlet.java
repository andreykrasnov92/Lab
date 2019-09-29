package servlets;

import controller.JDBCController;
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
import static shared.util.XmlUtils.*;

@WebServlet(name = "StartGroupDeletingServlet", urlPatterns = {"/deletegroup.jsp"})
public class StartGroupDeletingServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        JDBCController controller = JDBCController.getInstance();
        try {
            String groups = convertXMLToString(getGroupsXmlElement(controller.readGroups()));
            session.setAttribute("groups", groups);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/startgroupdeleting.jsp");
            if (dispatcher != null) {
                dispatcher.forward(request, response);
            }
        } catch (SQLException ex) {
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
