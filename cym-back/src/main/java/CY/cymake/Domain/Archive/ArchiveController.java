package CY.cymake.Domain.Archive;

import CY.cymake.Domain.Archive.Dto.NewsResDto;
import CY.cymake.Response.CommonResult;
import CY.cymake.Response.GlobalResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/archive")
@Tag(name = "Archive", description = "아카이브 API")
public class    ArchiveController {
    private final GlobalResponseHandler globalResponseHandler;
    private final ArchiveService archiveService;

    /*
     * 주제 별 전체 뉴스 기사 조회(DB에서 조회해서 반환해줌)
     */
    @Operation(description = "주제 별 전체 뉴스 기사 조회")
    @GetMapping(value = "/total/{subject}")
    public CommonResult<List<NewsResDto>> getTotalNews(@PathVariable(value = "subject") String subject) {
        return  globalResponseHandler.SendSuccessAndContent(archiveService.getTotalNews(subject));
    }

    /*
     * 주제 별 뉴스 기사 조회(n개)
     */
    @Operation(description = "주제 별 뉴스 기사 조회(n개)")
    @GetMapping(value = "/{subject}")
    public CommonResult<List<NewsResDto>> getNews(@PathVariable(value = "subject") String subject) {
        return globalResponseHandler.SendSuccessAndContent(archiveService.getNews(subject));
    }
}
