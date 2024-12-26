package be.ugent.reeks1.models;

import java.util.random.RandomGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class BlogPost {
    // identifier
    @Id
    Integer id;

    String title;
    String content;

    public BlogPost() {
        this.id = RandomGenerator.getDefault().nextInt(100000, 999999);
    }

    public Integer getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
