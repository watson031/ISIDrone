<%-- 
    Document   : newCategory.jsp
    Created on : May 8, 2021, 9:47:57 AM
    Author     : rdossant
--%>
<%@page import="util.Misc"%>
<%@page import="java.util.HashMap"%>
<%@page import="entities.Category"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    @SuppressWarnings(  "unchecked")
    HashMap<String, String> hm_fieldErrorMsg = (HashMap<String, String>) request.getAttribute("hm_fieldErrorMsg");
    String error = (String) request.getAttribute("error");
    HashMap<String, String> hm_formParamValue = (HashMap<String, String>) request.getAttribute("hm_formParamValue");
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

        <input type="hidden" name="action" value="addCategory" />

        <div class="panel-heading">
            <h3 class="panel-title">Ajouter une Categorie</h3>
        </div>

        <div class="panel-body">
            <fieldset class="col-sm-6 col-lg-6 col-md-6">
                <legend>Information Categorie</legend>

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
                        <input type="text" id="categoryName" class="form-control" name="categoryName" value="<%=Misc.getOrDefault(hm_formParamValue, "categoryName", "")%>" required />
                    </div>
                </div>

                <%
                    if (hm_fieldErrorMsg != null && hm_fieldErrorMsg.containsKey("categoryDescription")) {
                %>
                <div class="alert alert-warning" style="margin-bottom: 0px; white-space: pre-line;"><%=hm_fieldErrorMsg.get("categoryDescription")%></div>
                <%
                    }
                %>
                <div class="form-group">
                    <div class="col-sm-10">
                        <label for="categoryDescription" class="col-sm-2 control-label">Description</label>
                        <textarea rows="4" cols="50" class="form-control" id="categoryDescription" class="form-control" name="categoryDescription" >
                            <%=Misc.getOrDefault(hm_formParamValue, "categoryDescription", "")%>
                        </textarea>

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
                        <label for="categoryPosition" class="col-sm-2 control-label">Order</label>
                        <input type="number" step="1" min="1" id="categoryPosition" class="form-control" name="categoryPosition" value="<%=Misc.getOrDefault(hm_formParamValue, "categoryPosition", "")%>" required />
                    </div>
                </div>

                <%
                    if (hm_fieldErrorMsg != null && hm_fieldErrorMsg.containsKey("categoryStatut")) {
                %>
                <div class="alert alert-warning" style="margin-bottom: 0px; white-space: pre-line;"><%=hm_fieldErrorMsg.get("categoryStatut")%></div>
                <%
                    }
                %>

                <div class="form-group">
                    <div class="col-sm-10">
                        <label for="isCategoryActif" class="col-sm-2 control-label">Statut</label>
                        <select class="form-control" name="categoryStatut" id="categoryStatut">                     

                            <option value="1"  <%= Integer.parseInt(Misc.getOrDefault(hm_formParamValue, "categoryStatut", "-1")) == 1 ? "selected" : ""%> >Actif</option>
                            <option value="0"   required <%= Integer.parseInt(Misc.getOrDefault(hm_formParamValue, "categoryStatut", "-1")) == 0 ? "selected" : ""%> >Inactif</option>

                        </select>                      
                    </div>
                </div>

            </fieldset>

            <div class="form-group text-center" style="clear: left; top: 15px; margin-bottom: 15px;">
                <button type="submit" class="btn btn-default">Ajouter</button>
                <button onclick="event.preventDefault();window.history.back();" class="btn btn-default">Annuler</button>
            </div>
        </div>
    </form>


</div>
<jsp:include page="<%=Const.PATH_FOOTER_JSP%>" />