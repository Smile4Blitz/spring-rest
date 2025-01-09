package be.ugent.arno.rest.repository;

import java.util.List;

import be.ugent.arno.rest.exception.BlogPostExistsException;
import be.ugent.arno.rest.exception.BlogPostNotFoundException;
import be.ugent.arno.rest.model.BlogPost;

public interface IBlogPostRepository {

    List<BlogPost> getAllBlogPosts();

    BlogPost getBlogPost(Integer id) throws BlogPostNotFoundException;

    void addBlogPost(BlogPost post) throws BlogPostExistsException;

    void updateBlogPost(BlogPost post) throws BlogPostNotFoundException;

    void deleteBlogPostByBlogPost(BlogPost post) throws BlogPostNotFoundException;

    void deleteBlogPostById(Integer id) throws BlogPostNotFoundException;

    void clear();

}