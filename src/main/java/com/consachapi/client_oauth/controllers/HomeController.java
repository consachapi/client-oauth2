package com.consachapi.client_oauth.controllers;

import com.consachapi.client_oauth.config.RestAdapter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestAdapter
@RequestMapping(HomeController.HOME)
public class HomeController {
    public static final String HOME = "/inicio";

    @GetMapping("/admin")
    public ResponseEntity<String> admin() {
        return new ResponseEntity<>("Admin Ok", HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<String> user() {
        return new ResponseEntity<>("User Ok", HttpStatus.OK);
    }
}
