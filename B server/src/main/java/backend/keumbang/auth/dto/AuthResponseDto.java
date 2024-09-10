package backend.keumbang.auth.dto;

import lombok.Data;

@Data
public class AuthResponseDto {
    private int id;
    private String userName;
    private String role;
}