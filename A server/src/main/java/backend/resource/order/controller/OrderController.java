package backend.resource.order.controller;

import backend.resource.grpc.GrpcClientService;
import backend.resource.order.dto.OrderRequestDto;
import backend.resource.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;
    private final GrpcClientService grpcClientService;

    @Autowired
    public OrderController(OrderService orderService, GrpcClientService grpcClientService) {
        this.orderService = orderService;
        this.grpcClientService = grpcClientService;
    }

    @PostMapping("")
    public void signup(@RequestBody @Valid OrderRequestDto requestDto){
        log.info("[주문 요청] 상품명: {}", requestDto.getProductId());
        orderService.RegisterService(requestDto);
    }
}
