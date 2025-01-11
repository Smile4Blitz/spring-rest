package com.example.api.service;

import com.example.api.model.ExampleResponse;
import com.example.api.repository.ExampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExampleService {

    private final ExampleRepository exampleRepository;

    @Autowired
    public ExampleService(ExampleRepository exampleRepository) {
        this.exampleRepository = exampleRepository;
    }

    public ExampleResponse getExampleById(Long id) {
        return exampleRepository.findById(id).orElseThrow(() -> new RuntimeException("Example not found"));
    }

    public List<ExampleResponse> getAllExamples() {
        return exampleRepository.findAll();
    }

    public Page<ExampleResponse> searchExamples(String keyword, Pageable pageable) {
        return exampleRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }

    public ExampleResponse createExample(ExampleResponse exampleResponse) {
        return exampleRepository.save(exampleResponse);
    }

    public ExampleResponse updateExample(Long id, ExampleResponse updatedExample) {
        ExampleResponse existingExample = getExampleById(id);
        existingExample.setName(updatedExample.getName());
        existingExample.setDescription(updatedExample.getDescription());
        return exampleRepository.save(existingExample);
    }

    public void deleteExample(Long id) {
        exampleRepository.deleteById(id);
    }
}