package org.CyfrSheets.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("test")
public class TestUIController {

    @RequestMapping(value = "passhash", method = RequestMethod.GET)
    public String testPassHash(Model model) {

        Boolean loggingin = false;
        model.addAttribute("loggingin",loggingin);

        return "test-ui";
    }

    @RequestMapping(value = "passhash", method = RequestMethod.POST)
    public String testPassHash(Model model, @RequestParam String user, @RequestParam String pass, @RequestParam Boolean loggingin) {

        if(loggingin) {

            

            return "test-ui";
        }



        return "test-ui";
    }
}
