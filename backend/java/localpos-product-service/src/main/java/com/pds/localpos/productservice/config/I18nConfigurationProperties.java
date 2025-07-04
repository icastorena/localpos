package com.pds.localpos.productservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class I18nConfigurationProperties {

    private String locale;

    public Locale getLocale() {
        return (locale != null && !locale.isBlank()) ? Locale.forLanguageTag(locale) : null;
    }
}
