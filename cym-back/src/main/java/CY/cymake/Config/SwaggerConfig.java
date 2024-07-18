package CY.cymake.Config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


//OpenAP 명세서 정의
//API의 제목, 설명, 버전 지정
@OpenAPIDefinition(
        info = @Info(title = "CYMAKE API 명세서",
                description = "CYMAKE API 명세서 상세",
                version = "v1"))


@RequiredArgsConstructor
@Component
public class SwaggerConfig{
    //JWT token 사용시 설정하는 부분
    private static final String BEARER_TOKEN_PREFIX = "Bearer";

    @Bean
    public OpenAPI openAPI() {
        String securityJwtName = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(securityJwtName);
        Components components = new Components()
                //보안 스키마 설정
                .addSecuritySchemes(securityJwtName, new SecurityScheme()
                        .name(BEARER_TOKEN_PREFIX) //보안 스키마 이름 설정
                        .type(SecurityScheme.Type.HTTP) //HTTP 타입
                        .scheme(BEARER_TOKEN_PREFIX) //Bearer 스키마
                        .bearerFormat(securityJwtName));
        return new OpenAPI()
                .addSecurityItem(securityRequirement) //보안 요구사항 정의
                .components(components);
    }

    //그룹 설정 시 설정하는 부분
    @Bean
    public GroupedOpenApi chatOpenApi() {
        String[] paths = {"/v1/**"};

        return GroupedOpenApi.builder()
                .group("    v1")
                .pathsToMatch(paths)
                .build();
    }
}

