package backend.resource.order.controller;

import backend.resource.common.constants.SuccessCode;
import backend.resource.common.pagination.PaginationLinks;
import backend.resource.common.response.CommonResponse;
import backend.resource.grpc.GrpcClientService;
import backend.resource.invoice.dto.InvoiceRequestDto;
import backend.resource.invoice.service.InvoiceService;
import backend.resource.order.dto.OrderDetailResponseDto;
import backend.resource.order.dto.OrderRequestDto;
import backend.resource.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Slf4j
@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;
    private final InvoiceService invoiceService;
    private final GrpcClientService grpcClientService;

    @Autowired
    public OrderController(OrderService orderService, InvoiceService invoiceService, GrpcClientService grpcClientService) {
        this.orderService = orderService;
        this.invoiceService = invoiceService;
        this.grpcClientService = grpcClientService;
    }

    @PostMapping("")
    public CommonResponse order(@RequestBody @Valid OrderRequestDto requestDto){
        log.info("[주문 요청] 상품명: {}", requestDto.getProductId());
        orderService.RegisterService(requestDto);
        return CommonResponse.res(SuccessCode.ORDER_SUCCESS);
    }

    @GetMapping("")
    public CommonResponse<List<OrderDetailResponseDto>> orderDetail(
            @RequestParam(required = false) LocalDate date,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(required = false) String type
    ){
        String invoiceType = (type != null) ? type.toUpperCase(Locale.ROOT) : null;

        InvoiceRequestDto requestDto = InvoiceRequestDto.builder()
                .date(date)
                .limit(limit)
                .offset(offset)
                .invoiceType(invoiceType)
                .build();

        // OrderDetailResponseDto 객체 생성
        List<OrderDetailResponseDto> orders = invoiceService.OrderDetailService(requestDto);

        // Pagination 링크 생성
        PaginationLinks paginationLinks = invoiceService.PaginationService(requestDto);

        return CommonResponse.pageRes(SuccessCode.ORDER_SUCCESS, orders, paginationLinks);
    }
}
