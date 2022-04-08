package com.example.user;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String message) {
        super(message); // 부모 클래스에서 전달받은 message
    }

}
