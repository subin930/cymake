package CY.cymake.Domain.Chat;

import CY.cymake.Domain.Chat.Dto.ChatResDto;
import CY.cymake.Response.CommonResult;
import CY.cymake.Response.GlobalResponseHandler;
import CY.cymake.Security.CustomUserDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/chat")
@Tag(name = "Chat API", description = "챗봇 채팅 관련 기능 컨트롤러")
public class ChatController {
    private final GlobalResponseHandler globalResponseHandler;
    private final ChatService chatService;
    @PostMapping(value = "/question", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResult<ChatResDto> chat (@RequestPart(value = "sessionId") String sessionId, @RequestPart(value = "question") String question, @RequestPart(value = "file", required = false) MultipartFile file, @AuthenticationPrincipal CustomUserDetails user) throws IOException {
        return globalResponseHandler.SendSuccessAndContent(chatService.sendToFlask(sessionId, question, user.getUser().getCompanyCode().getCode(), file));
        //return globalResponseHandler.SendSuccessAndContent(chatService.sendToFlask(sessionId, question, "123"));
    }
}
