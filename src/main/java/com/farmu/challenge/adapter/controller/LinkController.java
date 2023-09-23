package com.farmu.challenge.adapter.controller;

import com.farmu.challenge.adapter.controller.model.LinkRequest;
import com.farmu.challenge.application.ShortenLinkUseCase;
import com.farmu.challenge.application.UnshortenLinkUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.http.HttpStatus.MOVED_PERMANENTLY;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@Slf4j
@RestController
@RequiredArgsConstructor
public class LinkController {

    private final ShortenLinkUseCase shortenLink;
    private final UnshortenLinkUseCase unshortenLink;

    @PostMapping(path = "/shorten")
    public String shorten(@RequestBody LinkRequest request) {
        String serverPath = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
        log.info(">> execute controller to shorten url: {}", request.link());
        var response = shortenLink.execute(serverPath, request);
        log.info("<< controller response url: {}", response);
        return response;
    }

    @GetMapping(path = "/{shortlink}")
    public ResponseEntity<?> unshorten(@PathVariable("shortlink") String shortlink) throws URISyntaxException {
        log.info(">> execute controller to unshorten url: {}", shortlink);
        var response = unshortenLink.execute(shortlink);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(new URI(response));
        log.info("<< controller response url: {}", response);
        return new ResponseEntity<>(httpHeaders, MOVED_PERMANENTLY);
    }
}
