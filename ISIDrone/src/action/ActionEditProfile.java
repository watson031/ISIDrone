/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import entities.Address;
import entities.User;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import manager.MSignUp;
import manager.MUser;
import util.Hash;
import util.Misc;
import util.Restriction;
import util.ResultValidation;
import util.Validation;

/**
 *
 * @author rdossant
 */
public class ActionEditProfile {
    
    public static boolean editProfile( User currUser, HttpServletRequest request, HttpServletResponse response) {
        String[] s_formParamsNeeded = {"lastName", "firstName", "email", "oldPassword", "password", "confirmPassword",
            "addr_no", "addr_street", "addr_zip", "addr_city", "addr_state", "addr_country"},
                s_formParamsOptional = {"addr_appt"};
        String[] s_formValuesNeeded = {
            request.getParameter(s_formParamsNeeded[0]),
            request.getParameter(s_formParamsNeeded[1]),
            request.getParameter(s_formParamsNeeded[2]).toLowerCase(),
            request.getParameter(s_formParamsNeeded[3]).toLowerCase(),
            request.getParameter(s_formParamsNeeded[4]),
            request.getParameter(s_formParamsNeeded[5]),
            request.getParameter(s_formParamsNeeded[6]),
            request.getParameter(s_formParamsNeeded[7]),
            request.getParameter(s_formParamsNeeded[8]),
            request.getParameter(s_formParamsNeeded[9]),
            request.getParameter(s_formParamsNeeded[10]),
            request.getParameter(s_formParamsNeeded[11])};

        String[] s_formValueOptional = {
            request.getParameter(s_formParamsOptional[0])
        };

        boolean isCompleted = true;

        //HashMap des donn??es obligatoire
        HashMap<String, String> hm_formParamValue = new HashMap<String, String>();
        for (int i = 0; i < s_formValuesNeeded.length; i++) {
            hm_formParamValue.put(s_formParamsNeeded[i], s_formValuesNeeded[i]);
        }

        //HasMap des donn??es Optionnel
        for (int i = 0; i < s_formValueOptional.length; i++) {
            hm_formParamValue.put(s_formParamsOptional[i], s_formValueOptional[i]);
        }
        //
        String newPassword = hm_formParamValue.get("oldPassword");
        try {
            newPassword = Hash.SHA1(newPassword);
        } catch ( NoSuchAlgorithmException | UnsupportedEncodingException e) {
                e.printStackTrace();
        }
        //
        Boolean pwdOk = ( currUser.getPassword().equals(newPassword));
        //
        //Valide le formulaire et enregriste les message d'erreur dans la requ??te
        isCompleted = validateForm(hm_formParamValue, request);

        if ( isCompleted){
            if ( !pwdOk){
                isCompleted = false;
                
            }
        }
        //
        //On fait maintenant l'insertion des donn??es dans la base de donn??es
        if (isCompleted) {
            // Cr??er l'addresse en premier
            // Creer l'utilisateur et relie l'adresse
            //
            User user = new User();
            Address address = new Address();
       
            //Utilisez Misc.getOrDefault pour les champs qui sont optionnel, car il se pourrait que sa retourne null
            // Par exemple si on le supprime du formulaire (en ??ditant la page) et il n'est pas test?? plus haut
            address.setId(currUser.getId());
            address.setNo(hm_formParamValue.get("addr_no"));
            address.setAppt(Misc.getOrDefault(hm_formParamValue, "addr_appt", ""));
            address.setStreet(hm_formParamValue.get("addr_street"));
            address.setZip(hm_formParamValue.get("addr_zip"));
            address.setCity(hm_formParamValue.get("addr_city"));
            address.setState(hm_formParamValue.get("addr_state"));
            address.setCountry(hm_formParamValue.get("addr_country"));
            //
            user.setId(currUser.getShipAddress().getId());
            //
            user.setLastName(hm_formParamValue.get("lastName"));
            user.setFirstName(hm_formParamValue.get("firstName"));
            user.setEmail(hm_formParamValue.get("email"));
            user.setPassword(hm_formParamValue.get("password"));
            user.setShipAdress(address);
            //
            int rep = MUser.editClient(user);
            int rep1 = MUser.editClientPassword(user);
            //
            //Si une erreur est arriv??
            if (rep < 1) {
                isCompleted = false;
                if (rep == 0) {
                    request.setAttribute("error", "accountExisting");
                } else if (rep == -1) {
                    request.setAttribute("error", "DBProblem");
                }
            }
        }

        //HashMap pour conserver les valeurs entr?? par l'utilisateur (on ne le forcera pas ?? tous r????crire)
        if (!isCompleted) {
            request.setAttribute("hm_formParamValue", hm_formParamValue);
        }

        return isCompleted;
    }

    public static boolean validateForm(HashMap<String, String> hm_formParamValue, HttpServletRequest request) {
        //On cr??er un HashMap pour contenir les potiennelles message d'erreurs
        HashMap<String, String> hm_fieldErrorMsg = new HashMap<String, String>();

        //Cr??ation des restriction de validation
        Restriction restrict1 = new Restriction(1, 45, Pattern.compile("^([a-zA-Z??????????????????????????])+([ -][a-zA-Z??????????????????????????]+)*"));

        Restriction restrictEmail = new Restriction(1, 45, Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?"));
        

        Restriction restrictPassword = new Restriction(8, 40, Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$"));
        Restriction restrictConfirmPassword = new Restriction(hm_formParamValue.get("password"));

        Restriction restrictAddr_no = new Restriction(1, 10, Pattern.compile("[a-zA-z0-9]*"));
        Restriction restrictionAddr_app = new Restriction(true, 1, 10, Pattern.compile("[a-zA-z0-9]*"));
        Restriction restrictionAddr_street = new Restriction(1, 45);
        Restriction restrictAddr_zip = new Restriction();
        
        //Cr??ation d'un objet Validation et ajout des restrictions ?? ce dernier
        Validation validation = new Validation(hm_formParamValue);
        validation.addRestriction("lastName", restrict1);
        validation.addRestriction("firstName", restrict1);
        validation.addRestriction("email", restrictEmail);
        validation.addRestriction("password", restrictPassword);
        validation.addRestriction("confirmPassword", restrictConfirmPassword);
        validation.addRestriction("addr_no", restrictAddr_no);
        validation.addRestriction("addr_appt", restrictionAddr_app);
        validation.addRestriction("addr_street", restrictionAddr_street);
        validation.addRestriction("addr_zip", restrictAddr_zip);
        validation.addRestriction("addr_city", restrict1);
        validation.addRestriction("addr_state", restrict1);
        validation.addRestriction("addr_country", restrict1);

        //On conserve les r??sultat des tests
        ArrayList<ResultValidation> resultValidations = validation.validate();
        //On parcours les r??sultat des tests
        for (ResultValidation rv : resultValidations) {
            //Si le test ne passe pas, alors on ajoute un message d'erreur
            if (rv.getCode() != 0) {
                hm_fieldErrorMsg.put(rv.getKey(), getErrorMsg(rv));
            }
        }

        //On passe le hashMap en attribut ?? la requ??te
        request.setAttribute("hm_fieldErrorMsg", hm_fieldErrorMsg);

        return validation.isValidate();
    }

    private static String getErrorMsg(ResultValidation resValid) {
        String errorMsg = "";
        switch (resValid.getKey()) {
            case "lastName":
                errorMsg += getErrorMsgForLastName(resValid);
                break;
            case "firstName":
                errorMsg += getErrorMsgForFirstName(resValid);
                break;
            case "email":
                errorMsg += getErrorMsgForEmail(resValid);
                break;
            case "oldPassword":
                errorMsg += getErrorMsgForOldPassword(resValid);
                break;
            case "password":
                errorMsg += getErrorMsgForPassword(resValid);
                break;
            case "confirmPassword":
                errorMsg += getErrorMsgForConfirmPassword(resValid);
                break;
            case "addr_no":
                errorMsg += getErrorMsgForAddr_no(resValid);
                break;
            case "addr_appt":
                errorMsg += getErrorMsgForAddr_appt(resValid);
                break;
            case "addr_street":
                errorMsg += getErrorMsgForAddr_street(resValid);
                break;
            case "addr_zip":
                errorMsg += getErrorMsgForAddr_zip(resValid);
                break;
            case "addr_city":
                errorMsg += getErrorMsgForAddr_city(resValid);
                break;
            case "addr_state":
                errorMsg += getErrorMsgForAddr_state(resValid);
                break;
            case "addr_country":
                errorMsg += getErrorMsgForAddr_country(resValid);
                break;
        }

        return errorMsg;
    }

    private static String getErrorMsgForLastName(ResultValidation resValid) {
        String errorMsg = "";
        Restriction restriction = resValid.getRestriction();

        switch (resValid.getCode()) {
            case 1:
                errorMsg += "Vous devez remplir le champ nom.\n";
                break;
            case 2:
            case 3:
                errorMsg += "Vous devez saisir entre " + restriction.getMinLength() + " et " + restriction.getMaxLength() + " caract??re(s).\n";
                break;
            case 4:
                errorMsg += "Lettre seulement, pas de chiffre ou de caract??re sp??ciaux. Espace et trait d'union accept??, sauf s'ils ont au d??but ou ?? la fin du nom)";
                break;
            case 5:
                break;
        }

        return errorMsg;
    }

    private static String getErrorMsgForFirstName(ResultValidation resValid) {
        String errorMsg = "";
        Restriction restriction = resValid.getRestriction();

        switch (resValid.getCode()) {
            case 1:
                errorMsg += "Vous devez remplir le champ pr??nom.\n";
            case 2:
            case 3:
                errorMsg += "Vous devez saisir entre " + restriction.getMinLength() + " et " + restriction.getMaxLength() + " caract??re(s).\n";
            case 4:
                errorMsg += "Lettre seulement, pas de chiffre ou de caract??re sp??ciaux. Espace et trait d'union accept??, sauf s'ils ont au d??but ou ?? la fin du nom)";
                break;
            case 5:
                break;
        }

        return errorMsg;
    }

    private static String getErrorMsgForEmail(ResultValidation resValid) {
        String errorMsg = "";
        Restriction restriction = resValid.getRestriction();

        switch (resValid.getCode()) {
            case 1:
                errorMsg += "Vous devez remplir le champ email.\n";
            case 2:
            case 3:
                errorMsg += "Vous devez saisir entre " + restriction.getMinLength() + " et " + restriction.getMaxLength() + " caract??re(s).\n";
            case 4:
                errorMsg += "Veuillez entrez une adresse e-mail valide";
            case 5:
                break;
        }

        return errorMsg;
    }

    private static String getErrorMsgForOldPassword(ResultValidation resValid) {
        String errorMsg = "";

        switch (resValid.getCode()) {
            case 1:
                errorMsg += "Vous devez remplir le champ avce votre mot de passe actuel.\n";
                break;
            case 2:
            case 3:
            case 4:
            case 5:
                errorMsg += "Le mot de passe saisie ne correspond pas a votre mot de passe actuel.";
                break;
        }

        return errorMsg;
    }

    private static String getErrorMsgForPassword(ResultValidation resValid) {
        String errorMsg = "";
        Restriction restriction = resValid.getRestriction();

        switch (resValid.getCode()) {
            case 1:
                errorMsg += "Vous devez remplir le champ mot de passe.\n";
            case 2:
            case 3:
                errorMsg += "Vous devez saisir entre " + restriction.getMinLength() + " et " + restriction.getMaxLength() + " caract??re(s).\n";
            case 4:
                errorMsg += "Veuillez entrez une mot de passe comprenand au moins 1 minuscule, 1 majuscule, 1 chiffre et 1 caract??re sp??cial.";
            case 5:
                break;
        }

        return errorMsg;
    }

    private static String getErrorMsgForConfirmPassword(ResultValidation resValid) {
        String errorMsg = "";

        switch (resValid.getCode()) {
            case 1:
                errorMsg += "Vous devez remplir le champ de confirmation pour le mot de passe.\n";
                break;
            case 2:
            case 3:
            case 4:
            case 5:
                errorMsg += "Le mot de passe ne correspond pas avec la confirmation du mot de passe.";
                break;
        }

        return errorMsg;
    }

    private static String getErrorMsgForAddr_no(ResultValidation resValid) {
        String errorMsg = "";
        Restriction restriction = resValid.getRestriction();

        switch (resValid.getCode()) {
            case 1:
                errorMsg += "Vous devez remplir le champ no.\n";
            case 2:
            case 3:
                errorMsg += "Vous devez saisir entre " + restriction.getMinLength() + " et " + restriction.getMaxLength() + " caract??re(s).\n";
            case 4:
                errorMsg += "Le champ n'accepte que les nombres ou lettres sans espace";
            case 5:
                break;
        }

        return errorMsg;
    }

    private static String getErrorMsgForAddr_appt(ResultValidation resValid) {
        String errorMsg = "Le champ est optionnel, parcontre si vous le renseignez :\n";
        Restriction restriction = resValid.getRestriction();

        switch (resValid.getCode()) {
            case 1:
            case 2:
            case 3:
                errorMsg += "Vous devez saisir entre " + restriction.getMinLength() + " et " + restriction.getMaxLength() + " caract??re(s).\n";
            case 4:
                errorMsg += "Le champ n'accepte que les chiffres et lettres";
            case 5:
                break;
        }

        return errorMsg;
    }

    private static String getErrorMsgForAddr_street(ResultValidation resValid) {
        String errorMsg = "";
        Restriction restriction = resValid.getRestriction();

        switch (resValid.getCode()) {
            case 1:
                errorMsg += "Vous devez remplir le champ rue.\n";
            case 2:
            case 3:
                errorMsg += "Vous devez saisir entre " + restriction.getMinLength() + " et " + restriction.getMaxLength() + " caract??re(s).";
            case 4:
            case 5:
                break;
        }

        return errorMsg;
    }

    private static String getErrorMsgForAddr_zip(ResultValidation resValid) {
        String errorMsg = "";
        switch (resValid.getCode()) {
            case 1:
                errorMsg += "Vous devez remplir le champ code postal";
            case 2:
            case 3:
            case 4:
            case 5:
                break;
        }

        return errorMsg;
    }

    private static String getErrorMsgForAddr_city(ResultValidation resValid) {
        String errorMsg = "";
        Restriction restriction = resValid.getRestriction();

        switch (resValid.getCode()) {
            case 1:
                errorMsg += "Vous devez remplir le champ ville.\n";
            case 2:
            case 3:
                errorMsg += "Vous devez saisir entre " + restriction.getMinLength() + " et " + restriction.getMaxLength() + " caract??re(s).\n";
            case 4:
                errorMsg += "Seules les lettres et le caract??re d'espacement est accept??.";
            case 5:
                break;
        }

        return errorMsg;
    }

    private static String getErrorMsgForAddr_state(ResultValidation resValid) {
        String errorMsg = "";
        Restriction restriction = resValid.getRestriction();

        switch (resValid.getCode()) {
            case 1:
                errorMsg += "Vous devez remplir le champ province.\n";
            case 2:
            case 3:
                errorMsg += "Vous devez saisir entre " + restriction.getMinLength() + " et " + restriction.getMaxLength() + " caract??re(s).\n";
            case 4:
                errorMsg += "Seules les lettres et le caract??re d'espacement est accept??.";
            case 5:
                break;
        }

        return errorMsg;
    }

    private static String getErrorMsgForAddr_country(ResultValidation resValid) {
        String errorMsg = "";
        Restriction restriction = resValid.getRestriction();

        switch (resValid.getCode()) {
            case 1:
                errorMsg += "Vous devez remplir le champ pays\n";
            case 2:
            case 3:
                errorMsg += "Vous devez saisir entre " + restriction.getMinLength() + " et " + restriction.getMaxLength() + " caract??re(s).\n";
            case 4:
                errorMsg += "Seules les lettres et le caract??re d'espacement est accept??.";
            case 5:
                break;
        }

        return errorMsg;
    }
    
    
}
