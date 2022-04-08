package com.example.user;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private UserDaoService service;

    public UserController(UserDaoService service){ // 의존성 주입으로 생성 시 개발자가 프로그램 실행 중에 변경할 수 없어 일관된 사용 가능
        this.service = service;
    }

    @GetMapping("/users") // = @RequestMapping(value="/users", method=RequestMethod.GET)
    public List<User> retrieveAllUsers(){
        return service.findAll();
    }

    @GetMapping("/users/{id}")
    public User retrieveUser(@PathVariable int id) {
        return service.findOne(id);
    }

    @PostMapping("/users")
    public void createUser(@RequestBody User user) {
        User saveUser = service.save(user);
    }

}
