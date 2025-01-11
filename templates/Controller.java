package com.example.api.controller;

import com.example.api.service.ExampleService;
import com.example.api.model.ExampleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@RestController
@RequestMapping("/api/v1/examples")
public class ExampleController {

    private final ExampleService exampleService;

    @Autowired
    public ExampleController(ExampleService exampleService) {
        this.exampleService = exampleService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExampleResponse> getExampleById(@PathVariable Long id) {
        return ResponseEntity.ok(exampleService.getExampleById(id));
    }

    @GetMapping
    public ResponseEntity<List<ExampleResponse>> getAllExamples() {
        return ResponseEntity.ok(exampleService.getAllExamples());
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ExampleResponse>> searchExamples(@RequestParam String keyword, Pageable pageable) {
        return ResponseEntity.ok(exampleService.searchExamples(keyword, pageable));
    }

    @PostMapping
    public ResponseEntity<ExampleResponse> createExample(@RequestBody ExampleResponse exampleResponse) {
        return ResponseEntity.ok(exampleService.createExample(exampleResponse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExampleResponse> updateExample(@PathVariable Long id, @RequestBody ExampleResponse exampleResponse) {
        return ResponseEntity.ok(exampleService.updateExample(id, exampleResponse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExample(@PathVariable Long id) {
        exampleService.deleteExample(id);
        return ResponseEntity.noContent().build();
    }
}