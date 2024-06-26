import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ukma.model.entity.ProductCategoryEntity;
import ukma.services.category.ProductCategoryService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ProductCategoryTest {
    ProductCategoryService service = new ProductCategoryService();
    ProductCategoryEntity category;

    @BeforeEach
    void setUp() {
        category = new ProductCategoryEntity("test name", "test description");
    }

    @Test
    public void createProductCategoryWithCorrectDataTest() {
        Integer id = service.create(category);
        assertEquals(1, service.getAll().size());
        service.delete(id);
    }

    @Test
    public void cantCreateProductCategoryWithNullFiledsTest() {
        try {
            category.setName("    ");
            Integer id = service.create(category);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(0, service.getAll().size());
    }

    @Test
    public void cantCreateProductCategoryWithSameNameTest() {
        Integer id = service.create(category);
        try {
            service.create(category);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(1, service.getAll().size());
        service.delete(id);
    }

    @Test
    public void updateProductCategoryTest() {
        Integer id = service.create(category);
        assertEquals(1, service.getAll().size());

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
        assertEquals(1, service.getAll().size());

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

        assertEquals(3, service.getAll().size());

        service.delete(id1);
        service.delete(id2);
        service.delete(id3);
    }

    @Test
    public void deleteProductCategoryWithProductsTest() {
        
    }

}
