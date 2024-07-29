package CY.cymake.Domain.total;

import CY.cymake.Domain.total.Dto.TotalSearchDto;
import CY.cymake.Response.CommonResult;
import CY.cymake.Response.GlobalResponseHandler;
import CY.cymake.Security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/total")
@Tag(name = "Total", description = "통합검색창 API")
public class TotalController {
    private final GlobalResponseHandler globalResponseHandler;
    private final TotalService totalService;

    @GetMapping(value = "/search")
    @Operation(description = "통합 검색")
    public CommonResult<TotalSearchDto> searchTotal(@AuthenticationPrincipal CustomUserDetails user, @Valid @RequestParam(value = "searchBody") String searchBody) throws IOException {
        return globalResponseHandler.SendSuccessAndContent(totalService.searchTotal(user.getUser(), searchBody));
    }
}