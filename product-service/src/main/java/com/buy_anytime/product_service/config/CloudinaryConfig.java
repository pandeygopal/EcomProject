package com.buy_anytime.product_service.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
//di2cllznu-->name
//846424393491587-->api key
//61D9ZYBYU18_7P35XZrYjGZ84W4-->secreat key
//    @Value("${cloudinary.cloud-name}")
	@Value("di2cllznu")
    private String cloudName;

//    @Value("${cloudinary.api-key}")
	@Value("846424393491587")
    private String apiKey;

//    @Value("${cloudinary.api-secret}")
	@Value("61D9ZYBYU18_7P35XZrYjGZ84W4")
    private String apiSecret;

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true
        ));
    }
}