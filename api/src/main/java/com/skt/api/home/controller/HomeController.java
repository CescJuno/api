package com.skt.api.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/front")
public class HomeController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String main(Model model){
        model.addAttribute("test","1234");
        return "home";
    }
}
