package ukma.model.entity;

import com.sun.istack.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank
    @Column(name = "name")
    private String name;

    @NotBlank
    @Column(name = "description")
    private String description;

    @NotBlank
    @Column(name = "producer")
    private String producer;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private Double price;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private ProductCategoryEntity category;

    public ProductEntity(String name, String description, String producer, Integer quantity, Double price, ProductCategoryEntity category) {
        this.name = name;
        this.description = description;
        this.producer = producer;
        this.quantity = quantity;
        this.category = category;
        this.price = price;
    }
}
