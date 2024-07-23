package CY.cymake.Domain.Drive;

import CY.cymake.Domain.Drive.Dto.Item;
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
    private final SearchDriveService searchDriveService;
    private final GlobalResponseHandler globalResponseHandler;
    /*
     * 파일 업로드
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "파일 업로드")
    public CommonResult<String> upload(
            @Parameter(required = true, description = "파일 업로드 요청 정보") @Valid @RequestPart("file") MultipartFile multipartFile, @Valid @RequestPart("title") String postTitle,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) throws IOException {
        return globalResponseHandler.SendSuccessAndContent(driveService.uploadFile(customUserDetails.getUser(), multipartFile, postTitle));
    }
    /*
     * 파일 다운로드
     */
    @GetMapping(value = "/download")
    @Operation(description = "파일 다운로드")
    public ResponseEntity<byte[]> download(@RequestParam String filename, @AuthenticationPrincipal CustomUserDetails user) throws IOException{
        return driveService.download(user.getUser(), filename);
    }
    /*
     * 파일 삭제
     */
    @DeleteMapping(value = "/delete")
    @Operation(description = "파일 삭제")
    public CommonBaseResult delete(
            @Parameter(required = true, description = "파일 삭제 요청 정보") @Valid @RequestBody String filename, @AuthenticationPrincipal CustomUserDetails user
    ) throws IOException {
        driveService.deleteFile(user.getUser(), filename);
        return globalResponseHandler.SendSuccess();
    }

    /*
     * 파일 수정
     */
    @PutMapping(value = "/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "파일 수정")
    public CommonBaseResult edit(@Parameter(required = true, description = "파일 수정 요청 정보")@Valid @RequestPart String originalFilename, @Valid @RequestPart String postTitle, @RequestPart("file") MultipartFile multipartFile, @AuthenticationPrincipal CustomUserDetails user) throws IOException {
        driveService.updateFile(user.getUser(), multipartFile, originalFilename, postTitle);
        return globalResponseHandler.SendSuccess();
    }

    /*
     * 파일 검색
     */
    @GetMapping(value = "/search")
    @Operation(description = "통합 아카이브 검색")
    public CommonResult<List<Item>> searchDrive(@Parameter(required = true, description = "post 검색 요청 정보") @Valid @RequestParam String searchBody) throws IOException {
        searchDriveService.indexData();
        return globalResponseHandler.SendSuccessAndContent(searchDriveService.searchDrive(searchBody));
    }

    /*
     * post 리스트 전송
     */
    @GetMapping(value = "/list")
    @Operation(description = "post 리스트 전송")
    public CommonResult<List<PostListResDto>> getPostList() {
        return globalResponseHandler.SendSuccessAndContent(driveService.getPostList());
    }

}
