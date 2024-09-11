package backend.resource.order.dto;

import backend.resource.order.entity.ItemType;
import backend.resource.order.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class OrderDetailResponseDto {
    private String orderId;
    private String userName;
    private ItemType itemType;
    private double quantity;
    private int totalPrice;
    private OrderStatus orderStatus;
    private LocalDate orderDate;
    private String address;
}
