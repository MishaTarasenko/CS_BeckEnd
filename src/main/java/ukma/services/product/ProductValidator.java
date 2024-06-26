package ukma.services.product;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import ukma.model.entity.ProductEntity;
import ukma.util.ValidatorConfig;

import java.util.Set;

public class ProductValidator {

    private static final Validator validator = ValidatorConfig.createValidator();

    public void valideProduct(ProductEntity entity) {
        Set<ConstraintViolation<ProductEntity>> exceptions =  validator.validate(entity);
        if (!exceptions.isEmpty()) {
            throw new IllegalArgumentException("Product validation failed");
        }
    }
}
