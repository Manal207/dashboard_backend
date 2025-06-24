package tech.webapp.opticsmanager.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import jakarta.servlet.MultipartConfigElement;

@Configuration
public class FileUploadConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(10));
        factory.setMaxRequestSize(DataSize.ofMegabytes(10));
        return factory.createMultipartConfig();
    }
}

//package tech.webapp.opticsmanager.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.web.servlet.MultipartConfigFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.util.unit.DataSize;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import jakarta.servlet.MultipartConfigElement;
//import java.io.File;
//
//@Configuration
//public class FileUploadConfig implements WebMvcConfigurer {
//
//    @Value("${app.upload.dir:uploads/images}")
//    private String uploadDir;
//
//    @Bean
//    public MultipartConfigElement multipartConfigElement() {
//        MultipartConfigFactory factory = new MultipartConfigFactory();
//        factory.setMaxFileSize(DataSize.ofMegabytes(10));
//        factory.setMaxRequestSize(DataSize.ofMegabytes(10));
//        return factory.createMultipartConfig();
//    }
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // Get absolute path
//        File uploadFolder = new File(uploadDir);
//        String absolutePath = uploadFolder.getAbsolutePath();
//
//        System.out.println("Configuring resource handler for path: " + absolutePath);
//
//        // Serve images via /uploads/images/ URL pattern
//        registry.addResourceHandler("/uploads/images/**")
//                .addResourceLocations("file:" + absolutePath + "/")
//                .setCachePeriod(3600);
//    }
//}
//
////package tech.webapp.opticsmanager.config;
////
////import org.springframework.boot.web.servlet.MultipartConfigFactory;
////import org.springframework.context.annotation.Bean;
////import org.springframework.context.annotation.Configuration;
////import org.springframework.util.unit.DataSize;
////import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
////import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
////
////import jakarta.servlet.MultipartConfigElement;
////
////@Configuration
////public class FileUploadConfig implements WebMvcConfigurer {
////
////    @Bean
////    public MultipartConfigElement multipartConfigElement() {
////        MultipartConfigFactory factory = new MultipartConfigFactory();
////        factory.setMaxFileSize(DataSize.ofMegabytes(10));
////        factory.setMaxRequestSize(DataSize.ofMegabytes(10));
////        return factory.createMultipartConfig();
////    }
////
////    @Override
////    public void addResourceHandlers(ResourceHandlerRegistry registry) {
////        // This allows serving static files from the uploads directory
////        registry.addResourceHandler("/uploads/**")
////                .addResourceLocations("file:uploads/");
////    }
////}