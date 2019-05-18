package org.CyfrSheets.controllers;


import org.CyfrSheets.models.Participant;
import org.CyfrSheets.models.data.ParticipantDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("test")
public class TestUIController {

    private List<Participant> participants = new ArrayList<>();

    @Autowired
    private ParticipantDao participantDao;

    @RequestMapping(value = "passhash", method = RequestMethod.GET)
    public String testPassHash(Model model) {

        model.addAttribute("title","Testing PassHash");

        return "test-ui";
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

            return "test-ui";
        }

        model.addAttribute("loggedin",false);

        if (user.isEmpty() || pass.isEmpty()) {
            model.addAttribute("registered",false);
            return "test-ui";
        }

        for (Participant p : participantDao.findAll()) {
            if (p.checkName(user)) {
                model.addAttribute("nameTaken",true);
                return "test-ui";
            }
        }

        Participant partyboi = new Participant(user, pass);
        participants.add(partyboi);
        participantDao.save(partyboi);
        model.addAttribute("registered",true);

        return "test-ui";
    }
}
