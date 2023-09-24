package com.farmu.challenge.adapter.controller.model;


import jakarta.validation.constraints.Pattern;
import lombok.NonNull;
public record LinkRequest (
        @NonNull
        @Pattern(regexp = URL_REGEX)
        String link) {
    private static final String URL_REGEX = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$";
}
