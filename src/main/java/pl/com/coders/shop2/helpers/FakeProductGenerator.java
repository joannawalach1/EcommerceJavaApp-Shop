//package pl.com.coders.shop2.helpers;
//
//import com.github.javafaker.Faker;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import pl.com.coders.shop2.domain.Category;
//import pl.com.coders.shop2.domain.Product;
//import pl.com.coders.shop2.repository.CategoryRepository;
//import pl.com.coders.shop2.repository.ProductRepository;
//
//import javax.annotation.PostConstruct;
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class FakeProductGenerator {
//    private final Faker faker;
//    private final ProductRepository productRepository;
//    private final CategoryRepository categoryRepository;
//
//    @Autowired
//    public FakeProductGenerator(ProductRepository productRepository, CategoryRepository categoryRepository) {
//        this.faker = new Faker();
//        this.productRepository = productRepository;
//        this.categoryRepository = categoryRepository;
//    }
//
//
//    public void initProduct() {
//        productRepository.deleteAll();
//        List<Product> fakeProducts = generateFakeProducts(10);
//        for (Product product : fakeProducts) {
//            productRepository.add(product);
//        }
//        System.out.println(fakeProducts);
//    }
//
//    public Product generateFakeProduct() {
//        List<Category> categories = categoryRepository.findAll();
//        Category randomCategory = categories.get(faker.random().nextInt(categories.size()));
//
//        String name = faker.commerce().productName();
//        String description = faker.lorem().sentence();
//        BigDecimal price = BigDecimal.valueOf(faker.number().randomDouble(2, 100, 2000));
//        int quantity = faker.number().numberBetween(1, 10);
//        Product product = new Product(randomCategory, name, description, price, quantity);
//        productRepository.add(product);
//
//        return product;
//    }
//
//    public List<Product> generateFakeProducts(int count) {
//        List<Product> products = new ArrayList<>();
//        for (int i = 0; i < count; i++) {
//            Product product = generateFakeProduct();
//            products.add(product);
//        }
//        System.out.println(products);
//        return products;
//    }
//}
