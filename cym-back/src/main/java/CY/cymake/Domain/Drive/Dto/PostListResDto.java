package CY.cymake.Domain.Drive.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Schema(description = "post 리스트 Dto")
@Builder
public class PostListResDto {
    @NotBlank
    @Schema(description = "fileName")
    String fileName;

    @NotBlank
    @Schema(description = "postTitle")
    String postTitle;

    @NotBlank
    @Schema(description = "id")
    String id;

    @NotBlank
    @Schema(description = "username")
    String username;

    @NotBlank
    @Schema(description = "date")
    Timestamp uploadDate;
    /*
    @NotBlank
    @Schema(description = "size")
    Long size;
     */
}
