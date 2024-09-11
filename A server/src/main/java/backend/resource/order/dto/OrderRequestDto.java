package backend.resource.order.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderRequestDto {
    private long productId; // 상품
    private Double quantity; // 수량
    private String address; // 주소
}
