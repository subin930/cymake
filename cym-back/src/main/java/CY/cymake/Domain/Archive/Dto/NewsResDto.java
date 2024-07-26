package CY.cymake.Domain.Archive.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Schema(description = "크롤링된 뉴스 조회 Dto")
public class NewsResDto {
    @NotBlank
    @Schema(description = "뉴스 제목")
    private String title;

    @NotBlank
    @Schema(description = "업로드 날짜")
    private Timestamp uploadDate;

    @NotBlank
    @Schema(description = "뉴스 링크")
    private String newsLink;

    @NotBlank
    @Schema(description = "이미지 url")
    private String imgUrl;
}
