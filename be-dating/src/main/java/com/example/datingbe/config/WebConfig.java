package com.example.datingbe.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.MultipartConfigElement;

@Configuration

public class WebConfig extends WebMvcConfigurerAdapter {

    //cấu hình hỗ trợ CORS (Cross-Origin Resource Sharing)
    //CORS là một cơ chế cho phép hoặc hạn chế các yêu cầu từ các nguồn gốc khác nhau.
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //Cho phép tất cả các nguồn gốc truy cập tài nguyên.
        registry.addMapping("/**").allowedOrigins("*")
        //Không cho phép gửi thông tin xác thực (credentials) (ví dụ: cookies) khi thực hiện yêu cầu.
                .allowCredentials(false)
        //Chấp nhận các nguồn gốc có địa chỉ bắt đầu bằng "http://".
                .allowedOriginPatterns("http://*")
        //Cho phép tất cả các phương thức HTTP (GET, POST, PUT, DELETE, vv.).
                .allowedMethods("*")
        //Cho phép tất cả các loại header trong yêu cầu.
                .allowedHeaders("*")
         //Đặt thời gian tối đa mà các thông tin CORS được lưu trong bộ nhớ cache của trình duyệt.
                .maxAge(1800).exposedHeaders("Authorization,Link,X-Total-Count,X-${jhipster.clientApp.name}-alert,X-${jhipster.clientApp.name}-error,X-${jhipster.clientApp.name}-params");

    }

    // Phương thức này được ghi đè để cấu hình việc xử lý tài nguyên tĩnh (ví dụ: hình ảnh, CSS)
    // bằng cách ánh xạ đường dẫn tới các tài nguyên đó trong ứng dụng.
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");
    }
    // Phương thức này trả về một cấu hình cho việc xử lý dữ liệu đa phần (multipart). Trong trường hợp này, nó thiết lập giới hạn kích thước tối đa cho cả dữ liệu đa phần trong
    // một yêu cầu và một tệp tải lên là 1,000,000,000 byte (khoảng 1GB).
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofBytes(1000000000L));
        factory.setMaxRequestSize(DataSize.ofBytes(1000000000L));
        return factory.createMultipartConfig();
    }
    //long để chạy dòng 41 MessageService
//    @Bean
//    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
//        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//        converter.setObjectMapper(objectMapper);
//        return converter;
//    }
}

