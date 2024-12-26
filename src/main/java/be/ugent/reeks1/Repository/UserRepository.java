package be.ugent.reeks1.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import be.ugent.reeks1.models.BlogPostUser;

public interface UserRepository extends JpaRepository<BlogPostUser, Integer> {
    BlogPostUser findByUsername(String username);
}