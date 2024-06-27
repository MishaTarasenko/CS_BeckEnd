import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ukma.model.entity.ProductCategoryEntity;
import ukma.model.view.ProductView;
import ukma.services.category.ProductCategoryService;
import ukma.services.product.ProductService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ProductCategoryTest {
    ProductCategoryService service = new ProductCategoryService();
    ProductService productService = new ProductService();
    ProductCategoryEntity category;
    Integer productCategoryCount = 0;
    Integer productCount = 0;

    @BeforeEach
    void setUp() {
        category = new ProductCategoryEntity("test name", "test description");
        productCategoryCount = service.getAll().size();
        productCount = productService.getAll().size();
    }

    @Test
    public void createProductCategoryWithCorrectDataTest() {
        Integer id = service.create(category);
        assertEquals(productCategoryCount + 1, service.getAll().size());
        service.delete(id);
    }

    @Test
    public void cantCreateProductCategoryWithNullFieldsTest() {
        try {
            category.setName("    ");
            Integer id = service.create(category);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(productCategoryCount + 0, service.getAll().size());
    }

    @Test
    public void cantCreateProductCategoryWithSameNameTest() {
        Integer id = service.create(category);
        try {
            service.create(category);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(productCategoryCount + 1, service.getAll().size());
        service.delete(id);
    }

    @Test
    public void updateProductCategoryTest() {
        Integer id = service.create(category);
        assertEquals(productCategoryCount + 1, service.getAll().size());

        ProductCategoryEntity updatedCategory = new ProductCategoryEntity(id, "new name", "new description");
        service.update(updatedCategory);

        ProductCategoryEntity c = service.getById(id);
        assertNotEquals(c.getName(), category.getName());
        assertNotEquals(c.getDescription(), category.getDescription());

        service.delete(id);
    }

    @Test
    public void getProductCategoryTest() {
        Integer id = service.create(category);
        assertEquals(productCategoryCount + 1, service.getAll().size());

        ProductCategoryEntity c = service.getById(id);
        assertEquals(id, c.getId());
        assertEquals(category.getName(), c.getName());
        assertEquals(category.getDescription(), c.getDescription());

        service.delete(id);
    }

    @Test
    public void getAllProductCategoriesTest() {
        Integer id1 = service.create(category);
        ProductCategoryEntity categoryTwo = new ProductCategoryEntity("test name-2", "test description");
        Integer id2 = service.create(categoryTwo);
        ProductCategoryEntity categoryThree = new ProductCategoryEntity("test name-3", "test description");
        Integer id3 = service.create(categoryThree);

        assertEquals(productCategoryCount + 3, service.getAll().size());

        service.delete(id1);
        service.delete(id2);
        service.delete(id3);
    }

    @Test
    public void deleteProductCategoryWithProductsTest() {
        Integer id = service.create(category);
        assertEquals(productCategoryCount + 1, service.getAll().size());

        ProductView productOne = new ProductView("test name", "test description", "I", 99.9, id, 10);
        Integer id1 = productService.create(productOne);
        ProductView productTwo = new ProductView("test name1", "test description1", "I", 99.9, id, 10);
        Integer id2 = productService.create(productTwo);
        ProductView productThree = new ProductView("test name2", "test description2", "I", 99.9, id, 10);
        Integer id3 = productService.create(productThree);
        assertEquals(productCount + 3, productService.getAll().size());

        service.delete(id);

        assertEquals(productCount + 0, productService.getAll().size());
    }
}
