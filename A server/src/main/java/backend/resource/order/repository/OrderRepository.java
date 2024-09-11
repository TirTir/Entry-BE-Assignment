package backend.resource.order.repository;

import backend.resource.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
    public interface OrderRepository extends JpaRepository<Order, String> {
        @Query("SELECT o FROM Order o WHERE o.userName = :userName AND o.date >= :startDate AND o.date < :endDate")
        Page<Order> findByUserNameAndDate(
                @Param("userName") String userName,
                @Param("startDate") LocalDateTime startDate,
                @Param("endDate") LocalDateTime endDate,
                Pageable pageable
        );
        @Query("SELECT COUNT(o) FROM Order o WHERE o.date >= :startDate AND o.date < :endDate")
        long countByDate(
                @Param("startDate") LocalDateTime startDate,
                @Param("endDate") LocalDateTime endDate
        );
}
