package be.ugent.reeks1.interfaces;

import java.util.List;

import be.ugent.reeks1.exception.BlogPostAlreadyExists;
import be.ugent.reeks1.exception.BlogPostNotFoundException;
import be.ugent.reeks1.models.BlogPost;

public interface IRepository {

    List<BlogPost> getAll();

    BlogPost addPost(BlogPost blogPost) throws BlogPostAlreadyExists;

    BlogPost deletePost(Integer id) throws BlogPostNotFoundException;

    BlogPost getPost(Integer id) throws BlogPostNotFoundException;

    BlogPost setPost(BlogPost blogPost) throws BlogPostNotFoundException;

}