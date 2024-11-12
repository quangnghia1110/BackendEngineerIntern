package filum.ai.BackendEngineerIntern.model.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(min = 4, max = 20, message = "Tên đăng nhập phải từ 4 đến 20 ký tự")
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 12, max = 50, message = "Mật khẩu phải trên 12 và dưới 50 ký tự")
    private String password;
}
