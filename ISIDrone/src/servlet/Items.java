package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.Const;
import action.ActionCart;
import action.ActionCategory;
import action.ActionItems;
import entities.User;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Products
 */
@WebServlet(name = "products", urlPatterns = {"/items"})
public class Items extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Items() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Recuperer les params
        String action = request.getParameter("action");
        String searchedValue = request.getParameter("itemSearch");

        //Recuperer la session
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("user");
        int idAdmin = 1;

        if (user != null) {
            if (action != null && action.equals("delete") && user.getUserType() == idAdmin) {
                doDelete(request, response);

            } else {
                //Si le paramètre category est présent

                ActionItems.getItems(request, response);

                if (searchedValue != null) {
                    ActionItems.getItemsBySearchedValues(searchedValue, request, response);
                }

                //Redirection
                if (request.getParameter("listproducts") != null && user != null && user.getUserType() == idAdmin) {
                    ActionCategory.getCategories(request, response);
                    request.getRequestDispatcher(Const.PATH_PAGE_LIST_PRODUCTS).forward(request, response);
                } else {
                    ActionCategory.getClientCategories(request, response);
                    request.getRequestDispatcher(Const.PATH_PAGE_ITEMS).forward(request, response);
                }
            }
        } else {
            ActionItems.getItems(request, response);
            ActionCategory.getClientCategories(request, response);
            request.getRequestDispatcher(Const.PATH_PAGE_ITEMS).forward(request, response);
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strId = request.getParameter("itemId");
        String strQty = request.getParameter("qty");

        ActionCart.addItem(request, response, strId, strQty);

        doGet(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idProduct = Integer.parseInt(request.getParameter("product"));

        //Recuperer la session
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("user");
        int idAdmin = 1;

        //Si le paramètre category est présent
        ActionCategory.getCategories(request, response);
        ActionItems.deleteItemById(idProduct, request, response);

        //Redirection
        if (user != null && user.getUserType() == idAdmin) {
            request.getRequestDispatcher(Const.PATH_PAGE_LIST_PRODUCTS).forward(request, response);
        } else {
            request.getRequestDispatcher(Const.PATH_PAGE_ITEMS).forward(request, response);
        }

    }

}
