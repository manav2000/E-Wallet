package com.txnservice.request;

import javax.validation.constraints.Positive;

import com.txnservice.entity.Transaction;

public class TransactionCreateRequest {
	
	@Positive
    private Integer senderUserId;

    @Positive
    private Integer receiverUserId;

    @Positive
    private Double amount;

    private String purpose;

	public TransactionCreateRequest() {
	}

	public TransactionCreateRequest(@Positive Integer senderUserId, @Positive Integer receiverUserId,
			@Positive Double amount, String purpose) {
		this.senderUserId = senderUserId;
		this.receiverUserId = receiverUserId;
		this.amount = amount;
		this.purpose = purpose;
	}

	public Integer getSenderUserId() {
		return senderUserId;
	}

	public void setSenderUserId(Integer senderUserId) {
		this.senderUserId = senderUserId;
	}

	public Integer getReceiverUserId() {
		return receiverUserId;
	}

	public void setReceiverUserId(Integer receiverUserId) {
		this.receiverUserId = receiverUserId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
    
	public Transaction toTransaction() {
		
		Transaction transaction = new Transaction();
		
		transaction.setSenderUserId(senderUserId);
		transaction.setReceiverUserId(receiverUserId);
		transaction.setAmount(amount);
		transaction.setPurpose(purpose);
		
		return transaction;
	}
}
