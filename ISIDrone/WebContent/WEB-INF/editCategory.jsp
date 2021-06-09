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
    Category category = (Category) request.getAttribute("category");
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
    <form action="category" method="post" class="panel panel-primary form-horizontal" style="float: unset; margin: auto;">

        <input type="hidden" name="action" value="editCategory" />
        <input type="hidden" name="category_id" value="<%=category.getId()%>" />

        <div class="panel-heading">
            <h3 class="panel-title">Modification de la categorie </h3>
        </div>

        <div class="panel-body">
            <fieldset class="col-sm-6 col-lg-6 col-md-6">
                <legend>Information de la categorie</legend>


                <%
                    if (hm_fieldErrorMsg != null && hm_fieldErrorMsg.containsKey("categoryName")) {
                %>
                <div class="alert alert-warning" style="margin-bottom: 0px; white-space: pre-line;"><%=hm_fieldErrorMsg.get("categoryName")%></div>
                <%
                    }
                %>

                <div class="form-group">
                    <div class="col-sm-10">
                        <label for="categoryName" class="col-sm-2 control-label">Nom</label>
                        <input type="text" id="categoryName" class="form-control" name="categoryName" value="<%=category.getName()%>" required />
                    </div>
                </div>


                <%
                    if (hm_fieldErrorMsg != null && hm_fieldErrorMsg.containsKey("categoryPosition")) {
                %>
                <div class="alert alert-warning" style="margin-bottom: 0px; white-space: pre-line;"><%=hm_fieldErrorMsg.get("categoryPosition")%></div>
                <%
                    }
                %>
                <div class="form-group">	
                    <div class="col-sm-10">
                        <label for="categoryPosition" class="col-sm-2 control-label">Position</label>
                        <input type="number"  id="categoryPosition" class="form-control" name="categoryPosition" value="<%=category.getOrder()%>" required />
                    </div>
                </div>



                <%
                    if (hm_fieldErrorMsg != null && hm_fieldErrorMsg.containsKey("categoryDescription")) {
                %>
                <div class="alert alert-warning" style="margin-bottom: 0px; white-space: pre-line;"><%=hm_fieldErrorMsg.get("categoryDescription")%></div>
                <%
                    }
                %>

                
                <label for="categoryDescription" class="col-sm-2 control-label">Description</label>
                <textarea  class="form-control" name="categoryDescription" id="categoryDescription">  <%=category.getDescription() != null ? category.getDescription().trim() : ""%></textarea>

                <%
                    if (hm_fieldErrorMsg != null && hm_fieldErrorMsg.containsKey("categoryStatut")) {
                %>
                <div class="alert alert-warning" style="margin-bottom: 0px; white-space: pre-line;"><%=hm_fieldErrorMsg.get("categoryStatut")%></div>
                <%
                    }
                %>
                <div class="form-group">
                    <div class="col-sm-10">
                        <label for="categoryStatut" class="col-sm-2 control-label">Statut</label>
                        <select class="form-control" name="categoryStatut" id="categoryStatut">                     
                            <option value="0" <%=category.getIsActive() ? "" : "selected"%> >Inactif</option>
                            <option value="1" <%=category.getIsActive() ? "selected" : ""%> required>Actif</option>
                        </select>                      
                    </div>
                </div>


            </fieldset>

            <div class="form-group text-center" style="clear: left; top: 15px; margin-bottom: 15px;">
                <button type="submit" class="btn btn-default">Modifier</button>
                <button onclick="event.preventDefault();window.history.back();" class="btn btn-default">Annuler</button>
            </div>
        </div>
    </form>


</div>
<jsp:include page="<%=Const.PATH_FOOTER_JSP%>" />