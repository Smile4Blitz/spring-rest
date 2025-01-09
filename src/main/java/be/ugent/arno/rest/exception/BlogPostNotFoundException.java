package be.ugent.arno.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class BlogPostNotFoundException extends Exception {
    public BlogPostNotFoundException() {
        super("Blogpost not found");
    }
}
