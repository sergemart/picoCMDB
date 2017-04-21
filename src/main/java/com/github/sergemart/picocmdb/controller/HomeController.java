package com.github.sergemart.picocmdb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

import com.github.sergemart.picocmdb.domain.ManagedArea;
import com.github.sergemart.picocmdb.system.CurrentSessionSettings;
import com.github.sergemart.picocmdb.domain.ConfigurationItemRelationType;
import com.github.sergemart.picocmdb.service.ConfigurationItemRelationTypeService;


@Controller
public class HomeController {

	@Autowired
	ConfigurationItemRelationTypeService ciRelationTypeService;

	@Autowired
	CurrentSessionSettings currentSessionSettings;


	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String processGetHome() {
		return "redirect:/graph";
	}


	@RequestMapping(value = "/graph", method = RequestMethod.GET)
    public String processGetMain(Model model) {
		List<ConfigurationItemRelationType> availableCiRelationTypes = ciRelationTypeService.getAllRelationTypes();
		model.addAttribute("availableCiRelationTypes", availableCiRelationTypes);

		ManagedArea currentManagedArea = currentSessionSettings.getCurrentManagedArea();
		model.addAttribute("currentManagedArea", currentManagedArea);

		ConfigurationItemRelationType currentCiRelationType = currentSessionSettings.getCurrentCiRelationType();
		model.addAttribute("currentCiRelationType", currentCiRelationType);

		return "graph";
    }
	
}
