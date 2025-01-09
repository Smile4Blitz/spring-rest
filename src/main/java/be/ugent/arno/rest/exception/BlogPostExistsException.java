package be.ugent.arno.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import be.ugent.arno.rest.model.BlogPost;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class BlogPostExistsException extends Exception {
    public BlogPostExistsException() {}
    public BlogPostExistsException(BlogPost post) {}
}
