package ukma.repository;

import ukma.model.entity.UserEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class UserRepository {
    private static EntityManagerFactory emf;

    public UserRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public UserEntity findByEmail(String email) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT u FROM UserEntity u WHERE u.email = :email", UserEntity.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }
}
