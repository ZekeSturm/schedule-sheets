package org.CyfrSheets.controllers;

import org.CyfrSheets.models.Participant;
import org.CyfrSheets.models.SEvent;
import org.CyfrSheets.models.TempUser;
import org.CyfrSheets.models.User;
import org.CyfrSheets.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class EventController {

    @Autowired
    private UserDao userDao;


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
        for (TempUser tU : container.getTempUsers()) {
            if (tU.checkName(username)) {
                if (tU.hasPass()) {
                    if (tU.checkPassword(pass)) {
                        logSuccess = true;
                        eLog.logSucceeded(tU);
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

        private Participant p;


        public LoginErrorLog() {
            userExists = false;
            nameTaken = false;
            badPassword = false;

            logSuccess = false;
        }

        private void logSucceeded(Participant p) {
            // Pass login status and registered/unregistered status along to the session handler.... also figure out how
            // session handlers work.
            logSuccess = true;
            this.p = p;
            isUser = p.isUser();
        }
    }

}
