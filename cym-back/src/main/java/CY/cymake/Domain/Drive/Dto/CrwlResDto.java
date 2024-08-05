package CY.cymake.Domain.Drive.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Data
@Builder
@Schema(description = "전체 크롤링 수 전송 Dto")
public class CrwlResDto {
    @NotBlank
    @Schema(description = "자동차 크롤링 총 수")
    List<CrwlTotalDto> carCrwlData;

    @NotBlank
    @Schema(description = "화장품 크롤링 총 수")
    List<CrwlTotalDto> beautyCrwlData;
}
