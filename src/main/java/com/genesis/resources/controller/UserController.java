package com.genesis.resources.controller;

import com.genesis.resources.dto.UserCreateRequest;
import com.genesis.resources.dto.UserDetailResponse;
import com.genesis.resources.dto.UserUpdateRequest;
import com.genesis.resources.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController
{
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService service;

    public UserController(UserService service)
    {
        this.service = service;
    }

    @PostMapping
    public UserDetailResponse create(@Valid @RequestBody UserCreateRequest req)
    {
        logger.info("HTTP POST /api/v1/users");
        return service.create(req);
    }

    @GetMapping("/{id}")
    public Object get(@PathVariable long id,
                      @RequestParam(defaultValue = "false") boolean detail)
    {
        logger.info("HTTP GET /api/v1/users/{}?detail={}", id, detail);
        return detail ? service.getDetail(id) : service.get(id);
    }

    @GetMapping
    public Object getAll(@RequestParam(defaultValue = "false") boolean detail)
    {
        logger.info("HTTP GET /api/v1/users?detail={}", detail);
        return detail ? service.getAllDetail() : service.getAll();
    }

    @PutMapping("/{id}")
    public void update(@PathVariable long id, @RequestBody UserUpdateRequest req)
    {
        logger.info("HTTP PUT /api/v1/users/{}", id);
        service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id)
    {
        logger.info("HTTP DELETE /api/v1/users/{}", id);
        service.delete(id);
    }
}
