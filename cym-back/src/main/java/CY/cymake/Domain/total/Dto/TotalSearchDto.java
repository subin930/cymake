package CY.cymake.Domain.total.Dto;

import CY.cymake.Domain.Archive.Dto.NewsResDto;
import CY.cymake.Domain.Drive.Dto.PostListResDto;
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
public class TotalSearchDto {
    @NotBlank
    @Schema(description = "지식 아카이브 검색 결과 개수(car)")
    private int numCar;

    @NotBlank
    @Schema(description = "지식 아카이브 검색 결과 리스트(car)")
    private List<NewsResDto> archiveCarSearchResult;

    @NotBlank
    @Schema(description = "지식 아카이브 검색 결과 개수(beauty)")
    private int numBeauty;

    @NotBlank
    @Schema(description = "지식 아카이브 검색 결과 리스트(beauty)")
    private List<NewsResDto> archiveBeautySearchResult;

    @NotBlank
    @Schema(description = "통합 자료실 검색 결과 개수")
    private int numDrive;

    @NotBlank
    @Schema(description = "통합 자료실 검색 결과 리스트")
    private List<PostListResDto> driveSearchResult;
}
