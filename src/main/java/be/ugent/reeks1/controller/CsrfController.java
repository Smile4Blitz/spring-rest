package be.ugent.reeks1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class CsrfController {
    @GetMapping("/csrf")
    public ResponseEntity<Object> getCsrf(HttpServletRequest request) {
        return ResponseEntity.ok(request.getAttribute(CsrfToken.class.getName()));
    }

}
