package ukma.services.product;

import ukma.merger.ProductMerger;
import ukma.model.entity.ProductEntity;
import ukma.model.view.ProductView;
import ukma.repository.ProductRepository;

import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.List;

public class ProductService {

    private static final ProductRepository repository = new ProductRepository(Persistence.createEntityManagerFactory("CS_BeckEnd"));
    private static final ProductValidator validator = new ProductValidator();

    public Integer create(ProductView view) {
        try {
            repository.findByName(view.getName());
            throw new IllegalArgumentException("Product already exists");
        } catch (NoResultException e) {
            ProductEntity entity = new ProductEntity();
            try {
                ProductMerger.mergeCreate(entity, view);
            } catch (NoResultException e1) {
                throw new IllegalArgumentException("Category with id " + view.getCategoryId() + " does not exist");
            }
            validator.valideProduct(entity);
            return repository.create(entity);
        }
    }

    public Boolean delete(Integer id) {
        return repository.deleteById(id);
    }

    public Boolean update(ProductView view) {
        try {
            ProductEntity entity = repository.findByName(view.getName());
            if (entity.getId() != view.getId())
                throw new IllegalArgumentException("Product already exists");
            ProductMerger.mergeUpdate(entity, view);
            validator.valideProduct(entity);
            return repository.update(entity);
        } catch (NoResultException e) {
            ProductEntity entity = repository.findById(view.getId());
            ProductMerger.mergeUpdate(entity, view);
            validator.valideProduct(entity);
            return repository.update(entity);
        }
    }

    public List<ProductEntity> getAll() {
        return repository.findAll();
    }

    public List<ProductEntity> getAllByCriteria(HashMap<String, Object> criteria) {
        return repository.findAllByCriteria(criteria);
    }

    public ProductEntity getById(Integer id) {
        return repository.findById(id);
    }
}
