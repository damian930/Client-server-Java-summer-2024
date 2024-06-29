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

    public Product_DAO() {
        this.sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    public Product findById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Product.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

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

    public void updateQuantity(Product product, int newQuantity) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            Product managedProduct = session.get(Product.class, product.getId());
            if (managedProduct != null) {
                managedProduct.setQuantity(newQuantity);
                session.update(managedProduct);
            } else {
                System.err.println("Product with ID " + product.getId() + " not found.");
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

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

    public void decrementQuantity(Product product, int quantityToRemove) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            Product managedProduct = session.get(Product.class, product.getId());
            if (managedProduct != null) {
                int currentQuantity = managedProduct.getQuantity();
                if (currentQuantity >= quantityToRemove) {
                    managedProduct.setQuantity(currentQuantity - quantityToRemove);
                    session.update(managedProduct);
                } else {
                    System.err.println("Insufficient quantity to remove.");
                }
            } else {
                System.err.println("Product with ID " + product.getId() + " not found.");
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

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

    public void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}