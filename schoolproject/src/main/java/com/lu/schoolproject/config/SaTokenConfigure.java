package com.lu.schoolproject.config;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.serializer.impl.SaSerializerTemplateForJdkUseBase64;
import cn.dev33.satoken.stp.StpUtil;
import com.lu.schoolproject.entitys.properties.SaTokenProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import javax.annotation.PostConstruct;

@Configuration
@RequiredArgsConstructor
public class SaTokenConfigure implements WebMvcConfigurer {
    private final SaTokenProperties saTokenProperties;

    @PostConstruct
    public void rewriteWhitelist() {
        SaManager.setSaSerializerTemplate(new SaSerializerTemplateForJdkUseBase64());
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 创建 SaInterceptor 并配置路径匹配
        registry.addInterceptor(new SaInterceptor(handler -> {
                    SaRouter.match("/**")  // 匹配所有路径
                            .notMatch(saTokenProperties.getWhitelist()) // 排除路径
                            .check(r -> StpUtil.checkLogin()); // 执行登录检查
                }))
                .addPathPatterns("/**"); // 全局拦截所有请求路径
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*") //指定跨域地址  .allowedOrigins("http://localhost:3000", "https://yourdomain.com")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
