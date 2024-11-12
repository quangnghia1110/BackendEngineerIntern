package filum.ai.BackendEngineerIntern.model.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class RegisterRequest {
    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(min = 4, max = 20, message = "Tên đăng nhập phải từ 4 đến 20 ký tự")
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 12, max = 50, message = "Mật khẩu phải trên 12 và dưới 50 ký tự")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@&*%!$#^])[A-Za-z\\d@&*%!$#^]{50}$",
            message = "Mật khẩu phải bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt, không chứa ký tự dễ đoán"
    )
    private String password;
}
