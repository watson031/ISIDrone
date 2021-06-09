<%@page import="action.ActionLogin"%>
<%@page import="entities.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ page import="util.Const"%>

<%
//S'il n'y a pas d'utilisateur déjà de connecté
    User user = (User) session.getAttribute("user");

//S'il n'y a pas d'utilisaeur de connecté présentement, on vérifie dans les cookies
// et si nous somme pas en train de nous déconnecter
    if (user == null && request.getAttribute("logout") == null) {
        user = ActionLogin.getUserFromAutoLogin(request);
        session.setAttribute("user", user);
    }%>

<!-- Navigation -->
<div class="container">
    <div class="panel panel-default">
        <div class="panel-body">
            <a href="home"><img src="images/isi_drone.png" /></a>
            <div class="navbar-right" style="margin-right:0">
                <ul class="nav navbar-nav">
                    <li>
                        <!-- 						La recherche n'est pas fini d'être implementé -->
                        <form class="navbar-form" role="search" action="items?">
                            <div id="auto-search" class="form-group"  style="padding-right:0;">
                                <input class="form-control biginput" name="itemSearch" placeholder="Rechercher" id="autocomplete" type="text">
                            </div>
                            <button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-search"></span></button>
                        </form>
                    </li>
                    <jsp:include page="<%=Const.PATH_CART_DROPDOWN_JSP%>"/>
                </ul>
            </div>
        </div>
    </div>
    <nav class="navbar navbar-default" role="navigation">
        <div class="container">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse"
                        data-target="#bs-example-navbar-collapse-1">
                    <span class="sr-only">Toggle navigation</span> <span
                        class="icon-bar"></span> <span class="icon-bar"></span> <span
                        class="icon-bar"></span>
                </button>
                <%-- 				<a class="navbar-brand" href="home"><%=Const.COMP_NAME %></a> --%>
            </div>
            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse"
                 id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav">
                    <li><a href="about">À Propos</a></li>
                    <li><a href="service">Services</a></li>
                    <li><a href="contact">Contact</a></li>
                    <li><a href="items?category=1">Boutique</a></li>
                        <%--
                           <%
                                if (user != null && user.getUserType() == 1) {%>
                        
                                     <li><a href="items?listproducts=1&category=1">Liste Produits</a></li>
                                     <li><a href="item?op=25">Ajouter Produit</a></li>
                                     <li><a href="category?listcategory=1&category=1">Liste Categories</a></li>
                                     <li><a href="category?op=25">Ajouter Categorie</a></li>
                                     <li><a href="order?listOrders=1">Liste Commandes</a></li>

                            <%}%>
                        --%>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <jsp:include page="loginState.jsp" />
                </ul>
            </div>
            <!-- /.navbar-collapse -->
        </div>
    </nav>
</div>
