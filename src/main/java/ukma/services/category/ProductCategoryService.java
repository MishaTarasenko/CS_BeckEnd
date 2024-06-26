package ukma.services.category;

import ukma.model.entity.ProductCategoryEntity;
import ukma.repository.ProductCategoryRepository;
import ukma.repository.ProductRepository;

import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import java.util.List;

public class ProductCategoryService {

    private static final ProductCategoryRepository repository = new ProductCategoryRepository(Persistence.createEntityManagerFactory("CS_BeckEnd"));
    private static final ProductRepository productRepository = new ProductRepository(Persistence.createEntityManagerFactory("CS_BeckEnd"));
    private static final ProductCategoryValidator validator = new ProductCategoryValidator();

    public Integer create(ProductCategoryEntity entity) {
        try {
        repository.findByName(entity.getName());
        throw new IllegalArgumentException("Category already exists");
        } catch (NoResultException e) {
            validator.valideProductCategory(entity);
            return repository.create(entity);
        }
    }

    public Boolean delete(Integer id) {
        productRepository.deleteAllByCategoryId(id);
        return repository.deleteById(id);
    }

    public Boolean update(ProductCategoryEntity entity) {
        try {
            ProductCategoryEntity en = repository.findByName(entity.getName());
            if (en.getId() != entity.getId())
                throw new IllegalArgumentException("Category already exists");
            validator.valideProductCategory(entity);
            return repository.update(entity);
        } catch (NoResultException e) {
            validator.valideProductCategory(entity);
            return repository.update(entity);
        }
    }

    public List<ProductCategoryEntity> getAll() {
        return repository.findAll();
    }

    public ProductCategoryEntity getById(Integer id) {
        return repository.findById(id);
    }
}
