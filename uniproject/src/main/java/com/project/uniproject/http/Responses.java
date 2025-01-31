package com.project.uniproject.http;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;

public class Responses {

    private static HashMap<String, Object> response;

    public static Responses success() {

        response = new HashMap<>();
        response.put("status", "success");
        response.put("code", HttpStatus.OK.value());
        return new Responses();
    }

    public static Responses error() {

        response = new HashMap<>();
        response.put("status", "error");
        response.put("code", HttpStatus.BAD_REQUEST.value());
        return new Responses();
    }


    // метод за статус код
    public Responses withCode(HttpStatus code) {
        response.put("code", code.value());
        return this;
    }

    // метод за съобщения
    public Responses withMessage(String message) {
        response.put("message", message);
        return this;
    }

    public Responses withData(Object data) {
        response.put("data", data);
        return this;
    }

    public Responses withDataAsArray(Object data) {

        ArrayList<Object> list = new ArrayList<>();
        list.add(data);
        return this.withData(list);
    }

    public ResponseEntity<Object> build() {

        int code = (int) response.get("code");
        return new ResponseEntity<Object>(response, HttpStatusCode.valueOf(code));
    }
}
