package be.ugent.arno.rest.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import be.ugent.arno.rest.exception.BlogPostExistsException;
import be.ugent.arno.rest.exception.BlogPostNotFoundException;
import be.ugent.arno.rest.model.BlogPost;

@Service
@Profile("prod")
public class BlogPostH2Memory implements IBlogPostRepository {
    final BlogPostRepository blogPostRepository;

    @Autowired
    public BlogPostH2Memory(BlogPostRepository blogPostRepository) {
        this.blogPostRepository = blogPostRepository;
    }

    @Override
    public List<BlogPost> getAllBlogPosts() {
        return blogPostRepository
                .findAll();
    }

    @Override
    public BlogPost getBlogPost(Integer id) throws BlogPostNotFoundException {
        return blogPostRepository
                .findById(id)
                .orElseThrow(BlogPostNotFoundException::new);
    }

    @Override
    public void addBlogPost(BlogPost post) throws BlogPostExistsException {
        if(post.getId() == null)
                blogPostRepository.saveAndFlush(post);

        blogPostRepository
                .findById(post.getId())
                .ifPresent(BlogPostExistsException::new);
        blogPostRepository
                .saveAndFlush(post);
    }

    @Override
    public void updateBlogPost(BlogPost post) throws BlogPostNotFoundException {
        blogPostRepository
                .findById(post.getId())
                .orElseThrow(BlogPostNotFoundException::new);
        blogPostRepository
                .saveAndFlush(post);
    }

    @Override
    public void deleteBlogPostByBlogPost(BlogPost post) throws BlogPostNotFoundException {
        blogPostRepository
                .findById(post.getId())
                .ifPresentOrElse(
                        blogPostRepository::delete,
                        BlogPostNotFoundException::new);
    }

    @Override
    public void deleteBlogPostById(Integer id) throws BlogPostNotFoundException {
        blogPostRepository
                .findById(id)
                .ifPresentOrElse(
                        blogPostRepository::delete,
                        BlogPostNotFoundException::new);
    }

    @Override
    public void clear() {
        blogPostRepository
                .deleteAll();
    }

}
