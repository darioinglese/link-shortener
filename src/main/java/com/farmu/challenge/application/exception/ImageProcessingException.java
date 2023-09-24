package com.farmu.challenge.application.exception;


import com.farmu.challenge.config.ErrorCode;

public class ImageProcessingException extends BusinessException {
    public ImageProcessingException(ErrorCode errorCode) {
        super(errorCode);
    }
}