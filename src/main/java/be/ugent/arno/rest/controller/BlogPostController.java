package be.ugent.arno.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import be.ugent.arno.rest.exception.BlogPostExistsException;
import be.ugent.arno.rest.exception.BlogPostNotFoundException;
import be.ugent.arno.rest.model.BlogPost;
import be.ugent.arno.rest.repository.IBlogPostRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import jakarta.annotation.security.RolesAllowed;

@RestController
@EnableMethodSecurity(jsr250Enabled = true)
@RequestMapping("/blogposts")
public class BlogPostController {
    final IBlogPostRepository repository;
    final MeterRegistry meterRegistry;

    @Autowired
    public BlogPostController(IBlogPostRepository repository) {
        this.meterRegistry = Metrics.globalRegistry;
        this.repository = repository;
    }

    @GetMapping()
    @RolesAllowed("ADMIN")
    public List<BlogPost> getBlogPosts() {
        meterRegistry.counter("blogposts", "request", "get").increment();
        return repository.getAllBlogPosts();
    }

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public BlogPost getBlogPost(@PathVariable(name = "id") Integer id) throws BlogPostNotFoundException {
        meterRegistry.counter("blogposts", "request", "get").increment();
        return this.repository.getBlogPost(id);
    }

    @PostMapping()
    @RolesAllowed("ADMIN")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<BlogPost> createBlogPost(@RequestBody(required = true) BlogPost post)
            throws BlogPostExistsException {
        meterRegistry.counter("blogposts", "request", "post").increment();
        this.repository.addBlogPost(post);
        return ResponseEntity
                .created(
                        UriComponentsBuilder
                                .fromUriString("/blogposts/" + post.getId())
                                .build()
                                .toUri())
                .body(post);
    }

    @PutMapping("/{id}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Void> updateBlogPost(
            @PathVariable(name = "id") Integer id,
            @RequestBody(required = true) BlogPost post) throws BlogPostNotFoundException {
        meterRegistry.counter("blogposts", "request", "put").increment();
        if (id.compareTo(post.getId()) != 0)
            return ResponseEntity.status(HttpStatus.CONFLICT).build();

        try {
            this.repository.updateBlogPost(post);
        } catch (BlogPostNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    @RolesAllowed("ADMIN")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @ExceptionHandler(BlogPostNotFoundException.class)
    public void deleteBlogPost(@PathVariable(name = "id") Integer id) throws BlogPostNotFoundException {
        meterRegistry.counter("blogposts", "request", "delete").increment();
        this.repository.deleteBlogPostById(id);
    }
}
