package com.cloudstorage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ResultController {

    @GetMapping("/result")
    public String result(@RequestParam("success") boolean success, Model model) {
        model.addAttribute("success", success);
        return "result";
    }

}