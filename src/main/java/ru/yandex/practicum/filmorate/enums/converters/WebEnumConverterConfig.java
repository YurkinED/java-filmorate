package ru.yandex.practicum.filmorate.enums.converters;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebEnumConverterConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new SortByStringToEnumConverter());
        registry.addConverter(new SearchingStringToEnumConverter());
    }
}
