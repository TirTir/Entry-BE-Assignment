package backend.resource.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {
    ORDER_SUCCESS(true, "Success to register order"),
    SEARCH_SUCCESS(true, "Success to search invoices"),
    UPDATE_SUCCESS(true, "Success to update order");

    private final Boolean success;
    private final String message;
}
