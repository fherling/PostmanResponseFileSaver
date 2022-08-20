package com.prodyna.postmanresponsefilesaver.config;

import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
@Data
public class ApplicationConfigProperties {
  @NotNull
  private String contentDir;

}
