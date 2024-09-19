package com.booleanuk.api.controllers;

import com.booleanuk.api.models.Post;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
public class PostController extends GenericController<Post, Integer>{
}
