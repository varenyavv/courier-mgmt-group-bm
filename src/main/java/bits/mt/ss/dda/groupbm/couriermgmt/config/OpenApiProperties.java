package bits.mt.ss.dda.groupbm.couriermgmt.config;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

/**
 * Properties class that is populated from one of the application.yml or application.properties
 * files that is used to populate the OpenApi/Swagger documentation information.
 */
@ConfigurationProperties(prefix = "application.api")
@ConstructorBinding
@Validated
public class OpenApiProperties {
  /** Title of the Swagger API. */
  @NotBlank private final String title;

  /** Short description of what this api does. */
  @NotBlank private final String description;

  /**
   * Current semantic version of this API. Note that the company spec requires that this be a full
   * semantic version as defined here: https://semver.org/
   */
  @NotBlank private final String version;

  /** The list of default media types that this API creates. */
  @NotNull
  @Size(min = 1)
  private final List<String> producesMediatypes;

  /** The list of default media types that this API consumes. */
  @NotNull
  @Size(min = 1)
  private final List<String> consumesMediatypes;

  /** Contact name of the team supporting this API. */
  @NotBlank private final String contactName;

  /** Email address of the team supporting this API. */
  @NotBlank private final String contactEmail;

  /** Optional URL of the team that supports this API. */
  private final String contactUrl;

  public OpenApiProperties(
      @NotBlank String title,
      @NotBlank String description,
      @NotBlank String version,
      @NotNull List<String> producesMediatypes,
      @NotNull List<String> consumesMediatypes,
      @NotBlank String contactName,
      @NotBlank String contactEmail,
      String contactUrl) {
    this.title = title;
    this.description = description;
    this.version = version;
    this.producesMediatypes = requireNonNull(producesMediatypes);
    this.consumesMediatypes = requireNonNull(consumesMediatypes);
    this.contactName = contactName;
    this.contactEmail = contactEmail;
    this.contactUrl = contactUrl;
  }

  public List<String> getConsumesMediatypes() {
    return consumesMediatypes;
  }

  public String getContactName() {
    return contactName;
  }

  public String getContactEmail() {
    return contactEmail;
  }

  public String getContactUrl() {
    return contactUrl;
  }

  public List<String> getProducesMediatypes() {
    return producesMediatypes;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public String getVersion() {
    return version;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("title", title)
        .append("description", description)
        .append("version", version)
        .append("producesMediatypes", producesMediatypes)
        .append("consumesMediatypes", consumesMediatypes)
        .append("contactName", contactName)
        .append("contactEmail", contactEmail)
        .append("contactUrl", contactUrl)
        .toString();
  }
}
