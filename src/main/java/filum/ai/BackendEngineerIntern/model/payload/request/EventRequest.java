package filum.ai.BackendEngineerIntern.model.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {
    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 100, message = "Tiêu đề không được quá 100 ký tự")
    private String title;

    @Size(max = 500, message = "Mô tả không được quá 500 ký tự")
    private String description;

    @NotNull(message = "Ngày không được để trống")
    private LocalDate date;

    @NotBlank(message = "Địa điểm không được để trống")
    @Size(max = 200, message = "Địa điểm không được quá 200 ký tự")
    private String location;

    @NotBlank(message = "Danh mục không được để trống")
    @Size(max = 50, message = "Danh mục không được quá 50 ký tự")
    private String category;

    @NotNull(message = "Sức chứa không được để trống")
    private Integer capacity;
}
