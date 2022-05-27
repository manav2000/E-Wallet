package com.userservice.dao;

import com.userservice.entity.User;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

    @Autowired
    private SessionFactory sessionFactory;

    public User findById(int id) {

        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery("from User where id=:id");
        query.setParameter("id", id);
        
        User user = (User) query.getSingleResult();

        return user;
    }

    public void update(User user) {

        Session session = sessionFactory.getCurrentSession();

        session.update(user);
    }

    public int save(User user) {
        
        Session session = sessionFactory.getCurrentSession();

        return (Integer) session.save(user);
    }

}
