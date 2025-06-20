package org.aps.common.config;

import lombok.RequiredArgsConstructor;
import org.aps.common.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class AppConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    // 인터셉터 등록 설정
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)

                // 인증이 필요한 경로
                .addPathPatterns(
                        "/api/auth/change-password",
                        "/api/management/**",
                        "/api/scenarios/**"

                )

                // 로그인 없이 접근 가능한 경로
                .excludePathPatterns(
                        "/api/auth/login",
                        "/api/auth/signup",
                        "/api/auth/find-password"
                );
    }
}
