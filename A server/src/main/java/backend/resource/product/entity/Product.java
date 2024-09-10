package backend.resource.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    private Long id; // 상품 ID

    @Column(name = "product_name", nullable = false)
    private String productName; // 상품명

    @Column(name = "price", nullable = false)
    private int price; // 가격

    @Enumerated(EnumType.STRING)
    @Column(name = "item_type", nullable = false)
    private String itemType; // 품목
}
