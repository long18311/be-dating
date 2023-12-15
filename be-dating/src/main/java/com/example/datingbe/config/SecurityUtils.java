package com.example.datingbe.config;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class SecurityUtils {


    /*Phương thức này trả về tên đăng nhập của người dùng hiện đang đăng nhập trong hệ thống.
    Phương thức trả về một đối tượng kiểu Optional<String>,
     cho phép giá trị trả về có thể là một chuỗi tên đăng nhập hoặc không có giá trị (rỗng).*/
    public static Optional<String> getCurrentUserLogin() {

        SecurityContext securityContext = SecurityContextHolder.getContext();

        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }
     /*. Đối tượng Authentication chứa thông tin về việc xác thực người dùng,
      bao gồm cả thông tin người dùng và quyền.*/
    private static String extractPrincipal(Authentication authentication) {
       // Nếu là null, nghĩa là không có thông tin xác thực, và phương thức trả về null.
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails) {

            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            //thì phương thức trích xuất tên đăng nhập từ đối tượng UserDetails.
            return springSecurityUser.getUsername();

        } else if (authentication.getPrincipal() instanceof String) {

            return (String) authentication.getPrincipal();
        }
        return null;
    }
}