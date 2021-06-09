package servlet;

import action.ActionOrder;
import action.ActionCategory;
import action.ActionItems;
import action.ActionUsers;
import util.Const;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import javax.servlet.http.HttpSession;
import manager.MOrder;
import manager.MUser;

@WebServlet(name = "user", urlPatterns = {"/user"})
public class User extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String DESACTIVER = "DISACTIVATED";
    private static final String ACTIVER = "ACTIVATED";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public User() {
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String actionisActif = request.getParameter("actionisActif");
        String userSearch = request.getParameter("userSearch");
        HttpSession session = request.getSession(true);
        entities.User user = (entities.User) session.getAttribute("user");
        ActionCategory.getCategories(request, response);
        ActionItems.getItems(request, response);

        if (user != null) {
            if (actionisActif != null) {
                if (actionisActif.equals("actionDesactiver")) {
                    if (user.getUserType() == 1) {
                        deactivateClient(request, response);
                    } else {
                        request.getRequestDispatcher(Const.PATH_PAGE_ITEMS).forward(request, response);
                    }

                }
                if (actionisActif.equals("actionActif")) {
                    if (user.getUserType() == 1) {
                        activateClient(request, response);
                    } else {
                        request.getRequestDispatcher(Const.PATH_PAGE_ITEMS).forward(request, response);
                    }

                }

            } else {
                if (user.getUserType() == 1) {
                    if (request.getParameter("client_id") != null && user != null && user.getUserType() == 1) {
                        try {
                            int clientId = Integer.parseInt(request.getParameter("client_id"));
                            ActionUsers.getUserById(clientId, request, response);
                            request.getRequestDispatcher(Const.PATH_PAGE_EDIT_CLIENT).forward(request, response);
                        } catch (NumberFormatException nfe) {
                            //do nothing
                            request.getRequestDispatcher(Const.PATH_PAGE_LIST_CLIENTS).forward(request, response);
                        }

                    } else {
                        if (userSearch != null) {
                            ActionUsers.getClientsSearch(request, response, userSearch);
                        } else {
                            ActionUsers.getClients(request, response);
                        }

                        request.getRequestDispatcher(Const.PATH_PAGE_LIST_CLIENTS).forward(request, response);
                    }
                } else {
                    request.getRequestDispatcher(Const.PATH_PAGE_ITEMS).forward(request, response);
                }

            }
        } else {
            request.getRequestDispatcher(Const.PATH_PAGE_ITEMS).forward(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action != null && action.equals("editClient")) {
            doPut(request, response);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        try {
            int clientId = Integer.parseInt(request.getParameter("client_id"));
            ActionUsers.getUserById(clientId, request, response);
            if (ActionUsers.editClient(request, response)) {
                response.sendRedirect("user?listOrders=1");
            } else {
                request.getRequestDispatcher(Const.PATH_PAGE_EDIT_CLIENT).forward(request, response);
            }
        } catch (NumberFormatException nfe) {
            //do nothing
            request.getRequestDispatcher(Const.PATH_PAGE_LIST_CLIENTS).forward(request, response);
        }

    }

    protected void deactivateClient(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //Recuperer la session
        HttpSession session = request.getSession(true);
        entities.User user = (entities.User) session.getAttribute("user");

        int idUser = Integer.parseInt(request.getParameter("idUser"));
        //Redirection
        if (user != null && user.getUserType() == 1) {
            ActionUsers.setStatutClient(request, response, DESACTIVER, idUser,user.getId());
            ActionUsers.getClients(request, response);
            request.getRequestDispatcher(Const.PATH_PAGE_LIST_CLIENTS).forward(request, response);

        } else {
            request.getRequestDispatcher(Const.PATH_PAGE_HOME).forward(request, response);
        }
    }

    protected void activateClient(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //Recuperer la session
        HttpSession session = request.getSession(true);
        entities.User user = (entities.User) session.getAttribute("user");
        int idAdmin = 1;

        int idUser = Integer.parseInt(request.getParameter("idUser"));
        //Redirection
        if (user != null && user.getUserType() == idAdmin) {

            ActionUsers.setStatutClient(request, response, ACTIVER, idUser,user.getId());
            ActionUsers.getClients(request, response);
            request.getRequestDispatcher(Const.PATH_PAGE_LIST_CLIENTS).forward(request, response);
        } else {
            request.getRequestDispatcher(Const.PATH_PAGE_HOME).forward(request, response);
        }
    }
}
