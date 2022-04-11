package com.example.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
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
        User user = service.findOne(id);
        if(user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }
        return user;
    }

    @PostMapping("/users") //Restful API를 설계할 때 복수형 설계 권장. 단일 객체만을 위한 URI이 아니라, USERS라는 데이터 리소스에 새로운 목록의 데이터를 추가하기 위한 API 이므로
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) { // ResponseEntity 사용 시 반환 데이터를 위한 Response Status Code를 포함해서 전달할 수 있기 때문에
        User saveUser = service.save(user);

        // ServletUriComponentsBuilder : 사용자 요청에 따른 작업을 처리한 다음, 결과 값을 토대로 관련 URI를 생성해 주는 역할
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saveUser.getId())
                .toUri();

        return ResponseEntity.created(location).build(); // created() 메소드는 반환 객체에 대한 response 타입을 결정하는데, created로 할 경우 201 코드를 반환
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User user) {
        User updateUser = service.updateUser(id, user);
        if(updateUser == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }
        return updateUser;
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        User user = service.deleteById(id);
        if(user == null){
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }
    }

}
