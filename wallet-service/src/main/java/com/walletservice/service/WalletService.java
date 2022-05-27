package com.walletservice.service;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.walletservice.dao.WalletDao;
import com.walletservice.entity.Wallet;

@Service
public class WalletService {
	
	private static final Double onboardingAmount = 100.0;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	@Autowired
	private WalletDao walletDao;
	
	private static final String USER_CREATE_TOPIC = "user_create";
    private static final String TXN_CREATE_TOPIC = "txn_create";
    private static final String WALLET_UPDATE_TOPIC = "wallet_update";
    
    @Transactional
    @KafkaListener(topics = {USER_CREATE_TOPIC}, groupId = "ewallet_consumer_grp")
    public void createWallet(String message) throws Exception {
    	
    	JSONObject jsonObject = (JSONObject) new JSONParser().parse(message);
    	
    	if(!jsonObject.containsKey("userId")) {
    		throw new Exception("userId is not present in the user event");
    	}
    	
    	int userId = ((Long) jsonObject.get("userId")).intValue();
    	
    	Wallet wallet = new Wallet();
    	
    	wallet.setUserId(userId);
    	wallet.setBalance(onboardingAmount);
    	
    	walletDao.save(wallet);
    }
    
    @Transactional
    @KafkaListener(topics = {TXN_CREATE_TOPIC}, groupId = "ewallet_consumer_grp")
    public void walletUpdate(String message) throws Exception {
    	
    	JSONObject jsonObject = (JSONObject) new JSONParser().parse(message);
    	
    	if(!jsonObject.containsKey("sender") ||
                !jsonObject.containsKey("receiver") ||
                !jsonObject.containsKey("amount") ||
                !jsonObject.containsKey("txnId")){
            throw new Exception("some of the details are not present in the txn create event");
        }
    	
    	Integer receiverId = ((Long) jsonObject.get("receiver")).intValue();
        Integer senderId = ((Long) jsonObject.get("sender")).intValue();
        Double amount = (Double) jsonObject.get("amount");
        String txnId = (String) jsonObject.get("txnId");
        
        JSONObject walletUpdateEvent = new JSONObject();
        walletUpdateEvent.put("txnId", txnId);
        
        Wallet senderWallet = walletDao.findByUserId(senderId);
        if(senderWallet.getBalance() < amount){
            // This status is telling about the wallet updation whether it's successful or not
            walletUpdateEvent.put("status", "FAILED");
        }else{
        	walletDao.updateWalletBalance(receiverId, amount);
        	walletDao.updateWalletBalance(senderId, 0 - amount);
            walletUpdateEvent.put("status", "SUCCESSFUL");
        }
        
        kafkaTemplate.send(WALLET_UPDATE_TOPIC, walletUpdateEvent.toJSONString());
    }
	
}
