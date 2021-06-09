<%@page import="entities.Item"%>
<%@page import="entities.Product"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="entities.Category"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="util.Const"%>
<%
    @SuppressWarnings(  "unchecked")
    ArrayList<Category> categories = (ArrayList<Category>) request.getAttribute("categoriesByOrder");

     Category category = (Category) request.getAttribute("category");

    ArrayList<Item> items = (ArrayList<Item>) request.getAttribute("items");
%>
<jsp:include page="<%=Const.PATH_HEAD_JSP%>"/>
<jsp:include page="<%=Const.PATH_MENU_JSP%>"/>
<!-- /.container -->
<!-- Page Content -->
<div class="container">

    <div >
        <div >
            <div class="row">
                <h2 style="margin-top: 0px; margin-bottom: 10px; text-align: center; font-size: 72px; text-decoration: underline; color: rgb(51, 51, 51);">Liste des Categories</h2>
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th scope="col">Nom</th>
                            <th scope="col">Ordre d'affichage</th>
                            <th scope="col">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%

                            if (categories != null && categories.size() > 0) {
                                int index = 0;
                                for (Category item : categories) {
                                    index++;
                                    boolean canBeDeleted = true;
                                    for (Item item1 : items) {
                                        if (item1.getCategory() == item.getId()) {
                                            canBeDeleted = false;
                                        }
                                    }
                        %>
                        <tr>
                            <th scope="row"><%=item.getName()%></th>
                            <td><%=item.getOrder()%></td>
                            <td>

                                <button type="button" onclick="location.href = 'category?category_id=<%=item.getId()%>';" class="btn btn-primary">
                                    Modifier
                                </button>
                             

                                <%
                                     if (canBeDeleted) {%>

                                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModalCenter<%= index%>">
                                    Delete &nbsp;&nbsp;&nbsp;&nbsp;
                                </button>

                            </td>
                            <!-- Modal -->
                    <div class="modal fade" id="exampleModalCenter<%= index%>" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
                        <div class="modal-dialog modal-dialog-centered" role="document">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                    <h5 class="modal-title" id="exampleModalLongTitle"> <strong><%=item.getName()%></strong> </h5>

                                </div>
                                <div class="modal-body">
                                    Etes-vous sur de vouloir supprimer ce produit ?
                                
                                            <div class="modal-footer">
                                                <form action="category" method="GET">
                                                    <button type="button" class="btn btn-light" data-dismiss="modal">Non</button>
                                                    <input type="hidden" class="btn btn-primary" name="action" value="delete" >
                                                    <input type="hidden" class="btn btn-primary" name="category" value="<%=item.getId()%>" >
                                                    <input type="submit" class="btn btn-primary" value="OUI" >
                                                </form>
                                            </div>
                                        </div>
                                    </div
                                    <%} else {%>
                                    <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModalCenter<%= index%>">
                                        Delete 
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-exclamation-triangle" viewBox="0 0 16 16">
                                        <path d="M7.938 2.016A.13.13 0 0 1 8.002 2a.13.13 0 0 1 .063.016.146.146 0 0 1 .054.057l6.857 11.667c.036.06.035.124.002.183a.163.163 0 0 1-.054.06.116.116 0 0 1-.066.017H1.146a.115.115 0 0 1-.066-.017.163.163 0 0 1-.054-.06.176.176 0 0 1 .002-.183L7.884 2.073a.147.147 0 0 1 .054-.057zm1.044-.45a1.13 1.13 0 0 0-1.96 0L.165 13.233c-.457.778.091 1.767.98 1.767h13.713c.889 0 1.438-.99.98-1.767L8.982 1.566z"/>
                                        <path d="M7.002 12a1 1 0 1 1 2 0 1 1 0 0 1-2 0zM7.1 5.995a.905.905 0 1 1 1.8 0l-.35 3.507a.552.552 0 0 1-1.1 0L7.1 5.995z"/>
                                        </svg>
                                    </button>
                                    <!-- Modal -->
                                    <div class="modal fade" id="exampleModalCenter<%= index%>" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
                                        <div class="modal-dialog modal-dialog-centered" role="document">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                                        <span aria-hidden="true">&times;</span>
                                                    </button>
                                                    <h5 class="modal-title" id="exampleModalLongTitle"> <strong><%=item.getName()%></strong> </h5>

                                                </div>
                                                <div class="modal-body">
                                                    Impossible de supprimer une  categorie  associé à d'autres produits
                                                </div>
                                                <div class="modal-footer">
                                                    <div>
                                                        <input type="submit" class="btn btn-primary" data-dismiss="modal" value="OK" >
                                                    </div>
                                                </div>
                                            </div>
                                        </div
                                        <% } %>
                                     
                                        </td>
                                        </tr>
                                        <%}
                                            }%>
                                        </tbody>
                                        </table>
                                    </div>


                                </div>
                                </div>
                           

                                </div>
                                <!-- Footer -->
                                <jsp:include page="<%=Const.PATH_FOOTER_JSP%>"/>