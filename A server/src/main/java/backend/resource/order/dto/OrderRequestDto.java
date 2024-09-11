package backend.resource.order.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderRequestDto {
    private long productId; // 상품
    private BigDecimal quantity; // 수량
    private String address; // 주소
}
