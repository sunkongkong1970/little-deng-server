package org.deng.littledengserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    // 注册RestTemplate到Spring容器中
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
