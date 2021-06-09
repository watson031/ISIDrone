<%-- 
    Document   : AddProduct.jsp
    Created on : May 5, 2021, 11:37:57 AM
    Author     : bndiaye
--%>
<%@page import="util.Misc"%>
<%@page import="java.util.HashMap"%>
<%@page import="entities.Category"%>
<%@page import="java.util.ArrayList"%>
<%@page import="entities.Item"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    @SuppressWarnings(  "unchecked")
    ArrayList<Category> categories = (ArrayList<Category>) request.getAttribute("categories");
    HashMap<String, String> hm_fieldErrorMsg = (HashMap<String, String>) request.getAttribute("hm_fieldErrorMsg");
    String error = (String) request.getAttribute("error");
      HashMap<String, String> hm_formParamValue = (HashMap<String, String>) request.getAttribute("hm_formParamValue");
      error = (String) request.getAttribute("error");
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

        <input type="hidden" name="action" value="addProduct" />

        <div class="panel-heading">
            <h3 class="panel-title">Ajouter un produit</h3>
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
                        <input type="text" id="productName" class="form-control" name="productName" value="<%=Misc.getOrDefault(hm_formParamValue, "productName", "")%>" required />
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
                            <%=Misc.getOrDefault(hm_formParamValue, "productDescription", "")%>
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
                                int  i = 0;
                                for (Category category : categories) {
                                       if(i != 0){
                            %>
                    
                            <option value = "<%=category.getId()%>" <%=category.getId()  ==Integer.parseInt(Misc.getOrDefault(hm_formParamValue, "productCategory", "-1") ) ? "selected" : ""%> required > <%= category.getName()%></option>                          
                            <% } i++; }%>
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
                        <input type="number" step="0.01" min="1" id="productPrice" class="form-control" name="productPrice" value="<%=Misc.getOrDefault(hm_formParamValue, "productPrice", "")%>" required />
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
                        <input type="text" id="productNoSerie" class="form-control" name="productNoSerie" value="<%=Misc.getOrDefault(hm_formParamValue, "productNoSerie", "")%>" required />
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
                        <input type="number" min="0" id="qtyStock" class="form-control" name="qtyStock" value="<%=Misc.getOrDefault(hm_formParamValue, "qtyStock", "")%>" required />
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
                            <option value="1"  <%= Integer.parseInt(Misc.getOrDefault(hm_formParamValue, "isProductActif", "-1")) == 1? "selected" : ""%> >Actif</option>
                            <option value="0"   required <%= Integer.parseInt(Misc.getOrDefault(hm_formParamValue, "isProductActif", "-1")) == 0  ? "selected" : ""%> >Inactif</option>
                        </select>                      
                    </div>
                </div>

            </fieldset>

            <fieldset>
                <legend>Image du Produit</legend>
                <img src="images/products/default_drone_image.jpg" alt="image of product" width="300" height="300"">
                <input type="hidden" id="productImage" class="form-control" name="productImage" value="default_drone_image.jpg"  />
            </fieldset>

            <div class="form-group text-center" style="clear: left; top: 15px; margin-bottom: 15px;">
                <button type="submit" class="btn btn-default">Ajouter</button>
                <button onclick="event.preventDefault();window.history.back();" class="btn btn-default">Annuler</button>
            </div>
        </div>
    </form>


</div>
<jsp:include page="<%=Const.PATH_FOOTER_JSP%>" />