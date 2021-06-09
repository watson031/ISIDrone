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
import action.ActionSignUp;
import entities.User;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Item
 */
@WebServlet(name = "item", urlPatterns = {"/item"})
public class Item extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Item() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String searchedValue = request.getParameter("itemSearch");
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("user");
        ActionCategory.getCategories(request, response);
        
        if (request.getParameter("product_id") != null && user != null && user.getUserType()== 1) {
            try {
                int productId = Integer.parseInt(request.getParameter("product_id"));
                ActionItems.getItemById(productId, request, response);
                request.getRequestDispatcher(Const.PATH_PAGE_EDIT_PRODUCT).forward(request, response);
            } catch (NumberFormatException nfe) {
                //do nothing
            }

        } else {
            if(request.getParameter("op") != null && user != null && user.getUserType()== 1){
                request.getRequestDispatcher(Const.PATH_PAGE_NEW_PRODUCT).forward(request, response);
            }else{
                if (searchedValue == null) {
                int item;
                try {
                    item = Integer.parseInt(request.getParameter("item"));
                } catch (NumberFormatException e) {
                    item = -1;
                }
                ActionItems.getItemById(item, request, response);
            } else {
                ActionItems.getItemsBySearchedValues(searchedValue, request, response);
            }

            request.getRequestDispatcher(Const.PATH_PAGE_ITEM).forward(request, response);
            }
            
        }

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strId = request.getParameter("itemId");
        String strQty = request.getParameter("qty");
        ActionCategory.getCategories(request, response);
        
        String action = request.getParameter("action");
        try {
            int productId = Integer.parseInt(request.getParameter("product_id"));
            ActionItems.getItemById(productId, request, response);
        } catch (NumberFormatException nfe) {
            //do nothing
        }
        if (action != null) {
            switch (action) {
                case "editProduct":
                    doPut(request, response);
                    break;
                case "addProduct":
                     request.setCharacterEncoding("UTF-8");
                      if (ActionItems.addItem(request, response)) {
                            response.sendRedirect("items?listproducts=1&category=1");
                        } else {
                            request.getRequestDispatcher(Const.PATH_PAGE_NEW_PRODUCT).forward(request, response);
                        }
            }

        } else {
            ActionCart.addItem(request, response, strId, strQty);
            doGet(request, response);
        }

    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (ActionItems.editItem(request, response)) {
            response.sendRedirect("items?listproducts=1&category=1");
        } else {
            request.getRequestDispatcher(Const.PATH_PAGE_EDIT_PRODUCT).forward(request, response);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doDelete(request, response); //To change body of generated methods, choose Tools | Templates.
        String selectedProductId = request.getParameter("itemId");
    }
        
}
