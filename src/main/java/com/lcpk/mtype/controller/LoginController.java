package com.lcpk.mtype.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
	@GetMapping("/lgin")
	public String login() {
		return "lgin";
	}
}
