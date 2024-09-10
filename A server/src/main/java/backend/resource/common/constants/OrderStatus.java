package backend.resource.common.constants;

public enum OrderStatus {
    // 구매 주문 상태 (user)
    ORDER_PLACED("주문 완료", Role.USER),
    PAYMENT_COMPLETED("입금 완료", Role.USER),
    SHIPPED("발송 완료", Role.USER),

    // 판매 주문 상태 (admin)
    ORDER_RECEIVED("주문 완료", Role.ADMIN),
    PAYMENT_SENT("송금 완료", Role.ADMIN),
    RECEIVED("수령 완료", Role.ADMIN);

    private final String description;
    private final Role applicableRole; // 어떤 역할에서 사용 가능한지

    OrderStatus(String description, Role applicableRole) {
        this.description = description;
        this.applicableRole = applicableRole;
    }

    public String getDescription() {
        return description;
    }

    public Role getApplicableRole() {
        return applicableRole;
    }
}
