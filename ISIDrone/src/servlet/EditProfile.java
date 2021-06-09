/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import action.ActionEditProfile;
import action.ActionUsers;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import manager.MSession;
import util.Const;

/**
 *
 * @author rdossant
 */
@WebServlet(name = "editProfile", urlPatterns = {"/editProfile"})
public class EditProfile extends HttpServlet {

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditProfile() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            //
            HttpSession session = MSession.getSession(request);
	    entities.User client = (entities.User)session.getAttribute("user");
            //
            ActionUsers.getUserById(client.getId(),request, response);
            
            request.getRequestDispatcher(Const.PATH_PAGE_EDIT_PROFILE).forward(request, response);
            //
            
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            request.setCharacterEncoding("UTF-8");
            //
            HttpSession session = MSession.getSession(request);
	    entities.User client = (entities.User)session.getAttribute("user");
            //
            if(ActionEditProfile.editProfile(client, request, response))
                    if(request.getParameter("fromCart") == null)
                            request.getRequestDispatcher(Const.PATH_PAGE_SIGNUP_COMPLETE).forward(request, response);
                    else
                            request.getRequestDispatcher(Const.PATH_PAGE_LOGIN).forward(request, response);
            else {
                ActionUsers.getUserById(client.getId(),request, response);
                request.getRequestDispatcher(Const.PATH_PAGE_EDIT_PROFILE).forward(request, response);
            }
    }
}
