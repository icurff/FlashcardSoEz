package com.flashcardsoez.DAO;

import com.flashcardsoez.FlashcardSoEz;
import com.flashcardsoez.model.Course;
import com.flashcardsoez.model.Test;
import com.flashcardsoez.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class CourseDAO implements AbstractFactory<Course> {

    @Override
    public void add(Course course) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(course);
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

    public void addUserToCourse(Course course) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(course);
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
    public void update(Course t) {
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
    public void delete(Course t) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Course course = session.get(Course.class, t.getId());

            if (course != null) {
                for (Test test : course.getTestsInCourse()) {
                    session.remove(test);
                }
                // Clear associated collections
                course.getStudentsInCourse().clear();
                session.remove(course);
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
    public List<Course> getList() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Course c WHERE c.teacher.id = :id";
            Query<Course> query = session.createQuery(hql, Course.class);
            query.setParameter("id", FlashcardSoEz.curUser.getId());
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving course list.");
            return null;
        }
        
    }

    public List<Course> getJoinedCourses() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT c FROM Course c JOIN c.studentsInCourse s WHERE s.id = :id";
            Query<Course> query = session.createQuery(hql, Course.class);
            query.setParameter("id", FlashcardSoEz.curUser.getId());
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving joined courses.");
            return null;
        }
        
    }

    @Override
    public Course getById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Course.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving course by id.");
            return null;
        }
        
    }
}
