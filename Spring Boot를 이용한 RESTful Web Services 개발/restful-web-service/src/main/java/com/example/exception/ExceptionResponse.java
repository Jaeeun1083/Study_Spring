package com.example.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 예외 처리를 위한 일반화된 자바 POJO 객체
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {

    private Date timestamp;
    private String message;
    private String details;

}
