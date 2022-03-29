package bits.mt.ss.dda.groupbm.couriermgmt.config;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;

@Configuration
public class JsonConfig {

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer objectMapperConfiguration() {
    return jacksonObjectMapperBuilder -> {
      jacksonObjectMapperBuilder.featuresToDisable(
          DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
      jacksonObjectMapperBuilder.serializationInclusion(JsonInclude.Include.NON_NULL);
    };
  }
}
