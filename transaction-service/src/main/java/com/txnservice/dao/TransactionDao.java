package com.txnservice.dao;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.txnservice.entity.Transaction;

@Repository
public class TransactionDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public Transaction findByTxnId(String txnId) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createQuery("from Transaction where txnId=:txnId");
		query.setParameter("txnId", txnId);
		
		Transaction transaction = null;
		
		transaction = (Transaction) query.getSingleResult();
		
		return transaction;
	}
	
	public int save(Transaction transaction) {
		
		Session session = sessionFactory.getCurrentSession();
		
		return (int) session.save(transaction);
	}
	
	public void update(Transaction transaction) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createQuery("update Transaction set transactionStatus=:transactionStatus where txnId=:txnId");
		query.setParameter("transactionStatus", transaction.getTransactionStatus().name());
		query.setParameter("txnId", transaction.getTxnId());
		
		query.executeUpdate();
	}
	
}
