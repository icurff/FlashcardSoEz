package com.flashcardsoez.DAO;

import com.flashcardsoez.FlashcardSoEz;
import com.flashcardsoez.utils.HibernateUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import com.flashcardsoez.model.Test;

public class TestDAO implements AbstractFactory<Test> {

    @Override
    public void add(Test test) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(test);
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
    public void update(Test t) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Test test = session.get(Test.class, t.getId());
            test.setTitle(t.getTitle());
            session.merge(test);
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
    public void delete(Test t) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Test test = session.get(Test.class, t.getId());
            session.remove(test);
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
    public List<Test> getList() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Test";
            Query<Test> query = session.createQuery(hql, Test.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving test list.");
            return null;
        }
        
    }

    public List<Test> getListByCourseId(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Test t WHERE t.course.id = :id";
            Query<Test> query = session.createQuery(hql, Test.class);
            query.setParameter("id", id);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving tests by course id.");
            return null;
        }
        
    }

    @Override
    public Test getById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Test.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving test by id.");
            return null;
        }
        
    }
}
