package com.project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = {"yandexTranslate.property", "googleAddress.property"})
public class AppConfiguration {

}
