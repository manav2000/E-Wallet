package com.walletservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WalletController {

	@GetMapping("/test")
	public String testWalletApi() {
		return "message from test api";
	}
	
}
