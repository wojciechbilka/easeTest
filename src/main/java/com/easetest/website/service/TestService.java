package com.easetest.website.service;

import com.easetest.website.model.Test;
import com.easetest.website.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;

    public void save(Test test) {
        testRepository.save(test);
    }

    public Set<Test> getAll() {
        return testRepository.findAll().stream().collect(Collectors.toSet());
    }

    public Test getById(int id) {
        return testRepository.getOne(id);
    }

    public void deleteById(int id) {
        testRepository.deleteById(id);
    }

    public void deleteById(Test test) {
        testRepository.delete(test);
    }
}
