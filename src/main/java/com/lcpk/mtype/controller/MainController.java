package com.lcpk.mtype.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
	@GetMapping("/")
	public String index(Model model) {
		return "index";
	}
	
	@Value("${portone.public.store-id}")
	private String portoneStoreId;
	
	@Value("${portone.public.channel-key}")
	private String portoneChannelKey;
	
	@GetMapping("/portone")
	public String portone(Model model) {
		model.addAttribute("portoneStoreId", portoneStoreId);
		model.addAttribute("portoneChannelKey", portoneChannelKey);
		return "portone";
	}
}
