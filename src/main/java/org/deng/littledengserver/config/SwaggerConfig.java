package org.deng.littledengserver.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        // 正确创建Info对象，确保使用的是具体实现类而非抽象类
        Info info = new Info()
                .title("little-deng-server API")
                .version("1.0.0")
                .description("这是一个使用SpringDoc-OpenAPI生成的API文档示例")
                .contact(new Contact()
                        .name("开发者")
                        .email("developer@example.com")
                        .url("https://example.com"))
                .license(new License()
                        .name("Apache 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0"));

        return new OpenAPI().info(info);
    }
}
