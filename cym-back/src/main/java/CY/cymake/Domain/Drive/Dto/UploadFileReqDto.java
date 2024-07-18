package CY.cymake.Domain.Drive.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Schema(description = "새로운 post 등록 dto")
public class UploadFileReqDto {
    @Schema(description = "post_title")
    private String postTitle;

    @Schema(description = "file")
    private MultipartFile file;
}
