package org.zerock.ex3.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:C:/Users/Administrator/Desktop/GIT/Project2/2.back/upload/");
                //http://localhost:포트번호/upload/test.jpg
                //와 같은 URL로 접근가능
    }
}
