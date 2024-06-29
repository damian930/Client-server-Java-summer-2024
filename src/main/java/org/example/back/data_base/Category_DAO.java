package org.example.back.data_base;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class Category_DAO {

    private SessionFactory sessionFactory;

    public Category_DAO() {
        this.sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    public Category findById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Category.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Category findByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Category WHERE name = :name", Category.class)
                    .setParameter("name", name)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean existsByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Long count = session.createQuery("SELECT COUNT(*) FROM Category WHERE name = :name", Long.class)
                    .setParameter("name", name)
                    .uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Category> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Category", Category.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void save(Category category) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.save(category);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

    public void update(Category category) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.update(category);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            if (sessionFactory != null && sessionFactory.isOpen()) {
                sessionFactory.close();
            }
        }
    }

    public void delete(Category category) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.delete(category); // Cascades to delete associated products
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
       this.sessionFactory = sessionFactory;
    }

    public void close() {
        sessionFactory.close();
    }
}
