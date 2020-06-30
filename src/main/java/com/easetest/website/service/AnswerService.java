package com.easetest.website.service;

import com.easetest.website.model.Answer;
import com.easetest.website.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;

    public void save(Answer answer) {
        answerRepository.save(answer);
    }

    public Set<Answer> getAll() {
        return answerRepository.findAll().stream().collect(Collectors.toSet());
    }

    public Answer getById(int id) {
        return answerRepository.getOne(id);
    }

    public void deleteById(int id) {
        answerRepository.deleteById(id);
    }

    public void deleteById(Answer answer) {
        answerRepository.delete(answer);
    }
}