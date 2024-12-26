package be.ugent.reeks1.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import be.ugent.reeks1.exception.BlogPostAlreadyExists;
import be.ugent.reeks1.exception.BlogPostNotFoundException;
import be.ugent.reeks1.interfaces.IRepository;
import be.ugent.reeks1.models.BlogPost;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;

@RestController
public class BlogPostController {
    @Autowired
    IRepository repository;

    // /actuator/metrics/requests?tag=method:GET
    MeterRegistry registry = Metrics.globalRegistry;

    @GetMapping("/blogposts")
    public List<BlogPost> getBlogPosts() {
        registry.counter("requests", "method", "GET").increment();
        return repository.getAll();
    }

    @GetMapping("/blogposts/{id}")
    public BlogPost getBlogPost(@PathVariable("id") Integer id) {
        try {
            registry.counter("requests", "method", "GET").increment();
            return this.repository.getPost(id);
        } catch (BlogPostNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not found", exception);
        }
    }

    @PostMapping("/blogposts")
    public ResponseEntity<BlogPost> addBlogPost(@RequestBody(required = true) BlogPost blogPost) {
        try {
            registry.counter("requests", "method", "POST").increment();            
            this.repository.addPost(blogPost);
            URI uri = ServletUriComponentsBuilder
                    .fromPath("/blogposts/{id}")
                    .buildAndExpand(blogPost.getId())
                    .toUri();
            return ResponseEntity.created(uri).build();
        } catch (BlogPostAlreadyExists exception) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "already exists", exception);
        }
    }

    @PutMapping("/blogposts/{id}")
    public ResponseEntity<Object> putBlogPost(
            @PathVariable("id") Integer id,
            @RequestBody(required = true) BlogPost blogPost) {
        try {
            registry.counter("requests", "method", "PUT").increment();
            if (id != blogPost.getId())
                throw new ResponseStatusException(HttpStatusCode.valueOf(409), "id doesn't match");
            this.repository.setPost(blogPost);
            return ResponseEntity.noContent().build();
        } catch (BlogPostNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not found", exception);
        }
    }

    @DeleteMapping("/blogposts/{id}")
    public ResponseEntity<Object> deleteBlogPost(@PathVariable(name = "id", required = true) Integer id) {
        try {
            registry.counter("requests", "method", "DELETE").increment();
            this.repository.deletePost(id);
            return ResponseEntity.noContent().build();
        } catch (BlogPostNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not found", exception);
        }
    }
}
