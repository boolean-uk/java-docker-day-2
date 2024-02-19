package com.booleanuk.api.controllers;

import com.booleanuk.api.models.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController extends ControllerTemplate<User> {
}
