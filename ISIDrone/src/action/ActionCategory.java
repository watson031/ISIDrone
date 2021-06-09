package action;

import entities.Category;
import entities.Item;

import entities.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import manager.MCategory;

import manager.MItem;
import util.Misc;
import util.Restriction;
import util.ResultValidation;
import util.Validation;

import util.Restriction;
import util.ResultValidation;
import util.Validation;

public class ActionCategory {

    public static void getCategories(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("categories", MCategory.getCategories());
    }

    public static void getClientCategories(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("categories", MCategory.getClientCategories());
    }

    public static int getSelectedCategory(HttpServletRequest request, HttpServletResponse response) {
        //Permet de recevoir la catégorie sélectionné par l'utilisateur
        String paramCategory = request.getParameter("category");

        //ArrayList<Category> categories = MCategory.getCategories();
        int categorySelected;

        if (paramCategory != null) {
            try {
                //Si l'utilisateur entre lui même une valeur pour le paramêtre category dans la barre d'adresse
                // alors s'il la catégorie est invalide, alors la catégorie sélectionné deviendra 1 (qui représente toutes les catégories)
                categorySelected = Integer.valueOf(paramCategory);
                if (MCategory.isExist(categorySelected) != 0) {
                    categorySelected = 1;
                }
            } catch (NumberFormatException e) {
                categorySelected = 1;
            }
        } else {
            categorySelected = 1;
        }

        return categorySelected;
    }

    public static void getCategoriesByOrder(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("categoriesByOrder", MCategory.getCategoriesByOrder());
    }

    public static boolean updateCategory(Boolean isEdited, HttpServletRequest request, HttpServletResponse response) {
        // 
        String[] s_formParamsNeeded = {"categoryName", "categoryPosition", "categoryStatut"},
                s_formParamsOptional = {"categoryDescription"
                };
        //
        String[] s_formValuesNeeded = {
            request.getParameter(s_formParamsNeeded[0]),
            request.getParameter(s_formParamsNeeded[1]),
            request.getParameter(s_formParamsNeeded[2]),};

        String[] s_formValueOptional = {
            request.getParameter(s_formParamsOptional[0])
        };
        //
        boolean isCompleted = true;
        //HashMap des données obligatoire
        HashMap<String, String> hm_formParamValue = new HashMap<String, String>();
        for (int i = 0; i < s_formValuesNeeded.length; i++) {
            hm_formParamValue.put(s_formParamsNeeded[i], s_formValuesNeeded[i]);
        }

        for (int i = 0; i < s_formValueOptional.length; i++) {
            hm_formParamValue.put(s_formParamsOptional[i], s_formValueOptional[i]);
        }
        // 
        isCompleted = validateForm(hm_formParamValue, request,isEdited);
        //
        if (isCompleted) {
            //
            Category category = new Category();

            category.setName(request.getParameter("categoryName"));
            category.setDescription(Misc.getOrDefault(hm_formParamValue, "categoryDescription", ""));
            category.setOrder(Integer.parseInt(hm_formParamValue.get("categoryPosition")));
            category.setIsActive((hm_formParamValue.get("categoryStatut").equals("1")));
            String categoryId = request.getParameter("category_id");
            int rep = -1;
            if (isEdited) {
                if (categoryId != null) {
                    int catId = Integer.parseInt(categoryId);
                    category.setId(catId);
                    rep = MCategory.modifyCategory(category);
                }

            } else {
                rep = MCategory.addCategory(category);
            }
            //Si une erreur est arrivé
            if (rep < 0) {
                isCompleted = false;
                request.setAttribute("error", "DBProblem");

            }
        }
        //HashMap pour conserver les valeurs entré par l'utilisateur (on ne le forcera pas à tous réécrire)
        if (!isCompleted) {
            hm_formParamValue.put("categoryName", request.getParameter("categoryName"));
            request.setAttribute("hm_formParamValue", hm_formParamValue);
        }
        //
        return isCompleted;
    }

    public static boolean addCategory(HttpServletRequest request, HttpServletResponse response) {
        return updateCategory(Boolean.FALSE, request, response);
    }

    public static boolean validateForm(HashMap<String, String> hm_formParamValue, HttpServletRequest request, Boolean isEdited) {
        //On créer un HashMap pour contenir les potiennelles message d'erreurs
        HashMap<String, String> hm_fieldErrorMsg = new HashMap<String, String>();

        //Création des restriction de validation
        Restriction restrictName = new Restriction(false, 1, Pattern.compile("^([a-zA-Z0-9àéèêâïçÀÉÈÊÏÇ])+([ -][a-zA-Z0-9àéèêâïçÀÉÈÊÏÇ]+)*"));
        Restriction restrictStatut = new Restriction(0, 1);
        Restriction restrictNumber = new Restriction(false, 0, Pattern.compile("[0-9]*"));

        //Création d'un objet Validation et ajout des restrictions à ce dernier
        Validation validation = new Validation(hm_formParamValue);
        //validation.addRestriction("categoryName", restrictName);
        //validation.addRestriction("categoryDescription", restrictName);
        //
        validation.addRestriction("categoryPosition", restrictNumber);
        validation.addRestriction("categoryStatut", restrictStatut);
        //
        String categoryName = request.getParameter("categoryName");
        String categoryDescription = request.getParameter("categoryDescription");

        if (categoryName == null || categoryName.trim().equals("")) {
            hm_fieldErrorMsg.put("categoryName", "Veuillez remplir le champ nom");
        } else {
            // Validate by name
            if (MCategory.isExistByName(categoryName) == 1 && !isEdited) {
                hm_fieldErrorMsg.put("categoryName", "Categotie existant");
            }
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
            case "categoryName":
                errorMsg += getErrorMsgForName(resValid);
                break;
            case "categoryPosition":
                errorMsg += getErrorMsgForFieldNumber(resValid);
                break;
            case "categoryStatut":
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

    public static void getCategoryById(int id, HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("category", MCategory.getCategorieById(id));
    }

    public static void deleteCategoryById(int idCategory, HttpServletRequest request, HttpServletResponse response) {
        MCategory.deleteCategoryById(idCategory);
        getCategoriesByOrder(request, response);
    }

    public static boolean editCategory(HttpServletRequest request, HttpServletResponse response) {
        boolean isUpdated = updateCategory(Boolean.TRUE, request, response);
        ActionCategory.getCategoriesByOrder(request, response);
        return isUpdated;
    }

}
