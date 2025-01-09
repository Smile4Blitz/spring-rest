package be.ugent.arno.rest.repository;

import java.util.HashMap;
import java.util.List;
import java.util.random.RandomGenerator;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import be.ugent.arno.rest.exception.BlogPostExistsException;
import be.ugent.arno.rest.exception.BlogPostNotFoundException;
import be.ugent.arno.rest.model.BlogPost;

@Service
@Profile("test")
public class BlogPostDAOMemory implements IBlogPostRepository {
       final HashMap<Integer, BlogPost> memory;

       public BlogPostDAOMemory() {
              this.memory = new HashMap<Integer, BlogPost>();
              // dummyData();
       }

       @Override
       public List<BlogPost> getAllBlogPosts() {
              return this.memory.values().stream().toList();
       }

       @Override
       public BlogPost getBlogPost(Integer id) throws BlogPostNotFoundException {
              if (!this.memory.containsKey(id))
                     throw new BlogPostNotFoundException();
              return this.memory.get(id);
       }

       @Override
       public void addBlogPost(BlogPost post) throws BlogPostExistsException {
              if (post.getId() == null)
                     post.setId(RandomGenerator.getDefault().nextInt(100, 1000));

              if (this.memory.containsKey(post.getId()))
                     throw new BlogPostExistsException();
              this.memory.put(post.getId(), post);
       }

       @Override
       public void updateBlogPost(BlogPost post) throws BlogPostNotFoundException {
              if (!this.memory.containsKey(post.getId()))
                     throw new BlogPostNotFoundException();
              this.memory.put(post.getId(), post);
       }

       @Override
       public void deleteBlogPostByBlogPost(BlogPost post) throws BlogPostNotFoundException {
              this.deleteBlogPostById(post.getId());
       }

       @Override
       public void deleteBlogPostById(Integer id) throws BlogPostNotFoundException {
              if (!this.memory.containsKey(id))
                     throw new BlogPostNotFoundException();
              this.memory.remove(id);
       }

       @Override
       public void clear() {
              this.memory.clear();
       }
}
