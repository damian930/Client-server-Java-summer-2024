package org.example.back.data_base;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

/**
 * Data Access Object (DAO) class for handling Category entities in the database.
 * This class provides methods to perform CRUD (Create, Read, Update, Delete) operations
 * on Category objects using Hibernate ORM.
 */
public class Category_DAO {

    private SessionFactory sessionFactory;

    /**
     * Default constructor that initializes the Hibernate SessionFactory.
     */
    public Category_DAO() {
        this.sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    /**
     * Retrieves a Category object by its unique identifier.
     *
     * @param id The identifier of the Category to retrieve.
     * @return The Category object if found, otherwise null.
     */
    public Category findById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Category.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves a Category object by its name.
     *
     * @param name The name of the Category to retrieve.
     * @return The Category object if found, otherwise null.
     */
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

    /**
     * Checks if a Category with the given name exists in the database.
     *
     * @param name The name of the Category to check.
     * @return true if a Category with the name exists, false otherwise.
     */
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

    /**
     * Retrieves all Category objects from the database.
     *
     * @return A list of Category objects.
     */
    public List<Category> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Category", Category.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Saves a Category object to the database.
     *
     * @param category The Category object to save.
     */
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

    /**
     * Updates a Category object in the database.
     *
     * @param category The Category object to update.
     */
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

    /**
     * Deletes a Category object from the database.
     *
     * @param category The Category object to delete.
     */
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

    /**
     * Closes the SessionFactory.
     */
    public void close() {
        sessionFactory.close();
    }
}
