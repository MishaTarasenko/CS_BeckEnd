package ukma.model.view;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.Column;

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
}
