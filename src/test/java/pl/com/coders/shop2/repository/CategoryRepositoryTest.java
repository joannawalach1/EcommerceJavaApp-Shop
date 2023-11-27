package pl.com.coders.shop2.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.com.coders.shop2.domain.Category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@SpringBootTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void getCategoryById() {
        Long categoryId = 1L;
        Category category = categoryRepository.getCategoryById(categoryId);
        assertNotNull(category);
        assertEquals("ELEKTRONIKA", category.getName());
    }

    @Test
    void foundByName() {
        String categoryName = "ELEKTRONIKA";
        boolean found = categoryRepository.existsByName(categoryName);
        assertEquals(true, found);
    }
}