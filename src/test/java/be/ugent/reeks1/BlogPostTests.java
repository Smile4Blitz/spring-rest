package be.ugent.reeks1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import be.ugent.reeks1.exception.BlogPostNotFoundException;
import be.ugent.reeks1.interfaces.IRepository;
import be.ugent.reeks1.models.BlogPost;

@ActiveProfiles("dev")
@Import(TestSecurityConfig.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BlogPostTests {
    @Autowired
    WebTestClient wc;

    @Autowired
    IRepository repository;

    @Value("${spring.security.user.name}")
    String username;

    @Value("${spring.security.user.password}")
    String password;

    @Value("${spring.security.user.roles}")
    String roles;

    Integer mockId = 1;
    String mockTitle = "test";
    String mockContent = "test";

    @BeforeEach
    void mockMemory() {
        BlogPost blogPost = new BlogPost();
        blogPost.setId(mockId);
        blogPost.setTitle(mockTitle);
        blogPost.setContent(mockContent);

        try {
            repository.addPost(blogPost);
        } catch (Exception exception) {
        }
    }

    @Test
    void getAllJSON() {
        wc.get()
                .uri("/blogposts")
                .header("Accept", "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BlogPost.class)
                .hasSize(1);
    }

    @Test
    void getAllXML() {
        wc.get()
                .uri("/blogposts")
                .header("Accept", "application/xml")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .xml("<List><item><id>" + mockId + "</id><title>" + mockTitle + "</title><content>" + mockContent
                        + "</content></item></List>");
    }

    @Test
    @DirtiesContext
    void postBlogPost() throws BlogPostNotFoundException {
        BlogPost blogPost = new BlogPost();
        blogPost.setId(2);
        blogPost.setTitle("test");
        blogPost.setContent("test");

        String basicAuth = username.concat(":").concat(password);
        String encoded = Base64Coder.encodeString(basicAuth);

        wc.post()
                .uri("/blogposts")
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic ".concat(encoded))
                .bodyValue(blogPost)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location("/blogposts/" + blogPost.getId());

        BlogPost updated = repository.getPost(2);
        assertEquals(updated.getId(), blogPost.getId());
        assertEquals(updated.getTitle(), blogPost.getTitle());
        assertEquals(updated.getContent(), blogPost.getContent());
    }

    @Test
    @DirtiesContext
    void putBlogPost() throws BlogPostNotFoundException {
        String basicAuth = username.concat(":").concat(password);
        String encoded = Base64Coder.encodeString(basicAuth);

        // before
        BlogPost blogPost = this.repository.getPost(mockId);

        // update
        blogPost.setTitle("after");
        blogPost.setContent("after");
        wc.put()
                .uri("/blogposts/" + mockId)
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic ".concat(encoded))
                .bodyValue(blogPost)
                .exchange()
                .expectStatus().isNoContent();

        // after
        BlogPost updated = repository.getPost(blogPost.getId());
        assertEquals(updated.getId(), blogPost.getId());
        assertEquals(updated.getTitle(), blogPost.getTitle());
        assertEquals(updated.getContent(), blogPost.getContent());
    }

    @Test
    @DirtiesContext
    void deleteBlogPost() {
        String basicAuth = username.concat(":").concat(password);
        String encoded = Base64Coder.encodeString(basicAuth);

        wc.delete()
                .uri("/blogposts/" + mockId)
                .header("Authorization", "Basic ".concat(encoded))
                .exchange()
                .expectStatus().isNoContent();

        assertThrowsExactly(BlogPostNotFoundException.class, () -> repository.getPost(mockId));
    }
}
