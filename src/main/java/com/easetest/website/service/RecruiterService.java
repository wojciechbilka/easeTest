package com.easetest.website.service;

import com.easetest.website.model.Recruiter;
import com.easetest.website.repository.RecruiterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecruiterService {

    private final RecruiterRepository recruiterRepository;

    public void save(Recruiter recruiter) {
        recruiterRepository.save(recruiter);
    }

    public Set<Recruiter> getAll() {
        return recruiterRepository.findAll().stream().collect(Collectors.toSet());
    }

    public Recruiter getById(int id) {
        return recruiterRepository.getOne(id);
    }

    public void deleteById(int id) {
        recruiterRepository.deleteById(id);
    }

    public void deleteById(Recruiter recruiter) {
        recruiterRepository.delete(recruiter);
    }
}
