//package pl.com.coders.shop2.helpers;
//
//import com.github.javafaker.Faker;
//import org.springframework.stereotype.Component;
//import pl.com.coders.shop2.domain.Category;
//import pl.com.coders.shop2.repository.CategoryRepository;
//
//import javax.annotation.PostConstruct;
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class FakeCategoryGenerator {
//
//    private final Faker faker;
//    private final CategoryRepository categoryRepository;
//
//    public FakeCategoryGenerator(Faker faker, CategoryRepository categoryRepository) {
//        this.faker = faker;
//        this.categoryRepository = categoryRepository;
//    }
//
//    public List<Category> initCategory() {
//        categoryRepository.deleteAll();
//        List<Category> categories = generateFakeCategories(10);
//        for (Category category : categories) {
//            categoryRepository.save(category);
//        }
//
//        return categories;
//    }
//
//    public Category generateFakeCategory() {
//        String name = faker.commerce().department();
//        while (categoryRepository.foundByName(name)) {
//            name = faker.commerce().department();
//        }
//        Category category = new Category();
//        category.setName(name);
//        categoryRepository.save(category);
//        System.out.println(category);
//        return category;
//    }
//
//
//    public List<Category> generateFakeCategories(int count) {
//        List<Category> categories = new ArrayList<>();
//        for (int i = 0; i < count; i++) {
//            Category category = generateFakeCategory();
//            categories.add(category);
//        }
//        System.out.println(categories);
//        return categories;
//    }
//}
