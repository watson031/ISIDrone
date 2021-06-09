<%-- 
    Document   : editProduct.jsp
    Created on : May 4, 2021, 11:37:57 AM
    Author     : gblandin
--%>
<%@page import="java.util.HashMap"%>
<%@page import="entities.Category"%>
<%@page import="java.util.ArrayList"%>
<%@page import="entities.Item"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%
    @SuppressWarnings(  "unchecked")
    Item item = (Item) request.getAttribute("item");
    ArrayList<Category> categories = (ArrayList<Category>) request.getAttribute("categories");
    HashMap<String, String> hm_fieldErrorMsg = (HashMap<String, String>) request.getAttribute("hm_fieldErrorMsg");
    String error = (String) request.getAttribute("error");
%>
<%@page import="util.Const"%>

<jsp:include page="<%=Const.PATH_HEAD_JSP%>" />
<jsp:include page="<%=Const.PATH_MENU_JSP%>" />
<div class="container">
    <%
        if (error != null && error.equals("DBProblem")) {%>
    <div class="alert alert-danger">
        Une erreur de connexion c'est produite. Veuillez attendre quelques temps avant de faire une nouvelle tentative.
        Si vous voyez ce message pour la deuxième fois, veuillez contactez l'administrateur du site pour lui informer du
        problème.
    </div>
    <% }
    %>
    <form action="item" method="post" class="panel panel-primary form-horizontal" style="float: unset; margin: auto;">

        <input type="hidden" name="action" value="editProduct" />
        <input type="hidden" name="product_id" value="<%=item.getId()%>" />

        <div class="panel-heading">
            <h3 class="panel-title">Modification du produit</h3>
        </div>

        <div class="panel-body">
            <fieldset class="col-sm-6 col-lg-6 col-md-6">
                <legend>Information Produit</legend>

                <%
                    if (hm_fieldErrorMsg != null && hm_fieldErrorMsg.containsKey("productName")) {
                %>
                <div class="alert alert-warning" style="margin-bottom: 0px; white-space: pre-line;"><%=hm_fieldErrorMsg.get("productName")%></div>
                <%
                    }
                %>
                <div class="form-group">
                    <div class="col-sm-10">
                        <label for="productName" class="col-sm-2 control-label">Nom</label>
                        <input type="text" id="productName" class="form-control" name="productName" value="<%=item.getName()%>" required />
                    </div>
                </div>

                <%
                    if (hm_fieldErrorMsg != null && hm_fieldErrorMsg.containsKey("productDescription")) {
                %>
                <div class="alert alert-warning" style="margin-bottom: 0px; white-space: pre-line;"><%=hm_fieldErrorMsg.get("productDescription")%></div>
                <%
                    }
                %>
                <div class="form-group">
                    <div class="col-sm-10">
                        <label for="productDescription" class="col-sm-2 control-label">Description</label>
                        <textarea rows="4" cols="50" class="form-control" id="productDescription" class="form-control" name="productDescription" required >
                            <%=item.getDescription().trim()%>
                        </textarea>

                    </div>
                </div>

                <%
                    if (hm_fieldErrorMsg != null && hm_fieldErrorMsg.containsKey("productCategory")) {
                %>
                <div class="alert alert-warning" style="margin-bottom: 0px; white-space: pre-line;"><%=hm_fieldErrorMsg.get("productCategory")%></div>
                <%
                    }
                %>
                <div class="form-group">
                    <div class="col-sm-10">
                        <label for="productCategory" class="col-sm-2 control-label">Categorie</label>
                        <select class="form-control"   name = "productCategory" id = "productCategory">

                            <%
                                for (Category category : categories) {%>
                            <option value = "<%=category.getId()%>" <%=category.getId() == item.getCategory() ? "selected" : ""%> required > <%= category.getName()%></option>                          
                            <% }%>
                        </select>   

                    </div>
                </div>

                <%
                    if (hm_fieldErrorMsg != null && hm_fieldErrorMsg.containsKey("productPrice")) {
                %>
                <div class="alert alert-warning" style="margin-bottom: 0px; white-space: pre-line;"><%=hm_fieldErrorMsg.get("productPrice")%></div>
                <%
                    }
                %>
                <div class="form-group">	
                    <div class="col-sm-10">
                        <label for="productPrice" class="col-sm-2 control-label">Prix</label>
                        <input type="number" step="0.01" min="1" id="productPrice" class="form-control" name="productPrice" value="<%=item.getPrice()%>" required />
                    </div>
                </div>

                <%
                    if (hm_fieldErrorMsg != null && hm_fieldErrorMsg.containsKey("productNoSerie")) {
                %>
                <div class="alert alert-warning" style="margin-bottom: 0px; white-space: pre-line;"><%=hm_fieldErrorMsg.get("productNoSerie")%></div>
                <%
                    }
                %>
                <div class="form-group">
                    <div class="col-sm-10">
                        <label for="productNoSerie" class="col-sm-2 control-label">Numero&nbsp;de&nbsp;série</label>
                        <input type="text" id="productNoSerie" class="form-control" name="productNoSerie" value="<%=item.getSerial()%>" required />
                    </div>
                </div>

                <%
                    if (hm_fieldErrorMsg != null && hm_fieldErrorMsg.containsKey("qtyStock")) {
                %>
                <div class="alert alert-warning" style="margin-bottom: 0px; white-space: pre-line;"><%=hm_fieldErrorMsg.get("qtyStock")%></div>
                <%
                    }
                %>
                <div class="form-group">
                    <div class="col-sm-10">
                        <label for="qtyStock" class="col-sm-2 control-label">Quantité&nbsp;en&nbsp;stock</label>
                        <input type="number" min="0" id="qtyStock" class="form-control" name="qtyStock" value="<%=item.getStock()%>" required />
                    </div>
                </div>

                <%
                    if (hm_fieldErrorMsg != null && hm_fieldErrorMsg.containsKey("isProductActif")) {
                %>
                <div class="alert alert-warning" style="margin-bottom: 0px; white-space: pre-line;"><%=hm_fieldErrorMsg.get("isProductActif")%></div>
                <%
                    }
                %>

                <div class="form-group">
                    <div class="col-sm-10">
                        <label for="isProductActif" class="col-sm-2 control-label">Statut</label>
                        <select class="form-control" name="isProductActif" id="isProductActif">                     
                            <option value="1" selected><%= item.isActive() == 1 ? "Produit Actif" : "Produit Inactif"%></option>
                            <option value="0" required><%= item.isActive() == 0 ? "Produit Actif" : "Produit Inactif"%></option>
                        </select>                      
                    </div>
                </div>

            </fieldset>

            <fieldset>
                <legend>Image du Produit</legend>
                <img src="images/products/<%=item.getImage()%>" alt="image of product" width="300" height="300">
            </fieldset>

            <div class="form-group text-center" style="clear: left; top: 15px; margin-bottom: 15px;">
                <button type="submit" class="btn btn-default">Modifier</button>
                <button onclick="event.preventDefault();window.history.back();" class="btn btn-default">Annuler</button>
            </div>
        </div>
    </form>


</div>
<jsp:include page="<%=Const.PATH_FOOTER_JSP%>" />