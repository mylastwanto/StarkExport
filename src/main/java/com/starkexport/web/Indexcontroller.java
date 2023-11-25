package com.starkexport.web;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Indexcontroller {
    @RequestMapping(value="/", method = RequestMethod.GET)
    public String index(Model model){
        return "home";
    }
}
