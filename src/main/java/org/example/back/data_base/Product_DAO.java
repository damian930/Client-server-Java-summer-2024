package org.example.back.data_base;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;

import javax.persistence.OptimisticLockException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;

public class Product_DAO {

    private final SessionFactory sessionFactory;

    /**
     * Default constructor that initializes the Hibernate SessionFactory.
     */
    public Product_DAO() {
        this.sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    /**
     * Retrieves a Product object by its unique identifier.
     *
     * @param id The identifier of the Product to retrieve.
     * @return The Product object if found, otherwise null.
     */
    public Product findById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Product.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves Product objects belonging to a specific category.
     *
     * @param categoryName The name of the category to filter by.
     * @return A list of Product objects belonging to the specified category.
     */
    public List<Product> findByCategory(String categoryName) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
            Root<Product> root = criteria.from(Product.class);
            Join<Product, Category> categoryJoin = root.join("category");
            criteria.select(root).where(builder.equal(categoryJoin.get("name"), categoryName));
            return session.createQuery(criteria).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves a Product object by its UPC (Universal Product Code).
     *
     * @param upc The UPC of the Product to retrieve.
     * @return The Product object if found, otherwise null.
     */
    public Product findByUPC(String upc) {
        try (Session session = sessionFactory.openSession()) {
            org.hibernate.Query<Product> query = session.createQuery("FROM Product WHERE UPC = :upc", Product.class);
            query.setParameter("upc", upc);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves a Product object by its name.
     *
     * @param name The name of the Product to retrieve.
     * @return The Product object if found, otherwise null.
     */
    public Product findByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Product> query = session.createQuery("FROM Product WHERE name = :name", Product.class);
            query.setParameter("name", name);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves all Product objects from the database.
     *
     * @return A list of all Product objects.
     */
    public List<Product> findAll() {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
            Root<Product> root = criteria.from(Product.class);
            criteria.select(root);
            return session.createQuery(criteria).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Checks if a Product with the specified UPC exists in the database.
     *
     * @param upc The UPC to check.
     * @return true if a Product with the specified UPC exists, false otherwise.
     */
    public boolean existsByUPC(String upc) {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Product WHERE UPC = :upc", Long.class);
            query.setParameter("upc", upc);
            Long count = query.uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if a Product with the specified name exists in the database.
     *
     * @param name The name to check.
     * @return true if a Product with the specified name exists, false otherwise.
     */
    public boolean existsByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Product WHERE name = :name", Long.class);
            query.setParameter("name", name);
            Long count = query.uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Saves a Product object to the database.
     *
     * @param product The Product object to save.
     */
    public void save(Product product) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.save(product);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

    /**
     * Updates a Product object in the database.
     *
     * @param product The Product object to update.
     */
    public void update(Product product) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.update(product);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

    /**
     * Processes a purchase transaction for a Product by decrementing its quantity.
     *
     * @param productId The identifier of the Product to purchase.
     * @param quantityToBuy The quantity to purchase.
     * @throws RuntimeException if the Product is not found, or if there's not enough quantity available.
     */
    public void purchaseProduct(int productId, int quantityToBuy) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            Product product = session.get(Product.class, productId, LockMode.PESSIMISTIC_WRITE);

            if (product == null) {
                throw new RuntimeException("Product not found");
            }

            int availableQuantity = product.getQuantity();

            if (availableQuantity < quantityToBuy) {
                throw new RuntimeException("Not enough quantity available");
            }

            product.setQuantity(availableQuantity - quantityToBuy);
            session.update(product);

            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Another transaction has modified the product. Please try again.");
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to process purchase. Please try again later.");
        }
    }

    /**
     * Increases the quantity of a Product by adding a specified amount.
     *
     * @param productId The identifier of the Product to add quantity to.
     * @param addAmount The amount to add to the Product's quantity.
     * @throws RuntimeException if the Product is not found or if adding amount fails.
     */
    public void addQuantity(int productId, int addAmount) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            Product product = session.get(Product.class, productId, LockMode.PESSIMISTIC_WRITE);

            if (product == null) {
                throw new RuntimeException("Product not found");
            }

            int currentQuantity = product.getQuantity();
            product.setQuantity(currentQuantity + addAmount);
            session.update(product);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Failed to add amount. Please try again later.");
        }
    }

    /**
     * Deletes a Product object from the database.
     *
     * @param product The Product object to delete.
     */
    public void delete(Product product) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.delete(product);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

    /**
     * Closes the Hibernate SessionFactory.
     */
    public void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}