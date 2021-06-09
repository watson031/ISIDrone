<%-- 
    Document   : listOrders
    Created on : 7 mai 2021, 11 h 30 min 24 s
    Author     : bndiaye
--%>

<%@page import="entities.User"%>
<%@page import="entities.Order"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="entities.Category"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="util.Const"%>
<%
    @SuppressWarnings(  "unchecked")
    ArrayList<Order> orders = (ArrayList<Order>) request.getAttribute("orders");
    ArrayList<User> users = (ArrayList<User>) request.getAttribute("users");
%>
<jsp:include page="<%=Const.PATH_HEAD_JSP%>"/>
<jsp:include page="<%=Const.PATH_MENU_JSP%>"/>
<!-- /.container -->
<!-- Page Content -->
<div class="container">

    <div >
        <div >
            <div class="row">
                <h2 style="margin-top: 0px; margin-bottom: 10px; text-align: center; font-size: 72px; text-decoration: underline; color: rgb(51, 51, 51);">Liste des Commandes</h2>
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th scope="col">Nom du client</th>
                            <th scope="col">Date de commande</th>
                            <th scope="col">Status</th>
                            <th scope="col">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%

                            if (orders != null && orders.size() > 0) {
                                int index = 0;
                                for (Order order : orders) {
                                    index++;
                        %>
                        <tr>
                            <%
                                for (User user : users) {
                                    if (user.getId() == order.getUserId()) {%>
                            <th scope="row"><%=user.getFirstName()%>  <%=user.getLastName()%></th>
                                <%}
                                    }

                                %>    

                            <td><%=order.getDate() == null ? "----" : order.getDate()%></td>
                            <td>
                                <%if (!order.isIsShipped()) {%>
                                <div>En cours</div>
                                <% } else {%>
                                <div>Livré</div>
                                <% }%>
                            </td>
                            <td>

                                <%if (!order.isIsShipped()) {%>
                                <button type="submit" class="btn btn-primary" data-toggle="modal" data-target="#exampleModalCenterShip<%= index%>">
                                    Livré
                                </button>
                                <% } else {%>
                                <form  action="order" method="post">
                                    <input type="hidden" class="btn btn-primary" name="idOrderToUnShipped" value="<%=order.getId()%>" >
                                    <button type="submit" class="btn btn-primary" name="btnEnCours" id="btnEnCours" value='btnEnCours' > En cours </button>
                                </form>

                                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModalCenter<%= index%>">
                                    Delete
                                </button>
                                <%}%>

                            </td>
                            <!-- Modal -->
                    <div class="modal fade" id="exampleModalCenter<%= index%>" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
                        <div class="modal-dialog modal-dialog-centered" role="document">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                    <h5 class="modal-title" id="exampleModalLongTitle"> <strong>Id: </strong><%=order.getId()%> &nbsp;&nbsp; <strong>Date:  </strong><%=order.getDate()%></h5>

                                </div>
                                <div class="modal-body">
                                    Etes-vous sur de vouloir supprimer cette commande  ?
                                </div>
                                <div class="modal-footer">
                                    <form action="order" method="GET">
                                        <button type="button" class="btn btn-light" data-dismiss="modal">Non</button>
                                        <input type="hidden" class="btn btn-primary" name="action" value="delete" >
                                        <input type="hidden" class="btn btn-primary" name="idOrder" value="<%=order.getId()%>" >
                                        <input type="submit" class="btn btn-primary" value="OUI" >
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- Modal -->
                    <div class="modal fade" id="exampleModalCenterShip<%= index%>" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitleShip" aria-hidden="true">
                        <div class="modal-dialog modal-dialog-centered" role="document">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                    <h5 class="modal-title" id="exampleModalLongTitleShip"> <strong>Id: </strong><%=order.getId()%> &nbsp;&nbsp; <strong>Date:  </strong><%=order.getDate()%></h5>

                                </div>
                                <div class="modal-body">
                                    Etes-vous sur de vouloir passer cette commande pour l'envoyer ?
                                </div>
                                <div class="modal-footer">
                                    <form action="order" method="GET">
                                        <button type="button" class="btn btn-light" data-dismiss="modal">Non</button>
                                        <input type="hidden" class="btn btn-primary" name="action" value="ship" >
                                        <input type="hidden" class="btn btn-primary" name="idOrder" value="<%=order.getId()%>" >
                                        <input type="submit" class="btn btn-primary" value="OUI" >
                                    </form>
                                </div>
                            </div>
                        </div             

                        </tr>
                        <%
                                }
                            }%>
                        </tbody>
                </table>
            </div>
        </div>
    </div>

</div>
<!-- Footer -->
<jsp:include page="<%=Const.PATH_FOOTER_JSP%>"/>
