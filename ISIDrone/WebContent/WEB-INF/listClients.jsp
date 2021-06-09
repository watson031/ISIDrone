<%--
  Created by IntelliJ IDEA.
  User: bndiaye
  Date: 2021-05-11
  Time: 12:33
  To change this template use File | Settings | File Templates.
--%>

<%@page import="entities.User" %>
<%@page import="entities.Order" %>
<%@page import="java.text.DecimalFormat" %>
<%@page import="java.util.ArrayList" %>
<%@page import="entities.Category" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="util.Const" %>
<%
    @SuppressWarnings(  "unchecked")
    ArrayList<User> users = (ArrayList<User>) request.getAttribute("users");
%>
<jsp:include page="<%=Const.PATH_HEAD_JSP%>"/>
<jsp:include page="<%=Const.PATH_MENU_JSP%>"/>
<!-- /.container -->
<!-- Page Content -->
<div class="container">

    <div>


        <div>
            <div class="row">
                <h2 style="margin-top: 0px; margin-bottom: 10px; text-align: center; font-size: 72px; text-decoration: underline; color: rgb(51, 51, 51);">
                    Liste des Clients</h2>
                <div class="navbar-right" style="margin-right:0">
                    <div class="nav navbar-nav">
                        <div>
                            <form class="navbar-form" role="search" action="user?">
                                <div id="auto-search" class="form-group" style="padding-right:0;">
                                    <input class="form-control biginput" name="userSearch" placeholder="Rechercher"
                                           type="text">
                                </div>
                                <button type="submit" class="btn btn-default"><span
                                        class="glyphicon glyphicon-search"></span>
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
                <%

                    if (users != null && users.size() > 0) {%>
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th scope="col">Nom</th>
                            <th scope="col">Prenom</th>
                            <th scope="col">Email</th>
                            <th scope="col">Statut</th>
                            <th scope="col">Action</th>

                        </tr>
                    </thead>
                    <tbody>
                        <%
                            int index = 0;
                            for (User user : users) {
                                index++;
                        %>


                        <tr>
                            <td scope="row"><%=user.getLastName()%>
                            </td>
                            <td ><%=user.getFirstName()%>
                            </td>
                            <td><%= user.getEmail()%>
                            </td>
                            <td><%=user.getUserStatus().equals("ACTIVATED") ? "Actif" : "Desactivé"%></td>
                            <td>
                                <button type="button" onclick="location.href = 'user?client_id=<%=user.getId()%>';" class="btn btn-primary">
                                    Modifier
                                </button>

                                <%if (user.getUserStatus().equals("ACTIVATED")) {%>
                                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModalCenterInactif<%= index%>">
                                    Desactiver
                                </button>
                                <% } else {%>
                                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModalCenterActif<%= index%>">
                                    Activer&nbsp;&nbsp;&nbsp;&nbsp;
                                </button>
                                <%}%>
                            </td>


                    <div class="modal fade" id="exampleModalCenterInactif<%= index%>" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
                        <div class="modal-dialog modal-dialog-centered" role="document">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                    <h5 class="modal-title" id="exampleModalLongTitle "> <strong>Nom: </strong><%=user.getLastName()%> &nbsp;&nbsp; <strong>Prenom:  </strong><%=user.getFirstName()%></h5>

                                </div>
                                <div class="modal-body">
                                    Etes-vous sur de vouloir desactiver ce client ?
                                </div>
                                <div class="modal-footer">
                                    <form action="user" method="GET">
                                        <textarea type="text" class="col-sm-7 control-label" id="raison<%=user.getId()%>" name="raison" placeholder="Entrer la raison de desactivation"></textarea>
                                        <button type="button" class="btn btn-light" data-dismiss="modal">Non</button>
                                        <input type="hidden" class="btn btn-primary" name="actionisActif" value="actionDesactiver" >
                                        <input type="hidden" class="btn btn-primary" name="idUser" value="<%=user.getId()%>" >
                                        <input id="yes<%=user.getId()%>" type="submit" class="btn btn-primary" value="OUI" disabled title="Tooltip on bottom">

                                        <script>
                                            let yesBtn<%=user.getId()%> = document.getElementById("yes<%=user.getId()%>");
                                            let textArea<%=user.getId()%> = document.getElementById("raison<%=user.getId()%>");
                                            textArea<%=user.getId()%>.addEventListener("input", (event) => {
                                                const value = event.target.value
                                                yesBtn<%=user.getId()%>.disabled = value.trim() === '';
                                            })
                                        </script>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="modal fade" id="exampleModalCenterActif<%= index%>" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
                        <div class="modal-dialog modal-dialog-centered" role="document">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                    <h5 class="modal-title" id="exampleModalLongTitle"> <strong>Nom: </strong><%=user.getLastName()%> &nbsp;&nbsp; <strong>Prenom:  </strong><%=user.getFirstName()%></h5>

                                </div>
                                <div class="modal-body">
                                    Etes-vous sur de vouloir activer ce client ?
                                </div>
                                <div class="modal-footer">
                                    <form action="user" method="GET">
                                        <button type="button" class="btn btn-light" data-dismiss="modal">Non</button>
                                        <input type="hidden" class="btn btn-primary" name="actionisActif" value="actionActif" >
                                        <input type="hidden" class="btn btn-primary" name="idUser" value="<%=user.getId()%>" >
                                        <input type="submit" class="btn btn-primary" value="OUI" >
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                    </tr>
                    <%
                        }%>

                    </tbody>
                </table>
                <%} else {%>
            </div>
            <div class="alert alert-info">
                Aucun client ne correspond à votre demande.
            </div>
            <%}%>
        </div>
    </div>
</div>

</div>
<!-- Footer -->
<jsp:include page="<%=Const.PATH_FOOTER_JSP%>"/>

