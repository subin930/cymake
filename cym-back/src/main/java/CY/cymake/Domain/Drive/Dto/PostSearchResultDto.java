package CY.cymake.Domain.Drive.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Schema(description = "post 리스트 Dto")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostSearchResultDto {
    @NotBlank
    @Schema(description = "company_code")
    String company_code;

    @NotBlank
    @Schema(description = "file_name")
    String file_name;

    @NotBlank
    @Schema(description = "post_title")
    String post_title;

    @NotBlank
    @Schema(description = "file_url")
    String file_url;

    @NotBlank
    @Schema(description = "file_id")
    String file_id;

    @NotBlank
    @Schema(description = "uploader")
    String uploader;

    @NotBlank
    @Schema(description = "upload_date")
    Timestamp upload_date;

    @NotBlank
    @Schema(description = "last_edit_date")
    Timestamp last_edit_date;

    @NotBlank
    @Schema(description = "type")
    String type;

    @NotBlank
    @Schema(description = "size")
    Double size;
}
