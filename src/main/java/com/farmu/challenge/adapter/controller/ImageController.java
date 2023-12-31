package com.farmu.challenge.adapter.controller;

import com.farmu.challenge.adapter.controller.model.ImageRequest;
import com.farmu.challenge.adapter.controller.model.ImageResponse;
import com.farmu.challenge.application.ResizeUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ResizeUseCase resizeUseCase;

    @PostMapping(path = "/resize")
    public ImageResponse shorten(@RequestBody @Valid ImageRequest request) {
        log.info(">> execute controller to resize image to: {}, {}", request.x(), request.y());
        var response = resizeUseCase.execute(request);
        log.info("<< controller execution completed");
        return new ImageResponse(response);
    }
}
