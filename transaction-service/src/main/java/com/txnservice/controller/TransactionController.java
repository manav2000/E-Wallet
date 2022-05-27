package com.txnservice.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.txnservice.request.TransactionCreateRequest;
import com.txnservice.service.TransactionService;

@RestController
public class TransactionController {

	@Autowired
	private TransactionService transactionService;
	
	@PostMapping("/transact")
	public String createTxn(@Valid @RequestBody TransactionCreateRequest transactionCreateRequest) {
		
		String txnId = transactionService.createTxn(transactionCreateRequest.toTransaction());
		
		return "Your transaction has been initiated, here's the transaction id : " + txnId;
	}
	
}
