package com.flashcardsoez.DAO;

import com.flashcardsoez.model.TestScore;
import com.flashcardsoez.utils.HibernateUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class TestScoreDAO implements AbstractFactory<TestScore> {

    @Override
    public void add(TestScore t) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(t);
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
    public void update(TestScore t) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            TestScore ts = session.get(TestScore.class, t.getId());
            ts.setScore(t.getScore());
            session.merge(ts);
            tx.commit();
            System.out.println("Updated successfully.");
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            System.out.println("Error updating, transaction rolled back.");
        }
        
    }

    @Override
    public void delete(TestScore t) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.remove(t);
            tx.commit();
            System.out.println("Deleted successfully.");
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            System.out.println("Error deleting, transaction rolled back.");
        }
        
    }

    @Override
    public List<TestScore> getList() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM TestScore";
            return session.createQuery(hql, TestScore.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving test score list.");
            return null;
        }
        
    }

    @Override
    public TestScore getById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(TestScore.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving test score by id.");
            return null;
        }
        
    }
}
