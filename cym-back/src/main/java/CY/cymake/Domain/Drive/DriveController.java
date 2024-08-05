package CY.cymake.Domain.Drive;

import CY.cymake.Domain.Drive.Dto.CrwlResDto;
import CY.cymake.Domain.Drive.Dto.PostListResDto;
import CY.cymake.Response.CommonBaseResult;
import CY.cymake.Response.CommonResult;
import CY.cymake.Response.GlobalResponseHandler;
import CY.cymake.Security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/drive")
@Tag(name = "Drive", description = "통합관리자 API")
public class DriveController {
    private final DriveService driveService;
    private final GlobalResponseHandler globalResponseHandler;
    /*
     * 파일 업로드
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "파일 업로드")
    public CommonResult<String> upload(
            @Parameter(required = true, description = "파일 업로드 요청 정보") @Valid @RequestPart(value = "file") MultipartFile multipartFile, @Valid @RequestPart(value = "title") String postTitle,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) throws IOException, Exception {
        return globalResponseHandler.SendSuccessAndContent(driveService.uploadFile(customUserDetails.getUser(), multipartFile, postTitle));
    }
    /*
     * 파일 다운로드
     */
    /*
    @GetMapping(value = "/download")
    @Operation(description = "파일 다운로드")
    public ResponseEntity<byte[]> download(@RequestParam(value = "filename") String filename, @AuthenticationPrincipal CustomUserDetails user) throws IOException{
        return driveService.download(user.getUser(), filename);
    }

     */
    /*
     * 파일 삭제
     */
    @DeleteMapping(value = "/delete")
    @Operation(description = "파일 삭제")
    public CommonBaseResult delete(
            @Parameter(required = true, description = "파일 삭제 요청 정보") @Valid @RequestParam(value = "filename") String filename, @AuthenticationPrincipal CustomUserDetails user
    ) throws IOException {
        driveService.deleteFile(user.getUser(), filename);
        return globalResponseHandler.SendSuccess();
    }

    /*
     * 파일 수정
     */
    @PutMapping(value = "/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "파일 수정")
    public CommonBaseResult edit(@Valid @RequestPart(value = "postTitle") String postTitle, @Valid @RequestPart(value = "originalFilename") String originalFilename, @RequestPart(value = "file", required = false) MultipartFile newFile, @AuthenticationPrincipal CustomUserDetails user) throws IOException {
        driveService.updateFile(user.getUser(), newFile, originalFilename, postTitle);
        return globalResponseHandler.SendSuccess();
    }

    /*
     * post 리스트 전송
     */
    @GetMapping(value = "/list")
    @Operation(description = "post 리스트 전송")
    public CommonResult<List<PostListResDto>> getPostList(@AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException, Exception {
        return globalResponseHandler.SendSuccessAndContent(driveService.getPostList(customUserDetails.getUser()));
    }
    /*
     * post 검색
     */
    @GetMapping(value = "/search")
    @Operation(description = "post 검색")
    public CommonResult<List<PostListResDto>> searchPost(@AuthenticationPrincipal CustomUserDetails customUserDetails, @Valid @RequestParam(value = "searchBody", required = false) String searchBody) throws Exception {
        if(searchBody == null) {
            return globalResponseHandler.SendSuccessAndContent(driveService.getPostList(customUserDetails.getUser()));
        }
        else
            return globalResponseHandler.SendSuccessAndContent(driveService.searchPost(customUserDetails.getUser(), searchBody));
    }
    /*
     * 전체 크롤링 수 전송 api
     */
    @GetMapping(value = "/crwlTotal")
    @Operation(description = "전체 크롤링 수 전송")
    public CommonResult<CrwlResDto> getCrwlTotal() {
        return globalResponseHandler.SendSuccessAndContent(driveService.getCrwlTotal());
    }
}
