package bits.mt.ss.dda.groupbm.couriermgmt.config;

import java.util.Objects;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

/**
 * Configuration for the tool that automatically creates SpringDoc documentation for the API
 * associated with this microservice.
 */
@Configuration
@EnableConfigurationProperties(OpenApiProperties.class)
public class SpringDocConfig implements WebMvcConfigurer {

  private final OpenApiProperties openApiProperties;

  public SpringDocConfig(OpenApiProperties openApiProperties) {
    this.openApiProperties = Objects.requireNonNull(openApiProperties);
  }

  @Bean
  public OpenAPI springShopOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title(openApiProperties.getTitle())
                .description(openApiProperties.getDescription())
                .contact(buildContact())
                .version(openApiProperties.getVersion()));
  }

  private Contact buildContact() {
    return new Contact()
        .name(openApiProperties.getContactName())
        .url(openApiProperties.getContactUrl())
        .email(openApiProperties.getContactEmail());
  }
}
