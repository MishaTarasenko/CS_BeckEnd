package ukma.model.view;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductView {
    private Integer id;
    private String name;
    private String description;
    private String producer;
    private Integer quantity;
    private Double price;
    private Integer categoryId;

    public ProductView(String name, String description, String producer, Double price, Integer categoryId, Integer quantity) {
        this.name = name;
        this.description = description;
        this.producer = producer;
        this.price = price;
        this.categoryId = categoryId;
        this.quantity = quantity;
    }
}
