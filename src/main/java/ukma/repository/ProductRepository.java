package ukma.repository;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ukma.model.entity.ProductCategoryEntity;
import ukma.model.entity.ProductEntity;
import ukma.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.List;

public class ProductRepository {

    private static EntityManagerFactory emf;

    public ProductRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public Integer create(ProductEntity entity) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            if (entity.getCategory() != null) {
                entity.setCategory(em.merge(entity.getCategory()));
            }
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
            ProductEntity category = em.find(ProductEntity.class, id);
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

    public boolean update(ProductEntity category) {
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

    public ProductEntity findByName(String name) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT p FROM ProductEntity p WHERE name = :name", ProductEntity.class)
                    .setParameter("name", name).getSingleResult();
        } finally {
            em.close();
        }
    }

    public ProductEntity findById(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(ProductEntity.class, id);
        } finally {
            em.close();
        }
    }

    public List<ProductEntity> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT p FROM ProductEntity p", ProductEntity.class).getResultList();
        } finally {
            em.close();
        }
    }

    public int deleteAllByCategoryId(Integer id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            int deletedCount = em.createQuery(
                            "DELETE FROM ProductEntity p WHERE p.category.id = :categoryId")
                    .setParameter("categoryId", id)
                    .executeUpdate();
            transaction.commit();
            return deletedCount;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return 0;
        } finally {
            em.close();
        }
    }

    public List<ProductEntity> findAllByCriteria(HashMap<String, Object> criteria) {
        Session session = HibernateUtil.getHibernateSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<ProductEntity> cr = cb.createQuery(ProductEntity.class);
        Root<ProductEntity> root = cr.from(ProductEntity.class);

        System.out.println(criteria.toString());

        if (criteria.isEmpty()) {
            cr.select(root);
        } else {
            if (criteria.containsKey("categoryId")) {}
                cr.select(root).where(cb.equal(root.get("categoryId"), criteria.get("categoryId")));
            if (criteria.containsKey("name"))
                cr.select(root).where(cb.like(root.get("name"), "%" + criteria.get("name") + "%"));
        }

        Query<ProductEntity> query = session.createQuery(cr);
        return query.getResultList();
    }

    public void close() {
        if (emf != null) {
            emf.close();
        }
    }
}
