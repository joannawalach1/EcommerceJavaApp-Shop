package pl.com.coders.shop2.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.com.coders.shop2.domain.Users;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository
public class UserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Users create(Users user) {
        return entityManager.merge(user);
    }

    public Users findByEmail(String email) {
        TypedQuery<Users> query = entityManager.createQuery(
                "SELECT u FROM Users u WHERE u.email = :email", Users.class);
        query.setParameter("email", email);

        return query.getSingleResult();
    }
}

