package com.example.xxjob.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Author GoryLee
 * @Date 2023/12/8
 */
@Data
//@Configuration
//@ConfigurationProperties(prefix = "email")
public class EmailConfig {

    private String regionId;
    private String accessKeyId;
    private String secret;

}
