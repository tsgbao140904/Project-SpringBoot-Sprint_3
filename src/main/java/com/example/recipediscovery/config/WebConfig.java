package com.example.recipediscovery.config;

import com.example.recipediscovery.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    public WebConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");

        // ánh xạ /images/** tới thư mục uploads ở ngoài project
        String uploadPath = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + uploadPath);
    }
}
