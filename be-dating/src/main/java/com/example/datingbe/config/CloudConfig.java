package com.example.datingbe.config;
import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudConfig {
    @Bean
    public Cloudinary cloudinaryConfigs() {
        Cloudinary cloudinary = null;
        Map config = new HashMap();
        config.put("cloud_name", "ducauhnpz");
        config.put("api_key", "858276533397185");
        config.put("api_secret", "WbaXip3Oa4orpHmIE1AxkNU1tuY");
        cloudinary = new Cloudinary(config);
        return cloudinary;
    }
}
