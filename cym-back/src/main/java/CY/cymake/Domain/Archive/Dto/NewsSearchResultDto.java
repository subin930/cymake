package CY.cymake.Domain.Archive.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@Schema(description = "파일 검색 결과 Dto")
@NoArgsConstructor
@AllArgsConstructor
public class NewsSearchResultDto {
    @NotBlank
    @Schema(description = "news_id")
    Long news_id;

    @NotBlank
    @Schema(description = "subject")
    String subject;

    @NotBlank
    @Schema(description = "title")
    String title;
}
