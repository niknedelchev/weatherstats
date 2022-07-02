package com.group9.weatherstats.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.group9.weatherstats.model.User;
import com.group9.weatherstats.service.UserService;

@Controller
public class IndexController {
	@Autowired
	private UserService userService;

	@GetMapping(path = "/")
	public String getIndexPage(Model model) {
		return "index";
	}

	@GetMapping(path = "login")
	public String showLoginPage() {
		return "user/login";
	}

	@GetMapping(path = "register")
	public String showRegisterPage() {
		return "user/registration";
	}

	@PostMapping(path = "register")
	public String register(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String username,
			@RequestParam String password) {

		userService.registerRegular(firstName, lastName, username, password);
		return "redirect:/";
	}

	@GetMapping(path = "about")
	public String showAboutPage() {
		return "about";
	}


	
}
