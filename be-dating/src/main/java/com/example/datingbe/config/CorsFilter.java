package com.example.datingbe.config;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//Đây là 1  bộ lọc cho ứng dụng web để xử lý về vấn đề CORS
//Lớp CorsFilter này giúp thiết lập các tiêu đề CORS
// để cho phép truy cập từ các nguồn khác nhau.
@Component
public class CorsFilter extends OncePerRequestFilter {

    //-Mô phỏng về cơ chế hoạt động
    //-Class này thiết lập các tiêu đề cho phép truy cập từ các nguồn khác nhau
    //-Khi 1 http được gửi đến ứng dụng của bạn , Class này sẽ được hoạt động
    //Các tiêu đề CORS được thiết lập bằng cách sử dụng phương thức setHeader của đối
    // tượng response để cho phép truy cập từ các nguồn khác nhau:
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "authorization, content-type, xsrf-token");
        response.addHeader("Access-Control-Expose-Headers", "xsrf-token");

        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
