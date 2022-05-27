package com.txnservice.service;

import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.txnservice.dao.TransactionDao;
import com.txnservice.entity.Transaction;
import com.txnservice.entity.TransactionStatus;

@Service
public class TransactionService {
	
	private static final String TXN_CREATE_TOPIC = "txn_create";
    private static final String TXN_COMPLETE_TOPIC = "txn_complete";
    private static final String WALLET_UPDATE_TOPIC = "wallet_update";
    
    @Autowired
    private TransactionDao transactionDao;
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Transactional
    public String createTxn(Transaction transaction) {
    	
    	transaction.setTxnId(UUID.randomUUID().toString());
    	transaction.setTransactionStatus(TransactionStatus.PENDING);
    	
    	transactionDao.save(transaction);
    	
    	JSONObject jsonObject = new JSONObject();
    	jsonObject.put("sender", transaction.getSenderUserId());
        jsonObject.put("receiver", transaction.getReceiverUserId());
        jsonObject.put("amount", transaction.getAmount());
        jsonObject.put("txnId", transaction.getTxnId());
        
        kafkaTemplate.send(TXN_CREATE_TOPIC, jsonObject.toJSONString());
    	
    	return transaction.getTxnId();
    }
    
    @Transactional
    @KafkaListener(topics = {WALLET_UPDATE_TOPIC}, groupId = "ewallet_consumer_grp")
    public void updateTxn(String message) throws Exception {
    	
    	JSONObject jsonObject = (JSONObject) new JSONParser().parse(message);
    	
    	String txnId = (String) jsonObject.get("txnId");
        String status = (String) jsonObject.get("status");
        
        TransactionStatus transactionStatus;
        
        if("FAILED".equals(status)){
            transactionStatus = TransactionStatus.FAILED;
        }else{
            transactionStatus = TransactionStatus.SUCCESSFUL;
        }
        
        Transaction transaction = transactionDao.findByTxnId(txnId);
        transaction.setTransactionStatus(transactionStatus);
        
        transactionDao.save(transaction);
        
        Integer receiverId = transaction.getReceiverUserId();
        Integer senderId = transaction.getSenderUserId();
        
        JSONObject sender = restTemplate.getForObject("http://localhost:8081/user-service/user?id=" + senderId, JSONObject.class);
        String senderEmail = (String) sender.get("email");

        JSONObject receiver = restTemplate.getForObject("http://localhost:8081/user-service/user?id=" + receiverId, JSONObject.class);
        String receiverEmail = (String) receiver.get("email");
        
        JSONObject txnCompleteEvent = new JSONObject();
        txnCompleteEvent.put("txnId", txnId);
        txnCompleteEvent.put("sender", senderEmail);
        txnCompleteEvent.put("receiver", receiverEmail);
        txnCompleteEvent.put("status", transaction.getTransactionStatus().name());
        txnCompleteEvent.put("amount", transaction.getAmount());

        kafkaTemplate.send(TXN_COMPLETE_TOPIC, txnCompleteEvent.toJSONString());
        
        System.out.println("DONE");
    }
	
}
