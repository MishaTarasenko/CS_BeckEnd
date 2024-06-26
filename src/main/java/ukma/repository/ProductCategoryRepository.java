package ukma.repository;

import ukma.model.entity.ProductCategoryEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.List;

public class ProductCategoryRepository {

    private static EntityManagerFactory emf;
    private static ProductRepository productRepository;

    public ProductCategoryRepository(EntityManagerFactory emf) {
        this.emf = emf;
        productRepository = new ProductRepository(emf);
    }

    public Integer create(ProductCategoryEntity entity) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(entity);
            transaction.commit();
            return entity.getId();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public boolean deleteById(Integer id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            ProductCategoryEntity category = em.find(ProductCategoryEntity.class, id);
            if (category != null) {
                em.remove(category);
            }
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    public Boolean update(ProductCategoryEntity category) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(category);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    public ProductCategoryEntity findByName(String name) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT c FROM ProductCategoryEntity c WHERE name = :name", ProductCategoryEntity.class)
                    .setParameter("name", name).getSingleResult();
        } finally {
            em.close();
        }
    }

    public ProductCategoryEntity findById(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(ProductCategoryEntity.class, id);
        } finally {
            em.close();
        }
    }

    public List<ProductCategoryEntity> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT pc FROM ProductCategoryEntity pc", ProductCategoryEntity.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void close() {
        if (emf != null) {
            emf.close();
        }
    }
}
