package com.easetest.website.service;

import com.easetest.website.model.Question;
import com.easetest.website.model.Test;
import com.easetest.website.repository.QuestionRepository;
import com.easetest.website.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    public void save(Question question) {
        questionRepository.save(question);
    }

    public Set<Question> getAll() {
        return questionRepository.findAll().stream().collect(Collectors.toSet());
    }

    public Question getById(int id) {
        return questionRepository.getOne(id);
    }

    public void deleteById(int id) {
        questionRepository.deleteById(id);
    }

    public void deleteById(Question question) {
        questionRepository.delete(question);
    }
}
