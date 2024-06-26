package ukma.model.entity;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Credentials {
    private String email;
    private String password;
}
