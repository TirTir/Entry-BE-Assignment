package backend.keumbang.common.exceptions;

import backend.keumbang.common.dto.ErrorResponseDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@Builder
@EqualsAndHashCode(callSuper = false) // 부모 클래스의 equals, hashCode 무시
public class GeneralException extends RuntimeException {
    private final String message;
    private final HttpStatus status;

    public GeneralException(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }

    // ErrorResponseDto로 변환
    public ErrorResponseDto toErrorResponseDto() {
        return new ErrorResponseDto(this.message, this.status);
    }
}