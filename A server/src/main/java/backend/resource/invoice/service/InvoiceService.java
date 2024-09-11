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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final OrderRepository orderRepository;

    @Transactional
    public List<OrderDetailResponseDto> OrderDetailService(InvoiceRequestDto requestDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .orElseThrow(() -> new GeneralException(ErrorMessages.ROLE_NOT_FOUND, HttpStatus.BAD_REQUEST))
                .getAuthority();

        // 역할에 따른 인보이스 타입 유효성 검사
        if (requestDto.getInvoiceType() != null && !isValidInvoiceType(role, requestDto.getInvoiceType())) {
            throw new GeneralException(ErrorMessages.INVOICETYPE_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }

        // Order 목록을 필터링
        Pageable pageable = PageRequest.of(requestDto.getOffset(), requestDto.getLimit());
        LocalDate date = requestDto.getDate();

        Page<Order> ordersPage = orderRepository.findByUserNameAndDate(
                authentication.getName(),  // 현재 로그인한 사용자 정보 사용
                date.atStartOfDay(), // 00:00:00
                date.plusDays(1).atStartOfDay(),
                pageable
        );


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
        LocalDate date = requestDto.getDate();
        long totalItems = orderRepository.countByDate(
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay()
        );

        String baseUrl = "/api/order?";

        String nextUrl = baseUrl + "offset=" + (offset + limit) + "&limit=" + limit;
        String prevUrl = offset - limit >= 0 ? baseUrl + "offset=" + (offset - limit) + "&limit=" + limit : null;
        String firstUrl = baseUrl + "offset=0&limit=" + limit;
        String lastUrl = baseUrl + "offset=" + ((totalItems - 1) / limit * limit) + "&limit=" + limit;

        return new PaginationLinks(firstUrl, lastUrl, nextUrl, prevUrl);
    }

    private boolean isValidInvoiceType(String role, String type) {
        InvoiceType invoiceType;

        try {
            invoiceType = InvoiceType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return false;
        }

        switch (role.toLowerCase()) {
            case "admin":
                // admin은 판매만 가능
                return invoiceType == InvoiceType.SALE;
            case "user":
                // user는 구매만 가능
                return invoiceType == InvoiceType.PURCHASE;
            default:
                return false;
        }
    }
}
