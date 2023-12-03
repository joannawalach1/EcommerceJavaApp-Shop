package pl.com.coders.shop2.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.com.coders.shop2.domain.Category;

    @Repository
    public interface CategoryRepository extends CrudRepository<Category, Long> {
        boolean existsByName(String name);
        Category getCategoryById(Long categoryId);
    }