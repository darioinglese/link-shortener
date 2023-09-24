package com.farmu.challenge.adapter.controller.model;

import lombok.NonNull;

public record ImageRequest(
        @NonNull
        String image,
        int x,
        int y) {
}
