package backend.resource.invoice.service;

import backend.resource.common.pagination.PaginationLinks;
import backend.resource.common.constants.ErrorMessages;
import backend.resource.common.exceptions.GeneralException;
import backend.resource.invoice.dto.InvoiceRequestDto;
import backend.resource.invoice.entity.InvoiceType;
import backend.resource.order.dto.OrderDetailResponseDto;
import backend.resource.order.entity.Order;
import backend.resource.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final OrderRepository orderRepository;

    @Transactional
    public List<OrderDetailResponseDto> OrderDetailService(InvoiceRequestDto requestDto){
        Pageable pageable = PageRequest.of(requestDto.getOffset(), requestDto.getLimit());

        if (requestDto.getInvoiceType() != null && !isValidInvoiceType(requestDto.getInvoiceType())) {
            throw new GeneralException(ErrorMessages.INVOICETYPE_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }

        // Order 목록을 필터링
        Page<Order> ordersPage = orderRepository.findByDateAndInvoiceType(
                requestDto.getDate(),
                String.valueOf(requestDto.getInvoiceType()),
                pageable);

        // OrderDetailResponseDto로 변환
        return ordersPage.getContent().stream()
                .map(order -> new OrderDetailResponseDto(
                        order.getId(),
                        order.getUserName(),
                        order.getProduct().getItemType(),
                        order.getQuantity().doubleValue(),
                        order.getTotalPrice(),
                        order.getStatus(),
                        order.getDate().toLocalDate(),
                        order.getAddress()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public PaginationLinks PaginationService(InvoiceRequestDto requestDto){
        int offset = requestDto.getOffset(); // 페이지 당 항목 수
        int limit = requestDto.getLimit(); // 데이터 시작 지점

        // 조건에 맞는 전체 개수
        long totalItems = orderRepository.countByDateAndInvoiceType(
                requestDto.getDate(),
                String.valueOf(requestDto.getInvoiceType())
        );

        String baseUrl = "/api/order?";

        String nextUrl = baseUrl + "offset=" + (offset + limit) + "&limit=" + limit;
        String prevUrl = offset - limit >= 0 ? baseUrl + "offset=" + (offset - limit) + "&limit=" + limit : null;
        String firstUrl = baseUrl + "offset=0&limit=" + limit;
        String lastUrl = baseUrl + "offset=" + ((totalItems - 1) / limit * limit) + "&limit=" + limit;

        return new PaginationLinks(firstUrl, lastUrl, nextUrl, prevUrl);
    }

    private boolean isValidInvoiceType(String invoiceType) {
        for (InvoiceType type : InvoiceType.values()) {
            if (type.equals(invoiceType)) {
                return true;
            }
        }
        return false;
    }
}
