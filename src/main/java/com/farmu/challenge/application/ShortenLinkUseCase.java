package com.farmu.challenge.application;

import com.farmu.challenge.adapter.controller.model.LinkRequest;
import com.farmu.challenge.adapter.persistance.LinkRepository;
import com.farmu.challenge.domain.Link;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@Slf4j
@RequiredArgsConstructor
public class ShortenLinkUseCase {
    private final LinkRepository linkRepository;


    public String execute(String basePath, LinkRequest request) {
        var link = linkRepository.findByLongLink(request.link())
                .orElseGet(() -> createLink(request.link()));
        return basePath.concat("/").concat(link.getShortLink());
    }
    private Link createLink(String longLink) {
        Link link = linkRepository.save(
                Link.builder().longLink(longLink).build());
        link.setShortLink(shorten(link.getId()));
        log.info(">> saving url {} in db", link.getLongLink());
        return linkRepository.save(link);
    }
    private String shorten(Long id) {
        return Base64.getEncoder().encodeToString(id.toString().getBytes());
    }
}
