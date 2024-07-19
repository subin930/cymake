package CY.cymake.Domain.Archive;

import CY.cymake.Domain.Auth.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/archive")
@Tag(name = "Archive", description = "아카이브 API")
public class    ArchiveController {
    private final AuthService authService;

    /*
     * 주제 별 전체 뉴스 기사 조회(DB에서 조회해서 반환해줌)
     */
    /*
    @Operation(description = "주제 별 전체 뉴스 기사 조회")
    @GetMapping(value = "/total/{subject}")
    public CommonResult<NewsResDto> getTotalNews(@PathVariable String subject) {

    }

     */
}
