package backend.resource.order.repository;

import backend.resource.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
public interface OrderRepository extends JpaRepository<Order, String> {
    Page<Order> findByDateAndInvoiceType(LocalDateTime date, String invoiceType, Pageable pageable);
    long countByDateAndInvoiceType(LocalDateTime date, String invoiceType);
}
