package backend.resource.order.service;

import backend.keumbang.grpc.AuthResponse;
import backend.resource.common.constants.ErrorMessages;
import backend.resource.common.constants.OrderStatus;
import backend.resource.grpc.GrpcClientService;
import backend.resource.order.dto.OrderRequestDto;
import backend.resource.order.entity.Order;
import backend.resource.product.entity.Product;
import backend.resource.product.repository.ProductRepository;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;

    public void order(AuthResponse authResponse, OrderRequestDto requestDto){
        String role = authResponse.getRole();

        // 상품 찾기
        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.PRODUCT_NOT_FOUND));
        int totalPrice = (int) Math.round(requestDto.getQuantity() * product.getPrice());

        Order newOrder = Order.builder()
                .userName(authResponse.getUsername())
                .date(LocalDateTime.now())
                .status(defaultStatus(role))
                .quantity(requestDto.getQuantity())
                .totalPrice(totalPrice)
                .address(requestDto.getAddress())
                .product(product)
                .build();

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
