package servlet;

import action.ActionItems;
import action.ActionOrder;
import entities.Order;
import entities.User;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import manager.MOrder;

import util.Const;

/**
 * Servlet implementation class Order
 */
@WebServlet(name = "order", urlPatterns = {"/order"})
public class ServletOrder extends HttpServlet {

    private static final int IS_NOT_SHIPPED = 0;
    private static final int IS_SHIPPED = 1;

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Recuperer la session

        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("user");
        int idAdmin = 1;
        String orders = request.getParameter("listOrders");
        String action = request.getParameter("action");
        ActionItems.getItems(request, response);
        if (user != null) {
            if (action != null && action.equals("ship")) {
                if (user.getUserType() == idAdmin) {
                    doShip(request, response);
                } else {

                    request.getRequestDispatcher(Const.PATH_PAGE_ITEMS).forward(request, response);
                }

            } else {
                if (action != null && action.equals("delete")) {
                    if (user.getUserType() == idAdmin) {
                        doDelete(request, response);
                    } else {
                        request.getRequestDispatcher(Const.PATH_PAGE_ITEMS).forward(request, response);
                    }

                } else {

                    if (orders != null) {
                        if (user != null && user.getUserType() == idAdmin) {

                            ActionOrder.getOrders(request, response);
                            request.getRequestDispatcher(Const.PATH_PAGE_LIST_ORDERS).forward(request, response);

                        } else {
                            request.getRequestDispatcher(Const.PATH_PAGE_HOME).forward(request, response);
                        }
                    } else {
                        request.getRequestDispatcher(Const.PATH_PAGE_INVOICE).forward(request, response);
                    }
                }
            }
        } else {

            request.getRequestDispatcher(Const.PATH_PAGE_ITEMS).forward(request, response);
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String btnEnCours = request.getParameter("btnEnCours");
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("user");
        int idAdmin = 1;
        if (user != null && user.getUserType() == idAdmin && btnEnCours != null) {
            String idOrderToUnShipped = request.getParameter("idOrderToUnShipped");
            MOrder.shipOrderById(Integer.parseInt(idOrderToUnShipped), IS_NOT_SHIPPED);
            ActionOrder.getOrders(request, response);
            request.getRequestDispatcher(Const.PATH_PAGE_LIST_ORDERS).forward(request, response);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idOrder = Integer.parseInt(request.getParameter("idOrder"));

        //Recuperer la session
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("user");
        int idAdmin = 1;

        ActionOrder.deleteOrderById(idOrder, request, response);
        //Redirection
        if (user != null && user.getUserType() == idAdmin) {
            request.getRequestDispatcher(Const.PATH_PAGE_LIST_ORDERS).forward(request, response);
        } else {
            request.getRequestDispatcher(Const.PATH_PAGE_HOME).forward(request, response);
        }
    }

    protected void doShip(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idOrder = Integer.parseInt(request.getParameter("idOrder"));

        //Recuperer la session
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("user");
        int idAdmin = 1;

        ActionOrder.shipOrderById(idOrder, IS_SHIPPED, request, response);
        //Redirection
        if (user != null && user.getUserType() == idAdmin) {
            request.getRequestDispatcher(Const.PATH_PAGE_LIST_ORDERS).forward(request, response);
        } else {
            request.getRequestDispatcher(Const.PATH_PAGE_HOME).forward(request, response);
        }
    }

}
