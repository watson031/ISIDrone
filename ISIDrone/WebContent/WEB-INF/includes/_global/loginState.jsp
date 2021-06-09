<%@page import="util.Const"%>
<%@page import="manager.MLogin"%>
<%@page import="action.ActionLogin"%>
<%@page import="manager.MCookies"%>
<%@ page import="entities.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
//S'il n'y a pas d'utilisateur déjà de connecté
User user = (User)session.getAttribute("user");

//S'il n'y a pas d'utilisaeur de connecté présentement, on vérifie dans les cookies
// et si nous somme pas en train de nous déconnecter
if(user == null && request.getAttribute("logout") == null) {
	user = ActionLogin.getUserFromAutoLogin(request);
	session.setAttribute("user", user);
}

//Si le autoLogin a fonctionné
if(user != null) {%>
<li id="loginState">
	<a href="#" id="user"><%=user.getFirstName()%></a>
	<ul id="userAction" class="list-unstyled navbar navbar-default">
            <% if ( user.getUserType() != 1 ) {%>
              <li><a type="button" class="btn btn-primary" href="<%="order-history"%>" style="width: 170px;">Historique commande</a></li>
              <li><a type="button" class="btn btn-primary" href="editProfile?id=<%=user.getId()%>" style="width: 170px;">Profile</a></li>
            <% }
              else if (user != null && user.getUserType() == 1){%>
                <li><a type="button" class="btn btn-primary"  href="items?listproducts=1&category=1" style="width: 170px;">Liste Produits</a></li>
                <li><a type="button" class="btn btn-primary"  href="item?op=25" style="width: 170px;">Ajouter Produit</a></li>
                <li><a type="button" class="btn btn-primary"  href="category?listcategory=1&category=1" style="width: 170px;">Liste Categories</a></li>
                <li><a type="button" class="btn btn-primary"  href="category?op=25" style="width: 170px;">Ajouter Categorie</a></li>
                <li><a type="button" class="btn btn-primary"  href="order?listOrders=1" style="width: 170px;">Liste Commandes</a></li>
                <li><a type="button" class="btn btn-primary"  href="user?listOrders=1" style="width: 170px;">Liste Clients</a></li>
            <% }%>

		<li>&nbsp;</li>
		<li><a type="button" class="btn btn-primary" href="login" style="width: 170px;">Déconnexion</a></li>
	</ul>
</li>
<% }
else
{
	
%>
<li><a href="signup<%=(request.getParameter("fromCart") != null ? "?fromCart=true" : "")%>">S'enregistrer</a></li>
<li id="loginState"><a href="login">Connexion</a></li>
<%	
}
%>