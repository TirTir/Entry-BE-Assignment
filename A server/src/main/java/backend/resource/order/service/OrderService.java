package backend.resource.order.service;

import backend.resource.common.constants.ErrorMessages;
import backend.resource.common.constants.OrderStatus;
import backend.resource.order.dto.OrderRequestDto;
import backend.resource.order.entity.Order;
import backend.resource.order.repository.OrderRepository;
import backend.resource.product.entity.Product;
import backend.resource.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public void RegisterService(OrderRequestDto requestDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.ROLE_NOT_FOUND))
                .getAuthority();

        // 상품 찾기
        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.PRODUCT_NOT_FOUND));

        // 가격 측정
        BigDecimal quantity = requestDto.getQuantity();
        BigDecimal price = BigDecimal.valueOf(product.getPrice()); // int -> BigDecimal

        BigDecimal totalPrice = quantity.multiply(price).setScale(0, BigDecimal.ROUND_HALF_UP);

        Order newOrder = Order.builder()
                .userName(authentication.getName())
                .date(LocalDateTime.now())
                .status(defaultStatus(role))
                .quantity(requestDto.getQuantity())
                .totalPrice(totalPrice.intValue())
                .address(requestDto.getAddress())
                .product(product)
                .build();

        orderRepository.save(newOrder);
    }

    private OrderStatus defaultStatus(String role) {
        if (role.equalsIgnoreCase("USER")) {
            return OrderStatus.ORDER_PLACED;
        } else if (role.equalsIgnoreCase("ADMIN")) {
            return OrderStatus.ORDER_RECEIVED;
        } else {
            throw new IllegalArgumentException(ErrorMessages.ROLE_NOT_FOUND);
        }
    }
}
