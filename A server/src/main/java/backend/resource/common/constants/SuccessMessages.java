package backend.resource.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessMessages {
    ORDER_SUCCESS(HttpStatus.OK, "주문 성공"),
    SEARCH_SUCCESS(HttpStatus.OK, "검색 성공"),
    UPDATE_SUCCESS(HttpStatus.OK, "상태 변경 성공");

    private final HttpStatus status;
    private final String message;
}
