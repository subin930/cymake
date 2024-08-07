package CY.cymake.Domain.Chat;

import CY.cymake.AWS.S3Service;
import CY.cymake.Domain.Chat.Dto.ChatReqDto;
import CY.cymake.Domain.Chat.Dto.ChatResDto;
import CY.cymake.Exception.ChatException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.json.Json;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ObjectMapper objectMapper;
    private final S3Service s3Service;
    @Transactional
    public ChatResDto sendToFlask(String sessionId, String question, String companyCode, MultipartFile file) throws IOException {
        String fileUrl = s3Service.uploadFile(file, "files/search/" + file.getOriginalFilename());
        System.out.println(fileUrl);
        ChatReqDto chatReqDto = new ChatReqDto(companyCode, question, sessionId, fileUrl);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new CustomResponseErrorHandler());
        //헤더를 JSON으로 설정
        HttpHeaders headers = new HttpHeaders();

        //파라미터로 들어온 Dto를 JSON 객체로 변환
        headers.setContentType(MediaType.APPLICATION_JSON);

        String param = objectMapper.writeValueAsString(chatReqDto);
        HttpEntity<String> entity = new HttpEntity<>(param, headers);

        //실제 Flask 서버랑 연결하기 위한 URL
        String url = "http://localhost:5000/tospring";

        //Flask 서버로 데이터를 전송하고 받은 응답 값을 return
        String result = restTemplate.postForObject(url, entity, String.class);
        //파일 s3에서 삭제
        s3Service.deleteFile("files/search/", file.getOriginalFilename());
        // String을 JSON 형식으로 변환
        Map<String, Object> responseMap = new HashMap<>();
        // String을 JSON 형식으로 변환
        JsonNode responseJson;
        try {
            responseJson = objectMapper.readTree(result);
        } catch (IOException e) {
            throw new ChatException("Failed to parse JSON response from Flask server", e);
        }

        // JSON에서 output.text와 sessionId를 추출
        String response = responseJson.path("output").path("text").asText();
        String responseSessionId = responseJson.path("sessionId").asText();

        return new ChatResDto(response, responseSessionId);
    }
    private static class CustomResponseErrorHandler extends DefaultResponseErrorHandler {
        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            String body = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
            throw new ChatException("Error from Flask server: " + body);
        }
    }
}
