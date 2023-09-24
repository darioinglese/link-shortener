package com.farmu.challenge.application.exception;


import com.farmu.challenge.config.ErrorCode;

public class LinkNotFoundException extends BusinessException {
    public LinkNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}