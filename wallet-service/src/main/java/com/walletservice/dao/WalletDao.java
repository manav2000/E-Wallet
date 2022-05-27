package com.walletservice.dao;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.walletservice.entity.Wallet;

@Repository
public class WalletDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public Wallet findByUserId(int userId) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createQuery("from Wallet where userId=:userId");
		query.setParameter("userId", userId);
		
		Wallet wallet = (Wallet) query.getSingleResult();
		
		return wallet;
	}
	
	public void updateWalletBalance(int userId, Double amount) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query query = session.createQuery("update Wallet set balance = balance + :amount where userId=:userId");
		query.setParameter("amount", amount);
		query.setParameter("userId", userId);
		
		int rowsAffected = query.executeUpdate();
		System.out.println(rowsAffected);
	}
	
	public void save(Wallet wallet) {
		
		Session session = sessionFactory.getCurrentSession();
		
		session.save(wallet);
	}
	
}
