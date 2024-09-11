package backend.resource.common.response;

import backend.resource.common.constants.SuccessMessages;
import backend.resource.common.pagination.PaginationLinks;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@Builder
public class CommonResponse<T> {
    private HttpStatus status;
    private String message;
    private T data;
    private PaginationLinks links;

    public CommonResponse(SuccessMessages success) {
        this.status = success.getStatus();
        this.message = success.getMessage();
    }

    public static<T> CommonResponse<T> res(SuccessMessages success) {
        return new CommonResponse(success);
    }

    public static<T> CommonResponse<T> pageRes(SuccessMessages success, final T data) {
        return CommonResponse.<T>builder()
                .status(success.getStatus())
                .message(success.getMessage())
                .data(data)
                .links(links)
                .build();
    }
}
