package CY.cymake.Domain.Chat;

import CY.cymake.Domain.Chat.Dto.ChatResDto;
import CY.cymake.Response.CommonResult;
import CY.cymake.Response.GlobalResponseHandler;
import CY.cymake.Security.CustomUserDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/chat")
@Tag(name = "Chat API", description = "챗봇 채팅 관련 기능 컨트롤러")
public class ChatController {
    private final GlobalResponseHandler globalResponseHandler;
    private final ChatService chatService;
    @PostMapping("/question")
    public CommonResult<ChatResDto> chat (@RequestParam(value = "sessionId") String sessionId, @RequestParam(value = "question") String question, @AuthenticationPrincipal CustomUserDetails user) throws JsonProcessingException {
        return globalResponseHandler.SendSuccessAndContent(chatService.sendToFlask(sessionId, question, user.getUser().getCompanyCode().getCode()));
        //return globalResponseHandler.SendSuccessAndContent(chatService.sendToFlask(sessionId, question, "123"));
    }
}
