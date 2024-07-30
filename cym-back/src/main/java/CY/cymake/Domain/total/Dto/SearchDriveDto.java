package CY.cymake.Domain.total.Dto;

import CY.cymake.Domain.Archive.Dto.NewsSearchResultDto;
import CY.cymake.Domain.Drive.Dto.PostSearchResultDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Schema(description = "통합 검색 결과 Dto")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchDriveDto {
    @NotBlank
    @Schema(description = "지식 아카이브 검색 결과 개수")
    private int num;

    @NotBlank
    @Schema(description = "지식 아카이브 검색 결과 리스트")
    List<PostSearchResultDto> postSearchResultDto;
}
