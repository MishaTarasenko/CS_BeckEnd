package ukma.model.response;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private Integer id;
    private String name;
    private String description;
    private String producer;
    private Integer quantity;
    private Double price;
    private String category;
}
