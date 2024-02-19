package com.booleanuk.api.controller;

import com.booleanuk.api.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("likes")
public class LikeController {
    @Autowired
    LikeRepository repository;
}
