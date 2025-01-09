package be.ugent.arno.rest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import be.ugent.arno.rest.exception.BlogPostExistsException;
import be.ugent.arno.rest.exception.BlogPostNotFoundException;
import be.ugent.arno.rest.model.BlogPost;
import be.ugent.arno.rest.repository.IBlogPostRepository;
import be.ugent.arno.rest.security.SecurityContextTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebClient
@ActiveProfiles("test")
public class BlogPostControllerTest {
    @Autowired
    BlogPostController blogPostController;

    @Autowired
    IBlogPostRepository memory;

    @Autowired
    WebTestClient wtc;

    @BeforeEach
    public void reset() {
        memory.clear();
    }

    @Test
    void testCreateBlogPostExistsException() throws BlogPostExistsException {
        BlogPost post = new BlogPost();
        post.setId(1);
        post.setTitle("test");
        post.setContext("test");
        memory.addBlogPost(post);

        wtc
                .method(HttpMethod.POST)
                .uri("/blogposts")
                .bodyValue(post)
                .exchange()
                .expectStatus().isEqualTo(409);
    }

    @Test
    void testCreateBlogPostWithId() {
        BlogPost post = new BlogPost();
        post.setId(1);
        post.setTitle("test");
        post.setContext("test");

        wtc
                .method(HttpMethod.POST)
                .uri("/blogposts")
                .bodyValue(post)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location("/blogposts/1");
    }

    @Test
    void testCreateBlogPostWithoutId() {
        BlogPost post = new BlogPost();
        post.setTitle("test");
        post.setContext("test");

        wtc
                .method(HttpMethod.POST)
                .uri("/blogposts")
                .bodyValue(post)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists("Location");
    }

    @Test
    void testDeleteBlogPostNotFoundException() {
        wtc
                .method(HttpMethod.DELETE)
                .uri("/blogposts/1")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testDeleteBlogPost() throws BlogPostExistsException {
        BlogPost post = new BlogPost();
        post.setId(1);
        post.setTitle("title");
        post.setContext("content");

        memory.addBlogPost(post);

        wtc
                .method(HttpMethod.DELETE)
                .uri("/blogposts/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testGetAllBlogPostsXML() throws BlogPostExistsException {
        for (int i = 1; i <= 10; i++) {
            BlogPost post = new BlogPost();
            post.setId(i);
            post.setTitle("title");
            post.setContext("content");
            memory.addBlogPost(post);
        }

        String response = wtc
                .method(HttpMethod.GET)
                .uri("/blogposts")
                .header("accept", "application/xml")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).returnResult().getResponseBody();

        assertTrue(response.startsWith("<List>"));
        assertTrue(response.contains("<item>"));
        assertTrue(response.contains("</item>"));
        assertTrue(response.endsWith("</List>"));
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    void testGetAllBlogPostsJSON() throws BlogPostExistsException {
        for (int i = 1; i <= 10; i++) {
            BlogPost post = new BlogPost();
            post.setId(i);
            post.setTitle("title");
            post.setContext("content");
            memory.addBlogPost(post);
        }

        wtc
                .method(HttpMethod.GET)
                .uri("/blogposts")
                .header("Accept", "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BlogPost.class)
                .hasSize(10);
    }

    @Test
    void testGetOneBlogPostNotFoundException() {
        wtc
                .method(HttpMethod.GET)
                .uri("/blogposts")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testGetOneBlogPostXML() throws BlogPostExistsException {
        BlogPost post = new BlogPost();
        post.setId(1);
        post.setTitle("title");
        post.setContext("content");

        memory.addBlogPost(post);

        String response = wtc
                .method(HttpMethod.GET)
                .uri("/blogposts/1")
                .header("Accept", "application/xml")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).returnResult().getResponseBody();

        assertTrue(
                response.equals("<BlogPost><context>content</context><id>1</id><title>title</title></BlogPost>"));
    }

    @Test
    void testGetOneBlogPostJSON() throws BlogPostExistsException {
        BlogPost post = new BlogPost();
        post.setId(1);
        post.setTitle("title");
        post.setContext("content");

        memory.addBlogPost(post);

        wtc
                .method(HttpMethod.GET)
                .uri("/blogposts/1")
                .header("Accept", "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBody(BlogPost.class).value(p -> p.getId().equals(post.getId()));
    }

    @Test
    void testUpdateBlogPostNotFoundException() throws BlogPostExistsException {
        BlogPost post = new BlogPost();
        post.setId(1);
        post.setTitle("test");
        post.setContext("test");

        wtc
                .method(HttpMethod.PUT)
                .uri("/blogposts/1")
                .bodyValue(post)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testUpdateBlogPostNotPathVariableAndIdConflictException() throws BlogPostExistsException {
        BlogPost post = new BlogPost();
        post.setId(1);
        post.setTitle("test");
        post.setContext("test");

        wtc
                .method(HttpMethod.PUT)
                .uri("/blogposts/2")
                .bodyValue(post)
                .exchange()
                .expectStatus().isEqualTo(409);
    }

    @Test
    void testUpdateBlogPost() throws BlogPostExistsException, BlogPostNotFoundException {
        BlogPost post = new BlogPost();
        post.setId(1);
        post.setTitle("test");
        post.setContext("test");
        memory.addBlogPost(post);

        post = new BlogPost();
        post.setId(1);
        post.setTitle("update");
        post.setContext("update");

        wtc
                .method(HttpMethod.PUT)
                .uri("/blogposts/1")
                .bodyValue(post)
                .exchange()
                .expectStatus().isNoContent();

        assertEquals(memory.getBlogPost(1).getId(), 1);
        assertEquals(memory.getBlogPost(1).getTitle(), "update");
        assertEquals(memory.getBlogPost(1).getContext(), "update");
    }
}
