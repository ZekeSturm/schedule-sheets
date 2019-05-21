package org.CyfrSheets.controllers;


import org.CyfrSheets.models.Participant;
import org.CyfrSheets.models.User;
import org.CyfrSheets.models.data.ParticipantDao;
import org.CyfrSheets.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.NoSuchAlgorithmException;

@Controller
@RequestMapping("test")
public class TestUIController {

    @Autowired
    private ParticipantDao participantDao;

    @Autowired
    private UserDao userDao;

    @RequestMapping
    public String testHome(Model model) {

        model.addAttribute("title","Test Home");

        return "test-ui-home";
    }

    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String testRegister(Model model) {

        model.addAttribute("title","New User Registration (Test)");
        model.addAttribute("registered", false);

        return "test-ui-register-user";
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String testRegister(Model model, @RequestParam String user, @RequestParam String pass, @RequestParam String passConfirm, @RequestParam String email) {

        // Store if there's been errors
        boolean invalid = false;
        boolean passFieldsValid = true;

        // Check for field emptiness or insufficient...ness. Replace this with proper validation later.
        if (user.length() < 3 || user.length() > 20) {
            invalid = true;
            if (user.isEmpty()) {
                model.addAttribute("userEmpty", true);
            } else {
                model.addAttribute("userOoB", true);
            }
        }

        if (pass.length() < 8 || pass.length() > 32 || passConfirm.length() < 8 || passConfirm.length() > 32) {
            invalid = true;
            passFieldsValid = false;
            if (pass.isEmpty() || passConfirm.isEmpty()) {
                model.addAttribute("passEmpty", true);
            } else {
                model.addAttribute("passOoB", true);
            }
        }

        // Check matching passwords/length
        if (passFieldsValid) {
            if (pass.length() != passConfirm.length()) {
                invalid = true;
                model.addAttribute("passLengthMismatch", true);
            } else {
                if (!pass.equals(passConfirm)) {
                    invalid = true;
                    model.addAttribute("passMismatch", true);
                }
            }
        }

        if (email.isEmpty()) {
            invalid = true;
            model.addAttribute("emailEmpty",true);
        }

        /** Just in case in-html email validation fails. Trim this at some point if it does - EDIT: Trimmed it
        if (emailFieldValid){
            boolean atFound = false;
            boolean dotFound = false;
            boolean afterDotFound = false;
            int i = 0;
            for (char c : email.toCharArray()) {
                if (dotFound) {
                    afterDotFound = true;
                }
                if (c == '.' && atFound) {
                    dotFound = true;
                }
                if (c == '@' && i != 0) {
                    atFound = true;
                }
                i++;
            }
            if (!atFound && !dotFound && !afterDotFound) {
                invalid = true;
                model.addAttribute("invalidEmail",true);
            }
        }
         */

        // check field/password validation
        if (!isValid(invalid,model)) {
            return "test-ui-register-user";
        }

        boolean userTaken = false;
        boolean emailUsed = false;

        for (User u: userDao.findAll()) {

            if (u.getEmail().toLowerCase().equals(email.toLowerCase())) {
                invalid = true;
                emailUsed = true;
            }
            if (u.checkName(user)) {
                invalid = true;
                userTaken = true;
            }
        }

        model.addAttribute(emailUsed);
        model.addAttribute(userTaken);

        //check taken user/email validation
        if (!isValid(invalid, model)) {
            return "test-ui-register-user";
        }

        try {
            User partyboi = new User(user, pass, email);
            userDao.save(partyboi);
            model.addAttribute("registered", true);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("How the fuck did you manage this?");
            model.addAttribute("registered", false);
        }

        return "test-ui-register-user";
    }

    @RequestMapping(value = "passhash", method = RequestMethod.GET)
    public String testPassHash(Model model) {

        model.addAttribute("title","Testing PassHash");

        return "test-ui-login-register";
    }

    @RequestMapping(value = "passhash", method = RequestMethod.POST)
    public String testPassHash(Model model, @RequestParam String user, @RequestParam String pass, @RequestParam(defaultValue = "false") boolean login) {

        Boolean loggedIn = false;

        if(login) {

            for (Participant p : participantDao.findAll()) {
                if (p.checkName(user)) {
                    loggedIn = p.checkPassword(pass);
                    if (loggedIn) { break; }
                }
            }

            /**
            for (Participant p : participants) {
                if (p.checkName(user)) {
                    loggedIn = p.checkPassword(pass);
                    if (loggedIn) { break; }
                }
            }
             */

            model.addAttribute("loggedin",loggedIn);

            return "test-ui-login-register";
        }

        model.addAttribute("loggedin",false);

        if (user.isEmpty() || pass.isEmpty()) {
            model.addAttribute("registered",false);
            return "test-ui-login-register";
        }

        for (Participant p : participantDao.findAll()) {
            if (p.checkName(user)) {
                model.addAttribute("nameTaken",true);
                return "test-ui-login-register";
            }
        }

        participantDao.save(new Participant(user, pass));
        model.addAttribute("registered",true);

        return "test-ui-login-register";
    }

    private boolean isValid(boolean invalid, Model model) {
        if (invalid) {
            model.addAttribute("registered", false);
        }
        return !invalid;
    }
}
