package CY.cymake.Domain.Archive.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "전송할 크롤링 총 수 데이터")
public class CrwlTotalDto {
    @NotBlank
    @Schema(description = "date")
    String date;

    @NotBlank
    @Schema(description = "total")
    Long total;
}
