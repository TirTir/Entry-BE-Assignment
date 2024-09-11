package backend.resource.order.entity;

import backend.resource.common.constants.OrderStatus;
import backend.resource.product.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
    private long id;

    @Column(name = "date", nullable = false)
    private LocalDateTime date; // 주문 일자

    @Column(name = "user_name", nullable = false)
    private String userName; // 주문자

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status; // 상태

    @Column(name = "quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity; // 수량(소수점 두 자리까지)

    @Column(name = "total_price", nullable = false)
    private int totalPrice;

    @Column(name = "address", nullable = false)
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product; // 상품
}
