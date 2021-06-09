/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import action.ActionCart;
import action.ActionCategory;
import action.ActionItems;
import entities.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import util.Const;

/**
 *
 * @author rdossant
 */
@WebServlet(name = "category", urlPatterns = {"/category"})
public class Category extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Category() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionItems.getItems(request, response);
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("user");
        int idAdmin = 1;
        //Si le paramètre category est présent
        ActionCategory.getCategoriesByOrder(request, response);
        String action = request.getParameter("action");
        if (request.getParameter("category_id") != null && user != null && user.getUserType() == 1) {
            try {
                int categoryId = Integer.parseInt(request.getParameter("category_id"));
                ActionCategory.getCategoryById(categoryId, request, response);
                request.getRequestDispatcher(Const.PATH_PAGE_EDIT_CATEGORY).forward(request, response);
            } catch (NumberFormatException nfe) {
                //do nothing
            }

        } else {

            if (user != null) {
                if (action != null && action.equals("delete") && user.getUserType() == idAdmin) {
                    doDelete(request, response);

                } else {
                    if (request.getParameter("listcategory") != null && user != null && user.getUserType() == idAdmin) {
                        ActionItems.getItems(request, response);
                        request.getRequestDispatcher(Const.PATH_PAGE_LIST_CATEGORIES).forward(request, response);
                    } else {
                        if (request.getParameter("op") != null && user != null && user.getUserType() == 1) {
                            request.getRequestDispatcher(Const.PATH_PAGE_NEW_CATEGORY).forward(request, response);
                        } else {
                            ActionItems.getItems(request, response);
                            request.getRequestDispatcher(Const.PATH_PAGE_ITEMS).forward(request, response);
                        }
                    }
                }
            } else {
                ActionItems.getItems(request, response);
                request.getRequestDispatcher(Const.PATH_PAGE_ITEMS).forward(request, response);
            }
        }
        //

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String strId = request.getParameter("itemId");
        String strQty = request.getParameter("qty");
        String action = request.getParameter("action");

        if (action != null) {
            switch (action) {
                case "editCategory":
                    doPut(request, response);
                    break;

            }

        } else {
            ActionCart.addItem(request, response, strId, strQty);
            doGet(request, response);
        }
        //
        if (action != null) {
            switch (action) {
                case "addCategory":
                    request.setCharacterEncoding("UTF-8");
                    if (ActionCategory.addCategory(request, response)) {
                        response.sendRedirect("category?listcategory=1&category=1");
                    } else {
                        request.getRequestDispatcher(Const.PATH_PAGE_NEW_CATEGORY).forward(request, response);
                    }
            }
        } else {
            doGet(request, response);
        }

    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        if (ActionCategory.editCategory(request, response)) {
            ActionItems.getItems(request, response);
            request.getRequestDispatcher(Const.PATH_PAGE_LIST_CATEGORIES).forward(request, response);

        } else {
            request.getRequestDispatcher(Const.PATH_PAGE_EDIT_CATEGORY).forward(request, response);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idCategory = Integer.parseInt(request.getParameter("category"));
        ActionItems.getItems(request, response);
        //Recuperer la session
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("user");
        int idAdmin = 1;
        ActionCategory.deleteCategoryById(idCategory, request, response);

        //Redirection
        if (user != null && user.getUserType() == idAdmin) {
            request.getRequestDispatcher(Const.PATH_PAGE_LIST_CATEGORIES).forward(request, response);
        } else {
            request.getRequestDispatcher(Const.PATH_PAGE_HOME).forward(request, response);
        }
    }
}
