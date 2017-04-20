package com.github.sergemart.picocmdb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class HomeController {
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String processGetHome() {
		return "redirect:/graph";
	}


	@RequestMapping(value = "/graph", method = RequestMethod.GET)
    public String processGetMain(Model model) {
        return "graph";
    }
	
}
