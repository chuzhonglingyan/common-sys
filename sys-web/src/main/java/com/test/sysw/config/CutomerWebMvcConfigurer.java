package com.test.sysw.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Administrator
 * @Date: 2020/3/1 0001 22:51
 * @Description:
 */
@Configuration
public class CutomerWebMvcConfigurer implements WebMvcConfigurer {

    @Value("${file.fileAbsolutePath}")
    private String filePath;

    @Value("${file.picAbsolutePath}")
    private String picPath;

    @Value("${file.fileRelativePath}")
    private String fileRelativePath;

    @Value("${file.picRelativePath}")
    private String picRelativePath;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String picUtl = "file:" + picPath.replace("\\", "/");
        String fileUtl = "file:" + filePath.replace("\\", "/");
        registry.addResourceHandler(picRelativePath+"/**").addResourceLocations(picUtl).setCachePeriod(0);
        registry.addResourceHandler(fileRelativePath+"/**").addResourceLocations(fileUtl).setCachePeriod(0);
        registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/resources/").setCachePeriod(0);
    }
}
