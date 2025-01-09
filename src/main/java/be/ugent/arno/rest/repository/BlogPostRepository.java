package be.ugent.arno.rest.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import be.ugent.arno.rest.model.BlogPost;

@Profile("prod")
public interface BlogPostRepository extends JpaRepository<BlogPost, Integer> {
}
