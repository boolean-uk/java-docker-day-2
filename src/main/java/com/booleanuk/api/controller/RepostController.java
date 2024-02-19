package com.booleanuk.api.controller;

import com.booleanuk.api.repository.RepostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("likes")
public class RepostController {
    @Autowired
    RepostRepository repository;
}
