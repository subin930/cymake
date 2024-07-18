package CY.cymake.Security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

//인증 실패 시
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    //추후 수정 -> errordto 이용해 에러 log 띄우기
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        // response.sendRedirect("/");
    }
}
