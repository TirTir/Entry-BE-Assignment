package backend.keumbang.auth.dto;

import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegisterRequestDto {
    @NotBlank(message = "이름을 입력해주세요.")
    @Size(min = 2, max = 8, message = "이름을 2~8자 사이로 입력해주세요.")
    private String userName;

    // 비밀번호(영문, 특수문자, 숫자 포함 8자 이상)
    @NotBlank
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&.])[A-Za-z\\d$@$!%*#?&.]{8,20}$",
            message = "비밀번호는 영문자와 특수문자를 포함하여 8자 이상 20자 이하로 입력해주세요."
    )
    private String password;
}
