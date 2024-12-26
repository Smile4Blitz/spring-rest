package be.ugent.reeks1.data;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import be.ugent.reeks1.exception.BlogPostAlreadyExists;
import be.ugent.reeks1.exception.BlogPostNotFoundException;
import be.ugent.reeks1.interfaces.IRepository;
import be.ugent.reeks1.models.BlogPost;

@Service
@Profile("prod")
public class BlogPostDaoH2 implements IRepository {
    @Autowired
    BlogPostRepository repository;

    @Override
    public List<BlogPost> getAll() {
        return repository.findAll();
    }

    @Override
    public BlogPost getPost(Integer id) throws BlogPostNotFoundException {
        try {
            return repository.findById(id).get();
        } catch (NoSuchElementException exception) {
            throw new BlogPostNotFoundException();
        }
    }

    @Override
    public BlogPost addPost(BlogPost blogPost) throws BlogPostAlreadyExists {
        if (repository.existsById(blogPost.getId()))
            throw new BlogPostAlreadyExists();
        return repository.save(blogPost);
    }

    @Override
    public BlogPost deletePost(Integer id) throws BlogPostNotFoundException {
        Optional<BlogPost> b = repository.findById(id);
        if (b.isPresent()) {
            repository.deleteById(id);
            return b.get();
        }
        throw new BlogPostNotFoundException();
    }

    @Override
    public BlogPost setPost(BlogPost blogPost) throws BlogPostNotFoundException {
        Optional<BlogPost> b = repository.findById(blogPost.getId());
        if (b.isPresent())
            return repository.save(blogPost);
        throw new BlogPostNotFoundException();
    }

}
