package be.ugent.reeks1.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import be.ugent.reeks1.exception.BlogPostAlreadyExists;
import be.ugent.reeks1.exception.BlogPostNotFoundException;
import be.ugent.reeks1.interfaces.IRepository;
import be.ugent.reeks1.models.BlogPost;

@Service
@Profile("dev")
public class BlogPostDaoMemory implements IRepository {
    Map<Integer, BlogPost> memory = new HashMap<>();
    
    @Override
    public List<BlogPost> getAll() {
        return this.memory.values().stream().toList();
    }

    @Override
    public BlogPost addPost(BlogPost blogPost) throws BlogPostAlreadyExists {
        if (this.memory.containsKey(blogPost.getId()))
            throw new BlogPostAlreadyExists();
        return this.memory.put(blogPost.getId(), blogPost);
    }

    @Override
    public BlogPost deletePost(Integer id) throws BlogPostNotFoundException {
        this.getPost(id);
        return this.memory.remove(id);
    }

    @Override
    public BlogPost getPost(Integer id) throws BlogPostNotFoundException {
        if (this.memory.containsKey(id))
            return this.memory.get(id);
        throw new BlogPostNotFoundException();
    }

    @Override
    public BlogPost setPost(BlogPost blogPost) throws BlogPostNotFoundException {
        this.getPost(blogPost.getId());
        return this.memory.put(blogPost.getId(), blogPost);
    }
}
