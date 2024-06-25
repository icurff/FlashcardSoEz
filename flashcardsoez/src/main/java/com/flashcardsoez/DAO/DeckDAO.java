package com.flashcardsoez.DAO;

import com.flashcardsoez.FlashcardSoEz;
import com.flashcardsoez.model.Card;
import com.flashcardsoez.model.Deck;
import com.flashcardsoez.utils.HibernateUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class DeckDAO implements AbstractFactory<Deck> {

    @Override
    public void add(Deck deck) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(deck);
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
    public void update(Deck t) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(t);
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
    public void delete(Deck t) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Deck deck = session.get(Deck.class, t.getId());

            if (deck != null) {
                for (Card card : deck.getCardList()) {
                    session.remove(card);
                }
                session.remove(deck);
            }

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
    public List<Deck> getList() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Deck d WHERE d.author.id = :id";
            Query<Deck> query = session.createQuery(hql, Deck.class);
            query.setParameter("id", FlashcardSoEz.curUser.getId());
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving deck list.");
            return null;
        }
        
    }

    @Override
    public Deck getById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Deck.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving deck by id.");
            return null;
        }
        
    }
}
