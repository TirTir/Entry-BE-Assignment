package backend.resource.common.response;

import backend.resource.common.constants.SuccessCode;
import backend.resource.common.pagination.PaginationLinks;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CommonResponse<T> {
    private Boolean success;
    private String message;
    private T data;
    private PaginationLinks links;

    public CommonResponse(SuccessCode success) {
        this.success = success.getSuccess();
        this.message = success.getMessage();
    }

    public static<T> CommonResponse<T> res(SuccessCode success) {
        return new CommonResponse(success);
    }

    public static<T> CommonResponse<T> pageRes(SuccessCode success, final T data) {
        return CommonResponse.<T>builder()
                .success(success.getSuccess())
                .message(success.getMessage())
                .data(data)
                .links(links)
                .build();
    }
}
