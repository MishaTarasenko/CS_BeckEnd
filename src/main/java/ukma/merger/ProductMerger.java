package ukma.merger;

import ukma.model.entity.ProductCategoryEntity;
import ukma.model.entity.ProductEntity;
import ukma.model.view.ProductView;
import ukma.repository.ProductCategoryRepository;

import javax.persistence.Persistence;

public class ProductMerger {
    private static final ProductCategoryRepository categoryRepository = new ProductCategoryRepository(Persistence.createEntityManagerFactory("CS_BeckEnd"));

    public static void mergeCreate(ProductEntity entity, ProductView view) {
        merge(entity, view);
        if (view.getCategoryId() != null) {
            ProductCategoryEntity category = categoryRepository.findById(view.getCategoryId());
            entity.setCategory(category);
        }
    }

    public static void mergeUpdate(ProductEntity entity, ProductView view) {
        merge(entity, view);
    }

    private static void merge(ProductEntity entity, ProductView view) {
        if (view.getId() != null)
            entity.setId(view.getId());
        if (view.getName() != null)
            entity.setName(view.getName());
        if (view.getDescription() != null)
            entity.setDescription(view.getDescription());
        if (view.getProducer() != null)
            entity.setProducer(view.getProducer());
        if (view.getQuantity() != null)
            entity.setQuantity(view.getQuantity());
        if (view.getPrice() != null)
            entity.setPrice(view.getPrice());
    }
}
