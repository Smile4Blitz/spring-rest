package be.ugent.reeks1.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import be.ugent.reeks1.models.BlogPost;

public interface BlogPostRepository extends JpaRepository<BlogPost,Integer> {
}
