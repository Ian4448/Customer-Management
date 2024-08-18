package com.borges.project.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomePageAPI {

    @RequestMapping({"/", "/home", "/home/"})
    public String getHome() {
        return "home";
    }
}
