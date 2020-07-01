package com.easetest.website.service;

import com.easetest.website.model.Candidate;
import com.easetest.website.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateService {
    private final CandidateRepository candidateRepository;

    public void save(Candidate candidate) {
        candidateRepository.save(candidate);
    }

    public Set<Candidate> getAll() {
        return candidateRepository.findAll().stream().collect(Collectors.toSet());
    }

    public Candidate getById(int id) {
        return candidateRepository.getOne(id);
    }

    public void deleteById(int id) {
        candidateRepository.deleteById(id);
    }

    public void deleteById(Candidate candidate) {
        candidateRepository.delete(candidate);
    }

    public Candidate getByPersonalKey(String key) {
        for (Candidate c : candidateRepository.findAll()) {
            if (c.getPersonalKey().equals(key)) {
                return c;
            }
        }
        return null;
    }
}