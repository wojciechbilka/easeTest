package com.easetest.website.converters;

import com.easetest.website.model.Answer;
import com.easetest.website.repository.QuestionRepository;
import com.easetest.website.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StringToAnswerConverter implements Converter<String, Answer> {

    @Override
    public Answer convert(String s) {
        String[] data = s.split(",");
        return new Answer(
                Integer.parseInt(getSubstringAfterChar(data[1], '=')),
                new String(getSubstringAfterChar(data[2], '=')),
                Boolean.parseBoolean(getSubstringAfterChar(data[3], '=')));
    }

    private String getSubstringAfterChar(String text, char character) {
        return text.substring(text.lastIndexOf(character) + 1);
    }
}
