package com.toilamanh.toilamanh.enu;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum Http {
    CREATE_SUCCESS_USER(HttpStatus.CREATED.value(), "Create success user"),
    CREATE_BAD_USER(HttpStatus.BAD_REQUEST.value(), ""),
    CREATE_ERROR_SERVER(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Server error"),
    ;
    private Integer status;
    private String message;
    Http(Integer status, String message) {
        this.status = status;
        this.message = message;
    }
}
