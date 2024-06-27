import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ukma.model.entity.ProductCategoryEntity;
import ukma.model.entity.ProductEntity;
import ukma.model.view.ProductView;
import ukma.services.category.ProductCategoryService;
import ukma.services.product.ProductService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ProductTest {

    ProductCategoryService productCategoryService = new ProductCategoryService();
    ProductService service = new ProductService();
    ProductCategoryEntity category;
    Integer categoryId;
    ProductView product;

    @BeforeEach
    void setUp() {
        category = new ProductCategoryEntity("test name", "test category");
        categoryId = productCategoryService.create(category);
        product = new ProductView("test name", "test description", "I", 99.9, categoryId, 10);
    }

    @AfterEach
    void tearDown() {
        productCategoryService.delete(categoryId);
    }

    @Test
    public void createProductWithCorrectDataTest() {
        Integer id = service.create(product);
        assertEquals(1, service.getAll().size());
        service.delete(id);
    }

    @Test
    public void cantCreateProductWithNullFieldsTest() {
        try {
            product.setName("    ");
            Integer id = service.create(product);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(0, service.getAll().size());
    }

    @Test
    public void updateProductCategoryTest() {
        Integer id = service.create(product);
        assertEquals(1, service.getAll().size());

        ProductView updatedProduct = new ProductView(id, "new name", "new description", "I", 10, 99.9, categoryId);
        service.update(updatedProduct);

        ProductEntity c = service.getById(id);
        assertNotEquals(c.getName(), product.getName());
        assertNotEquals(c.getDescription(), product.getDescription());

        service.delete(id);
    }

    @Test
    public void getProductCategoryTest() {
        Integer id = service.create(product);
        assertEquals(1, service.getAll().size());

        ProductEntity c = service.getById(id);
        assertEquals(id, c.getId());
        assertEquals(product.getName(), c.getName());
        assertEquals(product.getDescription(), c.getDescription());

        service.delete(id);
    }

    @Test
    public void getAllProductCategoriesTest() {
        Integer id1 = service.create(product);
        ProductView productTwo = new ProductView("test name1", "test description1", "I", 99.9, categoryId, 10);
        Integer id2 = service.create(productTwo);
        ProductView productThree = new ProductView("test name2", "test description2", "I", 99.9, categoryId, 10);
        Integer id3 = service.create(productThree);

        assertEquals(3, service.getAll().size());

        service.delete(id1);
        service.delete(id2);
        service.delete(id3);
    }
}
