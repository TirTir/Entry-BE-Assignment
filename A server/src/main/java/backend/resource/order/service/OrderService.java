package backend.resource.order.service;

import backend.resource.common.constants.ErrorMessages;
import backend.resource.common.constants.OrderStatus;
import backend.resource.common.exceptions.GeneralException;
import backend.resource.order.dto.OrderRequestDto;
import backend.resource.order.entity.Order;
import backend.resource.order.repository.OrderRepository;
import backend.resource.product.entity.Product;
import backend.resource.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public void RegisterService(OrderRequestDto requestDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .orElseThrow(() -> new GeneralException(ErrorMessages.ROLE_NOT_FOUND, HttpStatus.BAD_REQUEST))
                .getAuthority();

        // 상품 찾기
        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new GeneralException(ErrorMessages.PRODUCT_NOT_FOUND, HttpStatus.BAD_REQUEST));

        // 주문 번호
        String orderId = getOrderId(product.getId());

        // 가격 측정
        BigDecimal quantity = requestDto.getQuantity();
        BigDecimal price = BigDecimal.valueOf(product.getPrice()); // int -> BigDecimal
        BigDecimal totalPrice = quantity.multiply(price).setScale(0, BigDecimal.ROUND_HALF_UP);

        Order newOrder = Order.builder()
                .id(orderId)
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

    private String getOrderId(Long productId) {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String orderId;
        do {
            orderId = date + "-" + productId + "-" + getRandomNumber(3);
        } while (orderRepository.existsById(orderId)); // 중복 체크

        return orderId;
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

    private String getRandomNumber(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // 0부터 9까지의 숫자
        }
        return sb.toString();
    }
}
