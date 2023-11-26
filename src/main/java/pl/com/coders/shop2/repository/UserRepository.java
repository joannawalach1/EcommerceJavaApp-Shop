package pl.com.coders.shop2.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.com.coders.shop2.domain.User;
import pl.com.coders.shop2.exceptions.ProductWithGivenIdNotExistsException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public User create(User user) {
        return entityManager.merge(user);
    }

    public  boolean findByEmail(String email) {
        User user = entityManager.find(User.class, email);
        if (user == null) {
            throw new ProductWithGivenIdNotExistsException("User with the given email " + email + " doesn't exist");
        }
        return true;
    }
}
