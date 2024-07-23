package CY.cymake.Domain.Drive.Dto;

import CY.cymake.Entity.UsersEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Item {
    @NotBlank
    @Schema(description = "id")
    private int id;

    @NotBlank
    @Schema(description = "파일 이름")
    private String fileName;

    @NotBlank
    @Schema(description = "글 제목")
    private String postTitle;

    @NotBlank
    @Schema(description = "업로더")
    private String uploader;
}
