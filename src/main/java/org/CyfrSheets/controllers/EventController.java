package org.CyfrSheets.controllers;

import org.CyfrSheets.models.Participant;
import org.CyfrSheets.models.SEvent;
import org.CyfrSheets.models.TempUser;
import org.CyfrSheets.models.User;
import org.CyfrSheets.models.data.EventDao;
import org.CyfrSheets.models.data.UserDao;
import org.CyfrSheets.models.forms.NewEventForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("event")
public class EventController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private EventDao eventDao;

    // Landing page for events. Create new event. (Logged in registered users may get a different/"index" landing page later)
    //TODO - Implement basic static event functionality w/ temporary users and no login first. Focus on that and leave
    //the rest for later
    @GetMapping("add-event")
    public String newEvent(Model model) {

        model.addAttribute("title","Create New Event");
        model.addAttribute("form", new NewEventForm());

        return "/add";
    }

    private LoginErrorLog handleNewLogin(String username, String pass, SEvent container) {
        LoginErrorLog eLog = new LoginErrorLog();
        boolean logSuccess = false;
        for (User u : userDao.findAll()) {
            if (u.checkName(username)) {
                if (u.checkPassword(pass)) {
                    logSuccess = true;
                    eLog.logSucceeded(u);
                } else {
                    eLog.nameTaken = true;
                    eLog.userExists = true;
                    eLog.badPassword = true;
                }
            }
        }
        for (Map.Entry<Integer, String> entry : container.getTempUsers().entrySet()) {
            if (entry.getValue().equals(username)) {
                if (container.tempHasPass(entry.getKey())) {
                    if (container.passCheck(entry.getKey(),pass)) {
                        logSuccess = true;
                        eLog.logSucceeded(entry.getKey(),entry.getValue());
                    } else {
                        eLog.nameTaken = true;
                        eLog.badPassword = true;
                    }
                } else {
                    eLog.nameTaken = true;
                }
            }
        }

        if (logSuccess) {
            // handle login junk here. Or back when it's set to true, this is a placeholder while I work on cookies and sessions. Or handle it in logSucceeded I'm honestly running blind here for now
        }

        return eLog;
    }



    private class LoginErrorLog {

        public boolean userExists;
        public boolean nameTaken;
        public boolean badPassword;

        private boolean logSuccess;
        private boolean isUser;

        private User user;
        private int tempUserID;
        private String tempUserName;


        public LoginErrorLog() {
            userExists = false;
            nameTaken = false;
            badPassword = false;

            logSuccess = false;
        }

        private void logSucceeded(User u) {
            // Pass login status and registered/unregistered status along to the session handler.... also figure out how
            // session handlers work.
            logSuccess = true;
            user = u;
            isUser = true;
        }

        private void logSucceeded(int id, String name) {
            // Temporary user login shit as above. Figure out... new storage for all this now that non-registered user
            // classes are being phased out/deprecated
            tempUserID = id;
            tempUserName = name;
            isUser = false;
        }
    }

}
