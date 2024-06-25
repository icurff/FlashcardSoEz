package com.flashcardsoez.DAO;

import com.flashcardsoez.FlashcardSoEz;
import com.flashcardsoez.model.Course;
import com.flashcardsoez.model.User;
import com.flashcardsoez.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class UserDAO implements AbstractFactory<User> {

    @Override
    public void add(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
            System.out.println("User added successfully.");
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            System.out.println("Error persisting user.");
        }
    }

    @Override
    public void update(User t) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(t);
            tx.commit();
            System.out.println("Updated successfully.");
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            System.out.println("Error updating, transaction rolled back.");
        }
    }

    @Override
    public void delete(User t) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.remove(t);
            tx.commit();
            System.out.println("Deleted successfully.");
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            System.out.println("Error deleting, transaction rolled back.");
        }
    }

    @Override
    public List<User> getList() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM User";
            Query<User> query = session.createQuery(hql, User.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving user list.");
            return null;
        }
    }

    public User getUserByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM User u WHERE u.username = :username";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("username", username);
            List<User> userList = query.getResultList();
            return userList.isEmpty() ? null : userList.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving user by username.");
            return null;
        }
    }

    public User getUserByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM User u WHERE u.email = :email";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("email", email);
            List<User> userList = query.getResultList();
            return userList.isEmpty() ? null : userList.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving user by email.");
            return null;
        }
    }

    public User getById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(User.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving user by id.");
            return null;
        }
    }

    public List<Course> getListCoursesJoined() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT c FROM Course c JOIN c.studentsInCourse u WHERE u.id = :userId";
            Query<Course> query = session.createQuery(hql, Course.class);
            query.setParameter("userId", FlashcardSoEz.curUser.getId());
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving courses joined by user.");
            return null;
        }
    }
}
