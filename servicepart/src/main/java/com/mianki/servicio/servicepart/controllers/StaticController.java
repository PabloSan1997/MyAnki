package com.mianki.servicio.servicepart.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaticController {
    @GetMapping({"/login", "/home", "/"})
    public String index() {
        return "index";
    }
}
