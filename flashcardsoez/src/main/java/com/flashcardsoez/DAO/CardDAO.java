package com.flashcardsoez.DAO;

import com.flashcardsoez.FlashcardSoEz;
import com.flashcardsoez.utils.HibernateUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.flashcardsoez.model.Card;

public class CardDAO implements AbstractFactory<Card> {

    @Override
    public void add(Card card) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(card);
            tx.commit();
            System.out.println("Added successfully.");
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            System.out.println("Error persisting, transaction rolled back.");
        }
        
    }

    @Override
    public void update(Card card) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(card);
            tx.commit();
            System.out.println("Updated successfully.");
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            System.out.println("Error persisting, transaction rolled back.");
        }
        
    }

    @Override
    public void delete(Card card) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.remove(card);
            tx.commit();
            System.out.println("Deleted successfully.");
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            System.out.println("Error persisting, transaction rolled back.");
        }
        
    }

    @Override
    public List<Card> getList() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Card c WHERE c.author.id = :id";
            Query<Card> query = session.createQuery(hql, Card.class);
            query.setParameter("id", FlashcardSoEz.curUser.getId());
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving card list.");
            return null;
        }
        
    }

    @Override
    public Card getById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Card.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving card by id.");
            return null;
        }
        
    }
}
