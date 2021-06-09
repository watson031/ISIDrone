<%--
  Created by IntelliJ IDEA.
  User: bndiaye
  Date: 2021-05-11
  Time: 15:56
  To change this template use File | Settings | File Templates.
--%>
<%@page import="util.Misc" %>
<%@page import="java.util.HashMap" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="util.Const" %>
<%@ page import="servlet.User" %>
<jsp:include page="<%=Const.PATH_HEAD_JSP%>"/>
<jsp:include page="<%=Const.PATH_MENU_JSP%>"/>

<%
    @SuppressWarnings("unchecked")
    entities.User client = (entities.User) request.getAttribute("user");
    @SuppressWarnings("unchecked")
    HashMap<String, String> hm_fieldErrorMsg = (HashMap<String, String>) request.getAttribute("hm_fieldErrorMsg");
    String error = (String) request.getAttribute("error");
%>
<!-- Page Content -->
<div class="container">
    <%
        if (request.getParameter("fromCart") != null) {
    %>
    <div class="progress-cart">
        <span class="label label-success">Panier</span>
        <span class="label label-success">Sommaire</span>
        <span class="label label-warning">Compte</span>
        <span class="label label-warning">Facture</span>
    </div>
    <%
        }
    %>
    <%
        if (error != null) {
            if (error.equals("accountExisting")) {
    %>
    <div class="alert alert-info">
        Un compte existe déjà pour cette adresse email.
    </div>
    <%
    } else if (error.equals("DBProblem")) {
    %>
    <div class="alert alert-danger">
        Une erreur de connexion c'est produite. Veuillez attendre quelques temps avant de faire une nouvelle tentative.
        Si vous voyez ce message pour la deuxième fois, veuillez contactez l'administrateur du site pour lui informer du
        problème.
    </div>
    <%
            }
        }
    %>

    <form action="user" method="post" class="panel panel-primary form-horizontal" style="float: unset; margin: auto;">
        <input type="hidden" name="action" value="editClient">
        <input type="hidden" name="client_id" value="<%= client.getId()%>">
        <div class="panel-heading">
            <h3 class="panel-title">Modification d'un compte</h3>
        </div>
        <div class="panel-body">
            <fieldset class="col-sm-6 col-lg-6 col-md-6">
                <legend>Information de l'Utilisateur</legend>
                <%
                    if (hm_fieldErrorMsg != null && hm_fieldErrorMsg.containsKey("lastName")) {
                %>
                <div class="alert alert-warning"
                     style="margin-bottom: 0; white-space: pre-line;"><%=hm_fieldErrorMsg.get("lastName")%>
                </div>
                <%
                    }
                %>
                <div class="form-group">
                    <div class="col-sm-10">
                        <label class="col-sm-2 control-label">Nom</label>
                        <input type="text" class="form-control" name="lastName" value="<%= client.getLastName() %>"
                               required/>
                    </div>
                </div>
                <%
                    if (hm_fieldErrorMsg != null && hm_fieldErrorMsg.containsKey("firstName")) {
                %>
                <div class="alert alert-warning"
                     style="margin-bottom: 0px; white-space: pre-line;"><%=hm_fieldErrorMsg.get("firstName")%>
                </div>
                <%
                    }
                %>
                <div class="form-group">
                    <div class="col-sm-10">
                        <label for="firstName" class="col-sm-2 control-label">Prénom</label>
                        <input type="text" id="firstName" class="form-control" name="firstName"
                               value="<%=client.getFirstName() %>" required/>
                    </div>
                </div>
                <%
                    if (hm_fieldErrorMsg != null && hm_fieldErrorMsg.containsKey("email")) {
                %>
                <div class="alert alert-warning"
                     style="margin-bottom: 0px; white-space: pre-line;"><%=hm_fieldErrorMsg.get("email")%>
                </div>
                <%
                    }
                %>
                <div class="form-group">
                    <div class="col-sm-10">
                        <label for="email" class="col-sm-2 control-label">Email</label>
                        <input type="email" id="email" class="form-control" name="email" placeholder="Email"
                               value="<%=client.getEmail()%>" required/>
                    </div>
                </div>
                <%--client.getUserStatus().equals("ACTIVATED")--%>
                <div class="form-group">
                    <div class="col-sm-10">
                        <label for="userStatut" class="col-sm-2 control-label">Statut</label>
                        <select class="form-control" name="userStatut" id="userStatut">
                            <option value="ACTIVATED" <%=client.getUserStatus().equals("ACTIVATED") ? "selected" : ""%> >Activé</option>
                            <option value="DISACTIVATED" <%=client.getUserStatus().equals("DISACTIVATED") ? "selected" : ""%> >Désactivé</option>
                        </select>
                    </div>
                </div>

            </fieldset>
            <fieldset class="col-sm-6 col-lg-6 col-md-6">
                <legend>Adresse</legend>
                <%
                    if (hm_fieldErrorMsg != null && hm_fieldErrorMsg.containsKey("addr_no")) {
                %>
                <div class="alert alert-warning"
                     style="margin-bottom: 0px; white-space: pre-line;"><%=hm_fieldErrorMsg.get("addr_no")%>
                </div>
                <%
                    }
                %>
                <div class="form-group">
                    <div class="col-sm-10">
                        <label for="addr_no" class="col-sm-2 control-label">Numéro&nbsp;civique</label>
                        <input type="text" id="addr_no" class="form-control" name="addr_no"
                               value="<%=client.getShipAddress().getNo()%>" required/>
                    </div>
                </div>
                <%
                    if (hm_fieldErrorMsg != null && hm_fieldErrorMsg.containsKey("addr_appt")) {
                %>
                <div class="alert alert-warning"
                     style="margin-bottom: 0px; white-space: pre-line;"><%=hm_fieldErrorMsg.get("addr_appt")%>
                </div>
                <%
                    }
                %>
                <div class="form-group">
                    <div class="col-sm-10">
                        <label for="addr_appt" class="col-sm-2 control-label">Appartement</label>
                        <input type="text" id="addr_appt" class="form-control" name="addr_appt"
                               value="<%=client.getShipAddress().getAppt()%>"/>
                    </div>
                </div>
                <%
                    if (hm_fieldErrorMsg != null && hm_fieldErrorMsg.containsKey("addr_street")) {
                %>
                <div class="alert alert-warning"
                     style="margin-bottom: 0px; white-space: pre-line;"><%=hm_fieldErrorMsg.get("addr_street")%>
                </div>
                <%
                    }
                %>
                <div class="form-group">
                    <div class="col-sm-10">
                        <label for="addr_street" class="col-sm-2 control-label">Rue</label>
                        <input type="text" id="addr_street" class="form-control" name="addr_street"
                               value="<%=client.getShipAddress().getStreet()%>" required/>
                    </div>
                </div>
                <%
                    if (hm_fieldErrorMsg != null && hm_fieldErrorMsg.containsKey("addr_zip")) {
                %>
                <div class="alert alert-warning"
                     style="margin-bottom: 0px; white-space: pre-line;"><%=hm_fieldErrorMsg.get("addr_zip")%>
                </div>
                <%
                    }
                %>
                <div class="form-group">
                    <div class="col-sm-10">
                        <label for="addr_zip" class="col-sm-2 control-label">Code&nbsp;postal</label>
                        <input type="text" id="addr_zip" class="form-control" name="addr_zip"
                               value="<%=client.getShipAddress().getZip()%>" required/>
                    </div>
                </div>
                <%
                    if (hm_fieldErrorMsg != null && hm_fieldErrorMsg.containsKey("addr_city")) {
                %>
                <div class="alert alert-warning"
                     style="margin-bottom: 0px; white-space: pre-line;"><%=hm_fieldErrorMsg.get("addr_city")%>
                </div>
                <%
                    }
                %>
                <div class="form-group">
                    <div class="col-sm-10">
                        <label for="addr_city" class="col-sm-2 control-label">Ville</label>
                        <input type="text" id="addr_city" class="form-control" name="addr_city"
                               value="<%=client.getShipAddress().getCity()%>" required/>
                    </div>
                </div>
                <%
                    if (hm_fieldErrorMsg != null && hm_fieldErrorMsg.containsKey("addr_state")) {
                %>
                <div class="alert alert-warning"
                     style="margin-bottom: 0px; white-space: pre-line;"><%=hm_fieldErrorMsg.get("addr_state")%>
                </div>
                <%
                    }
                %>
                <div class="form-group">
                    <div class="col-sm-10">
                        <label for="addr_state" class="col-sm-2 control-label">Province</label>
                        <input type="text" id="addr_state" class="form-control" name="addr_state"
                               value="<%=client.getShipAddress().getState()%>" required/>
                    </div>
                </div>
                <%
                    if (hm_fieldErrorMsg != null && hm_fieldErrorMsg.containsKey("addr_country")) {
                %>
                <div class="alert alert-warning"
                     style="margin-bottom: 0px; white-space: pre-line;"><%=hm_fieldErrorMsg.get("addr_country")%>
                </div>
                <%
                    }
                %>
                <div class="form-group">
                    <div class="col-sm-10">
                        <label for="addr_country" class="col-sm-2 control-label">Pays</label>
                        <input type="text" id="addr_country" class="form-control" name="addr_country"
                               value="<%=client.getShipAddress().getCountry()%>" required/>
                    </div>
                </div>
            </fieldset>
            <%
                if (request.getParameter("fromCart") != null) {
            %>
            <input type="hidden" name="fromCart" value="true">
            <%
                }
            %>
            <div class="form-group text-center" style="clear: left; top: 15px; margin-bottom: 15px;">
                <button type="submit" class="btn btn-default">Modifier</button>
            </div>
        </div>
    </form>
</div>
<!-- Footer -->
<jsp:include page="<%=Const.PATH_FOOTER_JSP%>"/>
