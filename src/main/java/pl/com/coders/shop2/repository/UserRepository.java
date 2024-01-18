package pl.com.coders.shop2.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.com.coders.shop2.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository
public class UserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public User create(User user) {
        return entityManager.merge(user);
    }

    @Transactional
    public User findByEmail(String email) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        return query.getSingleResult();

    }


    public User findById(Long userId) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.id = :userId", User.class);
        query.setParameter("userId", userId);
        return query.getSingleResult();
    }

    public User findByLastName(String userLastName) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.lastName = :userLastName", User.class);
        query.setParameter("userLastName", userLastName);
        return query.getSingleResult();
    }

    @Transactional
    public void updateUser(Long userId, String newUsername) {
        entityManager.createQuery("UPDATE User u SET u.username = :newUsername WHERE u.id = :userId")
                .setParameter("newUsername", newUsername)
                .setParameter("userId", userId)
                .executeUpdate();
    }
}

