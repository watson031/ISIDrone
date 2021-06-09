package action;

import static action.ActionSignUp.validateForm;
import entities.Address;
import entities.Item;
import entities.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import manager.MCategory;

import manager.MItem;
import manager.MSignUp;
import util.Misc;
import util.Restriction;
import util.ResultValidation;
import util.Validation;

public class ActionItems {

    public static void getItems(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("items", MItem.getItems(ActionCategory.getSelectedCategory(request, response)));
    }

    public static void getItemsBySearchedValues(String searchedValue, HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("items", MItem.getItemsBySearchedValues(searchedValue));
    }

    public static void getItemById(int id, HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("item", MItem.getItemById(id));
    }

    public static void deleteItemById(int id, HttpServletRequest request, HttpServletResponse response) {
        int rowAffected = MItem.deleteItemById(id);
        getItems(request, response);
    }

    public static boolean updateItem(Boolean isEdited, HttpServletRequest request, HttpServletResponse response) {
        String[] s_formParamsNeeded = {"productCategory", "productPrice", "productNoSerie", "qtyStock", "isProductActif", "productDescription"};

        String[] s_formValuesNeeded = {
            request.getParameter(s_formParamsNeeded[0]),
            request.getParameter(s_formParamsNeeded[1]),
            request.getParameter(s_formParamsNeeded[2]),
            request.getParameter(s_formParamsNeeded[3]),
            request.getParameter(s_formParamsNeeded[4]),
            request.getParameter(s_formParamsNeeded[5]),};
        boolean isCompleted = true;

        //HashMap des données obligatoire
        HashMap<String, String> hm_formParamValue = new HashMap<String, String>();
        for (int i = 0; i < s_formValuesNeeded.length; i++) {
            hm_formParamValue.put(s_formParamsNeeded[i], s_formValuesNeeded[i]);
        }

        isCompleted = validateForm(hm_formParamValue, request);

        if (isCompleted) {
            // Créer l'addresse en premier
            // Creer l'utilisateur et relie l'adresse
            Item item = new Item();

            item.setName(request.getParameter("productName"));
            item.setCategory(Integer.parseInt(hm_formParamValue.get("productCategory")));
            item.setSerial(hm_formParamValue.get("productNoSerie"));
            item.setPrice(Double.parseDouble(hm_formParamValue.get("productPrice")));
            item.setStock(Integer.parseInt(hm_formParamValue.get("qtyStock")));
            item.setActive(Integer.parseInt(hm_formParamValue.get("isProductActif")));
            item.setImage(request.getParameter("productImage"));
            item.setDescription(request.getParameter("productDescription").trim());
            int rep = -1;
            if (isEdited) {
                item.setId(Integer.parseInt(request.getParameter("product_id")));
                rep = MItem.modifyProductById(item);
            } else {
                rep = MItem.addProduct(item);
            }

            //Si une erreur est arrivé
            if (rep < 0) {
                isCompleted = false;
                request.setAttribute("error", "DBProblem");

            }
        }

        //HashMap pour conserver les valeurs entré par l'utilisateur (on ne le forcera pas à tous réécrire)
        if (!isCompleted) {
            hm_formParamValue.put("productName", request.getParameter("productName"));
            request.setAttribute("hm_formParamValue", hm_formParamValue);
        }

        return isCompleted;
    }

    public static boolean editItem(HttpServletRequest request, HttpServletResponse response) {
        return updateItem(Boolean.TRUE, request, response);
    }

    public static boolean addItem(HttpServletRequest request, HttpServletResponse response) {
        return updateItem(Boolean.FALSE, request, response);
    }

    public static boolean validateForm(HashMap<String, String> hm_formParamValue, HttpServletRequest request) {
        //On créer un HashMap pour contenir les potiennelles message d'erreurs
        HashMap<String, String> hm_fieldErrorMsg = new HashMap<String, String>();

        //Création des restriction de validation
        Restriction restrictName = new Restriction(false, 1, Pattern.compile("^([a-zA-Z0-9àéèêâïçÀÉÈÊÏÇ])+([ -][a-zA-Z0-9àéèêâïçÀÉÈÊÏÇ]+)*"));
        Restriction restrictStatut = new Restriction(0, 1);
        Restriction restrictNumber = new Restriction(false, 0, Pattern.compile("[0-9]*"));
        Restriction restrictDecimal = new Restriction(false, 1, Pattern.compile("[0-9]+(\\.[0-9][0-9]?)?"));

        //Création d'un objet Validation et ajout des restrictions à ce dernier
        Validation validation = new Validation(hm_formParamValue);
        //validation.addRestriction("productName", restrictName);
        validation.addRestriction("productCategory", restrictNumber);
        validation.addRestriction("productPrice", restrictDecimal);
        validation.addRestriction("productNoSerie", restrictName);
        validation.addRestriction("qtyStock", restrictNumber);
        validation.addRestriction("isProductActif", restrictStatut);
        String productName = request.getParameter("productName");
        String productDescription = request.getParameter("productDescription");

        if (productName == null || productName.trim().equals("")) {
            hm_fieldErrorMsg.put("productName", "Veuillez remplir le champ nom");
        }

        if (productDescription == null || productDescription.trim().equals("")) {
            hm_fieldErrorMsg.put("productDescription", "Veuillez remplir le champ description");
        }

        //On conserve les résultat des tests
        ArrayList<ResultValidation> resultValidations = validation.validate();

        //On parcours les résultat des tests
        for (ResultValidation rv : resultValidations) {
            //Si le test ne passe pas, alors on ajoute un message d'erreur
            if (rv.getCode() != 0) {
                hm_fieldErrorMsg.put(rv.getKey(), getErrorMsg(rv));
            }
        }

        //On passe le hashMap en attribut à la requête
        request.setAttribute("hm_fieldErrorMsg", hm_fieldErrorMsg);

        return hm_fieldErrorMsg.size() == 0;

    }

    private static String getErrorMsg(ResultValidation resValid) {
        String errorMsg = "";
        switch (resValid.getKey()) {
            case "productName":
                errorMsg += getErrorMsgForName(resValid);
                break;
            case "productCategory":
                errorMsg += getErrorMsgForFieldNumber(resValid);
                break;
            case "productPrice":
                errorMsg += getErrorMsgForProductPrice(resValid);
                break;
            case "productNoSerie":
                errorMsg += getErrorMsgForName(resValid);
                break;
            case "qtyStock":
                errorMsg += getErrorMsgForFieldNumber(resValid);
                break;
            case "isProductActif":
                errorMsg += getErrorMsgForStatut(resValid);
                break;

        }

        return errorMsg;
    }

    private static String getErrorMsgForName(ResultValidation resValid) {
        String errorMsg = "";
        Restriction restriction = resValid.getRestriction();

        switch (resValid.getCode()) {
            case 1:
                errorMsg += "Vous devez remplir le champ nom.\n";
                break;
            case 2:
            case 3:
                errorMsg += "Vous devez saisir entre " + restriction.getMinLength() + " et " + restriction.getMaxLength() + " caractère(s).\n";
                break;
            case 4:
                errorMsg += "Lettre seulement, pas de chiffre ou de caractère spéciaux. Espace et trait d'union accepté, sauf s'ils ont au début ou à la fin du nom)";
                break;
            case 5:
                break;
        }

        return errorMsg;
    }

    private static String getErrorMsgForFieldNumber(ResultValidation resValid) {
        String errorMsg = "";

        switch (resValid.getCode()) {
            case 1:
                errorMsg += "Vous devez remplir le champ \n";
            case 2:
            case 3:
            case 4:
                errorMsg += "Champ invalide \n";
            case 5:
                break;
        }

        return errorMsg;
    }

    private static String getErrorMsgForProductPrice(ResultValidation resValid) {
        String errorMsg = "";

        switch (resValid.getCode()) {
            case 1:
                errorMsg += "Vous devez remplir le champ \n";
            case 2:
            case 3:
                errorMsg += "Vous devez saisir une valuer plus grande que 0 \n ";
            case 4:
                errorMsg += "Le champ n'accepte que les nombres en decimal \n";
            case 5:
                break;
        }

        return errorMsg;
    }

    private static String getErrorMsgForStatut(ResultValidation resValid) {
        String errorMsg = "";
        switch (resValid.getCode()) {
            case 1:
                errorMsg += "Vous devez remplir le champ \n";
            case 2:
            case 3:
            case 4:
                errorMsg += "Le champ invalide";
            case 5:
                break;
        }

        return errorMsg;
    }
}
