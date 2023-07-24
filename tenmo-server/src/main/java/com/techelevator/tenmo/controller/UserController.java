package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class UserController
{
    private UserDao userDao;

    // constructor
    public UserController(JdbcUserDao userDao) { this.userDao = userDao; }

    @RequestMapping (path = "/users", method = RequestMethod.GET)
    public List<User> listAllOtherUsers(Principal principal) {
        return userDao.listAllOtherUsers(principal.getName());
    }

}
