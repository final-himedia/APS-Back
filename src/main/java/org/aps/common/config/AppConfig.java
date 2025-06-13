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
                // 인터셉터를 적용할 URL 패턴
                .addPathPatterns(
                        "/api/management/**"
                )
                // 필요 없는 URL은 제외
                .excludePathPatterns(
                        "/api/auth/**"
                );
    }
}
