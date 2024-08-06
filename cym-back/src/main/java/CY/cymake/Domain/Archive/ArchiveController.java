package CY.cymake.Domain.Archive;

import CY.cymake.Domain.Archive.Dto.NewsResDto;
import CY.cymake.Domain.Archive.Dto.CrwlResDto;
import CY.cymake.Response.CommonResult;
import CY.cymake.Response.GlobalResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public CommonResult<List<NewsResDto>> getTotalNews(@PathVariable(value = "subject") String subject) throws Exception {
        return  globalResponseHandler.SendSuccessAndContent(archiveService.getTotalNews(subject));
    }

    /*
     * 주제 별 뉴스 기사 조회(n개)
     */
    @Operation(description = "주제 별 뉴스 기사 조회(n개)")
    @GetMapping(value = "/{subject}")
    public CommonResult<List<NewsResDto>> getNews(@PathVariable(value = "subject") String subject) throws Exception {
        return globalResponseHandler.SendSuccessAndContent(archiveService.getNews(subject));
    }
    /*
     * 뉴스 기사 검색(주제별로)
     */
    @Operation(description = "주제 별 뉴스 기사 검색")
    @GetMapping(value = "/total/{subject}/search")
    public CommonResult<List<NewsResDto>> searchNews(@PathVariable(value = "subject") String subject, @RequestParam(value = "searchBody", required = false) String searchBody) throws Exception {
        if(searchBody == null) {
            return globalResponseHandler.SendSuccessAndContent(archiveService.getTotalNews(subject));
        }
        else
            return globalResponseHandler.SendSuccessAndContent(archiveService.searchNews(subject, searchBody));
    }
    /*
     * 전체 크롤링 수 전송 api
     */
    @GetMapping(value = "/crwlTotal")
    @Operation(description = "전체 크롤링 수 전송")
    public CommonResult<CrwlResDto> getCrwlTotal() {
        return globalResponseHandler.SendSuccessAndContent(archiveService.getCrwlTotal());
    }
}
