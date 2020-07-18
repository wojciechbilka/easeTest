package com.easetest.website.config;

import com.easetest.website.converters.StringToAnswerConverter;
import com.easetest.website.service.QuestionService;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToAnswerConverter());
    }
}