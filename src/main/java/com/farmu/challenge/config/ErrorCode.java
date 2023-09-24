package com.farmu.challenge.config;

public enum ErrorCode {
    INTERNAL_ERROR("Internal error", "INTERNAL_ERROR"),

    IMAGE_PROCESSING_EXCEPTION("Couldn't process the image", "IMAGE_PROCESSING_ERROR"),
    IMAGE_DECODING_EXCEPTION("Couldn't decode the image", "IMAGE_DECODING_ERROR"),
    LINK_NOT_FOUND("Unshortened url not found","LINK_MISSING" ),
    ;

    private final String message;
    private final String code;

    ErrorCode(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return this.code;
    }
}
