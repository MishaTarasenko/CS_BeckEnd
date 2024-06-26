package ukma.services.category;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import ukma.model.entity.ProductCategoryEntity;
import ukma.util.ValidatorConfig;

import java.util.Set;


public class ProductCategoryValidator {

    private static final Validator validator = ValidatorConfig.createValidator();

    public void valideProductCategory(ProductCategoryEntity entity) {
        Set<ConstraintViolation<ProductCategoryEntity>> exceptions =  validator.validate(entity);
        if (!exceptions.isEmpty()) {
            throw new IllegalArgumentException("Product category validation failed");
        }
    }
}
