<%@page import="entities.Category"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="entities.Cart"%>
<%@page import="entities.Item"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="util.Const"%>
<%
    @SuppressWarnings(  "unchecked")
    ArrayList<Item> items = (ArrayList<Item>) request.getAttribute("items");
    ArrayList<Category> categories = (ArrayList<Category>) request.getAttribute("categories");
%>
<jsp:include page="<%=Const.PATH_HEAD_JSP%>"/>
<jsp:include page="<%=Const.PATH_MENU_JSP%>"/>
<!-- /.container -->
<!-- Page Content -->
<div class="container">

    <div >
        <div >
            <div class="row">
                <h2 style="margin-top: 0px; margin-bottom: 10px; text-align: center; font-size: 72px; text-decoration: underline; color: rgb(51, 51, 51);">Liste des Produits</h2>
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th scope="col">Nom</th>
                            <th scope="col">Catégorie</th>
                            <th scope="col">Prix</th>
                            <th scope="col">Quantité en stock</th>
                            <th scope="col">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            //Format a deux decimal
                            DecimalFormat df = new DecimalFormat("####0.00");

                            if (items != null && items.size() > 0) {
                                int index = 0;
                                for (Item item : items) {
                                    index++;

                        %>
                        <tr>
                            <th scope="row"><%=item.getName()%></th>
                            <td><%
                                for (Category category : categories) {
                                    if (category.getId() == item.getCategory()) {%>
                                <%= category.getName()%>
                                <% }
                                    }
                                %></td>
                            <td><%=item.getPrice()%></td>
                            <td><%=item.getStock()%></td>

                            <!-- Button trigger modal -->
                            <td>
                                <button type="button" onclick="location.href = 'item?product_id=<%=item.getId()%>';" class="btn btn-primary">
                                    Modifier
                                </button>
                                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModalCenter<%= index%>">
                                    Delete
                                </button>

                            </td>


                            <!-- Modal -->
                    <div class="modal fade" id="exampleModalCenter<%= index%>" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
                        <div class="modal-dialog modal-dialog-centered" role="document">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="exampleModalLongTitle"> <strong><%=item.getName()%></strong> </h5>
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                </div>
                                <div class="modal-body">
                                    Etes-vous sur de vouloir supprimer ce produit ?
                                </div>
                                <div class="modal-footer">
                                    <form action="items" method="GET">
                                        <button type="button" class="btn btn-light" data-dismiss="modal">Non</button>
                                        <input type="hidden" class="btn btn-primary" name="action" value="delete" >
                                        <input type="hidden" class="btn btn-primary" name="product" value="<%=item.getId()%>" >
                                        <input type="hidden" class="btn btn-primary" name="category" value="1" >
                                        <input type="submit" class="btn btn-primary" value="OUI" >
                                    </form>
                                </div>
                            </div>
                        </div

                        </td>
                        </tr>

                        <%
                            }
                        } else {
                        %>
                        <div class="alert alert-info">
                            Aucun produit trouvé.
                        </div>
                        <%
                            }
                        %>
                        </tbody>
                </table>
            </div>
        </div>
    </div>

</div>
<script src="https://code.jquery.com/jquery-1.11.1.min.js"></script>

<script src="https://code.jquery.com/ui/1.11.1/jquery-ui.min.js"></script>

<link rel="stylesheet" href="https://code.jquery.com/ui/1.11.1/themes/smoothness/jquery-ui.css" />
<script src="js/delete_product_popup.js"></script>
<!-- Footer -->
<jsp:include page="<%=Const.PATH_FOOTER_JSP%>"/>