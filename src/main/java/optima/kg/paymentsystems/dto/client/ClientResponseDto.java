package optima.kg.paymentsystems.dto.client;

import lombok.*;

import java.util.List;

/**
 * @author Abubakir Dev
 */
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientResponseDto {
    private Long id;
    private String name;
    private List<Long> cardIds;
}
